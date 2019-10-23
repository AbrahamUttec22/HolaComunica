package net.tecgurus.holacomunicate.model
import android.support.annotation.Keep
@Keep
class Costos(var id: String = "",
             var plan: String = "",
             var tipo_plan: String = "",
             var costo: String = "") {

    override fun toString() = "$id  $plan $costo $tipo_plan "
}