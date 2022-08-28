package com.example.android.guesstheword.screens.game

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

class GameFragment : Fragment() {

    private lateinit var binding: GameFragmentBinding
    private lateinit var viewModel: GameViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )

        // Connecting ViewModel To fragment
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        // Connecting Binding with viewModel
        binding.gameViewModel = viewModel
        binding.lifecycleOwner = this

        // Observing State of Game
        viewModel.eventGameFinished.observe(viewLifecycleOwner, Observer {isGameFinished->
            if(isGameFinished){
                gameFinished()
                viewModel.onGameFinishedCompleted()
            }
        })

        // Buzzes when triggered with different buzz events
        viewModel.eventBuzz.observe(viewLifecycleOwner, Observer { buzzType ->
            if (buzzType != GameViewModel.BuzzType.NO_BUZZ) {
                // making timer text red when it is in COUNT_DOWN_PANIC State
                if(buzzType == GameViewModel.BuzzType.COUNTDOWN_PANIC){
                    binding.timerText.setTextColor(resources.getColor(R.color.selected_red_color))
                }
                buzz(buzzType.pattern)
                viewModel.onBuzzComplete()
            }
        })

        return binding.root
    }

    // Called when the game is finished
    private fun gameFinished() {
        val action = GameFragmentDirections.actionGameToScore(viewModel.score.value!!)
        findNavController(this).navigate(action)
    }

    // Buzz Method
    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService<Vibrator>()

        buzzer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
            }
        }
    }

}