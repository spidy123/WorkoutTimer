package com.shubhamh.android.apps.workouttimer

import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.button.MaterialButton

class TimerActivity : AppCompatActivity() {
    private lateinit var rippleView: RippleView
    private lateinit var animationView: LottieAnimationView
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

        timerButton = findViewById(R.id.timerButton)
        titleText = findViewById(R.id.titleText)
        rippleView = findViewById(R.id.rippleView)
        animationView = findViewById(R.id.animationView)
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
                rippleView.startAnimation(getColorFromState())
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
            restTimerLiveData.observe(this@TimerActivity, { updateUi(it) })
            setsCountLiveData.observe(this@TimerActivity, { updateTitleText(it) })
            setWithTimerMap.observe(this@TimerActivity, { if (it.size == 0)  finish() })
            currentStateLiveData.observe(this@TimerActivity, {
                updateButtonColor()
                playVoice(it)
            })
        }

        savedInstanceState ?: run {
            setupData()
            timerViewModel.setMapData(setWithTimer, restTime)
            timerViewModel.startCountDownTimer()
            playVoice(TimerViewModel.State.WORKOUT_TIME)
            updateAnimation()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.about -> {
                startActivity(IntentUtil.getAboutActivityIntent(this))
                true
            }
            R.id.feedback -> {
                startActivity(IntentUtil.getFeedbackActivityIntent(this))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateUi(timerText: Int) {
        timerButton.text = timerText.toString()
    }

    private fun getColorFromState(): Int? {
        val currentState = timerViewModel.currentStateLiveData.value ?: return null
        return when(currentState) {
            TimerViewModel.State.REST_TIME -> ContextCompat.getColor(this, R.color.cool)
            TimerViewModel.State.WORKOUT_TIME -> ContextCompat.getColor(this, R.color.teal_200)
            TimerViewModel.State.ALERT_TIME -> ContextCompat.getColor(this, R.color.alert)
        }
    }

    private fun getAnimationFromState(): Int? {
        val currentState = timerViewModel.currentStateLiveData.value ?: return null
        return when(currentState) {
            TimerViewModel.State.REST_TIME -> R.raw.rest
            TimerViewModel.State.WORKOUT_TIME -> R.raw.workout
            TimerViewModel.State.ALERT_TIME -> R.raw.alert
        }
    }

    private fun updateButtonColor() {
        val color = getColorFromState() ?: return
        timerButton.setBackgroundColor(color)
        rippleView.startAnimation(color)
        updateAnimation()
    }

    private fun updateAnimation() {
        val animationAsset = getAnimationFromState()
        if (animationAsset == null) {
            animationView.setAnimation(R.raw.workout)
            animationView.playAnimation()
        } else {
            animationView.setAnimation(animationAsset)
            animationView.playAnimation()
        }
    }

    private fun playVoice(state: TimerViewModel.State) {
        when(state) {
            TimerViewModel.State.WORKOUT_TIME -> {
                val mediaPlayer = MediaPlayer.create(this, R.raw.start_workout)
                mediaPlayer.start()
            }
            TimerViewModel.State.REST_TIME -> {
                val mediaPlayer = MediaPlayer.create(this, R.raw.rest_time)
                mediaPlayer.start()
            }
            TimerViewModel.State.ALERT_TIME -> {
                val mediaPlayer = MediaPlayer.create(this, R.raw.approaching_rest)
                mediaPlayer.start()
            }
        }
    }


    private fun updateTitleText(setCount: Int) {
        titleText.text = getString(R.string.timer_screen_title, setCount)
    }

    private fun setupData() {
        restTime = intent.getIntExtra(IntentUtil.REST_TIME_EXTRA, 0)
        setWithTimer = intent.getSerializableExtra(IntentUtil.SET_TIMER_MAP_EXTRA) as HashMap<Int, Int>
    }
}