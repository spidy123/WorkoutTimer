package com.shubhamh.android.apps.workouttimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    private lateinit var setsCountText: TextView
    private lateinit var setsTimerText: TextView
    private lateinit var restTimerText: TextView
    private lateinit var countersViewModel: CountersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countersViewModel =
            ViewModelProviders.of(/* activity= */ this).get(CountersViewModel::class.java)
        countersViewModel.apply {
            setsCountLiveData.observe(this@MainActivity, { updateSetsCountText() })
            setsTimerLiveData.observe(this@MainActivity, { updateSetsTimerText() })
            restTimerLiveData.observe(this@MainActivity, { updateRestTimerText() })
        }
        setupSetsCountLayout()
        setupSetsTimerLayout()
        setupRestTimerLayout()
        setupPlayAndRestartButtons()
    }

    private fun setupPlayAndRestartButtons() {
      findViewById<MaterialButton>(R.id.resetButton).setOnClickListener {
          countersViewModel.resetCounters()
      }

      findViewById<MaterialButton>(R.id.playButton).setOnClickListener {

      }
    }

    private fun setupSetsCountLayout() {
        val setsCountLayout = findViewById<LinearLayout>(R.id.setsCountLayout)
        setsCountLayout.apply {
            setsCountText = findViewById<MaterialButton>(R.id.countText)
            findViewById<MaterialButton>(R.id.plusButton).setOnClickListener {
                countersViewModel.increaseSetCount(
                    setsCountText.text.toString()
                )
            }
            findViewById<MaterialButton>(R.id.minusButton).setOnClickListener {
                countersViewModel.decreaseSetCount(
                    baseContext,
                    setsCountText.text.toString()
                )
            }
        }
    }

    private fun setupSetsTimerLayout() {
        val setTimerLayout = findViewById<LinearLayout>(R.id.setsTimerLayout)
        setTimerLayout.apply {
            setsTimerText = findViewById<MaterialButton>(R.id.countText)
            findViewById<MaterialButton>(R.id.plusButton).setOnClickListener {
                countersViewModel.increaseTimerCount(
                    setsTimerText.text.toString()
                )
            }
            findViewById<MaterialButton>(R.id.minusButton).setOnClickListener {
                countersViewModel.decreaseTimerCount(
                    baseContext, setsTimerText.text.toString()
                )
            }
            findViewById<MaterialButton>(R.id.adjustButton).apply {
                setOnClickListener {
                    val dialog = SetDifferentTimerForEachSetDialog()
                    dialog.show(supportFragmentManager, "UpdateSetsTimerDialog")
                }
            }
        }
    }

    private fun setupRestTimerLayout() {
        val restTimerLayout = findViewById<LinearLayout>(R.id.restTimerLayout)
        restTimerLayout.apply {
            restTimerText = findViewById<MaterialButton>(R.id.countText)
            findViewById<MaterialButton>(R.id.plusButton).setOnClickListener {
                countersViewModel.increaseRestTimer(
                    restTimerText.text.toString()
                )
            }
            findViewById<MaterialButton>(R.id.minusButton).setOnClickListener {
                countersViewModel.decreaseRestTimer(
                    baseContext, restTimerText.text.toString()
                )
            }
        }
    }

    private fun updateSetsCountText() {
        setsCountText.text = countersViewModel.setsCountLiveData.value.toString()
        countersViewModel.updateSetWithTimerMap(true)
    }

    private fun updateSetsTimerText() {
        setsTimerText.text = countersViewModel.setsTimerLiveData.value.toString()
        countersViewModel.updateSetWithTimerMap(true)
    }

    private fun updateRestTimerText() {
        restTimerText.text = countersViewModel.restTimerLiveData.value.toString()
    }
}