
package com.example.android.guesstheword.screens.score

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.ScoreFragmentBinding


class ScoreFragment : Fragment() {

    private lateinit var viewModel: ScoreViewModel
    private lateinit var viewModelFactory: ScoreViewModelFactory

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate view and obtain an instance of the binding class.
        val binding: ScoreFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.score_fragment, container, false)

        // Get args using by navArgs property delegate
        val scoreFragmentArgs by navArgs<ScoreFragmentArgs>()

        // Get ViewModel and ViewModelFactory
        viewModelFactory = ScoreViewModelFactory(scoreFragmentArgs.score)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ScoreViewModel::class.java)

        // Connecting Binding with viewModel
        binding.scoreViewModel = viewModel
        binding.lifecycleOwner = this

        // Adding Observer for playAgainState
        viewModel.eventOnPlayAgain.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigate(ScoreFragmentDirections.actionRestart())
                viewModel.onPlayAgainCompleted()
            }
        })

        return binding.root
    }
}
