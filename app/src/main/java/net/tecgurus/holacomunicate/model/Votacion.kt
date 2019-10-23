package net.tecgurus.holacomunicate.model

import android.support.annotation.Keep

@Keep
class Votacion(var id_pregunta: String = "",
               var respuesta: String = "",
               var correo: String = "",
               var id_empresa: String = "") {
    override fun toString() = "$id_pregunta  $respuesta  $correo $id_empresa"
}