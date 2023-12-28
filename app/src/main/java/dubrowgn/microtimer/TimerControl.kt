package dubrowgn.microtimer

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.ImageButton
import android.widget.LinearLayout
import dubrowgn.microtimer.db.Alarm

class TimerControl(context: Context, val alarm: Alarm) : LinearLayout(context) {
    var onDelete: (() -> Unit)? = null
    var onPause: (() -> Unit)? = null
    var onResume: (() -> Unit)? = null

    private var paused: Boolean = false

    private val lblValue: RoTimeControl
    private val btnPause: ImageButton
    private val btnResume: ImageButton

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        orientation = HORIZONTAL

        val red = Color.rgb(233, 30, 30)
        val green = Color.rgb(76, 175, 80)
        val white = Color.rgb(255, 255, 255)

        val btnDelete = ImageButton(context)
        btnDelete.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        btnDelete.backgroundTintList = ColorStateList.valueOf(red)
        btnDelete.foregroundTintList = ColorStateList.valueOf(white)
        btnDelete.setImageResource(android.R.drawable.ic_menu_delete)
        btnDelete.setOnClickListener { onDelete?.invoke() }
        addView(btnDelete)

        lblValue = RoTimeControl(context)
        lblValue.layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
        addView(lblValue)

        btnPause = ImageButton(context)
        btnPause.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        btnPause.backgroundTintList = ColorStateList.valueOf(green)
        btnPause.foregroundTintList = ColorStateList.valueOf(white)
        btnPause.setImageResource(android.R.drawable.ic_media_pause)
        btnPause.setOnClickListener { onPause?.invoke() }
        addView(btnPause)

        btnResume = ImageButton(context)
        btnResume.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        btnResume.backgroundTintList = ColorStateList.valueOf(green)
        btnResume.foregroundTintList = ColorStateList.valueOf(white)
        btnResume.setImageResource(android.R.drawable.ic_media_play)
        btnResume.setOnClickListener { onResume?.invoke() }
        // don't add; paused by default
    }

    fun update(): TimerControl {
        lblValue.setValue(alarm.remaining)
        if (alarm.remaining.digits == 0u) {
            lblValue.setColor(Color.RED)
            btnPause.isEnabled = false
            btnPause.backgroundTintList = ColorStateList.valueOf(Color.GRAY)
        }

        if (paused == alarm.paused)
            return this

        if (alarm.paused) {
            removeView(btnPause)
            addView(btnResume)
        } else {
            removeView(btnResume)
            addView(btnPause)
        }
        paused = alarm.paused

        return this
    }
}
