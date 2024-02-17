package dubrowgn.microtimer.ui.ext

import android.graphics.Typeface
import android.widget.TextView

fun <T:TextView> T.withHint(hint: String): T {
    return apply { this.hint = hint }
}

fun <T:TextView> T.withText(text: String): T {
    return apply { this.text = text }
}
fun <T:TextView> T.withTextColor(color: Int): T {
    return apply { setTextColor(color) }
}
fun <T:TextView> T.withTextSize(textSp: Float): T {
    return apply { textSize = textSp }
}

fun <T:TextView> T.withTypeface(typeface: Typeface): T {
    return apply { this.typeface = typeface }
}
