package com.shubhamh.android.apps.workouttimer

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel: ViewModel() {
    private val _setsCountLiveData = MutableLiveData<Int>()
    val setsCountLiveData = _setsCountLiveData

    private val _setsTimerLiveData = MutableLiveData<Int>()
    val setsTimerLiveData = _setsTimerLiveData


    private val _setWithTimerMap = MutableLiveData<HashMap<Int, Int>>()
    val setWithTimerMap = _setWithTimerMap

    private val setWithTimer = HashMap<Int, Int>()
    private var restTime = 0

    private var countDownTimer: CountDownTimer? = null
    private var originalSizeOfMap = 0

    fun setMapData(data: HashMap<Int, Int>, restTime: Int) {
        setWithTimer.putAll(data)
        _setWithTimerMap.postValue(setWithTimer)
        originalSizeOfMap = setWithTimer.size
        this.restTime = restTime
    }

    fun startTimer() {
        if (setWithTimer.isEmpty()) {
            android.util.Log.d("TimerViewModel", "Empty map found in timer.")
            return
        }

        val time = setWithTimer.values.first()
        countDownTimer = object : CountDownTimer(time.toLong() * MILLIS_IN_SECONDS, MILLIS_IN_SECONDS) {
            override fun onTick(time: Long) {
                val newTimerForSet = (time / MILLIS_IN_SECONDS).toInt()
                _setsTimerLiveData.postValue(newTimerForSet)
                setWithTimer[setWithTimer.entries.first().key] = newTimerForSet
                _setWithTimerMap.postValue(setWithTimer)
                _setsCountLiveData.postValue((originalSizeOfMap + 1) - setWithTimer.size)
            }

            override fun onFinish() {
                if (setWithTimer.isNotEmpty()) {
                    setWithTimer.remove(setWithTimer.entries.first().key)
                    _setWithTimerMap.postValue(setWithTimer)
                    startTimer()
                }
            }

        }

        countDownTimer?.start()
    }

    fun pauseTimer() {
        countDownTimer?.cancel()
    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
    }

}