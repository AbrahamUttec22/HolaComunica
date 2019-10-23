package net.tecgurus.holacomunicate.formularios
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.*
import com.alejandrolora.finalapp.goToActivity
import com.alejandrolora.finalapp.toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import net.tecgurus.holacomunicate.R
import net.tecgurus.holacomunicate.adapter.UserAdapter
import net.tecgurus.holacomunicate.DashboarActivity
import net.tecgurus.holacomunicate.model.Actividades
import net.tecgurus.holacomunicate.model.Usuario
import net.tecgurus.holacomunicate.utils.Tools
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.activity_user.listView
import java.util.ArrayList

/**
 * @author Abraham
 * admin for empleados
 */
class UserActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    //private lateinit var adapterActividad: ActividadesVerAdapter
    //declare val for save the collection
    private val userCollection: CollectionReference
    private val actividadesCollection: CollectionReference
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private lateinit var usuarioList: List<Usuario>
    private lateinit var actividadesList: List<Actividades>

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        userCollection = FirebaseFirestore.getInstance().collection("Usuarios")
        actividadesCollection = FirebaseFirestore.getInstance().collection("Actividades")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        initToolbar()
        addMarksListener()
        swipeRefreshLayout = findViewById(R.id.swipeUsuario)
        swipeRefreshLayout!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            addMarksListener()
            swipeRefreshLayout!!.setRefreshing(false)

        })
    }

    /**
     * Listener for peopleCollection
     */
    private fun addMarksListener() {
        var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
        var id_empresa = sharedPreference.getString("id_empresa", "")
        userCollection.whereEqualTo("id_empresa", id_empresa).addSnapshotListener { snapshots, error ->
            if (error == null) {
                val changes = snapshots?.documentChanges
                if (changes != null) {
                    // addChanges(changes)
                    listenerDb()
                }
            } else {
                Toast.makeText(this, "Ha ocurrido un error intenta de nuevo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun listenerDb() {
        var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
        var id_empresa = sharedPreference.getString("id_empresa", "")
        val consul = userCollection.whereEqualTo("id_empresa", id_empresa)
                .whereEqualTo("estatus", "1")
        //beggin with consult
        consul.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                val itemUsuario = ArrayList<Usuario>()//lista local de una sola instancia
                var con = 0
                for (document in task.result!!) {
                    con++
                    val usuario = Usuario()
                    usuario.email = document.get("email").toString()
                    usuario.uid = document.get("uid").toString()
                    usuario.token = document.get("token").toString()
                    usuario.telefono = document.get("telefono").toString()
                    usuario.edad = document.get("edad").toString()
                    usuario.direccion = document.get("direccion").toString()
                    usuario.id = document.get("id").toString()
                    usuario.id_empresa = document.get("id_empresa").toString()
                    usuario.name = document.get("name").toString()
                    usuario.rol = document.get("rol").toString()
                    usuario.ubicacion = document.get("ubicacion").toString()
                    itemUsuario.add(usuario)
                }
                if (con == 0) {
                    iconDefaultUsuarios.setVisibility(View.VISIBLE)
                } else {
                    iconDefaultUsuarios.setVisibility(View.INVISIBLE)
                }
                usuarioList = itemUsuario
                adapter = UserAdapter(this, R.layout.list_view_usuario, usuarioList)
                listView.adapter = adapter
                //listView.onItemClickListener = object : AdapterView.OnItemClickListener {
                //  override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //    val id_usuario = view!!.textemail.text.toString()
                //  showDialog(id_usuario)
                //}
                //}
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }
        })//end for expression lambdas this very cool


    }


    /**
     * initToolbar(header)
     */
    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Usuarios"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)//menu de las opciones del toolbar, menu basic
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            goToActivity<DashboarActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        } else if (item.itemId == R.id.action_search) {//icono de search
            //Toast.makeText(applicationContext, item.title, Toast.LENGTH_SHORT).show()
            toast("Diste click en search")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        goToActivity<DashboarActivity> {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

}