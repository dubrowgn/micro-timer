package dubrowgn.dafttimer.db

import androidx.room.*
import dubrowgn.dafttimer.Dec6Duration

@Dao
interface AlarmDao {
    fun create(duration: Dec6Duration): Alarm {
        val alarm = Alarm(duration)
        alarm.id = insert(alarm)
        return alarm
    }

    @Query("delete from alarm where id = :id")
    fun delete(id: Int)

    @Delete
    fun delete(alarm: Alarm)

    @Insert
    fun insert(alarm: Alarm): Long

    @Query("select * from alarm where id = :id")
    fun read(id: Int): Alarm

    @Query("select * from alarm")
    fun readAll(): List<Alarm>

    @Update
    fun update(alarm: Alarm)
}
