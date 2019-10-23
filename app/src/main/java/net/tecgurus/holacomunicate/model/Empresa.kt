package net.tecgurus.holacomunicate.model
import android.support.annotation.Keep

@Keep
class Empresa( var id: String = "",
               var estado: String = "",
               var nombre: String = "",
               var correo: String = "",
               var telefono: String = "",
               var direccion: String = "",//direccion,edad,telefono campos nuevos
               var foto: String = "",
               var giro: String = "",
               var id_empresa: String = "",
               var estatus: String = "",
               var token: String = "",
               var tipo_plan: String = "",
               var fecha_vencimiento_plan: String = "",
               var fecha_registro: String = "",
               var uid: String = "") {

    override fun toString() = "$nombre $fecha_vencimiento_plan $correo $telefono $direccion" +
            " $foto $giro $id_empresa $token $fecha_registro $estatus $uid $id $tipo_plan"
}
