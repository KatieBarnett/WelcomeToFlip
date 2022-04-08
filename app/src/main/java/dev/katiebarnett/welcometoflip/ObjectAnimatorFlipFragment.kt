package dev.katiebarnett.welcometoflip

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.animation.doOnEnd
import androidx.core.view.doOnAttach
import androidx.core.view.doOnLayout
import androidx.core.view.doOnNextLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.katiebarnett.welcometoflip.databinding.ObjectAnimatorFlipFragmentBinding

@AndroidEntryPoint
class ObjectAnimatorFlipFragment : Fragment(R.layout.object_animator_flip_fragment) {

    private val viewModel: ObjectAnimatorViewModel by viewModels()
    
    private lateinit var binding: ObjectAnimatorFlipFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ObjectAnimatorFlipFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val flipOutAnimatorSet = AnimatorInflater.loadAnimator(context, R.animator.flip_out) as AnimatorSet
        val flipInAnimatorSet = AnimatorInflater.loadAnimator(context, R.animator.flip_in) as AnimatorSet
        
        binding.numberDeck.setOnClickListener {

            // Simulate the 'move' of the card:
            // Make the animation cards visible (will overlay our action deck)
            binding.flipCardFront.visibility = View.VISIBLE
            binding.flipCardBack.visibility = View.VISIBLE
            // Update the number deck position
            viewModel.nextCardNumberDeck()

            // Do the flip animation
            try {
                // Front card
                flipOutAnimatorSet.setTarget(binding.flipCardFront)

                // Back card
                flipInAnimatorSet.setTarget(binding.flipCardBack)

                flipOutAnimatorSet.start()
                flipInAnimatorSet.start()

                flipInAnimatorSet.doOnEnd {

                    // Update the action deck position 
                    viewModel.nextCardActionDeck()
                    
                    binding.actionDeck.invalidate()

                    // Hide the animation cards
                    binding.flipCardFront.visibility = View.GONE
                    binding.flipCardBack.visibility = View.GONE

                    // Reset the animation
                    flipOutAnimatorSet.currentPlayTime = 0
                    flipInAnimatorSet.currentPlayTime = 0

                    // Set the animation cards ready for the next flip
                    viewModel.updateFlipCardPosition()
                }
            } catch (e: Exception) {
                Log.e(this.javaClass.name, "Error in flip", e)
            }
        }

        binding.actionDeck.setOnClickListener {
            viewModel.previousCardNumberDeck()
            viewModel.previousCardActionDeck()
            viewModel.updateFlipCardPosition()
        }
    }

    inline fun View.waitForLayout(crossinline f: () -> Unit) = with(viewTreeObserver) {
        addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                removeOnGlobalLayoutListener(this)
                f()
            }
        })
    }
    
}