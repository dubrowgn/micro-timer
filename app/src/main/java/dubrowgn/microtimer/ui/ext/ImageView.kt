package dubrowgn.microtimer.ui.ext

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.widget.ImageView

fun <T: ImageView> T.withFgImg(drawable: Drawable?): T {
    return apply { setImageDrawable(drawable) }
}

fun <T: ImageView> T.withFgImgTint(colors: ColorStateList): T {
    return apply { imageTintList = colors }
}

fun <T: ImageView> T.withFgImgTint(color: Int): T {
    return withFgImgTint(ColorStateList.valueOf(color))
}
