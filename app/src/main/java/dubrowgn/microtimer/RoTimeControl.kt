package dubrowgn.microtimer

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import dubrowgn.microtimer.ui.ext.*

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
            .withLayout(matchParent, wrapContent)
            .withHint("HH:MM:SS")
            .withTextSize(40f)
            .withTextAlign(View.TEXT_ALIGNMENT_CENTER)
            .withTypeface(Typeface.MONOSPACE)
        addView(lblValue)
    }
}
