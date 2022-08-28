
package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

// Constants for Buzzer
private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

class GameViewModel : ViewModel(){

    // The current word
    private var _word = MutableLiveData<String>()
    val word : LiveData<String>
        get() = _word

    // The current score
    private var _score = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = _score

    // The wordList
    private var wordList : MutableList<String> = mutableListOf()

    // Event GameFinished
    private var _eventGameFinished = MutableLiveData<Boolean>()
    val eventGameFinished : LiveData<Boolean>
        get() = _eventGameFinished

    // Timer
    private lateinit var timer : CountDownTimer

    // Current Timer Time
    private var _currentTimerTime = MutableLiveData<Long>()
    val currentTimerTimeString = Transformations.map(_currentTimerTime) {currentTimerTime->
        DateUtils.formatElapsedTime(currentTimerTime)
    }

    // Enum class to hold the buzzing types
    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    // Companion Object For Timer & Buzz
    companion object {
        // This is when the game is over
        const val DONE = 0L
        // This is the time when the phone will start buzzing each second
        private const val COUNTDOWN_PANIC_SECONDS = 10L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 60000L
    }

    // Event that triggers the phone to buzz using different patterns, determined by BuzzType
    private val _eventBuzz = MutableLiveData<BuzzType>()
    val eventBuzz: LiveData<BuzzType>
        get() = _eventBuzz


    init {
        resetList()
        nextWord()
        _score.value = 0
        _eventGameFinished.value = false

        // Start Timer
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND){
            override fun onTick(millisUntilFinished: Long) {
                _currentTimerTime.value = millisUntilFinished/ ONE_SECOND
                if (millisUntilFinished / ONE_SECOND <= COUNTDOWN_PANIC_SECONDS) {
                    _eventBuzz.value = BuzzType.COUNTDOWN_PANIC
                }
            }

            override fun onFinish() {
                _currentTimerTime.value = DONE
                _eventGameFinished.value = true
                _eventBuzz.value = BuzzType.GAME_OVER
            }

        }.start()
    }


    // Skip current word
    fun onSkip() {
        if(_score.value!! > 0){
            _score.value = _score.value?.minus(1)
        }
        nextWord()
    }

    // Guess of word is correct
    fun onCorrect() {
        _score.value = _score.value?.plus(1)
        _eventBuzz.value = BuzzType.CORRECT
        Log.i("Buzz", "Correct")
        nextWord()
    }

    // Moves to the next word
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        } else {
            _word.value = wordList.removeAt(0)
        }
    }

    // Reset the whole list of words randomly
    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }

    // When game is finished, set the event to false
    fun onGameFinishedCompleted(){
        _eventGameFinished.value = false
    }

    // When Buzz is complete set it to NO_BUZZ in order to stop buzzing after the game is finished
    fun onBuzzComplete() {
        _eventBuzz.value = BuzzType.NO_BUZZ
    }

    // Cancel timer when viewModel is cleared
    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}