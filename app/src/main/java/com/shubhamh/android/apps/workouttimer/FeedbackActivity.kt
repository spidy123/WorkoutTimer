package com.shubhamh.android.apps.workouttimer

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.button.MaterialButton

class FeedbackActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feedback_activity)
       supportActionBar?.title = getString(R.string.feedback_menu)

        findViewById<LottieAnimationView>(R.id.animationView).apply {
            setAnimation(R.raw.feedback)
            playAnimation()
        }

        findViewById<MaterialButton>(R.id.submitButton).setOnClickListener {
            val feedbackTitle = findViewById<TextView>(R.id.feedback_title).text.toString()
            val feedbackDescription = findViewById<TextView>(R.id.feedback_description).text.toString()
            sendFeedback(feedbackTitle, feedbackDescription)
        }
    }

    private fun sendFeedback(subject: String, body: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.apply {
            type = getString(R.string.email_intent_type)
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_intent_to)))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }

        startActivity(Intent.createChooser(intent, "Send email"))
    }
}