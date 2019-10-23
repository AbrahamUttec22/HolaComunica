package net.tecgurus.holacomunicate.model
import android.support.annotation.Keep

@Keep
class Clave(var acceso: String = "",
            var id_empresa: String = "") {
    override fun toString() = "$acceso $id_empresa"
}