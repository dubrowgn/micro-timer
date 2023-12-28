package dubrowgn.microtimer

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

class RoTimeControl : FrameLayout {

    private lateinit var lblValue: TextView

    fun setValue(dur: Dec6Duration?): RoTimeControl {
        lblValue.text = dur?.toString() ?: ""
        return this
    }

    fun setColor(color: Int): RoTimeControl {
        lblValue.setTextColor(color)
        return this
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int)
            : super(context, attrs, defStyle)
    {
        init()
    }

    private fun init() {
        val matchParent = LinearLayout.LayoutParams.MATCH_PARENT
        val wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT

        layoutParams = LinearLayout.LayoutParams(wrapContent, wrapContent)

        lblValue = TextView(context)
        lblValue.layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)
        lblValue.hint = "HH:MM:SS"
        lblValue.setTextColor(Color.rgb(170, 170, 170))
        lblValue.textSize = 40f
        lblValue.textAlignment = View.TEXT_ALIGNMENT_CENTER
        lblValue.typeface = Typeface.MONOSPACE
        addView(lblValue)
    }
}
