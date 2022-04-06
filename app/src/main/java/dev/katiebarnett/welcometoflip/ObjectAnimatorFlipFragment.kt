package dev.katiebarnett.welcometoflip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.katiebarnett.welcometoflip.databinding.ObjectAnimatorFlipFragmentBinding

@AndroidEntryPoint
class ObjectAnimatorFlipFragment : Fragment(R.layout.object_animator_flip_fragment) {

    private val viewModel: DeckViewModel by viewModels()
    
    private lateinit var binding: ObjectAnimatorFlipFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ObjectAnimatorFlipFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }
}