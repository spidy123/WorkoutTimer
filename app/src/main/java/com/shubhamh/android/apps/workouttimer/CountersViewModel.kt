package com.shubhamh.android.apps.workouttimer

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CountersViewModel: ViewModel() {

    private val _setsCountLiveData = MutableLiveData<Int>()
    val setsCountLiveData = _setsCountLiveData

    private val _setsTimerLiveData = MutableLiveData<Int>()
    val setsTimerLiveData = _setsTimerLiveData

    private val _restTimerLiveData = MutableLiveData<Int>()
    val restTimerLiveData = _restTimerLiveData

    private val _setWithTimerMap = MutableLiveData<HashMap<Int, Int>>()
    val setWithTimerMap = _setWithTimerMap

    private val setWithTimer = HashMap<Int, Int>()

    fun increaseSetCount(currentSetCountText: String) {
        var currentSetCount = Integer.parseInt(currentSetCountText)
        _setsCountLiveData.postValue(++currentSetCount)
    }

    fun decreaseSetCount(context: Context, currentSetCountText: String) {
        var currentSetCount = Integer.parseInt(currentSetCountText)
        if (currentSetCount == 0) {
            Toast.makeText(context, "Minimum value can be set is 0", Toast.LENGTH_SHORT).show()
        } else {
            _setsCountLiveData.postValue(--currentSetCount)
        }
    }

    fun increaseTimerCount(currentTimerCountText: String) {
        var currentTimerCount = Integer.parseInt(currentTimerCountText)
        currentTimerCount += 5
        _setsTimerLiveData.postValue(currentTimerCount)
    }

    fun decreaseTimerCount(context: Context, currentTimerCountText: String) {
        var currentTimerCount = Integer.parseInt(currentTimerCountText)
        if (currentTimerCount == 0) {
            Toast.makeText(context, "Minimum value can be set is 0", Toast.LENGTH_SHORT).show()
        } else {
            currentTimerCount -= 5
            _setsTimerLiveData.postValue(currentTimerCount)
        }
    }

    fun updateSetWithTimerMap(shouldClearExistedData: Boolean) {
        val setsCountLiveData = setsCountLiveData.value
        val setsTimerLiveData = setsTimerLiveData.value
        if (shouldClearExistedData) {
            setWithTimer.clear()
            if (setsCountLiveData != null && setsCountLiveData != 0) {
                for (position in 0 until setsCountLiveData) {
                    setWithTimer[position] = setsTimerLiveData ?: 0
                }
                _setWithTimerMap.postValue(setWithTimer)
            }
        }
    }

    fun updateSetWithTimerMap(context: Context, setIndex: Int, shouldIncrease: Boolean) {
        if (shouldIncrease) {
            if (setIndex < setWithTimer.size) {
                var currentTimerValue = setWithTimer[setIndex]
                currentTimerValue?.let {
                    currentTimerValue += 5
                    setWithTimer[setIndex] = currentTimerValue
                    _setWithTimerMap.postValue(setWithTimer)
                }
            }
        } else {
            if (setIndex < setWithTimer.size) {
                var currentTimerValue = setWithTimer[setIndex]
                currentTimerValue?.let {
                    currentTimerValue -= 5
                    if (currentTimerValue >= 0) {
                        setWithTimer[setIndex] = currentTimerValue
                        _setWithTimerMap.postValue(setWithTimer)
                    } else {
                        Toast.makeText(context, "Minimum value can be set is 0", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun increaseRestTimer(currentRestTimerText: String) {
        var currentRestTimer = Integer.parseInt(currentRestTimerText)
        currentRestTimer += 5
        _restTimerLiveData.postValue(currentRestTimer)
    }

    fun decreaseRestTimer(context: Context, currentRestTimerText: String) {
        var currentRestTimer = Integer.parseInt(currentRestTimerText)
        if (currentRestTimer == 0) {
            Toast.makeText(context, "Minimum value can be set is 0", Toast.LENGTH_SHORT).show()
        } else {
            currentRestTimer -= 5
            _restTimerLiveData.postValue(currentRestTimer)
        }
    }

    fun resetCounters() {
        _setsTimerLiveData.postValue(0)
        _setsCountLiveData.postValue(0)
        _restTimerLiveData.postValue(0)
        _setWithTimerMap.value?.clear()
    }

    override fun onCleared() {
        super.onCleared()
        resetCounters()
    }
}