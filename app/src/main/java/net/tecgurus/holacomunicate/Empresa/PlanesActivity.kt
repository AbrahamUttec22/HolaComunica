package net.tecgurus.holacomunicate.Empresa

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.LightingColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.alejandrolora.finalapp.goToActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_mi_plan.*
import kotlinx.android.synthetic.main.activity_planes.*
import kotlinx.android.synthetic.main.activity_planes_pago.*
import kotlinx.android.synthetic.main.dialog_article_comments.view.*
import net.tecgurus.holacomunicate.DashboarActivity
import net.tecgurus.holacomunicate.R
import net.tecgurus.holacomunicate.paypal.PayPalPaymentActivity
import net.tecgurus.holacomunicate.paypal.PlanesPagoActivity
import net.tecgurus.holacomunicate.utils.Tools

/**
 * @author Abraham Casas Aguilar
 */
class PlanesActivity : AppCompatActivity() {

    //declare val for save the collection
    private val empresasCollection: CollectionReference

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        empresasCollection = FirebaseFirestore.getInstance().collection("Empresas")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planes)
        enviarcostos()
        initToolbar()
    }

    private fun enviarcostos() {
//                        btnPlan1.setVisibility(View.INVISIBLE)
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
                if (plan.equals("anual") || plan.equals("pruebainicial") || plan.equals("mensual")) {
                    if (tipo_plan.equals("Usuarios: 1 a 5")) {
                        btnPlan1.text = "TU CUENTAS CON ESTE PLAN"
                        btnPlan1.isEnabled = false
                        val color = Color.parseColor("#FD3B24")
                        btnPlan1.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))
                    }

                    if (tipo_plan.equals("Usuarios: 5 a 20")) {
                        btnPlan1.setVisibility(View.INVISIBLE)
                        btnPlan2.text = "TU CUENTAS CON ESTE PLAN"
                        btnPlan2.isEnabled = false
                        val color = Color.parseColor("#FD3B24")
                        btnPlan2.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                    }
                    if (tipo_plan.equals("Usuarios: 20 a 50")) {
                        btnPlan1.setVisibility(View.INVISIBLE)
                        btnPlan2.setVisibility(View.INVISIBLE)
                        btnPlan3.text = "TU CUENTAS CON ESTE PLAN"
                        btnPlan3.isEnabled = false
                        val color = Color.parseColor("#FD3B24")
                        btnPlan3.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))
                    }
                    if (tipo_plan.equals("Usuarios: 50 a 100")) {
                        btnPlan1.setVisibility(View.INVISIBLE)
                        btnPlan2.setVisibility(View.INVISIBLE)
                        btnPlan3.setVisibility(View.INVISIBLE)
                        btnPlan4.text = "TU CUENTAS CON ESTE PLAN"
                        btnPlan4.isEnabled = false
                        val color = Color.parseColor("#FD3B24")
                        btnPlan4.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))
                    }
                }


            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool


        btnPlan1.setOnClickListener {
            val i = Intent(this, PlanesPagoActivity::class.java)
            i.putExtra("plan", "Usuarios: 1 a 5")
            startActivity(i)
        }

        btnPlan2.setOnClickListener {
            val i = Intent(this, PlanesPagoActivity::class.java)
            i.putExtra("plan", "Usuarios: 5 a 20")
            startActivity(i)
        }

        btnPlan3.setOnClickListener {
            val i = Intent(this, PlanesPagoActivity::class.java)
            i.putExtra("plan", "Usuarios: 20 a 50")
            startActivity(i)
        }

        btnPlan4.setOnClickListener {
            val i = Intent(this, PlanesPagoActivity::class.java)
            i.putExtra("plan", "Usuarios: 50 a 100")
            startActivity(i)
        }

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
            goToActivity<MiPlanActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        goToActivity<MiPlanActivity> {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
