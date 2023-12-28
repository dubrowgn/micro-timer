package dubrowgn.microtimer.db

import android.os.SystemClock
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dubrowgn.microtimer.Dec6Duration
import kotlin.math.ceil

@Entity(tableName = "alarm")
data class Alarm (
    @ColumnInfo(name = "duration_dec6")
    var duration: Dec6Duration
) {
    @ColumnInfo(name = "remaining_dec6")
    var remaining: Dec6Duration = duration.clone()

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @ColumnInfo(name = "expires_ms")
    var expiresMs: Long? = null

    val expired: Boolean
        get() = remaining.isZero

    val paused: Boolean
        get() = expiresMs == null

    init {
        unpause()
    }

    fun msToNextTick(nowMs: Long = SystemClock.elapsedRealtime()): Long? {
        val expiresMs = this.expiresMs ?: return null
        val remainingMs = expiresMs - nowMs
        return remainingMs % 1000
    }

    fun pause(): Alarm {
        expiresMs = null
        return this
    }

    fun unpause(): Alarm {
        expiresMs = SystemClock.elapsedRealtime() + remaining.totalSeconds.toLong() * 1000
        return this
    }

    fun update(): Long? {
        val expiresMs = this.expiresMs ?: return null

        val nowMs = SystemClock.elapsedRealtime()
        if (nowMs >= expiresMs) {
            remaining.zero()
            return null
        }

        val deltaSec = ceil((expiresMs - nowMs) / 1000.0)
        remaining.subtractSeconds(remaining.totalSeconds - deltaSec.toUInt())

        return msToNextTick(nowMs)
    }
}
