package net.tecgurus.holacomunicate
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.LightingColorFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.*
import com.alejandrolora.finalapp.goToActivity
import com.alejandrolora.finalapp.toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_dashboard_administrador.*
import net.tecgurus.holacomunicate.R
import net.tecgurus.holacomunicate.Empresa.PagosEmpresaActivity
import net.tecgurus.holacomunicate.actividadesfragment.GestionActividadesActivity
import net.tecgurus.holacomunicate.actividadesfragmentadmin.*
import net.tecgurus.holacomunicate.formularios.EstatusChecadorActivity
import net.tecgurus.holacomunicate.formularios.UserActivity
import net.tecgurus.holacomunicate.formularios.EncuestaActivity
import net.tecgurus.holacomunicate.formularios.*
import net.tecgurus.holacomunicate.formularios.LoginCardOverlap
import net.tecgurus.holacomunicate.checador.CheckActivity
import net.tecgurus.holacomunicate.checador.GenerarQrJActivity
import net.tecgurus.holacomunicate.model.*
import net.tecgurus.holacomunicate.utils.Tools
import kotlinx.android.synthetic.main.activity_dashboard_empresa.*
import kotlinx.android.synthetic.main.activity_dashboard_usuario.*
import net.tecgurus.holacomunicate.Empresa.MiPlanActivity
import net.tecgurus.holacomunicate.Empresa.PlanesActivity
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Abraham Casas Aguilar
 */
class DashboarActivity : AppCompatActivity() {

    //get instance of firebase
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    //declare val for save the collection
    private val userCollection: CollectionReference
    //declare val for save the collection
    private val empresaCollection: CollectionReference
    private var name: String = ""
    var id_empresa = ""
    //declare val for save the collection
    private val eventosCollection: CollectionReference
    //declare val for save the collection
    private val encuestasCollection: CollectionReference
    //declare val for save the collection
    private val anuncioCollection: CollectionReference
    //declare val for save the collection
    private val usuariosCollection: CollectionReference
    //declare val for save the collection
    private val actividadesCollection: CollectionReference
    //declare val for save the collection
    private val detalleEventosCollection: CollectionReference
    //declare val for save the collection
    private val detalleEncuestasCollection: CollectionReference
    //declare val for save the collection
    private val detalleAnuncioCollection: CollectionReference

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        userCollection = FirebaseFirestore.getInstance().collection("Usuarios")
        empresaCollection = FirebaseFirestore.getInstance().collection("Empresas")
        eventosCollection = FirebaseFirestore.getInstance().collection("Eventos")
        encuestasCollection = FirebaseFirestore.getInstance().collection("Encuestas")
        anuncioCollection = FirebaseFirestore.getInstance().collection("Anuncios")
        usuariosCollection = FirebaseFirestore.getInstance().collection("Usuarios")
        actividadesCollection = FirebaseFirestore.getInstance().collection("Actividades")
        //______
        detalleEventosCollection = FirebaseFirestore.getInstance().collection("detalleEventos")
        detalleEncuestasCollection = FirebaseFirestore.getInstance().collection("detalleEncuestas")
        detalleAnuncioCollection = FirebaseFirestore.getInstance().collection("detalleAnuncios")
    }

    private var rol2 = ""
    private var plan_pago = ""
    private var estado = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
        var rol = sharedPreference.getString("rol", "").toString()
        var sharedPreferencet = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
        id_empresa = sharedPreferencet.getString("id_empresa", "").toString()
        val email = mAuth.currentUser!!.email.toString()
        val empresa = userCollection.whereEqualTo("email", email)
        empresa.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    rol2 = document.get("rol").toString()
                }

                if (rol.equals("empresa")) {
                    setContentView(R.layout.activity_dashboard_empresa)
                    initToolbar()
                    consultasEmpresaNotificaciones()
                    val empresaEstatus = empresaCollection.whereEqualTo("id_empresa", id_empresa)
                    empresaEstatus.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {
                                plan_pago = document.get("estatus").toString()
                                estado = document.get("estado").toString()//1 true 0 false
                            }
                            when (estado) {
                                "0" -> {//empresa
                                    PagosEmpresa.setOnClickListener {
                                        showDialogEmpresaBloqueada()
                                    }
                                    //Perfil y usuarios (3) YA TIENE PROGRAMACION REACTIVA
                                    MiPerfilE.setOnClickListener {
                                        showDialogEmpresaBloqueada()
                                    }
                                    PanelUsuariosE.setOnClickListener {
                                        showDialogEmpresaBloqueada()
                                    }
                                    AdministradorActividadesAdminE.setOnClickListener {
                                        showDialogEmpresaBloqueada()
                                    }
                                    //Checador (6) no es necesario programacion reactiva
                                    GenerarQRE.setOnClickListener {
                                        showDialogEmpresaBloqueada()
                                    }
                                    EstatusChecadorE.setOnClickListener {
                                        showDialogEmpresaBloqueada()
                                    }

                                    EncuestasE.setOnClickListener {
                                        showDialogEmpresaBloqueada()
                                    }
                                    AnunciosE.setOnClickListener {
                                        showDialogEmpresaBloqueada()
                                    }
                                    EventosE.setOnClickListener {
                                        showDialogEmpresaBloqueada()
                                    }

                                    MiPlanE.setOnClickListener {
                                        showDialogEmpresaBloqueada()
                                    }
                                    CerrarSesionE.setOnClickListener {
                                        val builder = AlertDialog.Builder(this)
                                        builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                            mAuth.signOut()
                                            goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                        }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                .show()
                                    }
                                    InfoCodigoEmpresaE.setOnClickListener {
                                        showDialogEmpresaBloqueada()
                                    }
                                    //______________________________________________________________________
                                    MiPerfilE2.setOnClickListener {
                                        showDialogEmpresaBloqueada()
                                    }
                                    PanelUsuariosE2.setOnClickListener {
                                        showDialogEmpresaBloqueada()
                                    }
                                    PagosEmpresa2.setOnClickListener {
                                        showDialogEmpresaBloqueada()
                                    }

                                    //Checador (6) no es necesario programacion reactiva
                                    GenerarQRE2.setOnClickListener {
                                        showDialogEmpresaBloqueada()
                                    }
                                    EstatusChecadorE2.setOnClickListener {
                                        showDialogEmpresaBloqueada()
                                    }
                                    AdministradorActividadesAdminE2.setOnClickListener {
                                        showDialogEmpresaBloqueada()
                                    }

                                    EventosE2.setOnClickListener {
                                        showDialogEmpresaBloqueada()
                                    }
                                    EncuestasE2.setOnClickListener {
                                        showDialogEmpresaBloqueada()
                                    }
                                    AnunciosE2.setOnClickListener {
                                        showDialogEmpresaBloqueada()
                                    }

                                    MiPlanE2.setOnClickListener {
                                        showDialogEmpresaBloqueada()
                                    }
                                    CerrarSesionE2.setOnClickListener {
                                        val builder = AlertDialog.Builder(this)
                                        builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                            mAuth.signOut()
                                            goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                        }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                .show()
                                    }

                                }
                                else -> {
                                    when (plan_pago) {
                                        "mensual" -> {
                                            PagosEmpresa.setOnClickListener {
                                                goToActivity<PagosEmpresaActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }
                                            //Perfil y usuarios (3) YA TIENE PROGRAMACION REACTIVA
                                            MiPerfilE.setOnClickListener {
                                                //403 USUARIOS,404 EMPRESA
                                                //Hacer cnosulta para saber que perfil mostrar
                                                var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                                                var rol = sharedPreference.getString("rol", "").toString()
                                                if (rol.equals("empresa")) {
                                                    //403 USUARIOS,404 EMPRESA
                                                    //Hacer cnosulta para saber que perfil mostrar
                                                    goToActivity<PerfilEmpresaActivity> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                } else if (rol2.equals("administrador") || rol2.equals("usuario")) {
                                                    goToActivity<CardOverlap> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                }
                                            }
                                            PanelUsuariosE.setOnClickListener {
                                                //205
                                                goToActivity<UserActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }

                                            //Checador (6) no es necesario programacion reactiva
                                            GenerarQRE.setOnClickListener {
                                                //25003
                                                goToActivity<GenerarQrJActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }
                                            EstatusChecadorE.setOnClickListener {
                                                //25004
                                                goToActivity<EstatusChecadorActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }

                                            AdministradorActividadesAdminE.setOnClickListener {
                                                goToActivity<GestionActividadesAActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            EventosE.setOnClickListener {
                                                showDialogEventos()
                                            }
                                            EncuestasE.setOnClickListener {
                                                showDialogEncuestas()
                                            }
                                            AnunciosE.setOnClickListener {
                                                showDialogAnuncios()
                                            }

                                            MiPlanE.setOnClickListener {
                                                goToActivity<MiPlanActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }
                                            CerrarSesionE.setOnClickListener {
                                                val builder = AlertDialog.Builder(this)
                                                builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                                    mAuth.signOut()
                                                    goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                        .show()
                                            }
                                            InfoCodigoEmpresaE.setOnClickListener {
                                                showConfirmDialog()
                                            }
                                            //______________________________________________________________________
                                            MiPerfilE2.setOnClickListener {
                                                //403 USUARIOS,404 EMPRESA
                                                //Hacer cnosulta para saber que perfil mostrar
                                                var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                                                var rol = sharedPreference.getString("rol", "").toString()
                                                if (rol.equals("empresa")) {
                                                    //403 USUARIOS,404 EMPRESA
                                                    //Hacer cnosulta para saber que perfil mostrar
                                                    goToActivity<PerfilEmpresaActivity> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                } else if (rol2.equals("administrador") || rol2.equals("usuario")) {
                                                    goToActivity<CardOverlap> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                }
                                            }
                                            PanelUsuariosE2.setOnClickListener {
                                                //205
                                                goToActivity<UserActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }
                                            PagosEmpresa2.setOnClickListener {
                                                goToActivity<PagosEmpresaActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Checador (6) no es necesario programacion reactiva
                                            GenerarQRE2.setOnClickListener {
                                                //25003
                                                goToActivity<GenerarQrJActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }
                                            EstatusChecadorE2.setOnClickListener {
                                                //25004
                                                goToActivity<EstatusChecadorActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }

                                            AdministradorActividadesAdminE2.setOnClickListener {
                                                goToActivity<GestionActividadesAActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            EventosE2.setOnClickListener {
                                                showDialogEventos()
                                            }
                                            EncuestasE2.setOnClickListener {
                                                showDialogEncuestas()
                                            }
                                            AnunciosE2.setOnClickListener {
                                                showDialogAnuncios()
                                            }

                                            MiPlanE2.setOnClickListener {
                                                goToActivity<MiPlanActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }
                                            CerrarSesionE2.setOnClickListener {
                                                val builder = AlertDialog.Builder(this)
                                                builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                                    mAuth.signOut()
                                                    goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                        .show()
                                            }
                                        }
                                        "anual" -> {
                                            PagosEmpresa.setOnClickListener {
                                                goToActivity<PagosEmpresaActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }
                                            //Perfil y usuarios (3) YA TIENE PROGRAMACION REACTIVA
                                            MiPerfilE.setOnClickListener {
                                                //403 USUARIOS,404 EMPRESA
                                                //Hacer cnosulta para saber que perfil mostrar
                                                var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                                                var rol = sharedPreference.getString("rol", "").toString()
                                                if (rol.equals("empresa")) {
                                                    //403 USUARIOS,404 EMPRESA
                                                    //Hacer cnosulta para saber que perfil mostrar
                                                    goToActivity<PerfilEmpresaActivity> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                } else if (rol2.equals("administrador") || rol2.equals("usuario")) {
                                                    goToActivity<CardOverlap> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                }
                                            }
                                            PanelUsuariosE.setOnClickListener {
                                                //205
                                                goToActivity<UserActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }

                                            //Checador (6) no es necesario programacion reactiva
                                            GenerarQRE.setOnClickListener {
                                                //25003
                                                goToActivity<GenerarQrJActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }
                                            EstatusChecadorE.setOnClickListener {
                                                //25004
                                                goToActivity<EstatusChecadorActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }
                                            AdministradorActividadesAdminE.setOnClickListener {
                                                goToActivity<GestionActividadesAActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            EventosE.setOnClickListener {
                                                showDialogEventos()
                                            }
                                            EncuestasE.setOnClickListener {
                                                showDialogEncuestas()
                                            }
                                            AnunciosE.setOnClickListener {
                                                showDialogAnuncios()
                                            }
                                            MiPlanE.setOnClickListener {
                                                goToActivity<MiPlanActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }
                                            CerrarSesionE.setOnClickListener {
                                                val builder = AlertDialog.Builder(this)
                                                builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                                    mAuth.signOut()
                                                    goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                        .show()
                                            }
                                            InfoCodigoEmpresaE.setOnClickListener {
                                                showConfirmDialog()
                                            }
                                            //______________________________________________________________________
                                            MiPerfilE2.setOnClickListener {
                                                //403 USUARIOS,404 EMPRESA
                                                //Hacer cnosulta para saber que perfil mostrar
                                                var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                                                var rol = sharedPreference.getString("rol", "").toString()
                                                if (rol.equals("empresa")) {
                                                    //403 USUARIOS,404 EMPRESA
                                                    //Hacer cnosulta para saber que perfil mostrar
                                                    goToActivity<PerfilEmpresaActivity> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                } else if (rol2.equals("administrador") || rol2.equals("usuario")) {
                                                    goToActivity<CardOverlap> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                }
                                            }
                                            PanelUsuariosE2.setOnClickListener {
                                                //205
                                                goToActivity<UserActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }
                                            PagosEmpresa2.setOnClickListener {
                                                goToActivity<PagosEmpresaActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Checador (6) no es necesario programacion reactiva
                                            GenerarQRE2.setOnClickListener {
                                                //25003
                                                goToActivity<GenerarQrJActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }
                                            EstatusChecadorE2.setOnClickListener {
                                                //25004
                                                goToActivity<EstatusChecadorActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }
                                            AdministradorActividadesAdminE2.setOnClickListener {
                                                goToActivity<GestionActividadesAActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            EventosE2.setOnClickListener {
                                                showDialogEventos()
                                            }
                                            EncuestasE2.setOnClickListener {
                                                showDialogEncuestas()
                                            }
                                            AnunciosE2.setOnClickListener {
                                                showDialogAnuncios()
                                            }

                                            MiPlanE2.setOnClickListener {
                                                goToActivity<MiPlanActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }
                                            CerrarSesionE2.setOnClickListener {
                                                val builder = AlertDialog.Builder(this)
                                                builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                                    mAuth.signOut()
                                                    goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                        .show()
                                            }
                                        }
                                        "pruebainicial" -> {
                                            PagosEmpresa.setOnClickListener {
                                                goToActivity<PagosEmpresaActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }
                                            PagosEmpresa2.setOnClickListener {
                                                goToActivity<PagosEmpresaActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }
                                            //Perfil y usuarios (3) YA TIENE PROGRAMACION REACTIVA
                                            MiPerfilE.setOnClickListener {
                                                //403 USUARIOS,404 EMPRESA
                                                //Hacer cnosulta para saber que perfil mostrar
                                                var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                                                var rol = sharedPreference.getString("rol", "").toString()
                                                if (rol.equals("empresa")) {
                                                    //403 USUARIOS,404 EMPRESA
                                                    //Hacer cnosulta para saber que perfil mostrar
                                                    goToActivity<PerfilEmpresaActivity> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                } else if (rol2.equals("administrador") || rol2.equals("usuario")) {
                                                    goToActivity<CardOverlap> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                }
                                            }
                                            PanelUsuariosE.setOnClickListener {
                                                //205
                                                goToActivity<UserActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }

                                            //Checador (6) no es necesario programacion reactiva
                                            GenerarQRE.setOnClickListener {
                                                //25003
                                                goToActivity<GenerarQrJActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }
                                            EstatusChecadorE.setOnClickListener {
                                                //25004
                                                goToActivity<EstatusChecadorActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }

                                            AdministradorActividadesAdminE.setOnClickListener {
                                                goToActivity<GestionActividadesAActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            EventosE.setOnClickListener {
                                                showDialogEventos()
                                            }
                                            EncuestasE.setOnClickListener {
                                                showDialogEncuestas()
                                            }
                                            AnunciosE.setOnClickListener {
                                                showDialogAnuncios()
                                            }

                                            MiPlanE.setOnClickListener {
                                                goToActivity<MiPlanActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }
                                            CerrarSesionE.setOnClickListener {
                                                val builder = AlertDialog.Builder(this)
                                                builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                                    mAuth.signOut()
                                                    goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                        .show()
                                            }
                                            InfoCodigoEmpresaE.setOnClickListener {
                                                showConfirmDialog()
                                            }
                                            //______________________________________________________________________
                                            MiPerfilE2.setOnClickListener {
                                                //403 USUARIOS,404 EMPRESA
                                                //Hacer cnosulta para saber que perfil mostrar
                                                var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                                                var rol = sharedPreference.getString("rol", "").toString()
                                                if (rol.equals("empresa")) {
                                                    //403 USUARIOS,404 EMPRESA
                                                    //Hacer cnosulta para saber que perfil mostrar
                                                    goToActivity<PerfilEmpresaActivity> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                } else if (rol2.equals("administrador") || rol2.equals("usuario")) {
                                                    goToActivity<CardOverlap> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                }
                                            }
                                            PanelUsuariosE2.setOnClickListener {
                                                //205
                                                goToActivity<UserActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }

                                            //Checador (6) no es necesario programacion reactiva
                                            GenerarQRE2.setOnClickListener {
                                                //25003
                                                goToActivity<GenerarQrJActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }
                                            EstatusChecadorE2.setOnClickListener {
                                                //25004
                                                goToActivity<EstatusChecadorActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }

                                            AdministradorActividadesAdminE2.setOnClickListener {
                                                goToActivity<GestionActividadesAActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            EventosE2.setOnClickListener {
                                                showDialogEventos()
                                            }
                                            EncuestasE2.setOnClickListener {
                                                showDialogEncuestas()
                                            }
                                            AnunciosE2.setOnClickListener {
                                                showDialogAnuncios()
                                            }

                                            MiPlanE2.setOnClickListener {
                                                goToActivity<MiPlanActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }
                                            CerrarSesionE2.setOnClickListener {
                                                val builder = AlertDialog.Builder(this)
                                                builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                                    mAuth.signOut()
                                                    goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                        .show()
                                            }
                                        }
                                        "gratuita" -> {//empresa
                                            PagosEmpresa.setOnClickListener {
                                                goToActivity<PagosEmpresaActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }
                                            //Perfil y usuarios (3) YA TIENE PROGRAMACION REACTIVA
                                            MiPerfilE.setOnClickListener {
                                                showDialog()
                                            }
                                            PanelUsuariosE.setOnClickListener {
                                                showDialog()
                                            }
                                            AdministradorActividadesAdminE.setOnClickListener {
                                                goToActivity<GestionActividadesAActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            EventosE.setOnClickListener {
                                                showDialog()
                                            }

                                            //Checador (6) no es necesario programacion reactiva
                                            GenerarQRE.setOnClickListener {
                                                showDialog()
                                            }
                                            EstatusChecadorE.setOnClickListener {
                                                showDialog()
                                            }

                                            EncuestasE.setOnClickListener {
                                                showDialog()
                                            }
                                            AnunciosE.setOnClickListener {
                                                showDialog()
                                            }

                                            MiPlanE.setOnClickListener {
                                                goToActivity<MiPlanActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }
                                            CerrarSesionE.setOnClickListener {
                                                val builder = AlertDialog.Builder(this)
                                                builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                                    mAuth.signOut()
                                                    goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                        .show()
                                            }
                                            InfoCodigoEmpresaE.setOnClickListener {
                                                showConfirmDialog()
                                            }
                                            //______________________________________________________________________
                                            MiPerfilE2.setOnClickListener {
                                                showDialog()
                                            }
                                            PanelUsuariosE2.setOnClickListener {
                                                showDialog()
                                            }
                                            PagosEmpresa2.setOnClickListener {
                                                goToActivity<PagosEmpresaActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Checador (6) no es necesario programacion reactiva
                                            GenerarQRE2.setOnClickListener {
                                                showDialog()
                                            }
                                            EstatusChecadorE2.setOnClickListener {
                                                showDialog()
                                            }

                                            AdministradorActividadesAdminE2.setOnClickListener {
                                                goToActivity<GestionActividadesAActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            EventosE2.setOnClickListener {
                                                showDialog()
                                            }
                                            EncuestasE2.setOnClickListener {
                                                showDialog()
                                            }
                                            AnunciosE2.setOnClickListener {
                                                showDialog()
                                            }

                                            MiPlanE2.setOnClickListener {
                                                goToActivity<MiPlanActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }
                                            CerrarSesionE2.setOnClickListener {
                                                val builder = AlertDialog.Builder(this)
                                                builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                                    mAuth.signOut()
                                                    goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                        .show()
                                            }
                                        }
                                        else -> {
                                        }
                                    }
                                }
                            }
                        } else {
                            Log.w("saasas", "Error getting documents.", task.exception)
                        }
                    })//end for expression lambdas this very cool
                } else if (rol2.equals("usuario")) {
                    setContentView(R.layout.activity_dashboard_usuario)
                    initToolbar()
                    //MiPerfilU.isEnabled = false
                    //MiPerfilU2.isEnabled = false
                    val empresaEstatus = empresaCollection.whereEqualTo("id_empresa", id_empresa)
                    empresaEstatus.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {
                                plan_pago = document.get("estatus").toString()
                                estado = document.get("estado").toString()//1 true 0 false
                            }
                            when (estado) {
                                "0" -> {//empresa
                                    //Perfil y usuarios (3) YA TIENE PROGRAMACION REACTIVA
                                    MiPerfilU.setOnClickListener {
                                        showDialogUsuarioBloqueada()
                                    }
                                    VerActividadesU.setOnClickListener {
                                        showDialogUsuarioBloqueada()
                                    }

                                    //Checador (6) no es necesario programacion reactiva
                                    ChecarU.setOnClickListener {
                                        showDialogUsuarioBloqueada()
                                    }

                                    //Eventos  (9) YA TIENE PROGRAMACION REACTIVA
                                    VerEventosU.setOnClickListener {
                                        showDialogUsuarioBloqueada()
                                    }
                                    //Encuestas  (12) YA CONTIENE PROGRAMACION REACTIVA
                                    VerEncuestasU.setOnClickListener {
                                        showDialogUsuarioBloqueada()
                                    }
                                    //Anuncios (15) YA CONTIENE PROGRAMACION REACTIVA
                                    VerAnuncioU.setOnClickListener {
                                        showDialogUsuarioBloqueada()
                                    }

                                    CerrarSesionU.setOnClickListener {
                                        val builder = AlertDialog.Builder(this)
                                        builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                            mAuth.signOut()
                                            goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                        }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                .show()
                                    }
                                    //______________________________________________________________________
                                    //Perfil y usuarios (3) YA TIENE PROGRAMACION REACTIVA
                                    MiPerfilU2.setOnClickListener {
                                        showDialogUsuarioBloqueada()
                                    }
                                    VerActividadesU2.setOnClickListener {
                                        showDialogUsuarioBloqueada()
                                    }

                                    //Checador (6) no es necesario programacion reactiva
                                    ChecarU2.setOnClickListener {
                                        showDialogUsuarioBloqueada()
                                    }

                                    //Eventos  (9) YA TIENE PROGRAMACION REACTIVA
                                    VerEventosU2.setOnClickListener {
                                        showDialogUsuarioBloqueada()
                                    }
                                    //Encuestas  (12) YA CONTIENE PROGRAMACION REACTIVA
                                    VerEncuestasU2.setOnClickListener {
                                        showDialogUsuarioBloqueada()
                                    }
                                    //Anuncios (15) YA CONTIENE PROGRAMACION REACTIVA
                                    VerAnuncioU2.setOnClickListener {
                                        showDialogUsuarioBloqueada()
                                    }

                                    CerrarSesionU2.setOnClickListener {
                                        val builder = AlertDialog.Builder(this)
                                        builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                            mAuth.signOut()
                                            goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                        }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                .show()
                                    }
                                }
                                else -> {
                                    when (plan_pago) {
                                        "mensual" -> {
                                            //Perfil y usuarios (3) YA TIENE PROGRAMACION REACTIVA
                                            MiPerfilU.setOnClickListener {
                                                //403 USUARIOS,404 EMPRESA
                                                //Hacer cnosulta para saber que perfil mostrar
                                                var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                                                var rol = sharedPreference.getString("rol", "").toString()
                                                if (rol.equals("empresa")) {
                                                    //403 USUARIOS,404 EMPRESA
                                                    //Hacer cnosulta para saber que perfil mostrar
                                                    goToActivity<PerfilEmpresaActivity> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                } else if (rol2.equals("administrador") || rol2.equals("usuario")) {
                                                    goToActivity<CardOverlap> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                }
                                            }
                                            VerActividadesU.setOnClickListener {
                                                //301
                                                /*goToActivity<ActividadesActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)*/

                                                goToActivity<GestionActividadesActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Checador (6) no es necesario programacion reactiva
                                            ChecarU.setOnClickListener {
                                                //25002
                                                goToActivity<CheckActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Eventos  (9) YA TIENE PROGRAMACION REACTIVA
                                            VerEventosU.setOnClickListener {
                                                //401
                                                goToActivity<CardBasic> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Encuestas  (12) YA CONTIENE PROGRAMACION REACTIVA
                                            VerEncuestasU.setOnClickListener {
                                                //604
                                                goToActivity<EncuestaActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Anuncios (15) YA CONTIENE PROGRAMACION REACTIVA
                                            VerAnuncioU.setOnClickListener {
                                                //405
                                                goToActivity<CardWizardLight> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            CerrarSesionU.setOnClickListener {
                                                val builder = AlertDialog.Builder(this)
                                                builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                                    mAuth.signOut()
                                                    goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                        .show()
                                            }
                                            //______________________________________________________________________
                                            //Perfil y usuarios (3) YA TIENE PROGRAMACION REACTIVA
                                            MiPerfilU2.setOnClickListener {
                                                //403 USUARIOS,404 EMPRESA
                                                //Hacer cnosulta para saber que perfil mostrar
                                                var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                                                var rol = sharedPreference.getString("rol", "").toString()
                                                if (rol.equals("empresa")) {
                                                    //403 USUARIOS,404 EMPRESA
                                                    //Hacer cnosulta para saber que perfil mostrar
                                                    goToActivity<PerfilEmpresaActivity> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                } else if (rol2.equals("administrador") || rol2.equals("usuario")) {
                                                    goToActivity<CardOverlap> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                }
                                            }
                                            VerActividadesU2.setOnClickListener {
                                                //301
                                                /*goToActivity<ActividadesActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)*/

                                                goToActivity<GestionActividadesActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Checador (6) no es necesario programacion reactiva
                                            ChecarU2.setOnClickListener {
                                                //25002
                                                goToActivity<CheckActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Eventos  (9) YA TIENE PROGRAMACION REACTIVA
                                            VerEventosU2.setOnClickListener {
                                                //401
                                                goToActivity<CardBasic> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Encuestas  (12) YA CONTIENE PROGRAMACION REACTIVA
                                            VerEncuestasU2.setOnClickListener {
                                                //604
                                                goToActivity<EncuestaActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Anuncios (15) YA CONTIENE PROGRAMACION REACTIVA
                                            VerAnuncioU2.setOnClickListener {
                                                //405
                                                goToActivity<CardWizardLight> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            CerrarSesionU2.setOnClickListener {
                                                val builder = AlertDialog.Builder(this)
                                                builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                                    mAuth.signOut()
                                                    goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                        .show()
                                            }
                                        }
                                        "anual" -> {
                                            //Perfil y usuarios (3) YA TIENE PROGRAMACION REACTIVA
                                            MiPerfilU.setOnClickListener {
                                                //403 USUARIOS,404 EMPRESA
                                                //Hacer cnosulta para saber que perfil mostrar
                                                var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                                                var rol = sharedPreference.getString("rol", "").toString()
                                                if (rol.equals("empresa")) {
                                                    //403 USUARIOS,404 EMPRESA
                                                    //Hacer cnosulta para saber que perfil mostrar
                                                    goToActivity<PerfilEmpresaActivity> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                } else if (rol2.equals("administrador") || rol2.equals("usuario")) {
                                                    goToActivity<CardOverlap> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                }
                                            }
                                            VerActividadesU.setOnClickListener {
                                                //301
                                                /*goToActivity<ActividadesActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)*/

                                                goToActivity<GestionActividadesActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Checador (6) no es necesario programacion reactiva
                                            ChecarU.setOnClickListener {
                                                //25002
                                                goToActivity<CheckActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Eventos  (9) YA TIENE PROGRAMACION REACTIVA
                                            VerEventosU.setOnClickListener {
                                                //401
                                                goToActivity<CardBasic> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }
                                            //Encuestas  (12) YA CONTIENE PROGRAMACION REACTIVA
                                            VerEncuestasU.setOnClickListener {
                                                //604
                                                goToActivity<EncuestaActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }
                                            //Anuncios (15) YA CONTIENE PROGRAMACION REACTIVA
                                            VerAnuncioU.setOnClickListener {
                                                //405
                                                goToActivity<CardWizardLight> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            CerrarSesionU.setOnClickListener {
                                                val builder = AlertDialog.Builder(this)
                                                builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                                    mAuth.signOut()
                                                    goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                        .show()
                                            }
                                            //______________________________________________________________________
                                            //Perfil y usuarios (3) YA TIENE PROGRAMACION REACTIVA
                                            MiPerfilU2.setOnClickListener {
                                                //403 USUARIOS,404 EMPRESA
                                                //Hacer cnosulta para saber que perfil mostrar
                                                var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                                                var rol = sharedPreference.getString("rol", "").toString()
                                                if (rol.equals("empresa")) {
                                                    //403 USUARIOS,404 EMPRESA
                                                    //Hacer cnosulta para saber que perfil mostrar
                                                    goToActivity<PerfilEmpresaActivity> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                } else if (rol2.equals("administrador") || rol2.equals("usuario")) {
                                                    goToActivity<CardOverlap> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                }
                                            }
                                            VerActividadesU2.setOnClickListener {
                                                //301
                                                /*goToActivity<ActividadesActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)*/

                                                goToActivity<GestionActividadesActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Checador (6) no es necesario programacion reactiva
                                            ChecarU2.setOnClickListener {
                                                //25002
                                                goToActivity<CheckActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Eventos  (9) YA TIENE PROGRAMACION REACTIVA
                                            VerEventosU2.setOnClickListener {
                                                //401
                                                goToActivity<CardBasic> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Encuestas  (12) YA CONTIENE PROGRAMACION REACTIVA
                                            VerEncuestasU2.setOnClickListener {
                                                //604
                                                goToActivity<EncuestaActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Anuncios (15) YA CONTIENE PROGRAMACION REACTIVA
                                            VerAnuncioU2.setOnClickListener {
                                                //405
                                                goToActivity<CardWizardLight> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            CerrarSesionU2.setOnClickListener {
                                                val builder = AlertDialog.Builder(this)
                                                builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                                    mAuth.signOut()
                                                    goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                        .show()
                                            }
                                        }
                                        "pruebainicial" -> {
                                            //Perfil y usuarios (3) YA TIENE PROGRAMACION REACTIVA
                                            MiPerfilU.setOnClickListener {
                                                //403 USUARIOS,404 EMPRESA
                                                //Hacer cnosulta para saber que perfil mostrar
                                                var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                                                var rol = sharedPreference.getString("rol", "").toString()
                                                if (rol.equals("empresa")) {
                                                    //403 USUARIOS,404 EMPRESA
                                                    //Hacer cnosulta para saber que perfil mostrar
                                                    goToActivity<PerfilEmpresaActivity> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                } else if (rol2.equals("administrador") || rol2.equals("usuario")) {
                                                    goToActivity<CardOverlap> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                }
                                            }
                                            VerActividadesU.setOnClickListener {
                                                //301
                                                /*goToActivity<ActividadesActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)*/

                                                goToActivity<GestionActividadesActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Checador (6) no es necesario programacion reactiva
                                            ChecarU.setOnClickListener {
                                                //25002
                                                goToActivity<CheckActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Eventos  (9) YA TIENE PROGRAMACION REACTIVA
                                            VerEventosU.setOnClickListener {
                                                //401
                                                goToActivity<CardBasic> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Encuestas  (12) YA CONTIENE PROGRAMACION REACTIVA
                                            VerEncuestasU.setOnClickListener {
                                                //604
                                                goToActivity<EncuestaActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Anuncios (15) YA CONTIENE PROGRAMACION REACTIVA
                                            VerAnuncioU.setOnClickListener {
                                                //405
                                                goToActivity<CardWizardLight> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            CerrarSesionU.setOnClickListener {
                                                val builder = AlertDialog.Builder(this)
                                                builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                                    mAuth.signOut()
                                                    goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                        .show()
                                            }
                                            //______________________________________________________________________
                                            //Perfil y usuarios (3) YA TIENE PROGRAMACION REACTIVA
                                            MiPerfilU2.setOnClickListener {
                                                //403 USUARIOS,404 EMPRESA
                                                //Hacer cnosulta para saber que perfil mostrar
                                                var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                                                var rol = sharedPreference.getString("rol", "").toString()
                                                if (rol.equals("empresa")) {
                                                    //403 USUARIOS,404 EMPRESA
                                                    //Hacer cnosulta para saber que perfil mostrar
                                                    goToActivity<PerfilEmpresaActivity> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                } else if (rol2.equals("administrador") || rol2.equals("usuario")) {
                                                    goToActivity<CardOverlap> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                }
                                            }
                                            VerActividadesU2.setOnClickListener {
                                                //301
                                                /*goToActivity<ActividadesActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)*/

                                                goToActivity<GestionActividadesActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Checador (6) no es necesario programacion reactiva
                                            ChecarU2.setOnClickListener {
                                                //25002
                                                goToActivity<CheckActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Eventos  (9) YA TIENE PROGRAMACION REACTIVA
                                            VerEventosU2.setOnClickListener {
                                                //401
                                                goToActivity<CardBasic> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Encuestas  (12) YA CONTIENE PROGRAMACION REACTIVA
                                            VerEncuestasU2.setOnClickListener {
                                                //604
                                                goToActivity<EncuestaActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Anuncios (15) YA CONTIENE PROGRAMACION REACTIVA
                                            VerAnuncioU2.setOnClickListener {
                                                //405
                                                goToActivity<CardWizardLight> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            CerrarSesionU2.setOnClickListener {
                                                val builder = AlertDialog.Builder(this)
                                                builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                                    mAuth.signOut()
                                                    goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                        .show()
                                            }
                                        }
                                        "gratuita" -> {//empresa

                                            //Perfil y usuarios (3) YA TIENE PROGRAMACION REACTIVA
                                            MiPerfilU.setOnClickListener {
                                                showDialogInfoPagoUsuario()
                                            }
                                            VerActividadesU.setOnClickListener {
                                                //301
                                                /*goToActivity<ActividadesActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)*/

                                                goToActivity<GestionActividadesActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Checador (6) no es necesario programacion reactiva
                                            ChecarU.setOnClickListener {
                                                showDialogInfoPagoUsuario()
                                            }

                                            //Eventos  (9) YA TIENE PROGRAMACION REACTIVA
                                            VerEventosU.setOnClickListener {
                                                //401
                                                goToActivity<CardBasic> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Encuestas  (12) YA CONTIENE PROGRAMACION REACTIVA
                                            VerEncuestasU.setOnClickListener {
                                                //604
                                                goToActivity<EncuestaActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Anuncios (15) YA CONTIENE PROGRAMACION REACTIVA
                                            VerAnuncioU.setOnClickListener {
                                                //405
                                                goToActivity<CardWizardLight> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            CerrarSesionU.setOnClickListener {
                                                val builder = AlertDialog.Builder(this)
                                                builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                                    mAuth.signOut()
                                                    goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                        .show()
                                            }
                                            //______________________________________________________________________
                                            //Perfil y usuarios (3) YA TIENE PROGRAMACION REACTIVA
                                            MiPerfilU2.setOnClickListener {
                                                showDialogInfoPagoUsuario()
                                            }
                                            VerActividadesU2.setOnClickListener {
                                                //301
                                                /*goToActivity<ActividadesActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)*/

                                                goToActivity<GestionActividadesActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Checador (6) no es necesario programacion reactiva
                                            ChecarU2.setOnClickListener {
                                                showDialogInfoPagoUsuario()
                                            }

                                            //Eventos  (9) YA TIENE PROGRAMACION REACTIVA
                                            VerEventosU2.setOnClickListener {
                                                //401
                                                goToActivity<CardBasic> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Encuestas  (12) YA CONTIENE PROGRAMACION REACTIVA
                                            VerEncuestasU2.setOnClickListener {
                                                //604
                                                goToActivity<EncuestaActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Anuncios (15) YA CONTIENE PROGRAMACION REACTIVA
                                            VerAnuncioU2.setOnClickListener {
                                                //405
                                                goToActivity<CardWizardLight> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            CerrarSesionU2.setOnClickListener {
                                                val builder = AlertDialog.Builder(this)
                                                builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                                    mAuth.signOut()
                                                    goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                        .show()
                                            }
                                        }
                                        else -> {
                                        }
                                    }
                                }
                            }

                        } else {
                            Log.w("saasas", "Error getting documents.", task.exception)
                        }

                    })//end for expression lambdas this very cool
                    consultasUsuarioNotificaciones()
                    //   getDataUser()
                } else if (rol2.equals("administrador")) {
                    setContentView(R.layout.activity_dashboard_administrador)
                    initToolbar()
                    consultasAdministradorNotificaciones()
                    val empresaEstatus = empresaCollection.whereEqualTo("id_empresa", id_empresa)
                    empresaEstatus.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {
                                plan_pago = document.get("estatus").toString()
                                estado = document.get("estado").toString()//1 true 0 false
                            }

                            when (estado) {
                                "0" -> {//empresa
                                    //Perfil y usuarios (3) YA TIENE PROGRAMACION REACTIVA
                                    MiPerfilA.setOnClickListener {
                                        showDialogAdministradorBloqueada()
                                    }
                                    PanelUsuariosA.setOnClickListener {
                                        showDialogAdministradorBloqueada()
                                    }
                                    VerActividadesA.setOnClickListener {
                                        showDialogAdministradorBloqueada()
                                    }

                                    //Checador (6) no es necesario programacion reactiva
                                    ChecarA.setOnClickListener {
                                        showDialogAdministradorBloqueada()
                                    }
                                    GenerarQRA.setOnClickListener {
                                        showDialogAdministradorBloqueada()
                                    }
                                    EstatusChecadorA.setOnClickListener {
                                        showDialogAdministradorBloqueada()
                                    }

                                    AdministradorActividadesAdmin.setOnClickListener {
                                        showDialogAdministradorBloqueada()
                                    }

                                    //Eventos  (9) YA TIENE PROGRAMACION REACTIVA
                                    EventosA.setOnClickListener {
                                        showDialogAdministradorBloqueada()
                                    }
                                    EncuestasA.setOnClickListener {
                                        showDialogAdministradorBloqueada()
                                    }
                                    AnunciosA.setOnClickListener {
                                        showDialogAdministradorBloqueada()
                                    }
                                    CerrarSesionA.setOnClickListener {
                                        val builder = AlertDialog.Builder(this)
                                        builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                            mAuth.signOut()
                                            goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                        }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                .show()
                                    }
                                    //______________________________________________________________________
                                    MiPerfilA2.setOnClickListener {
                                        showDialogAdministradorBloqueada()
                                    }
                                    PanelUsuariosA2.setOnClickListener {
                                        showDialogAdministradorBloqueada()
                                    }
                                    VerActividadesA2.setOnClickListener {
                                        showDialogAdministradorBloqueada()
                                    }

                                    //Checador (6) no es necesario programacion reactiva
                                    ChecarA2.setOnClickListener {
                                        showDialogAdministradorBloqueada()
                                    }
                                    GenerarQRA2.setOnClickListener {
                                        showDialogAdministradorBloqueada()
                                    }
                                    EstatusChecadorA2.setOnClickListener {
                                        showDialogAdministradorBloqueada()
                                    }

                                    AdministradorActividadesAdmin2.setOnClickListener {
                                        showDialogAdministradorBloqueada()
                                    }

                                    EventosA2.setOnClickListener {
                                        showDialogAdministradorBloqueada()
                                    }
                                    EncuestasA2.setOnClickListener {
                                        showDialogAdministradorBloqueada()
                                    }
                                    AnunciosA2.setOnClickListener {
                                        showDialogAdministradorBloqueada()
                                    }

                                    CerrarSesionA2.setOnClickListener {
                                        val builder = AlertDialog.Builder(this)
                                        builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                            mAuth.signOut()
                                            goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                        }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                .show()
                                    }
                                }
                                else -> {
                                    when (plan_pago) {
                                        "mensual" -> {
                                            //Perfil y usuarios (3) YA TIENE PROGRAMACION REACTIVA
                                            MiPerfilA.setOnClickListener {
                                                //403 USUARIOS,404 EMPRESA
                                                //Hacer cnosulta para saber que perfil mostrar
                                                var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                                                var rol = sharedPreference.getString("rol", "").toString()
                                                if (rol.equals("empresa")) {
                                                    //403 USUARIOS,404 EMPRESA
                                                    //Hacer cnosulta para saber que perfil mostrar
                                                    goToActivity<PerfilEmpresaActivity> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                } else if (rol2.equals("administrador") || rol2.equals("usuario")) {
                                                    goToActivity<CardOverlap> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                }
                                            }
                                            PanelUsuariosA.setOnClickListener {
                                                //205
                                                goToActivity<UserActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }
                                            VerActividadesA.setOnClickListener {
                                                //301
                                                /* goToActivity<ActividadesActivity> {
                                                     flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                 }
                                                 overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)*/
                                                goToActivity<GestionActividadesActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Checador (6) no es necesario programacion reactiva
                                            ChecarA.setOnClickListener {
                                                //25002
                                                goToActivity<CheckActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }
                                            GenerarQRA.setOnClickListener {
                                                //25003
                                                goToActivity<GenerarQrJActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }
                                            EstatusChecadorA.setOnClickListener {
                                                //25004
                                                goToActivity<EstatusChecadorActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }

                                            AdministradorActividadesAdmin.setOnClickListener {
                                                goToActivity<GestionActividadesAActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            EventosA.setOnClickListener {
                                                showDialogEventos()
                                            }
                                            EncuestasA.setOnClickListener {
                                                showDialogAdministradorBloqueada()
                                            }
                                            AnunciosA.setOnClickListener {
                                                showDialogAdministradorBloqueada()
                                            }

                                            CerrarSesionA.setOnClickListener {
                                                val builder = AlertDialog.Builder(this)
                                                builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                                    mAuth.signOut()
                                                    goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                        .show()
                                            }
                                            //______________________________________________________________________
                                            MiPerfilA2.setOnClickListener {
                                                //403 USUARIOS,404 EMPRESA
                                                //Hacer cnosulta para saber que perfil mostrar
                                                var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                                                var rol = sharedPreference.getString("rol", "").toString()
                                                if (rol.equals("empresa")) {
                                                    //403 USUARIOS,404 EMPRESA
                                                    //Hacer cnosulta para saber que perfil mostrar
                                                    goToActivity<PerfilEmpresaActivity> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                } else if (rol2.equals("administrador") || rol2.equals("usuario")) {
                                                    goToActivity<CardOverlap> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                }
                                            }
                                            PanelUsuariosA2.setOnClickListener {
                                                //205
                                                goToActivity<UserActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }
                                            VerActividadesA2.setOnClickListener {
                                                //301
                                                /* goToActivity<ActividadesActivity> {
                                                     flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                 }
                                                 overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)*/
                                                goToActivity<GestionActividadesActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Checador (6) no es necesario programacion reactiva
                                            ChecarA2.setOnClickListener {
                                                //25002
                                                goToActivity<CheckActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }
                                            GenerarQRA2.setOnClickListener {
                                                //25003
                                                goToActivity<GenerarQrJActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }
                                            EstatusChecadorA2.setOnClickListener {
                                                //25004
                                                goToActivity<EstatusChecadorActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }

                                            AdministradorActividadesAdmin2.setOnClickListener {
                                                goToActivity<GestionActividadesAActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            EventosA2.setOnClickListener {
                                                showDialogEventos()
                                            }
                                            EncuestasA2.setOnClickListener {
                                                showDialogAnuncios()
                                            }
                                            AnunciosA2.setOnClickListener {
                                                showDialogAdministradorBloqueada()
                                            }

                                            CerrarSesionA2.setOnClickListener {
                                                val builder = AlertDialog.Builder(this)
                                                builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                                    mAuth.signOut()
                                                    goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                        .show()
                                            }
                                        }
                                        "anual" -> {
                                            //Perfil y usuarios (3) YA TIENE PROGRAMACION REACTIVA
                                            MiPerfilA.setOnClickListener {
                                                //403 USUARIOS,404 EMPRESA
                                                //Hacer cnosulta para saber que perfil mostrar
                                                var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                                                var rol = sharedPreference.getString("rol", "").toString()
                                                if (rol.equals("empresa")) {
                                                    //403 USUARIOS,404 EMPRESA
                                                    //Hacer cnosulta para saber que perfil mostrar
                                                    goToActivity<PerfilEmpresaActivity> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                } else if (rol2.equals("administrador") || rol2.equals("usuario")) {
                                                    goToActivity<CardOverlap> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                }
                                            }
                                            PanelUsuariosA.setOnClickListener {
                                                //205
                                                goToActivity<UserActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }
                                            VerActividadesA.setOnClickListener {
                                                //301
                                                /* goToActivity<ActividadesActivity> {
                                                     flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                 }
                                                 overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)*/
                                                goToActivity<GestionActividadesActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Checador (6) no es necesario programacion reactiva
                                            ChecarA.setOnClickListener {
                                                //25002
                                                goToActivity<CheckActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }
                                            GenerarQRA.setOnClickListener {
                                                //25003
                                                goToActivity<GenerarQrJActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }
                                            EstatusChecadorA.setOnClickListener {
                                                //25004
                                                goToActivity<EstatusChecadorActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }

                                            AdministradorActividadesAdmin.setOnClickListener {
                                                goToActivity<GestionActividadesAActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            EventosA.setOnClickListener {
                                                showDialogEventos()
                                            }
                                            EncuestasA.setOnClickListener {
                                                showDialogEncuestas()
                                            }
                                            AnunciosA.setOnClickListener {
                                                showDialogAnuncios()
                                            }


                                            CerrarSesionA.setOnClickListener {
                                                val builder = AlertDialog.Builder(this)
                                                builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                                    mAuth.signOut()
                                                    goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                        .show()
                                            }
                                            //______________________________________________________________________
                                            MiPerfilA2.setOnClickListener {
                                                //403 USUARIOS,404 EMPRESA
                                                //Hacer cnosulta para saber que perfil mostrar
                                                var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                                                var rol = sharedPreference.getString("rol", "").toString()
                                                if (rol.equals("empresa")) {
                                                    //403 USUARIOS,404 EMPRESA
                                                    //Hacer cnosulta para saber que perfil mostrar
                                                    goToActivity<PerfilEmpresaActivity> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                } else if (rol2.equals("administrador") || rol2.equals("usuario")) {
                                                    goToActivity<CardOverlap> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                }
                                            }
                                            PanelUsuariosA2.setOnClickListener {
                                                //205
                                                goToActivity<UserActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }
                                            VerActividadesA2.setOnClickListener {
                                                //301
                                                /* goToActivity<ActividadesActivity> {
                                                     flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                 }
                                                 overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)*/
                                                goToActivity<GestionActividadesActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Checador (6) no es necesario programacion reactiva
                                            ChecarA2.setOnClickListener {
                                                //25002
                                                goToActivity<CheckActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }
                                            GenerarQRA2.setOnClickListener {
                                                //25003
                                                goToActivity<GenerarQrJActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }
                                            EstatusChecadorA2.setOnClickListener {
                                                //25004
                                                goToActivity<EstatusChecadorActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }

                                            AdministradorActividadesAdmin2.setOnClickListener {
                                                goToActivity<GestionActividadesAActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            EventosA2.setOnClickListener {
                                                showDialogEventos()
                                            }
                                            EncuestasA2.setOnClickListener {
                                                showDialogEncuestas()
                                            }
                                            AnunciosA2.setOnClickListener {
                                                showDialogAnuncios()
                                            }

                                            CerrarSesionA2.setOnClickListener {
                                                val builder = AlertDialog.Builder(this)
                                                builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                                    mAuth.signOut()
                                                    goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                        .show()
                                            }
                                        }
                                        "pruebainicial" -> {
                                            //Perfil y usuarios (3) YA TIENE PROGRAMACION REACTIVA
                                            MiPerfilA.setOnClickListener {
                                                //403 USUARIOS,404 EMPRESA
                                                //Hacer cnosulta para saber que perfil mostrar
                                                var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                                                var rol = sharedPreference.getString("rol", "").toString()
                                                if (rol.equals("empresa")) {
                                                    //403 USUARIOS,404 EMPRESA
                                                    //Hacer cnosulta para saber que perfil mostrar
                                                    goToActivity<PerfilEmpresaActivity> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                } else if (rol2.equals("administrador") || rol2.equals("usuario")) {
                                                    goToActivity<CardOverlap> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                }
                                            }
                                            PanelUsuariosA.setOnClickListener {
                                                //205
                                                goToActivity<UserActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }
                                            VerActividadesA.setOnClickListener {
                                                //301
                                                /* goToActivity<ActividadesActivity> {
                                                     flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                 }
                                                 overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)*/
                                                goToActivity<GestionActividadesActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Checador (6) no es necesario programacion reactiva
                                            ChecarA.setOnClickListener {
                                                //25002
                                                goToActivity<CheckActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }
                                            GenerarQRA.setOnClickListener {
                                                //25003
                                                goToActivity<GenerarQrJActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }
                                            EstatusChecadorA.setOnClickListener {
                                                //25004
                                                goToActivity<EstatusChecadorActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }

                                            AdministradorActividadesAdmin.setOnClickListener {
                                                goToActivity<GestionActividadesAActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            EventosA.setOnClickListener {
                                                showDialogEventos()
                                            }
                                            EncuestasA.setOnClickListener {
                                                showDialogEncuestas()
                                            }
                                            AnunciosA.setOnClickListener {
                                                showDialogAnuncios()
                                            }

                                            CerrarSesionA.setOnClickListener {
                                                val builder = AlertDialog.Builder(this)
                                                builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                                    mAuth.signOut()
                                                    goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                        .show()
                                            }
                                            //______________________________________________________________________
                                            MiPerfilA2.setOnClickListener {
                                                //403 USUARIOS,404 EMPRESA
                                                //Hacer cnosulta para saber que perfil mostrar
                                                var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                                                var rol = sharedPreference.getString("rol", "").toString()
                                                if (rol.equals("empresa")) {
                                                    //403 USUARIOS,404 EMPRESA
                                                    //Hacer cnosulta para saber que perfil mostrar
                                                    goToActivity<PerfilEmpresaActivity> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                } else if (rol2.equals("administrador") || rol2.equals("usuario")) {
                                                    goToActivity<CardOverlap> {
                                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                                }
                                            }
                                            PanelUsuariosA2.setOnClickListener {
                                                //205
                                                goToActivity<UserActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }
                                            VerActividadesA2.setOnClickListener {
                                                //301
                                                /* goToActivity<ActividadesActivity> {
                                                     flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                 }
                                                 overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)*/
                                                goToActivity<GestionActividadesActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Checador (6) no es necesario programacion reactiva
                                            ChecarA2.setOnClickListener {
                                                //25002
                                                goToActivity<CheckActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }
                                            GenerarQRA2.setOnClickListener {
                                                //25003
                                                goToActivity<GenerarQrJActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }
                                            EstatusChecadorA2.setOnClickListener {
                                                //25004
                                                goToActivity<EstatusChecadorActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                                            }

                                            AdministradorActividadesAdmin2.setOnClickListener {
                                                goToActivity<GestionActividadesAActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            EventosA2.setOnClickListener {
                                                showDialogEventos()
                                            }
                                            EncuestasA2.setOnClickListener {
                                                showDialogEncuestas()
                                            }
                                            AnunciosA2.setOnClickListener {
                                                showDialogAnuncios()
                                            }


                                            CerrarSesionA2.setOnClickListener {
                                                val builder = AlertDialog.Builder(this)
                                                builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                                    mAuth.signOut()
                                                    goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                        .show()
                                            }
                                        }
                                        "gratuita" -> {//empresa
                                            //Perfil y usuarios (3) YA TIENE PROGRAMACION REACTIVA
                                            MiPerfilA.setOnClickListener {
                                                showDialogInfoPagoAdministrador()
                                            }
                                            PanelUsuariosA.setOnClickListener {
                                                showDialogInfoPagoAdministrador()
                                            }
                                            VerActividadesA.setOnClickListener {
                                                //301
                                                /* goToActivity<ActividadesActivity> {
                                                     flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                 }
                                                 overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)*/
                                                goToActivity<GestionActividadesActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Checador (6) no es necesario programacion reactiva
                                            ChecarA.setOnClickListener {
                                                showDialogInfoPagoAdministrador()
                                            }
                                            GenerarQRA.setOnClickListener {
                                                showDialogInfoPagoAdministrador()
                                            }
                                            EstatusChecadorA.setOnClickListener {
                                                showDialogInfoPagoAdministrador()
                                            }

                                            AdministradorActividadesAdmin.setOnClickListener {
                                                goToActivity<GestionActividadesAActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            EventosA.setOnClickListener {
                                                showDialogInfoPagoAdministrador()
                                            }
                                            EncuestasA.setOnClickListener {
                                                showDialogInfoPagoAdministrador()
                                            }
                                            AnunciosA.setOnClickListener {
                                                showDialogInfoPagoAdministrador()
                                            }


                                            CerrarSesionA.setOnClickListener {
                                                val builder = AlertDialog.Builder(this)
                                                builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                                    mAuth.signOut()
                                                    goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                        .show()
                                            }
                                            //______________________________________________________________________
                                            MiPerfilA2.setOnClickListener {
                                                showDialogInfoPagoAdministrador()
                                            }
                                            PanelUsuariosA2.setOnClickListener {
                                                showDialogInfoPagoAdministrador()
                                            }
                                            VerActividadesA2.setOnClickListener {
                                                //301
                                                /* goToActivity<ActividadesActivity> {
                                                     flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                 }
                                                 overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)*/
                                                goToActivity<GestionActividadesActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            //Checador (6) no es necesario programacion reactiva
                                            ChecarA2.setOnClickListener {
                                                showDialogInfoPagoAdministrador()
                                            }
                                            GenerarQRA2.setOnClickListener {
                                                showDialogInfoPagoAdministrador()
                                            }
                                            EstatusChecadorA2.setOnClickListener {
                                                showDialogInfoPagoAdministrador()
                                            }

                                            AdministradorActividadesAdmin2.setOnClickListener {
                                                goToActivity<GestionActividadesAActivity> {
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                }
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                            }

                                            EventosA2.setOnClickListener {
                                                showDialogInfoPagoAdministrador()
                                            }
                                            EncuestasA2.setOnClickListener {
                                                showDialogInfoPagoAdministrador()
                                            }
                                            AnunciosA2.setOnClickListener {
                                                showDialogInfoPagoAdministrador()
                                            }
                                            CerrarSesionA2.setOnClickListener {
                                                val builder = AlertDialog.Builder(this)
                                                builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                                                    mAuth.signOut()
                                                    goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                                                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                                        .show()
                                            }
                                        }
                                        else -> {
                                        }
                                    }
                                }
                            }

                        } else {
                            Log.w("saasas", "Error getting documents.", task.exception)
                        }
                    })//end for expression lambdas this very cool
                    //   getDataUser()
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }
        })//end for expression lambdas this very cool
        validarPlan()
        //getDataUser()
        //apartir de aqui se van a hacer instancias a las ventanas correspondientes
    }

    private fun consultasEmpresaNotificaciones() {
        var email_mio = mAuth.currentUser!!.email.toString()

        val consultaUsuario = userCollection.whereEqualTo("id_empresa", id_empresa)
        //beggin with consult
        consultaUsuario.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var con = 0
                for (document in task.result!!) {
                    con++
                }
                if (con == 0) {
                    EmpresaUsuariosN.text = "0"
                } else {
                    EmpresaUsuariosN.text = con.toString()
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool
    }

    private fun consultasAdministradorNotificaciones() {
        var email_mio = mAuth.currentUser!!.email.toString()
        val consultaUsuario = userCollection.whereEqualTo("id_empresa", id_empresa)
        //beggin with consult
        consultaUsuario.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                val itemUsuario = ArrayList<Usuario>()//lista local de una sola instancia
                var con = 0
                for (document in task.result!!) {
                    con++
                }
                if (con == 0) {
                    AdministradorUsuariosN.text = "0"
                } else {
                    AdministradorUsuariosN.text = con.toString()
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool
        //____________________________________________________________________
        var email = mAuth.currentUser!!.email.toString()
        val consultaActividad = actividadesCollection.whereEqualTo("correo", email).whereEqualTo("estatus", "actividades")
        //beggin with consult
        consultaActividad.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var con = 0
                for (document in task.result!!) {
                    con++
                }
                if (con == 0) {
                    AdministradorActividadesN.text = "0"

                } else {
                    AdministradorActividadesN.text = con.toString()
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool
    }

    private fun consultasUsuarioNotificaciones() {
        var email_mio = mAuth.currentUser!!.email.toString()
        //____________________________________________________________________
        var email = mAuth.currentUser!!.email.toString()
        val consultaActividad = actividadesCollection.whereEqualTo("correo", email).whereEqualTo("estatus", "actividades")
        //beggin with consult
        consultaActividad.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var con = 0
                for (document in task.result!!) {
                    con++
                }
                if (con == 0) {
                    UsuarioVerActividadesN.text = "0"

                } else {
                    UsuarioVerActividadesN.text = con.toString()
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool
        //____________________________________________________________________
        val consultaEvento = detalleEventosCollection.whereEqualTo("id_empresa", id_empresa).whereEqualTo("correo_usuario", email_mio).whereEqualTo("estatus", "0")
        //beggin with consult
        consultaEvento.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var con = 0
                for (document in task.result!!) {
                    con++

                }
                if (con == 0) {
                    UsuarioVerEventosN.text = "0"

                } else {
                    UsuarioVerEventosN.text = con.toString()
                }

            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool
        //____________________________________________________________________
        val consultaEncuestas = detalleEncuestasCollection.whereEqualTo("id_empresa", id_empresa).whereEqualTo("correo_usuario", email_mio).whereEqualTo("estatus", "0")
        //beggin with consult
        consultaEncuestas.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var con = 0
                for (document in task.result!!) {
                    con++
                }
                if (con == 0) {
                    UsuarioVerEncuestasN.text = "0"

                } else {
                    UsuarioVerEncuestasN.text = con.toString()
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool
        //____________________________________________________________________

        val anuncioConsulta = detalleAnuncioCollection.whereEqualTo("id_empresa", id_empresa).whereEqualTo("correo_usuario", email_mio).whereEqualTo("estatus", "0")
        //beggin with consult
        anuncioConsulta.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var con = 0
                for (document in task.result!!) {
                    con++
                }
                if (con == 0) {
                    UsuarioVerAnunciosN.text = "0"

                } else {
                    UsuarioVerAnunciosN.text = con.toString()
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool
    }

    //Pruebas pasadas con exito
    private fun validarPlan() {
        val empresaEstatus = empresaCollection.whereEqualTo("id_empresa", id_empresa)
        empresaEstatus.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var FECHA_VENCIMIENTO = ""
                var id = ""
                for (document in task.result!!) {
                    FECHA_VENCIMIENTO = document.get("fecha_vencimiento_plan").toString()
                    id = document.id
                }

                val c = Calendar.getInstance()
                val df = SimpleDateFormat("dd/MM/yyyy")
                val FECHA_HOY = df.format(c.getTime()).toString()

                var dia_vencimiento = FECHA_VENCIMIENTO.substring(0, 2).toInt()//dd
                var mes_vencimiento = FECHA_VENCIMIENTO.substring(3, 5).toInt()//mm
                var ano_vencimiento = FECHA_VENCIMIENTO.substring(6, 10).toInt()//yyyy


                var dia_hoy = FECHA_HOY.substring(0, 2).toInt()//dd
                var mes_hoy = FECHA_HOY.substring(3, 5).toInt()//mm
                var ano_hoy = FECHA_HOY.substring(6, 10).toInt()//yyyy

                var cont = 0
                if (mes_vencimiento == mes_hoy && ano_vencimiento == ano_hoy) {
                    if (dia_vencimiento >= dia_hoy) {
                        cont++
                        //plan sigue activo
                        //  toast("PLAN SIGUE ACTIVO")
                    } else {
                        cont++
                        //plan se acabo
                        //toast("PLAN SE ACABO")
                        //only this source I update the status,
                        empresaCollection.document(id).update("estatus", "gratuita").addOnSuccessListener {
                        }.addOnFailureListener {
                        }
                    }
                } else if (ano_vencimiento > ano_hoy) {
                    cont++
                    //plan sigue activo
                    //toast("PLAN SIGUE ACTIVO")
                }

                if (cont == 0) {
                    if (mes_vencimiento > mes_hoy && ano_vencimiento >= ano_hoy) {
                        //sigue siendo valido
                        //  toast("PLAN SIGUE ACTIVO")
                    } else if (mes_vencimiento == 1 && mes_hoy == 12 && ano_vencimiento > ano_hoy) {
                        //sigue siendo valido
                        //toast("PLAN SIGUE ACTIVO")
                    } else {
                        //plan se acabo
                        //toast("PLAN SE ACABO")
                        empresaCollection.document(id).update("estatus", "gratuita").addOnSuccessListener {
                        }.addOnFailureListener {
                        }
                    }
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool

    }

    private fun showConfirmDialog() {
        //the header from dialog
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_codigo)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        (dialog.findViewById<View>(R.id.bt_close) as AppCompatButton).setOnClickListener { v ->
            dialog.dismiss()
        }
        dialog.show()
        dialog.window!!.attributes = lp
    }

    private fun showDialog() {
        //the header from dialog
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_gratuita)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        //in this code I get the information on cloud firestore
        var txt1 = (dialog.findViewById<View>(R.id.cerrarGratuita) as TextView)
        txt1.setOnClickListener {
            dialog.dismiss()
        }
        //paypal
        (dialog.findViewById<View>(R.id.btn_paypal) as AppCompatButton).setOnClickListener {
            goToActivity<PlanesActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            dialog.dismiss()
        }
        //END FOR THE BACKEND ON SHOW
        (dialog.findViewById<View>(R.id.bt_close) as ImageButton).setOnClickListener { dialog.dismiss() }
        dialog.show()
        dialog.window!!.attributes = lp
    }

    private fun showDialogInfoPagoAdministrador() {
        //the header from dialog
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_gratuita_admin)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        //in this code I get the information on cloud firestore
        var txt1 = (dialog.findViewById<View>(R.id.cerrarGratuita) as TextView)
        txt1.setOnClickListener {
            dialog.dismiss()
        }
        //END FOR THE BACKEND ON SHOW
        (dialog.findViewById<View>(R.id.bt_close) as ImageButton).setOnClickListener { dialog.dismiss() }
        dialog.show()
        dialog.window!!.attributes = lp
    }

    private fun showDialogInfoPagoUsuario() {
        //the header from dialog
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_gratuita_usuario)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        //in this code I get the information on cloud firestore
        var txt1 = (dialog.findViewById<View>(R.id.cerrarGratuita) as TextView)
        txt1.setOnClickListener {
            dialog.dismiss()
        }

        //END FOR THE BACKEND ON SHOW
        (dialog.findViewById<View>(R.id.bt_close) as ImageButton).setOnClickListener { dialog.dismiss() }
        dialog.show()
        dialog.window!!.attributes = lp
    }

    ///cuando la empresa se ha dado de baja se bloquean las funciones
    private fun showDialogEmpresaBloqueada() {
        //the header from dialog
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_empresa_bloqueada)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        //in this code I get the information on cloud firestore
        var txt1 = (dialog.findViewById<View>(R.id.btnCerrarE) as TextView)
        txt1.setOnClickListener {
            dialog.dismiss()
        }
        //END FOR THE BACKEND ON SHOW
        (dialog.findViewById<View>(R.id.bt_closeE) as ImageButton).setOnClickListener { dialog.dismiss() }
        dialog.show()
        dialog.window!!.attributes = lp
    }

    private fun showDialogAdministradorBloqueada() {
        //the header from dialog
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_administrador_bloqueado)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        //in this code I get the information on cloud firestore
        var txt1 = (dialog.findViewById<View>(R.id.btnCerrarA) as TextView)
        txt1.setOnClickListener {
            dialog.dismiss()
        }
        //END FOR THE BACKEND ON SHOW
        (dialog.findViewById<View>(R.id.bt_closeA) as ImageButton).setOnClickListener { dialog.dismiss() }
        dialog.show()
        dialog.window!!.attributes = lp
    }

    private fun showDialogUsuarioBloqueada() {
        //the header from dialog
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_usuario_bloqueado)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        //in this code I get the information on cloud firestore
        var txt1 = (dialog.findViewById<View>(R.id.btnCerrarU) as TextView)
        txt1.setOnClickListener {
            dialog.dismiss()
        }
        //END FOR THE BACKEND ON SHOW
        (dialog.findViewById<View>(R.id.bt_closeU) as ImageButton).setOnClickListener { dialog.dismiss() }
        dialog.show()
        dialog.window!!.attributes = lp
    }

    //mostrar por aparte empresas
    private fun showDialogEventos() {
        //the header from dialog
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_eventos)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        //in this code I get the information on cloud firestore
        var agregar1 = (dialog.findViewById<View>(R.id.AgregarEventosE) as FloatingActionButton)
        var agregar2 = (dialog.findViewById<View>(R.id.AgregarEventosE2) as TextView)
        var verevento1 = (dialog.findViewById<View>(R.id.VerEventosE) as FloatingActionButton)
        var verevento2 = (dialog.findViewById<View>(R.id.VerEventosE2) as TextView)
        var administrar1 = (dialog.findViewById<View>(R.id.AdministrarEventosE) as FloatingActionButton)
        var administrar2 = (dialog.findViewById<View>(R.id.AdministrarEventosE2) as TextView)
        var notificaciones = (dialog.findViewById<View>(R.id.EmpresaVerEventoN) as TextView)

        var email_mio = mAuth.currentUser!!.email.toString()
        val consultaEvento = detalleEventosCollection.whereEqualTo("id_empresa", id_empresa).whereEqualTo("correo_usuario", email_mio).whereEqualTo("estatus", "0")
        //beggin with consult
        consultaEvento.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var con = 0
                for (document in task.result!!) {
                    con++
                }
                if (con == 0) {
                    notificaciones.text = "0"
                } else {
                    notificaciones.text = con.toString()
                }

            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool


        //Eventos  (9) YA TIENE PROGRAMACION REACTIVA
        agregar1.setOnClickListener {
            //1603
            goToActivity<FormProfileData> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        agregar2.setOnClickListener {
            //1603
            goToActivity<FormProfileData> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        verevento1.setOnClickListener {
            //401
            goToActivity<CardBasic> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        verevento2.setOnClickListener {
            //401
            goToActivity<CardBasic> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        administrar1.setOnClickListener {
            //1601
            goToActivity<AdministrarEventoActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        administrar2.setOnClickListener {
            //1601
            goToActivity<AdministrarEventoActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }


        //END FOR THE BACKEND ON SHOW
        //(dialog.findViewById<View>(R.id.bt_closeE) as ImageButton).setOnClickListener { dialog.dismiss() }
        dialog.show()
        dialog.window!!.attributes = lp
    }

    private fun showDialogEncuestas() {
        //the header from dialog
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_encuestas)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        //in this code I get the information on cloud firestore
        var agregar1 = (dialog.findViewById<View>(R.id.AgregarEncuestaE) as FloatingActionButton)
        var agregar2 = (dialog.findViewById<View>(R.id.AgregarEncuestaE2) as TextView)
        var ver1 = (dialog.findViewById<View>(R.id.VerEncuestasE) as FloatingActionButton)
        var ver2 = (dialog.findViewById<View>(R.id.VerEncuestasE2) as TextView)
        var administrar1 = (dialog.findViewById<View>(R.id.AdministrarEncuestasE) as FloatingActionButton)
        var administrar2 = (dialog.findViewById<View>(R.id.AdministrarEncuestasE2) as TextView)
        var notificaciones = (dialog.findViewById<View>(R.id.EmpresaVerEncuestaN) as TextView)

        var email_mio = mAuth.currentUser!!.email.toString()
        val consultaEncuestas = detalleEncuestasCollection.whereEqualTo("id_empresa", id_empresa).whereEqualTo("correo_usuario", email_mio).whereEqualTo("estatus", "0")
        //beggin with consult
        consultaEncuestas.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var con = 0
                for (document in task.result!!) {
                    con++
                }
                if (con == 0) {
                    notificaciones.text = "0"

                } else {
                    notificaciones.text = con.toString()
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool

        //Encuestas  (12) YA CONTIENE PROGRAMACION REACTIVA
        agregar1.setOnClickListener {
            //1604
            goToActivity<AgregarEncuestaActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        agregar2.setOnClickListener {
            //1604
            goToActivity<AgregarEncuestaActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        ver1.setOnClickListener {
            //604
            goToActivity<EncuestaActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        ver2.setOnClickListener {
            //604
            goToActivity<EncuestaActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        administrar1.setOnClickListener {
            //1605  (ver estadisticas)
            goToActivity<EstadisticaActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        administrar2.setOnClickListener {
            //1605  (ver estadisticas)
            goToActivity<EstadisticaActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        //END FOR THE BACKEND ON SHOW
        //(dialog.findViewById<View>(R.id.bt_closeE) as ImageButton).setOnClickListener { dialog.dismiss() }
        dialog.show()
        dialog.window!!.attributes = lp
    }

    private fun showDialogAnuncios() {
        //the header from dialog
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_anuncios)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        //in this code I get the information on cloud firestore
        var agregar1 = (dialog.findViewById<View>(R.id.AgregarAnuncioE) as FloatingActionButton)
        var agregar2 = (dialog.findViewById<View>(R.id.AgregarAnuncioE2) as TextView)
        var ver1 = (dialog.findViewById<View>(R.id.VerAnuncioE) as FloatingActionButton)
        var ver2 = (dialog.findViewById<View>(R.id.VerAnuncioE2) as TextView)
        var administrar1 = (dialog.findViewById<View>(R.id.AdministrarAnunciosE) as FloatingActionButton)
        var administrar2 = (dialog.findViewById<View>(R.id.AdministrarAnunciosE2) as TextView)
        var notificaciones = (dialog.findViewById<View>(R.id.EmpresaVerAnuncioN) as TextView)

        var email_mio = mAuth.currentUser!!.email.toString()

        val anuncioConsulta = detalleAnuncioCollection.whereEqualTo("id_empresa", id_empresa).whereEqualTo("correo_usuario", email_mio).whereEqualTo("estatus", "0")
        //beggin with consult
        anuncioConsulta.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var con = 0
                for (document in task.result!!) {
                    con++
                }
                if (con == 0) {
                    notificaciones.text = "0"
                } else {
                    notificaciones.text = con.toString()
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool

        //Anuncios (15) YA CONTIENE PROGRAMACION REACTIVA
        agregar1.setOnClickListener {
            //1602
            goToActivity<AgregarAnuncioActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        agregar2.setOnClickListener {
            //1602
            goToActivity<AgregarAnuncioActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        ver1.setOnClickListener {
            //405
            goToActivity<CardWizardLight> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        ver2.setOnClickListener {
            //405
            goToActivity<CardWizardLight> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        administrar1.setOnClickListener {
            //402
            goToActivity<AdministrarAnunciosActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }
        administrar2.setOnClickListener {
            //402
            goToActivity<AdministrarAnunciosActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }

        //END FOR THE BACKEND ON SHOW
        //(dialog.findViewById<View>(R.id.bt_closeE) as ImageButton).setOnClickListener { dialog.dismiss() }
        dialog.show()
        dialog.window!!.attributes = lp
    }

    /**
     * initToolbar(header)
     */
    @SuppressLint("ResourceType")
    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbarD) as Toolbar?
        if (toolbar != null) {
            val email = mAuth.currentUser!!.email.toString()
            val empleado = userCollection.whereEqualTo("email", email)
            //beggin with consult
            empleado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val rol = document.get("rol").toString()
                        name = document.get("name").toString()
                        toolbar.title = "Hola, " + name

                        if (rol == "administrador") {
                            // MostrarCodigoA.text = "Fue creado el 17/06/2019"
                            //tituloCodigoA.text = "Hola! Comunícate"
                        } else {
                            //MostrarCodigoU.text = "Fue creado el 17/06/2019"
                            //tituloCodigoU.text = "Hola! Comunícate"
                            //deshabilitar funciones
                            val color = Color.parseColor("#D3D3D3")
                            var filter = LightingColorFilter(Color.GRAY, Color.GRAY)

                            /*PanelUsuarios.isEnabled = false
                            PanelUsuarios.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))
                            //PanelUsuarios.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                            AgregarEventos.isEnabled = false
                            AgregarEventos.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                            //AgregarEventos.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                            AdministrarEventos.isEnabled = false
                            AdministrarEventos.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                            //AdministrarEventos.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                            AgregarEncuesta.isEnabled = false
                            AgregarEncuesta.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                            //AgregarEncuesta.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                            AdministrarEncuestas.isEnabled = false
                            AdministrarEncuestas.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                            //AdministrarEncuestas.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                            AgregarAnuncio.isEnabled = false
                            AgregarAnuncio.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                            //  AgregarAnuncio.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                            AdministrarAnuncios.isEnabled = false
                            AdministrarAnuncios.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                            //AdministrarAnuncios.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                            GenerarQR.isEnabled = false
                            GenerarQR.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                            //GenerarQR.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                            EstatusChecador.isEnabled = false
                            EstatusChecador.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                            //EstatusChecador.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))
        */
                        }
                    }

                } else {
                    Log.w("saasas", "Error getting documents.", task.exception)
                }
            })//end for expression lambdas this very cool
            //case for empresa
            val empresa = empresaCollection.whereEqualTo("correo", email)
            empresa.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val color = Color.parseColor("#D3D3D3")
                        //var filter = LightingColorFilter(Color.GRAY, Color.GRAY)

                        /* VerActividades.isEnabled = false
                         VerActividades.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                         //VerActividades.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                         Checar.isEnabled = false
                         Checar.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))
                         //Checar.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))
        */
                        name = document.get("estatus").toString()
                        if (name.equals("mensual")) {
                            toolbar.title = "Plan: " + "Mensual"

                        } else if (name.equals("anual")) {
                            toolbar.title = "Plan: " + "Anual"

                        } else if (name.equals("pruebainicial")) {
                            toolbar.title = "Plan: " + "Prueba de 15 días"

                        } else if (name.equals("gratuita")) {
                            toolbar.title = "Plan: " + "Gratuito"

                        }
                        //name = document.get("nombre").toString()
                        val id_empresa = document.get("id_empresa").toString()
                        MostrarCodigoE.text = id_empresa
                        tituloCodigoE.text = "Código Empresa"
                        InfoCodigoEmpresaE.text = "¿Qué es esto?"
                    }
                } else {
                    Log.w("saasas", "Error getting documents.", task.exception)
                }

            })//end for expression lambdas this very cool

            setSupportActionBar(toolbar)
            Tools.setSystemBarColor(this, R.color.colorPrimary)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)//menu de las opciones del toolbar, menu basic
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.action_search) {//icono de search
            //Toast.makeText(applicationContext, item.title, Toast.LENGTH_SHORT).show()
            toast("Diste click en search")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        confirmarCierre()
    }

    private var exitTime: Long = 0

    private fun confirmarCierre() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "Presiona de nuevo para salir de la aplicación", Toast.LENGTH_SHORT).show()
            exitTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }

}