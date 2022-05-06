package dubrowgn.dafttimer

class Dec6Duration(var digits: UInt) {

    constructor() : this(0u)

    val isZero: Boolean
        get() = digits == 0u

    val seconds: UInt
        get() = digits % 100u

    val minutes: UInt
        get() = digits / 100u % 100u

    val hours: UInt
        get() = digits / 10000u % 100u

    val totalSeconds: UInt
        get() = hours * 60u * 60u + minutes * 60u + seconds

    fun clone(): Dec6Duration {
        return Dec6Duration(digits)
    }

    fun popDigit(): Dec6Duration {
        digits /= 10u;
        return this
    }

    fun pushDigit(digit: UInt): Dec6Duration {
        digits *= 10u
        digits += digit
        digits %= 1000000u

        return this
    }

    fun subtractSeconds(rSeconds: UInt): Dec6Duration {
        if (rSeconds >= totalSeconds) {
            digits = 0u
            return this
        }

        val ls = seconds
        val lm = minutes
        val lh = hours

        val rs = rSeconds % 60u
        val rm = rSeconds % 3600u / 60u
        val rh = rSeconds / 3600u

        val bm = if (rs > ls) 1u else 0u
        val s = bm * 60u + ls - rs

        val bh = if (rm + bm > lm) 1u else 0u
        val m = bh * 60u + lm - bm - rm

        val h = lh - bh - rh

        digits = h * 10000u + m * 100u + s

        return this
    }

    override fun toString(): String {
        val padPart = { part: UInt -> part.toString().padStart(2, '0') }

        return "${padPart(hours)}:${padPart(minutes)}:${padPart(seconds)}"
    }

    fun zero(): Dec6Duration {
        digits = 0u
        return this
    }
}
