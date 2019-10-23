package net.tecgurus.holacomunicate.formularios

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.alejandrolora.finalapp.goToActivity
import com.google.firebase.auth.FirebaseAuth
import net.tecgurus.holacomunicate.R
import net.tecgurus.holacomunicate.DashboarActivity
import android.view.animation.AnimationUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_splash.*
import net.tecgurus.holacomunicate.admin.AdminDashboardActivity

/**
 * @author Abraham
 */
class MainEmptyActivity : AppCompatActivity() {

    //instance for firebase
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val adminCollection: CollectionReference

    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        adminCollection = FirebaseFirestore.getInstance().collection("Administrador")
    }

    /**
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val myanim = AnimationUtils.loadAnimation(this, R.anim.floating_up)
        imageLogo.startAnimation(myanim)
        Handler().postDelayed({
            //mAuth.signOut()
            //Validated for know if the user us already login
            if (mAuth.currentUser == null) {
                goToActivity<LoginCardOverlap> {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            } else {
                if (mAuth.currentUser!!.isEmailVerified) {
                    var correo = mAuth.currentUser!!.email.toString()
                    val consul = adminCollection.whereEqualTo("correo", correo)
                    //beggin with consult
                    consul.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                        if (task.isSuccessful) {
                            var con = 0
                            for (document in task.result!!) {
                                con++
                            }
                            if (con == 0) {
                                goToActivity<DashboarActivity> {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                            } else {
                                goToActivity<AdminDashboardActivity> {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                            }

                        } else {
                            Log.w("saasas", "Error getting documents.", task.exception)
                        }
                    })//end for expression lambdas this very cool


                } else {
                    goToActivity<LoginCardOverlap> {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                }
            }


        }, 4200)
    }//end for onCreate()

}