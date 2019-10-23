package net.tecgurus.holacomunicate.model
import android.support.annotation.Keep

@Keep
class detalleEncuestas(
        var correo_usuario: String = "",
        var estatus: String = "",
        var id_empresa: String = "",
        var id: String = "",
        var id_encuesta: String = "") {
    override fun toString() = "$correo_usuario  $estatus $id_empresa $id_encuesta $id"
}