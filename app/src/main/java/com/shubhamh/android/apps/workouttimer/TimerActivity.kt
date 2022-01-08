package com.shubhamh.android.apps.workouttimer

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.button.MaterialButton

class TimerActivity : AppCompatActivity() {
    private lateinit var rippleView: RippleView
    private lateinit var pauseButton: MaterialButton
    private lateinit var timerButton: MaterialButton
    private lateinit var titleText: TextView
    private var isTimerPaused = false
    private var restTime = 0
    private var setWithTimer = HashMap<Int, Int>()
    private lateinit var timerViewModel: TimerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timer_activity)

        savedInstanceState ?: setupData()

        timerButton = findViewById(R.id.timerButton)
        titleText = findViewById(R.id.titleText)
        rippleView = findViewById(R.id.rippleView)
        rippleView.startAnimation()

        pauseButton = findViewById(R.id.pauseButton)
        pauseButton.setOnClickListener {
            if (!isTimerPaused) {
                isTimerPaused = true
                timerViewModel.pauseTimer()
                rippleView.stopAnimation()
                pauseButton.icon = getDrawable(R.drawable.play_button)
            } else {
                pauseButton.icon = getDrawable(R.drawable.pause_button)
                isTimerPaused = false
                rippleView.startAnimation()
                timerViewModel.startTimer()
            }
        }
        findViewById<MaterialButton>(R.id.stopButton).setOnClickListener {
            this.finish()
        }

        timerViewModel =
            ViewModelProviders.of(/* activity= */ this).get(TimerViewModel::class.java)
        timerViewModel.apply {
            setsTimerLiveData.observe(this@TimerActivity, { updateUi(it) })
            setsCountLiveData.observe(this@TimerActivity, { updateTitleText(it) })
            setWithTimerMap.observe(this@TimerActivity, { if (it.size == 0)  finish() })
        }

        savedInstanceState ?: timerViewModel.setMapData(setWithTimer, restTime)

        timerViewModel.startTimer()
    }

    private fun updateUi(timerText: Int) {
        timerButton.text = timerText.toString()
    }

    private fun updateTitleText(setCount: Int) {
        titleText.text = getString(R.string.timer_screen_title, setCount)
    }

    private fun setupData() {
        restTime = intent.getIntExtra(IntentUtil.REST_TIME_EXTRA, 0)
        setWithTimer = intent.getSerializableExtra(IntentUtil.SET_TIMER_MAP_EXTRA) as HashMap<Int, Int>
    }
}