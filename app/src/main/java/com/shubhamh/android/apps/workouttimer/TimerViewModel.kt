package com.shubhamh.android.apps.workouttimer

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel: ViewModel() {
    private val _restTimerLiveData = MutableLiveData<Int>()
    val restTimerLiveData = _restTimerLiveData

    private val _currentStateLiveData = MutableLiveData<State>()
    val currentStateLiveData = _currentStateLiveData

    private val _setsCountLiveData = MutableLiveData<Int>()
    val setsCountLiveData = _setsCountLiveData

    private val _setsTimerLiveData = MutableLiveData<Int>()
    val setsTimerLiveData = _setsTimerLiveData


    private val _setWithTimerMap = MutableLiveData<HashMap<Int, Int>>()
    val setWithTimerMap = _setWithTimerMap

    private val setWithTimer = HashMap<Int, Int>()
    private var restTime = 0

    private var countDownTimer: CountDownTimer? = null
    private var restTimer: CountDownTimer? = null

    private var originalSizeOfMap = 0

    enum class State {
        REST_TIME,
        WORKOUT_TIME,
        ALERT_TIME
    }

    fun setMapData(data: HashMap<Int, Int>, restTime: Int) {
        setWithTimer.putAll(data)
        _setWithTimerMap.postValue(setWithTimer)
        originalSizeOfMap = setWithTimer.size
        this.restTime = restTime
    }

    fun startCountDownTimer() {
        if (setWithTimer.isEmpty()) {
            android.util.Log.d("TimerViewModel", "Empty map found in timer.")
            return
        }

        val time = setWithTimer.values.first()
        countDownTimer = object : CountDownTimer(time.toLong() * MILLIS_IN_SECONDS, MILLIS_IN_SECONDS) {
            override fun onTick(time: Long) {
                val newTimerForSet = (time / MILLIS_IN_SECONDS).toInt()
                if (newTimerForSet == 5) {
                    _currentStateLiveData.postValue(State.ALERT_TIME)
                }
                _setsTimerLiveData.postValue(newTimerForSet)
                setWithTimer[setWithTimer.entries.first().key] = newTimerForSet
                _setWithTimerMap.postValue(setWithTimer)
                _setsCountLiveData.postValue((originalSizeOfMap + 1) - setWithTimer.size)
            }

            override fun onFinish() {
                if (setWithTimer.isNotEmpty()) {
                    setWithTimer.remove(setWithTimer.entries.first().key)
                    _setWithTimerMap.postValue(setWithTimer)
                    _currentStateLiveData.postValue(State.REST_TIME)
                    startRestTimer()
                }
            }

        }

        restTimer?.cancel()
        countDownTimer?.start()
    }

    private fun startRestTimer() {
        var restTimerLiveDataValue = restTimerLiveData.value ?: restTime
        if (restTimerLiveDataValue == 0) {
            restTimerLiveDataValue = restTime
        }

        restTimer = object : CountDownTimer(restTimerLiveDataValue.toLong() * MILLIS_IN_SECONDS, MILLIS_IN_SECONDS) {
            override fun onTick(time: Long) {
                val remainingRestTime = (time / MILLIS_IN_SECONDS).toInt()
                _restTimerLiveData.postValue(remainingRestTime)
            }

            override fun onFinish() {
                if (setWithTimer.isNotEmpty()) {
                    android.util.Log.d("######", "posting WORKOUT_TIME")
                    _currentStateLiveData.postValue(State.WORKOUT_TIME)
                    startCountDownTimer()
                }
            }

        }

        countDownTimer?.cancel()
        restTimer?.start()
    }

    fun pauseTimer() {
        restTimer?.cancel()
        countDownTimer?.cancel()
    }

    fun startTimer() {
        val restTimeValue = restTimerLiveData.value
        if (restTimeValue != null && restTimeValue > 0) {
            startRestTimer()
        } else {
            startCountDownTimer()
        }
    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
    }

}