package com.example.android.guesstheword.screens.score


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(private var _finalScore: Int) : ViewModel(){

    // Final Score
    private var _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    // EventOnPlayAgain
    private var _eventOnPlayAgain = MutableLiveData<Boolean>()
    val eventOnPlayAgain : LiveData<Boolean>
        get() = _eventOnPlayAgain

    init {
        _score.value = _finalScore
        _eventOnPlayAgain.value = false
    }

    fun onPlayAgain(){
        _eventOnPlayAgain.value = true
    }
    fun onPlayAgainCompleted(){
        _eventOnPlayAgain.value = false
    }

}