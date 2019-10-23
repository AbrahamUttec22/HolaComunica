package net.tecgurus.holacomunicate.model
import android.support.annotation.Keep

@Keep
class Anuncio(var description: String = "",
              var titulo: String = "",
              var fecha: String = "",
              var ubicacion: String = "",
              var id: String = "",
              var id_empresa: String = "") {

    override fun toString() = "$description  $titulo $fecha $ubicacion $id $id_empresa"
}