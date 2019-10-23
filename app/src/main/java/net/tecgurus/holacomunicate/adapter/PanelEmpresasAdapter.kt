package net.tecgurus.holacomunicate.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.LightingColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.alejandrolora.finalapp.inflate
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import net.tecgurus.holacomunicate.model.Empresa
import kotlinx.android.synthetic.main.dialog_article_comments.view.*
import kotlinx.android.synthetic.main.list_view_panel_empresas.view.imageEmpresa

/**
 * @author Abraham Casas Aguilar
 */

class PanelEmpresasAdapter(val context: Context, val layout: Int, val list: List<Empresa>) : BaseAdapter() {


    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: EmpresasListViewHolder
        if (convertView == null) {
            view = parent!!.inflate(layout)
            vh = EmpresasListViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as EmpresasListViewHolder
        }
        //val uid = "${list[position].uid}"//no mostrar
        //val token = "${list[position].token}"//no mostrar
        val id_empresa = "${list[position].id_empresa}"//no mostrar
        val id = "${list[position].id}"//no mostrar
        val estado = "${list[position].estado}"//no mostrar
        if (estado.equals("0")) {
            view.btnDarBaja.text = "Volver a dar de alta"
            val color = Color.parseColor("#209CFE")
            var filter = LightingColorFilter(Color.GRAY, Color.GRAY)
            vh.btnDarBaja.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

        }


        val giro = "${list[position].giro}"//mostrar
        val direccion = "${list[position].direccion}"//mostrar

        val foto = "${list[position].foto}"
        val nombre = "${list[position].nombre}"
        val telefono = "${list[position].telefono}"
        val correo = "${list[position].correo}"
        val fecha_registro = "${list[position].fecha_registro}"
        val plan_pago = "${list[position].estatus}"
        val fecha_ve = "${list[position].fecha_vencimiento_plan}"

        Glide
                .with(this.context)
                .load(foto)
                .into(view.imageEmpresa)

        var contador = 0
        val userCollection: CollectionReference
        userCollection = FirebaseFirestore.getInstance().collection("Usuarios")
        val empleado = userCollection.whereEqualTo("id_empresa", id_empresa)
        //beggin with consult
        empleado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    contador++
                }
                /*  vh.datos.text = correo + " " + telefono + " Giro: " + giro +
                          " Direccion: " + direccion + " Usuarios en su aplicacion: " + contador.toString() +
                          " Plan de pago: " + plan_pago + " Fecha de registro: " + fecha_registro + " Fecha de vencimiento: " + fecha_ve
                */
                vh.nombreEmpresa.text = nombre
                vh.fechaRegistroEmpresa.text = fecha_registro
                vh.correoEmpresa.text = correo
                vh.telefonoEmpresa.text = telefono
                vh.giroEmpresa.text = giro
                vh.direccionEmpresa.text = direccion
                vh.usuariosEmpresa.text = contador.toString()
                vh.planPagoEmpresa.text = plan_pago
                vh.fechaRegistroEmpresa.text = fecha_registro
                vh.fechaVencimientoPlanEmpresaA.text = fecha_ve

            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }
        })//end for expression lambdas this very cool

        //  vh.descipcion.text = "Giro: " + giro + " Direccion: " + direccion
        //  vh.descipciontwo.text = "Usuarios en su aplicacion: " + contador.toString() + " Plan de pago: Prueba de 15 días"

        vh.btnDarBaja.setOnClickListener(object : View.OnClickListener {
            override fun onClick(position: View?) {
                if (estado.equals("1")) {
                    val empresa = Empresa()
                    empresa.id = id
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("Estas seguro de eliminar?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                        deleteEmpresa(empresa)
                    }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() }).show()
                }
                if (estado.equals("0")) {
                    val empresa = Empresa()
                    empresa.id = id
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("Estas seguro de habilitar?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                        habilitarEmpresa(empresa)
                    }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() }).show()
                }
            }

            private fun deleteEmpresa(empresa: Empresa) {
                FirebaseApp.initializeApp(context)
                val empresaCollection: CollectionReference
                empresaCollection = FirebaseFirestore.getInstance().collection("Empresas")
                //only this source I update the status,
                empresaCollection.document(empresa.id).update("estado", "0").addOnSuccessListener {
                    Toast.makeText(context, "Se ha dado de baja temporalmente", Toast.LENGTH_LONG).show()
                }.addOnFailureListener { Toast.makeText(context, "Intenta de nuevo", Toast.LENGTH_LONG).show() }
            }//end for hanlder

            private fun habilitarEmpresa(empresa: Empresa) {
                FirebaseApp.initializeApp(context)
                val empresaCollection: CollectionReference
                empresaCollection = FirebaseFirestore.getInstance().collection("Empresas")
                //only this source I update the status,
                empresaCollection.document(empresa.id).update("estado", "1").addOnSuccessListener {
                    Toast.makeText(context, "Se ha dado de baja temporalmente", Toast.LENGTH_LONG).show()
                }.addOnFailureListener { Toast.makeText(context, "Intenta de nuevo", Toast.LENGTH_LONG).show() }
            }//end for hanlder
        })
        return view
    }

}


class EmpresasListViewHolder(view: View) {
    val nombreEmpresa: TextView = view.txtNombreEmpresaA
    val fechaRegistroEmpresa: TextView = view.txtfechaRegistroA
    val correoEmpresa: TextView = view.txtCorreoEmpresaA
    val telefonoEmpresa: TextView = view.txtTelefonoEmpresaA

    val giroEmpresa: TextView = view.txtGiroEmpresaA
    val direccionEmpresa: TextView = view.txtDireccionEmpresaA
    val usuariosEmpresa: TextView = view.txtUsuarioEmpresaA

    val planPagoEmpresa: TextView = view.txtPlanPagoEmpresaA
    val fechaVencimientoPlanEmpresaA: TextView = view.txtfechaVencimeintoPlanEmpresaA
    val btnDarBaja: Button = view.btnDarBaja


}


