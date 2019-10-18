package net.tecgurus.holacomunicate.formularios

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alejandrolora.finalapp.goToActivity
import com.google.firebase.auth.FirebaseAuth
import net.tecgurus.holacomunicate.R
import net.tecgurus.holacomunicate.formularios.LoginCardOverlap

/**
 * @author Abraham
 */
class ToolbarBasic : AppCompatActivity() {

    //instance for firebase
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar_basic)
        mAuth.signOut()
        goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
    }


}
