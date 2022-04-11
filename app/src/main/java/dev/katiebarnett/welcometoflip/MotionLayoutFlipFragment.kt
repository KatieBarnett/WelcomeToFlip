package dev.katiebarnett.welcometoflip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.katiebarnett.welcometoflip.databinding.MotionLayoutFlipFragmentBinding
import dev.katiebarnett.welcometoflip.databinding.MotionLayoutSimpleFlipFragmentBinding


@AndroidEntryPoint
class MotionLayoutFlipFragment : Fragment(R.layout.motion_layout_flip_fragment) {

    private val viewModel: MotionLayoutViewModel by viewModels()
    
    private lateinit var binding: MotionLayoutFlipFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = MotionLayoutFlipFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.numberDeck.setOnClickListener { 
            doMoveRight()
        }

        binding.actionDeck.setOnClickListener {
            viewModel.previousCardNumberDeck()
            viewModel.previousCardActionDeck()
            viewModel.updateFlipCardPosition()
        }
    }
    
    fun doMoveRight() {
        binding.parentView.setTransition(R.id.moveRight)
        binding.parentView.transitionToEnd()
        binding.parentView.setTransitionListener(object: MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
                binding.flipCardFront.alpha = 1f
                binding.flipCardBack.alpha = 1f
                viewModel.nextCardNumberDeck()
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                // Not required
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                doFlip()
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
                // Not required
            }
        })
    }
    
    fun doFlip() {
        binding.parentView.setTransition(R.id.flipMotion)
        binding.parentView.transitionToEnd()
        binding.parentView.setTransitionListener(object: MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                // Not required
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                viewModel.nextCardActionDeck()
//                binding.flipCardFront.alpha = 0f
//                binding.flipCardBack.alpha = 0f
//                viewModel.updateFlipCardPosition()
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
                // Not required
            }
        })
        
    }
}