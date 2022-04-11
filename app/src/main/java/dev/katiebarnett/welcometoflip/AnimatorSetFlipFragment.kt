package dev.katiebarnett.welcometoflip

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import dev.katiebarnett.welcometoflip.databinding.AnimatorSetFlipFragmentBinding
import kotlinx.coroutines.delay
import okhttp3.internal.wait


@AndroidEntryPoint
class AnimatorSetFlipFragment : Fragment(R.layout.animator_set_flip_fragment) {

    private val viewModel: AnimatorSetViewModel by viewModels()
    
    private lateinit var binding: AnimatorSetFlipFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = AnimatorSetFlipFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val moveRightAnimator = ValueAnimator.ofFloat(0f, 1.0f).apply {
            duration = 1000    
            addUpdateListener { updatedAnimation ->
                val constraintSet = ConstraintSet()
                constraintSet.clone(binding.parentView)
                constraintSet.setHorizontalBias(R.id.flipCardBack, updatedAnimation.animatedValue as Float)
                constraintSet.applyTo(binding.parentView)
            }
        }

        val flipOutAnimatorSet = AnimatorInflater.loadAnimator(context, R.animator.flip_out) as AnimatorSet
        flipOutAnimatorSet.setTarget(binding.flipCardFront)
        
        val flipInAnimatorSet = AnimatorInflater.loadAnimator(context, R.animator.flip_in) as AnimatorSet
        flipInAnimatorSet.setTarget(binding.flipCardBack)

        val hideFlipCardAnimator = ValueAnimator.ofFloat(1.0f, 0.0f).apply {
            duration = 100
            addUpdateListener { updatedAnimation ->
                binding.flipCardBack.alpha = updatedAnimation.animatedValue as Float
            }
        }
        
        val showFlipCardAnimator = ValueAnimator.ofFloat(0.0f, 1.0f).apply {
            duration = 100
            addUpdateListener { updatedAnimation ->
                binding.flipCardBack.alpha = updatedAnimation.animatedValue as Float
            }
        }

        
        val moveLeftAnimator = ValueAnimator.ofFloat(1.0f, 0.0f).apply {
            duration = 1000
            addUpdateListener { updatedAnimation ->
                val constraintSet = ConstraintSet()
                constraintSet.clone(binding.parentView)
                constraintSet.setHorizontalBias(R.id.flipCardBack, updatedAnimation.animatedValue as Float)
                constraintSet.applyTo(binding.parentView)
            }
        }

        val resetMotion = AnimatorSet().apply {
            play(hideFlipCardAnimator).before(moveLeftAnimator)
            play(moveLeftAnimator)
            doOnEnd {
                viewModel.updateFlipCardPosition()
            }
        }

        val forwardMotion = AnimatorSet().apply {
            play(showFlipCardAnimator).before(moveRightAnimator)
            play(moveRightAnimator).before(flipOutAnimatorSet)
            play(flipOutAnimatorSet).with(flipInAnimatorSet)
            doOnEnd {
                // Update the action deck position 
                viewModel.nextCardActionDeck()
                resetMotion.start()
            }
        }
        
        binding.numberDeck.setOnClickListener {

            // Make the animation cards visible (will overlay our number deck)
            binding.flipCardFront.visibility = View.VISIBLE
            binding.flipCardBack.visibility = View.VISIBLE

            // Stop the view getting cut off 
            val scale = resources.displayMetrics.density
            val cameraDist = 8000 * scale
            binding.flipCardFront.cameraDistance = cameraDist
            binding.flipCardBack.cameraDistance = cameraDist
            
            try {
                // Do all the motion together
                forwardMotion.start()
                    
                // Update the number deck position
                viewModel.nextCardNumberDeck()
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