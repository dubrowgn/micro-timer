package dubrowgn.dafttimer

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    private fun debug(msg: String) {
        Log.d(this::class.java.name, msg)
    }

    private fun error(msg: String) {
        Log.e(this::class.java.name, msg)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        debug("onReceive()")

        if (context == null) {
            error("context is null")
            return
        }

        if (intent == null) {
            error("intent is null")
            return
        }

        Vibration.start(context)

        val noteMgr = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val id = intent.getLongExtra("id", -1)
        val name = intent.getStringExtra("name")

        val noteIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = Notification.Builder(context, CHANNEL_ID)
            .setAutoCancel(true)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText("Timer for $name Expired!")
            .setCategory(Notification.CATEGORY_ALARM)
            .setVisibility(Notification.VISIBILITY_PUBLIC)
            .setColor(Color.RED)
            .setFullScreenIntent(noteIntent, true)

        noteMgr.notify(id.toInt(), builder.build())
    }
}
