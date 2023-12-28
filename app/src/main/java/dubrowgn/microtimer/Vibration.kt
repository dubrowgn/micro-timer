package dubrowgn.microtimer

import android.content.Context
import android.media.AudioAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log

object Vibration {
    private fun debug(msg: String) {
        Log.d(this::class.java.name, msg)
    }

    private fun vibrator(context: Context): Vibrator {
        return context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    fun start(context: Context) {
        debug("start()")

        vibrator(context).vibrate(
            VibrationEffect.createWaveform(longArrayOf(0, 120, 120, 120, 360), 1),
            AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build()
        )
    }

    fun stop(context: Context) {
        debug("stop()")

        vibrator(context).cancel()
    }
}