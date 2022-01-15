package com.shubhamh.android.apps.workouttimer

import android.os.Bundle
import android.os.CountDownTimer
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            launchMainActivity()
        } else {
            supportActionBar?.hide()
            setContentView(R.layout.splash_screen_layout)

            object : CountDownTimer(5000, 1000) {
                override fun onFinish() {
                    startActivity(IntentUtil.getMainActivityIntent(this@SplashScreen))
                }

                override fun onTick(p0: Long) {
                    // No op
                }
            }.start()
        }
    }

    private fun launchMainActivity() {
        startActivity(IntentUtil.getMainActivityIntent(this@SplashScreen))
    }
}