package dubrowgn.dafttimer.db

import androidx.room.TypeConverter
import dubrowgn.dafttimer.Dec6Duration

class Dec6DurationConverter {
    @TypeConverter
    fun serialize(dur: Dec6Duration?): Int? {
        return dur?.digits?.toInt()
    }

    @TypeConverter
    fun deserialize(digits: Int?): Dec6Duration? {
        return if (digits == null) null else Dec6Duration(digits.toUInt())
    }
}
