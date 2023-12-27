package dubrowgn.dafttimer

import android.Manifest.permission
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.room.Room
import dubrowgn.dafttimer.db.Alarm
import dubrowgn.dafttimer.db.AlarmDao
import dubrowgn.dafttimer.db.Database

const val CHANNEL_ID = "daft-timer.alarm"
const val settingsName = "settings"

class MainActivity : Activity() {
    private lateinit var alarmDao: AlarmDao

    private var duration: Dec6Duration = Dec6Duration()
    private val tickHandler: Handler = Handler(Looper.getMainLooper())
    private var expiredCount: Int = 0

    private lateinit var layoutAlarms: LinearLayout
    private lateinit var lblValue: RoTimeControl

    private val alarmMgr
        get() = getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val noteMgr
        get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private fun debug(msg: String) {
        Log.d(this::class.java.name, msg)
    }

    enum class Perm { Granted, Denied, NotAsked }

    private fun getPerm(name: String) : Perm {
        val settings = getSharedPreferences(settingsName, MODE_PRIVATE)
        val perm =
            if (checkSelfPermission(name) == PackageManager.PERMISSION_GRANTED)
                Perm.Granted
            else if (settings.getBoolean("${name}_ASKED", false))
                Perm.Denied
            else
                Perm.NotAsked

        debug("getPerm($name) = $perm")

        return perm
    }

    private fun requestPerm(name: String) {
        debug("requestPerm($name)")
        getSharedPreferences(settingsName, MODE_PRIVATE)
            .edit()
            .putBoolean("${name}_ASKED", true)
            .apply()
        requestPermissions(arrayOf(name), 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        debug("onCreate()")

        super.onCreate(savedInstanceState)

        initDb()
        initPerms()
        initNotes()
        initUi()

        allowFullscreen()

        loadAlarms()
    }

    override fun onDestroy() {
        debug("onDestroy()")

        super.onDestroy()
    }

    private fun initDb() {
        alarmDao = Room.databaseBuilder(applicationContext, Database::class.java, "app-data")
            .allowMainThreadQueries()
            .build()
            .AlarmDao()
    }

    private fun allowFullscreen() {
        setShowWhenLocked(true)
        setTurnScreenOn(true)

        val keyguardMgr = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        keyguardMgr.requestDismissKeyguard(this, null)
    }

    private fun initNotes() {
        noteMgr.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                "Alarm Expired",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Sound and visual indicator"
            }
        )
    }

    private fun initPerms() {
        if (getPerm(permission.POST_NOTIFICATIONS) == Perm.NotAsked)
            requestPerm(permission.POST_NOTIFICATIONS)
    }

    private fun initUi() {
        actionBar?.hide()
        setContentView(R.layout.activity_main)

        layoutAlarms = findViewById(R.id.layoutAlarms)
        lblValue = findViewById(R.id.lblInput)

        findViewById<Button>(R.id.btn0).setOnClickListener { onDigit(0u) }
        findViewById<Button>(R.id.btn1).setOnClickListener { onDigit(1u) }
        findViewById<Button>(R.id.btn2).setOnClickListener { onDigit(2u) }
        findViewById<Button>(R.id.btn3).setOnClickListener { onDigit(3u) }
        findViewById<Button>(R.id.btn4).setOnClickListener { onDigit(4u) }
        findViewById<Button>(R.id.btn5).setOnClickListener { onDigit(5u) }
        findViewById<Button>(R.id.btn6).setOnClickListener { onDigit(6u) }
        findViewById<Button>(R.id.btn7).setOnClickListener { onDigit(7u) }
        findViewById<Button>(R.id.btn8).setOnClickListener { onDigit(8u) }
        findViewById<Button>(R.id.btn9).setOnClickListener { onDigit(9u) }
        findViewById<Button>(R.id.btn00).setOnClickListener { onDigit(0u); onDigit(0u) }
        findViewById<ImageButton>(R.id.btnBackspace).setOnClickListener { onBackspace() }
        findViewById<ImageButton>(R.id.btnClear).setOnClickListener { onClear() }
        findViewById<ImageButton>(R.id.btnStart).setOnClickListener { createTimer() }
    }

    private fun loadAlarms() {
        alarmDao
            .readAll()
            .map(::attachAlarm)
    }

    private fun attachAlarm(alarm: Alarm) {
        debug("attachAlarm(${alarm.id})")

        val tc = TimerControl(this, alarm)

        tc.onDelete = {
            debug("tc.onDelete(${tc.alarm.id})")

            tickHandler.removeCallbacksAndMessages(alarm.id)
            layoutAlarms.removeView(tc)

            if (alarm.expired) {
                expiredCount--
                if (expiredCount == 0)
                    Vibration.stop(this)
            }

            noteMgr.cancel(alarm.id!!.toInt())

            alarmDao.delete(alarm)
        }
        tc.onPause = {
            debug("tc.onPause(${tc.alarm.id})")

            alarm.pause()
            alarmDao.update(alarm)
            tc.update()
            tickHandler.removeCallbacksAndMessages(alarm.id)
        }
        tc.onResume = {
            debug("tc.onResume(${tc.alarm.id})")

            alarm.unpause()
            alarmDao.update(alarm)
            tc.update()
            alarm.msToNextTick()?.let { delayMs ->
                tickHandler.postDelayed({ tick(tc) }, alarm.id, delayMs)
            }
        }

        tick(tc)
        layoutAlarms.addView(tc)
    }

    private fun tick(tc: TimerControl) {
        debug("tick(${tc.alarm.id})")

        val deltaMs = tc.alarm.update()
        tc.update()

        if (tc.alarm.expired) {
            if (expiredCount == 0)
                Vibration.start(this)
            expiredCount++
            return
        }

        if (deltaMs == null)
            return

        tickHandler.postDelayed({ tick(tc) }, tc.alarm.id, deltaMs)
    }

    override fun onResume() {
        debug("onResume()")

        super.onResume()

        layoutAlarms.children.forEach { v ->
            val tc = v as TimerControl
            cancelAlarmTimeout(tc.alarm)
            tick(tc)
        }
    }

    override fun onPause() {
        debug("onPause()")

        super.onPause()

        layoutAlarms.children.forEach { v ->
            val tc = v as TimerControl
            tickHandler.removeCallbacksAndMessages(tc.alarm.id)
            if (!tc.alarm.expired)
                startAlarmTimeout(tc.alarm)
        }

        if (expiredCount > 0)
            Vibration.stop(this)
    }

    private fun onBackspace() {
        duration.popDigit()
        lblValue.setValue(if (duration.isZero) null else duration)
    }

    private fun onClear() {
        duration.zero()
        lblValue.setValue(null)
    }

    private fun onDigit(digit: UInt) {
        duration.pushDigit(digit)
        lblValue.setValue(duration)
    }

    private fun createAlarmIntent(alarm: Alarm): PendingIntent {
        return PendingIntent.getBroadcast(
            this,
            alarm.id!!.toInt(),
            Intent(this, AlarmReceiver::class.java).apply {
                action = "dubrowgn.dafttimer.ALARM_EXPIRED"
                putExtra("id", alarm.id)
                putExtra("name", alarm.duration.toString())
                flags = 0
            },
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun startAlarmTimeout(alarm: Alarm) {
        debug("startAlarmTimeout()")

        val remainingMs = alarm.remaining.totalSeconds.toLong() * 1000
        alarmMgr.setExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + remainingMs,
            createAlarmIntent(alarm)
        )
    }

    private fun cancelAlarmTimeout(alarm: Alarm) {
        alarmMgr.cancel(createAlarmIntent(alarm))
    }

    private fun createTimer() {
        debug("createTimer()")

        if (duration.isZero)
            return

        val remaining = duration.clone()
        val alarm = alarmDao.create(remaining)
        attachAlarm(alarm)

        onClear()
    }
}
