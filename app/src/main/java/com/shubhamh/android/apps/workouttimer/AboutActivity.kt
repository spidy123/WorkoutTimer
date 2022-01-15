package com.shubhamh.android.apps.workouttimer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class AboutActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_activity)
        supportActionBar?.title = getString(R.string.about_menu)
    }
}