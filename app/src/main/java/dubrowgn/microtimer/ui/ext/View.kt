package dubrowgn.microtimer.ui.ext

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout

fun <T:View> T.withBg(drawable: Drawable?): T {
    return apply { background = drawable }
}
fun <T:View> T.withBgTint(colors: ColorStateList): T {
    return apply { backgroundTintList = colors }
}
fun <T:View> T.withBgTint(color: Int): T {
    return withBgTint(ColorStateList.valueOf(color))
}
fun <T:View> T.withBgColor(color: Int): T {
    return apply { setBackgroundColor(color) }
}

fun <T:View> T.withEnabled(enabled: Boolean): T {
    return apply { isEnabled = enabled }
}

fun <T:View> T.withFg(drawable: Drawable?): T {
    return apply { foreground = drawable }
}
fun <T:View> T.withFgTint(colors: ColorStateList): T {
    return apply { foregroundTintList = colors }
}
fun <T:View> T.withFgTint(color: Int): T {
    return withFgTint(ColorStateList.valueOf(color))
}
fun <T:View> T.withFgGravity(gravity: Int): T {
    return apply { foregroundGravity = gravity }
}

fun <T:View> T.withOnClick(handler: OnClickListener?): T {
    return apply { setOnClickListener(handler) }
}

fun <T:View> T.withLayout(params: LinearLayout.LayoutParams): T {
    return apply { layoutParams = params }
}
fun <T:View> T.withLayout(width: Int, height: Int): T {
    return withLayout(LinearLayout.LayoutParams(width, height))
}
fun <T:View> T.withLayout(width: Int, height: Int, weight: Float): T {
    return withLayout(LinearLayout.LayoutParams(width, height, weight))
}

fun <T:View> T.withTextAlign(alignment: Int): T {
    return apply { textAlignment = alignment }
}
