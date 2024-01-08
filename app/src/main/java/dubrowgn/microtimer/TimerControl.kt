package dubrowgn.microtimer

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import dubrowgn.microtimer.db.Alarm

class TimerControl(context: Context, val alarm: Alarm) : LinearLayout(context) {
    var onDelete: (() -> Unit)? = null
    var onPause: (() -> Unit)? = null
    var onResume: (() -> Unit)? = null

    private var paused: Boolean = false

    private val lblValue: RoTimeControl
    private val btnPause: Button
    private val btnResume: Button

    init {
        val matchParent = LayoutParams.MATCH_PARENT
        val wrapContent = LayoutParams.WRAP_CONTENT

        layoutParams = LayoutParams(matchParent, wrapContent)
        orientation = HORIZONTAL

        val red = Color.rgb(233, 30, 30)
        val green = Color.rgb(76, 175, 80)
        val white = Color.rgb(255, 255, 255)

        val btnDelete = Button(context)
        btnDelete.layoutParams = LayoutParams(wrapContent, matchParent)
        btnDelete.backgroundTintList = ColorStateList.valueOf(red)
        btnDelete.foregroundTintList = ColorStateList.valueOf(white)
        btnDelete.foreground = context.getDrawable(R.drawable.ico_delete)
        btnDelete.foregroundGravity = Gravity.CENTER
        btnDelete.setOnClickListener { onDelete?.invoke() }
        addView(btnDelete)

        lblValue = RoTimeControl(context)
        lblValue.layoutParams = LayoutParams(0, matchParent, 1f)
        addView(lblValue)

        btnPause = Button(context)
        btnPause.layoutParams = LayoutParams(wrapContent, matchParent)
        btnPause.backgroundTintList = ColorStateList.valueOf(green)
        btnPause.foregroundTintList = ColorStateList.valueOf(white)
        btnPause.foreground = context.getDrawable(R.drawable.ico_pause)
        btnPause.foregroundGravity = Gravity.CENTER
        btnPause.setOnClickListener { onPause?.invoke() }
        addView(btnPause)

        btnResume = Button(context)
        btnResume.layoutParams = LayoutParams(wrapContent, matchParent)
        btnResume.backgroundTintList = ColorStateList.valueOf(green)
        btnResume.foregroundTintList = ColorStateList.valueOf(white)
        btnResume.foreground = context.getDrawable(R.drawable.ico_play)
        btnResume.foregroundGravity = Gravity.CENTER
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
