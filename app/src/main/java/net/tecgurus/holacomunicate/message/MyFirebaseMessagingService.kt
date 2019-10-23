package net.tecgurus.holacomunicate.message
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import net.tecgurus.holacomunicate.R
import net.tecgurus.holacomunicate.actividadesfragment.GestionActividadesActivity
import net.tecgurus.holacomunicate.actividadesfragmentadmin.GestionActividadesAActivity
import net.tecgurus.holacomunicate.formularios.CardBasic
import net.tecgurus.holacomunicate.formularios.CardWizardLight
import net.tecgurus.holacomunicate.formularios.EncuestaActivity
import net.tecgurus.holacomunicate.admin.PanelEmpresasActivity
import net.tecgurus.holacomunicate.DashboarActivity

/**
 * @author Abraham
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        // Check if message contains a data payload.
        remoteMessage?.data?.isNotEmpty()?.let {
            if (it) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                //scheduleJob()
            } else {
                // Handle message within 10 seconds
                handleNow()
            }
        }
        // Check if message contains a notification payload.
        remoteMessage?.notification?.let {
            Log.d("CLOUD", "Message Notification Body:  ${it.body}")
            val token = FirebaseInstanceId.getInstance().token
            Log.i("FCM-TOKEN", "FCM Registration Token: " + token!!)
            //sendNotification("${it.body}")
            showNotification("${it.body}", "${it.title}")
        }
        // remoteMessage.getNotification().getBody();
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */

    fun showNotification(messageBody: String, title: String) {

        val channelId = getString(R.string.default_notification_channel_id)
        val channelName: String = "my_channel_01"
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "YOUR_NOTIFICATION_CHANNEL_DISCRIPTION"
            mNotificationManager.createNotificationChannel(channel)
        }
        val mBuilder = NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.drawable.ic_logo_one) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(messageBody)// message for notification
                .setAutoCancel(true) // clear notification after click

        if (title.equals("Actividad")) {
            val intent = Intent(applicationContext, GestionActividadesActivity::class.java)
            val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            mBuilder.setContentIntent(pi)
            mNotificationManager.notify(0, mBuilder.build())
        } else if (title.equals("Eventos")) {
            val intent = Intent(applicationContext, CardBasic::class.java)
            val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            mBuilder.setContentIntent(pi)
            mNotificationManager.notify(0, mBuilder.build())
        } else if (title.equals("Encuestas")) {
            val intent = Intent(applicationContext, EncuestaActivity::class.java)
            val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            mBuilder.setContentIntent(pi)
            mNotificationManager.notify(0, mBuilder.build())
        } else if (title.equals("Anuncios")) {
            val intent = Intent(applicationContext, CardWizardLight::class.java)
            val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            mBuilder.setContentIntent(pi)
            mNotificationManager.notify(0, mBuilder.build())
        } else if (title.equals("Administrar Actividades")) {
            val intent = Intent(applicationContext, GestionActividadesAActivity::class.java)
            val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            mBuilder.setContentIntent(pi)
            mNotificationManager.notify(0, mBuilder.build())
        } else if (title.equals("Mis Actividades")) {
            val intent = Intent(applicationContext, GestionActividadesActivity::class.java)
            val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            mBuilder.setContentIntent(pi)
            mNotificationManager.notify(0, mBuilder.build())
        } else if (title.equals("Empresas")) {
            val intent = Intent(applicationContext, PanelEmpresasActivity::class.java)
            val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            mBuilder.setContentIntent(pi)
            mNotificationManager.notify(0, mBuilder.build())

        } else {
            val intent = Intent(applicationContext, DashboarActivity::class.java)
            val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            mBuilder.setContentIntent(pi)
            mNotificationManager.notify(0, mBuilder.build())
        }


    }

    override fun onNewToken(token: String?) {
        Log.d("CLOUD", "Refreshed token: $token")
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d("cloud", "Short lived task is done.")
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
    }

}
