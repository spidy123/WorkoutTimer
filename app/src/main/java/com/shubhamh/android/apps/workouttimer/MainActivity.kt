package com.shubhamh.android.apps.workouttimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    private lateinit var setsCountText: TextView
    private lateinit var setsTimerText: TextView
    private lateinit var restTimerText: TextView
    private lateinit var countersViewModel: CountersViewModel
    private lateinit var playButton: MaterialButton

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

    private fun setupPlayAndRestartButtons() {
      findViewById<MaterialButton>(R.id.resetButton).setOnClickListener {
          countersViewModel.resetCounters()
      }

      playButton = findViewById(R.id.playButton)
          playButton.setOnClickListener {
              val restTime = countersViewModel.restTimerLiveData.value ?: 0
              val setWithTimerMap = countersViewModel.setWithTimerMap.value ?: emptyMap()
          startActivity(IntentUtil.getTimerActivityIntent(this, restTime, setWithTimerMap))
      }
      updatePlayButtonState()
    }

    private fun shouldEnableButton(): Boolean {
        val setCount = countersViewModel.setsCountLiveData.value ?: return false
        val setTime = countersViewModel.setsTimerLiveData.value ?: return false

        return setCount != 0 && setTime != 0
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
        updatePlayButtonState()
    }

    private fun updateSetsTimerText() {
        setsTimerText.text = countersViewModel.setsTimerLiveData.value.toString()
        countersViewModel.updateSetWithTimerMap(true)
        updatePlayButtonState()
    }

    private fun updateRestTimerText() {
        restTimerText.text = countersViewModel.restTimerLiveData.value.toString()
    }

    private fun updatePlayButtonState() {
      playButton.isEnabled = shouldEnableButton()
    }
}