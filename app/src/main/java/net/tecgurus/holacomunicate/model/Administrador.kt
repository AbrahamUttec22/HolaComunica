package net.tecgurus.holacomunicate.model
import android.support.annotation.Keep

@Keep
class Administrador(var correo: String = "",
                    var id: String = "",
                    var imagen: String = "",
                    var contrasena: String = "",
                    var token: String = "") {

    override fun toString() = "$correo  $id $imagen $token $contrasena "
}