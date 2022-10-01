package com.hwangblood.timefighter

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var gameScoreTextView: TextView
    private lateinit var timeLeftTextView: TextView
    private lateinit var tapMeButton: Button

    private var score = 0

    private var gameStarted = false
    private lateinit var countDownTimer: CountDownTimer
    private val initialCountDown: Long = 60000
    private val countDownTimeSeconds = initialCountDown / 1000
    private val countDownInterval: Long = 1000
    private var timeLeft = 60

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // connect views to variables
        gameScoreTextView = findViewById(R.id.game_score_text_view)
        timeLeftTextView = findViewById(R.id.time_left_text_view)
        tapMeButton = findViewById(R.id.tap_me_button)
        tapMeButton.setOnClickListener { incrementScore() }

        resetGame()
    }

    private fun incrementScore() {
        // increment score logic
        if (!gameStarted) {
            startGame()
        }

        score++
        val newScore = getString(R.string.your_score, score)
        gameScoreTextView.text = newScore
    }

    private fun resetGame() {
        // reset game logic
        score = 0

        val initialScore = getString(R.string.your_score, score)
        gameScoreTextView.text = initialScore
        val initialTimeLeft = getString(R.string.time_left, countDownTimeSeconds)
        timeLeftTextView.text = initialTimeLeft

        countDownTimer = object : CountDownTimer(
            initialCountDown, countDownInterval
        ) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000
                val timeLeftString = getString(R.string.time_left, timeLeft)
                timeLeftTextView.text = timeLeftString
            }

            override fun onFinish() {
                // To Be Implemented Later
            }
        }
        gameStarted = false
    }

    private fun startGame() {
        // start game logic
        countDownTimer.start()
        gameStarted = true
    }

    private fun endGame() {
        // end game logic
    }
}