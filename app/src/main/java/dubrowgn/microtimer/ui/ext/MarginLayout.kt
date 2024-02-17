package dubrowgn.microtimer.ui.ext

import android.view.ViewGroup.MarginLayoutParams

fun <T:MarginLayoutParams> T.withMargins(l: Int, t: Int, r: Int, b: Int): T {
    return apply { setMargins(l, t, r, b) }
}

fun <T:MarginLayoutParams> T.withMargins(h: Int, v: Int): T {
    return withMargins(h, v, h, v)
}

fun <T:MarginLayoutParams> T.withMargins(m: Int): T {
    return withMargins(m, m, m, m)
}
