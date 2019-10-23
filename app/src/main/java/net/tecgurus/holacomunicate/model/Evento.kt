package net.tecgurus.holacomunicate.model
import android.support.annotation.Keep

@Keep
class Evento(var description: String = "",
             var fecha: String = "",
             var image: String = "",
             var titulo: String = "",
             var ubicacion: String = "",
             var id: String = "",
             var id_empresa: String = "",
             var hora: String = "") {
    override fun toString() = "$description  $fecha $image $titulo $ubicacion $id $id_empresa $hora"
}