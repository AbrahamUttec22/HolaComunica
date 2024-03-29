package net.tecgurus.holacomunicate.paypal

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.alejandrolora.finalapp.goToActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import net.tecgurus.holacomunicate.R
import kotlinx.android.synthetic.main.activity_planes_pago.*
import android.support.v7.app.AlertDialog
import android.widget.TextView
import net.tecgurus.holacomunicate.Empresa.PlanesActivity


/**
 * Created by:
 * @author Abraham Casas Aguilar
 */
class PlanesPagoActivity : AppCompatActivity() {

    //declare val for save the collection
    private val userCollection: CollectionReference
    private val costosCollection: CollectionReference
    lateinit var dialog: AlertDialog
    private var plan_adquirir = ""

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        userCollection = FirebaseFirestore.getInstance().collection("Usuarios")
        costosCollection = FirebaseFirestore.getInstance().collection("Costos")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planes_pago)
        val extras = intent.extras
        if (extras != null) {
            plan_adquirir = extras!!.getString("plan").toString()
        }

        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
        val message = dialogView.findViewById<TextView>(R.id.mensaje)
        message.text = ""
        builder.setView(dialogView)

        var sharedPreferencet = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
        var id_empresa = sharedPreferencet.getString("id_empresa", "")
        val empleado = userCollection.whereEqualTo("id_empresa", id_empresa)
        //beggin with consult
        empleado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                }

                if (plan_adquirir.equals("Usuarios: 1 a 5")) {//ready
                    var plan = "Usuarios: 1 a 5"
                    titlePago.text = plan
                    val costoConsultaMensual = costosCollection.whereEqualTo("tipo_plan", "mensual")
                            .whereEqualTo("plan", plan)
                    //beggin with consult
                    costoConsultaMensual.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                        if (task.isSuccessful) {
                            var precio = ""
                            for (document in task.result!!) {
                                precio = document.get("costo").toString()
                            }
                            precioMensualOriginal.setText(precio)
                            precioMensual.setText("Mensualidad: $" + precioMensualOriginal.text.toString() + " MXN")

                        } else {
                            Log.w("saasas", "Error getting documents.", task.exception)
                        }
                    })//end for expression lambdas this very cool
                    val costoConsultaAnual = costosCollection.whereEqualTo("tipo_plan", "anual")
                            .whereEqualTo("plan", plan)
                    //beggin with consult
                    costoConsultaAnual.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                        if (task.isSuccessful) {
                            var precio = ""
                            for (document in task.result!!) {
                                precio = document.get("costo").toString()
                            }
                            precioAnualOriginal.setText(precio)
                            precioAnual.setText("Anualidad: $" + precioAnualOriginal.text.toString() + " MXN")
                        } else {
                            Log.w("saasas", "Error getting documents.", task.exception)
                        }
                    })//end for expression lambdas this very cool
                } else if (plan_adquirir.equals("Usuarios: 6 a 20")) {
                    var plan = "Usuarios: 6 a 20"
                    titlePago.text = plan
                    val costoConsultaMensual = costosCollection.whereEqualTo("tipo_plan", "mensual")
                            .whereEqualTo("plan", plan)
                    //beggin with consult
                    costoConsultaMensual.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                        if (task.isSuccessful) {
                            var precio = ""
                            for (document in task.result!!) {
                                precio = document.get("costo").toString()
                            }
                            precioMensualOriginal.setText(precio)
                            precioMensual.setText("Mensualidad: $" + precioMensualOriginal.text.toString() + " MXN")
                        } else {
                            Log.w("saasas", "Error getting documents.", task.exception)
                        }
                    })//end for expression lambdas this very cool

                    val costoConsultaAnual = costosCollection.whereEqualTo("tipo_plan", "anual")
                            .whereEqualTo("plan", plan)
                    //beggin with consult
                    costoConsultaAnual.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                        if (task.isSuccessful) {
                            var precio = ""
                            for (document in task.result!!) {
                                precio = document.get("costo").toString()
                            }
                            precioAnualOriginal.setText(precio)
                            precioAnual.setText("Anualidad: $" + precioAnualOriginal.text.toString() + " MXN")
                        } else {
                            Log.w("saasas", "Error getting documents.", task.exception)
                        }
                    })//end for expression lambdas this very cool

                } else if (plan_adquirir.equals("Usuarios: 21 a 50")) {
                    var plan = "Usuarios: 21 a 50"
                    titlePago.text = plan
                    val costoConsultaMensual = costosCollection.whereEqualTo("tipo_plan", "mensual")
                            .whereEqualTo("plan", plan)
                    //beggin with consult
                    costoConsultaMensual.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                        if (task.isSuccessful) {
                            var precio = ""
                            for (document in task.result!!) {
                                precio = document.get("costo").toString()
                            }
                            precioMensualOriginal.setText(precio)
                            precioMensual.setText("Mensualidad: $" + precioMensualOriginal.text.toString() + " MXN")
                        } else {
                            Log.w("saasas", "Error getting documents.", task.exception)
                        }
                    })//end for expression lambdas this very cool

                    val costoConsultaAnual = costosCollection.whereEqualTo("tipo_plan", "anual")
                            .whereEqualTo("plan", plan)
                    //beggin with consult
                    costoConsultaAnual.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                        if (task.isSuccessful) {
                            var precio = ""
                            for (document in task.result!!) {
                                precio = document.get("costo").toString()
                            }
                            precioAnualOriginal.setText(precio)
                            precioAnual.setText("Anualidad: $" + precioAnualOriginal.text.toString() + " MXN")
                        } else {
                            Log.w("saasas", "Error getting documents.", task.exception)
                        }
                    })//end for expression lambdas this very cool

                } else if (plan_adquirir.equals("Usuarios: 51 a 100")) {
                    var plan = "Usuarios: 51 a 100"
                    titlePago.text = plan
                    val costoConsultaMensual = costosCollection.whereEqualTo("tipo_plan", "mensual")
                            .whereEqualTo("plan", plan)
                    //beggin with consult
                    costoConsultaMensual.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                        if (task.isSuccessful) {
                            var precio = ""
                            for (document in task.result!!) {
                                precio = document.get("costo").toString()
                            }
                            precioMensualOriginal.setText(precio)
                            precioMensual.setText("Mensualidad: $" + precioMensualOriginal.text.toString() + " MXN")
                        } else {
                            Log.w("saasas", "Error getting documents.", task.exception)
                        }
                    })//end for expression lambdas this very cool

                    val costoConsultaAnual = costosCollection.whereEqualTo("tipo_plan", "anual")
                            .whereEqualTo("plan", plan)
                    //beggin with consult
                    costoConsultaAnual.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                        if (task.isSuccessful) {
                            var precio = ""
                            for (document in task.result!!) {
                                precio = document.get("costo").toString()
                            }
                            precioAnualOriginal.setText(precio)
                            precioAnual.setText("Anualidad: $" + precioAnualOriginal.text.toString() + " MXN")
                        } else {
                            Log.w("saasas", "Error getting documents.", task.exception)
                        }
                    })//end for expression lambdas this very cool

                } else {
                    //contactanos
                }

                dialog.dismiss()
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }
        })//end for expression lambdas this very cool

        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()

        btn_Mensual.setOnClickListener {
            var costo_mensual = precioMensualOriginal.text.toString()
            var plan = titlePago.text.toString()

            if (!costo_mensual.isEmpty()) {
                val i = Intent(this, PayPalPaymentActivity::class.java)
                i.putExtra("costo_mensual", costo_mensual)
                i.putExtra("plan", plan)
                startActivity(i)
            }

        }
        btn_Anualidad.setOnClickListener {
            var costo_anual = precioAnualOriginal.text.toString()
            var plan = titlePago.text.toString()
            if (!costo_anual.isEmpty()) {
                val i = Intent(this, PayPalPaymentAnualidadActivity::class.java)
                i.putExtra("costo_anual", costo_anual)
                i.putExtra("plan", plan)
                startActivity(i)
            }
        }
        txtCancelar.setOnClickListener {
            goToActivity<PlanesActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }

    }

    override fun onBackPressed() {
        goToActivity<PlanesActivity> {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        super.onBackPressed()
    }

}