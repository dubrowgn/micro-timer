package dubrowgn.dafttimer.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Alarm::class], version = 1, exportSchema = false)
@TypeConverters(Dec6DurationConverter::class)
abstract class Database : RoomDatabase() {
    abstract fun AlarmDao(): AlarmDao
}
