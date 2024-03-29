package net.tecgurus.holacomunicate.formularios
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.alejandrolora.finalapp.goToActivity
import com.alejandrolora.finalapp.toast
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import net.tecgurus.holacomunicate.R
import net.tecgurus.holacomunicate.DashboarActivity
import net.tecgurus.holacomunicate.model.Anuncio
import net.tecgurus.holacomunicate.utils.Tools
import kotlinx.android.synthetic.main.item_card_wizard_light.view.*

/**
 * @author Abraham
 * 21/06/2019
 * see the anuncios
 */
class CardWizardLight : AppCompatActivity() {
    private var viewPager: ViewPager? = null
    private var myViewPagerAdapter: MyViewPagerAdapter? = null
    private var about_title_array = arrayOf("")
    private var about_description_array = arrayOf("")
    private var about_images_array = intArrayOf(R.drawable.img_wizard_1)
    private var ubicacion = arrayOf("")
    private val channelId = "com.example.vicky.notificationexample"
    private val detalleAnunciosCollection: CollectionReference
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    //declare val for save the collection
    private val anuncioCollection: CollectionReference

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        anuncioCollection = FirebaseFirestore.getInstance().collection("Anuncios")
        detalleAnunciosCollection = FirebaseFirestore.getInstance().collection("detalleAnuncios")
    }

    //  viewpager change listener
    internal var viewPagerPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(position: Int) {
            bottomProgressDots(position)
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {

        }

        override fun onPageScrollStateChanged(arg0: Int) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_wizard_light)
        cambiarSatus()
        MAX_STEP = 0
        about_images_array = intArrayOf(0)
        about_title_array = emptyArray()
        about_description_array = emptyArray()
        addMarksListener(applicationContext)
        viewPager = findViewById<View>(R.id.view_pager) as ViewPager
    }

    //backend
    private fun addMarksListener(applicationContext: Context) {
        var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
        var id_empresa = sharedPreference.getString("id_empresa", "")
        anuncioCollection.whereEqualTo("id_empresa", id_empresa).addSnapshotListener { snapshots, error ->
            if (error == null) {
                val changes = snapshots?.documentChanges
                if (changes != null) {
                    //addChanges(changes, applicationContext)
                    listenerDb()
                }
            } else {
                toast("Ha ocurrido un error intenta de nuevo")
            }
        }
    }


    private fun listenerDb() {
        var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
        var id_empresa = sharedPreference.getString("id_empresa", "")
        val consul = anuncioCollection.whereEqualTo("id_empresa", id_empresa)
        //beggin with consult
        consul.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                val itemAnuncio = ArrayList<Anuncio>()//lista local de una sola instancia
                for (document in task.result!!) {
                    itemAnuncio.add(document.toObject(Anuncio::class.java))//ir agregando los datos a la lista
                }
                addToList(itemAnuncio, applicationContext)//vista
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool


    }

    /**
     * Este metodo es para actualizar el estatus, es decir de que el usuario ya vio los anuncios
     * y por lo tanto ya no se veria como notificacion ene l dashboard
     * cambiar status a 1
     */
    private fun cambiarSatus() {
        var email_mio = mAuth.currentUser!!.email.toString()
        var sharedPreferencet = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
        var id_empresa = sharedPreferencet.getString("id_empresa", "")
        val consultaEvento = detalleAnunciosCollection.whereEqualTo("id_empresa", id_empresa).
                whereEqualTo("correo_usuario", email_mio).whereEqualTo("estatus", "0")
        //beggin with consult
        consultaEvento.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    var id_documento = document.get("id").toString()
                    detalleAnunciosCollection.document(id_documento).update("estatus", "1").addOnSuccessListener {
                    }.addOnFailureListener { }
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool

    }

    /**
     * @param changes
     * aqui se hace el recorrido de la coleccion de cloudfirestore
     */
    private fun addChanges(changes: List<DocumentChange>, applicationContext: Context) {
        val itemAnuncio = ArrayList<Anuncio>()//lista local de una sola instancia
        for (change in changes) {
            itemAnuncio.add(change.document.toObject(Anuncio::class.java))//ir agregando los datos a la lista
        }//una ves agregado los campos mandar a llamar la vista
        addToList(itemAnuncio, applicationContext)//vista
    }

    /**
     * @param itemAnuncio
     */
    private fun addToList(itemAnuncio: List<Anuncio>, applicationContext: Context) {
        //set empty the data
        MAX_STEP = 0
        about_title_array = emptyArray()
        about_description_array = emptyArray()
        about_images_array = intArrayOf()
        ubicacion = emptyArray()
        var con = 0
        for (item in itemAnuncio) {//recorremos la lista de usuario para agregarlo a la lista de people
            con++
            about_title_array += arrayOf(item.titulo)
            about_description_array += arrayOf(item.description)
            about_images_array += intArrayOf(R.drawable.img_wizard_1)
            ubicacion += item.ubicacion
            MAX_STEP++
        }
        if (con == 0) {
            about_title_array += arrayOf("")
            about_description_array += arrayOf("POR EL MOMENTO NO HAY ANUNCIOS")
            about_images_array += intArrayOf(R.drawable.img_wizard_1)
            val xd = "https://firebasestorage.googleapis.com/v0/b/tecapp-25ed3.appspot.com/o/uploads%2Fimage%3A119?alt=media&token=7f91a2a5-efe2-4f11-9574-a9460f4a28b2"
            ubicacion += xd
            MAX_STEP++
        }
        bottomProgressDots(0)//init the 0
        myViewPagerAdapter = MyViewPagerAdapter()
        viewPager!!.adapter = myViewPagerAdapter
        viewPager!!.addOnPageChangeListener(viewPagerPageChangeListener)
        Tools.setSystemBarColor(this, R.color.overlay_light_80)
        Tools.setSystemBarLight(this)
    }

    //see the view
    private fun bottomProgressDots(current_index: Int) {
        val dotsLayout = findViewById<View>(R.id.layoutDots) as LinearLayout
        val dots = arrayOfNulls<ImageView>(MAX_STEP)
        dotsLayout.removeAllViews()
        for (i in dots.indices) {
            dots[i] = ImageView(this)
            val width_height = 15
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams(width_height, width_height))
            params.setMargins(10, 10, 10, 10)
            dots[i]!!.setLayoutParams(params)
            dots[i]!!.setImageResource(R.drawable.shape_circle)
            dots[i]!!.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
            dotsLayout.addView(dots[i])
        }
        if (dots.size > 0) {
            dots[current_index]!!.setImageResource(R.drawable.shape_circle)
            dots[current_index]!!.setColorFilter(resources.getColor(R.color.light_green_600), PorterDuff.Mode.SRC_IN)
        }
    }

    override fun onBackPressed() {
        goToActivity<DashboarActivity> {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    /**
     * View pager adapter, for carrusel
     */
    inner class MyViewPagerAdapter : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null
        private var btnNext: Button? = null

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = layoutInflater!!.inflate(R.layout.item_card_wizard_light, container, false)
            (view.findViewById<View>(R.id.title) as TextView).text = about_title_array[position]
            (view.findViewById<View>(R.id.description) as TextView).text = about_description_array[position]
            Glide
                    .with(view.context)
                    .load("${ubicacion[position]}")
                    .into(view.imagenItem)


            btnNext = view.findViewById<View>(R.id.btn_next) as Button

            if (position == about_title_array.size - 1) {
                btnNext!!.text = "Entendido"
            } else {
                btnNext!!.text = "Siguiente"
            }


            btnNext!!.setOnClickListener {
                val current = viewPager!!.currentItem + 1
                // toast("valor de current es "+current.toString())
                //toast("valor de MAN STEP es "+MAX_STEP.toString())
                //toast(""+current+"<"+""+MAX_STEP)
                if (current < MAX_STEP) {
                    // move to next screen
                    viewPager!!.currentItem = current
                } else {
                    goToActivity<DashboarActivity> {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            }

            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return about_title_array.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }


        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }

    companion object {
        private var MAX_STEP = 0
    }

}