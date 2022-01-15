package com.shubhamh.android.apps.workouttimer

import android.content.Context
import android.content.Intent
import android.os.Bundle

object IntentUtil {
    const val REST_TIME_EXTRA = "rest_time_extra"
    const val SET_TIMER_MAP_EXTRA = "set_timer_map_extra"

    fun getTimerActivityIntent(context: Context, restTime: Int, setWithTimerMap: Map<Int, Int>?): Intent {
        val map = HashMap<Int, Int>()
        if (setWithTimerMap != null) {
            map.putAll(setWithTimerMap)
        }
        val bundle = Bundle().apply {
            putInt(REST_TIME_EXTRA, restTime)
            putSerializable(SET_TIMER_MAP_EXTRA, map)
        }

        return Intent(context, TimerActivity::class.java).putExtras(bundle)
    }

    fun getMainActivityIntent(context: Context): Intent {
        return Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    fun getAboutActivityIntent(context: Context) = Intent(context, AboutActivity::class.java)

    fun getFeedbackActivityIntent(context: Context) = Intent(context, FeedbackActivity::class.java)
}