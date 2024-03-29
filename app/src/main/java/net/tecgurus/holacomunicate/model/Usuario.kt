package net.tecgurus.holacomunicate.model

import android.support.annotation.Keep

@Keep
class Usuario(var email: String = "",
              var ubicacion: String = "",
              var name: String = "",
              var rol: String = "",//direccion,edad,telefono campos nuevos
              var direccion: String = "",
              var edad: String = "",
              var telefono: String = "",
              var id: String = "",
              var id_empresa: String = "",
              var token: String = "",
              var estatus: String = "",
              var uid: String = "") {

    override fun toString() = "$uid $email  $name $ubicacion $rol $direccion $edad $telefono $id $id_empresa $token $estatus"
}