package net.tecgurus.holacomunicate.model
import android.support.annotation.Keep

@Keep
class Checador(var fecha: String = "",
               var hora: String = "",
               var id: String = "",
               var id_empresa: String = "",
               var id_usuario: String = "",
               var nombre: String = "") {

    override fun toString() = "$fecha  $hora  $id_empresa $id_usuario $id $nombre"
}