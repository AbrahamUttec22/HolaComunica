package net.tecgurus.holacomunicate.Empresa

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.alejandrolora.finalapp.goToActivity
import com.alejandrolora.finalapp.toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_administrar_anuncios.*
import kotlinx.android.synthetic.main.activity_administrar_anuncios.listView
import kotlinx.android.synthetic.main.activity_encuesta.*
import kotlinx.android.synthetic.main.activity_mi_plan.*
import kotlinx.android.synthetic.main.activity_planes.*
import net.tecgurus.holacomunicate.DashboarActivity
import net.tecgurus.holacomunicate.R
import net.tecgurus.holacomunicate.actividadesfragment.GestionActividadesActivity
import net.tecgurus.holacomunicate.adapter.AdministrarAnuncioAdapter
import net.tecgurus.holacomunicate.model.Anuncio
import net.tecgurus.holacomunicate.utils.Tools
import java.util.ArrayList

/**
 * @author
 * Abraham Casas Aguilar
 */
class MiPlanActivity : AppCompatActivity() {

    //declare val for save the collection
    private val empresasCollection: CollectionReference
    private val userCollection: CollectionReference


    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        empresasCollection = FirebaseFirestore.getInstance().collection("Empresas")
        userCollection = FirebaseFirestore.getInstance().collection("Usuarios")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mi_plan)
        initToolbar()
        addMarksListener()
        btmCambiarPlan.setOnClickListener {
            //aqui vincular a la clase  que mostrar el paquete que debe de adquirir, y ademas abajo de este mismo
            //los siguientes paquetes subsecuentes que tambie puede adquirir
            goToActivity<PlanesActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    /**
     * Listener for peopleCollection
     */
    private fun addMarksListener() {
        var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
        var id_empresa = sharedPreference.getString("id_empresa", "")
        empresasCollection.whereEqualTo("id_empresa", id_empresa).addSnapshotListener { snapshots, error ->
            if (error == null) {
                val changes = snapshots?.documentChanges
                if (changes != null) {
                    //addChanges(changes)
                    listenerDb()
                }
            } else {
                toast("Ha ocurrido un error intente de nuevo")
            }
        }
    }

    private fun listenerDb() {
        var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
        var id_empresa = sharedPreference.getString("id_empresa", "")
        val consul = empresasCollection.whereEqualTo("id_empresa", id_empresa)
        //beggin with consult
        consul.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var tipo_plan = ""
                var fecha_vencimiento_plan = ""
                var plan = ""

                for (document in task.result!!) {
                    tipo_plan = document.get("tipo_plan").toString()
                    fecha_vencimiento_plan = document.get("fecha_vencimiento_plan").toString()
                    plan = document.get("estatus").toString()
                }

                when (plan) {
                    "anual" -> {
                        txtMiPlan.text = "Plan: " + plan
                        var textoDescripcion = "Tu plan es para : " + tipo_plan + ", el cual vence el " + fecha_vencimiento_plan+" si deseas adquirir otro plan por favor contactanos al correo  gguerrero@gmail.com"
                        txtDescripcionMiPlan.text = textoDescripcion
                        btmCambiarPlan.setVisibility(View.INVISIBLE)

                    }
                    "mensual" -> {
                        //aqui validar si el usuario tiene mas de 100 usuarios poner el telefono de contacto para
                        //comunicarse con el call center
                        var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                        var id_empresa = sharedPreference.getString("id_empresa", "")
                        val consultaUsuario = userCollection.whereEqualTo("id_empresa", id_empresa)
                        consultaUsuario.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                            if (task.isSuccessful) {
                                var con = 0
                                for (document in task.result!!) {
                                    con++
                                }
                                if(con>=99){
                                    txtMiPlan.text = "Plan: " + plan
                                    var textoDescripcion = "Tu plan es para : " + tipo_plan + ", el cual vence el " + fecha_vencimiento_plan+" si deseas adquirir otro plan por favor contactanos al correo  gguerrero@gmail.com"
                                    txtDescripcionMiPlan.text = textoDescripcion
                                    btmCambiarPlan.setVisibility(View.INVISIBLE)
                                }else{
                                    txtMiPlan.text = "Plan: " + plan
                                    var textoDescripcion = "Tu plan es para : " + tipo_plan + ", el cual vence el " + fecha_vencimiento_plan
                                    txtDescripcionMiPlan.text = textoDescripcion
                                    btmCambiarPlan.text = "Adquirir otro plan"
                                }

                            } else {
                                Log.w("saasas", "Error getting documents.", task.exception)
                            }
                        })//end for expression lambdas this very cool
                    }
                    "pruebainicial" -> {
                        txtMiPlan.text = "Plan: " + plan
                        var textoDescripcion = "Tienes una prueba de 15 dias, terminando esta prueba te sugerimos cambiar de plan "
                        txtDescripcionMiPlan.text = textoDescripcion
                        btmCambiarPlan.text = "Adquirir Plan"
                    }
                    "gratuita" -> {
                        txtMiPlan.text = "Plan: " + plan
                        var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                        var id_empresa = sharedPreference.getString("id_empresa", "")
                        val consultaUsuario = userCollection.whereEqualTo("id_empresa", id_empresa)
                        consultaUsuario.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                            if (task.isSuccessful) {
                                var con = 0
                                for (document in task.result!!) {
                                    con++
                                }
                                if(con>=99){
                                    txtMiPlan.text = "Plan: " + plan
                                    var textoDescripcion = "En tu aplicacion tienes "+con+" empleados registrados, contactanos a gguerrero@gmail.com, para cotizar tu plan"
                                    txtDescripcionMiPlan.text = textoDescripcion
                                    btmCambiarPlan.setVisibility(View.INVISIBLE)
                                }else{
                                    txtMiPlan.text = "Plan: " + plan
                                    var textoDescripcion = "Tu plan es gratuito, consulta el plan que se adecue a tus necesidades "
                                    txtDescripcionMiPlan.text = textoDescripcion
                                    btmCambiarPlan.text = "Adquirir Plan"
                                }

                            } else {
                                Log.w("saasas", "Error getting documents.", task.exception)
                            }
                        })//end for expression lambdas this very cool

                    }
                    else -> {
                    }
                }


            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool
    }

    //front end only
    //here the front end
    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Mi Plan"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_setting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            goToActivity<DashboarActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
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