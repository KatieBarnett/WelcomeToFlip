package dev.katiebarnett.welcometoflip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.katiebarnett.welcometoflip.databinding.NoAnimationFlipFragmentBinding

@AndroidEntryPoint
class NoAnimationFlipFragment : Fragment(R.layout.no_animation_flip_fragment) {

    private val viewModel: NoAnimationViewModel by viewModels()
    
    private lateinit var binding: NoAnimationFlipFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = NoAnimationFlipFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.numberDeck.setOnClickListener {
            viewModel.nextCardDeckUpdate()
        }
        
        binding.actionDeck.setOnClickListener {
            viewModel.previousCardDeckUpdate()
        }
    }
}