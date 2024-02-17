package dubrowgn.microtimer

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import dubrowgn.microtimer.db.Alarm
import dubrowgn.microtimer.ui.ext.*

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

        val icoDelete = context.getDrawable(R.drawable.ico_delete)
        val icoPlay = context.getDrawable(R.drawable.ico_play)
        val icoPause = context.getDrawable(R.drawable.ico_pause)

        val btnSizePx = resources.getDimension(R.dimen.icon_button_size).toInt()
        val btnMarginPx = resources.getDimension(R.dimen.button_margin).toInt()

        val fgColor = context.getColor(R.color.text_on_primary)
        val actColor = context.getColor(R.color.action)
        val delColor = context.getColor(R.color.delete)

        val lp = LayoutParams(btnSizePx, btnSizePx)
            .withMargins(btnMarginPx)

        val btnDelete = Button(context)
            .withLayout(lp)
            .withBg(context.getDrawable(R.drawable.btn_selector))
            .withBgTint(delColor)
            .withFgTint(fgColor)
            .withFg(icoDelete)
            .withFgGravity(Gravity.CENTER)
            .withOnClick { onDelete?.invoke() }
        addView(btnDelete)

        lblValue = RoTimeControl(context)
            .withLayout(0, matchParent, 1f)
        addView(lblValue)

        btnPause = Button(context)
            .withLayout(LayoutParams(lp))
            .withBg(context.getDrawable(R.drawable.btn_selector))
            .withBgTint(actColor)
            .withFgTint(fgColor)
            .withFg(icoPause)
            .withFgGravity(Gravity.CENTER)
            .withOnClick { onPause?.invoke() }
        addView(btnPause)

        btnResume = Button(context)
            .withLayout(LayoutParams(lp))
            .withBg(context.getDrawable(R.drawable.btn_selector))
            .withBgTint(actColor)
            .withFgTint(fgColor)
            .withFg(icoPlay)
            .withFgGravity(Gravity.CENTER)
            .withOnClick { onResume?.invoke() }
        // don't add; paused by default
    }

    fun update(): TimerControl {
        lblValue.setValue(alarm.remaining)
        if (alarm.remaining.digits == 0u) {
            lblValue.setColor(Color.RED)
            btnPause
                .withEnabled(false)
                .withBgTint(Color.GRAY)
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
