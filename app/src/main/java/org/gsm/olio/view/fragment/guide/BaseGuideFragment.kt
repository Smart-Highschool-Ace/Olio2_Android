package org.gsm.olio.view.fragment.guide

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.gsm.olio.R
import org.gsm.olio.databinding.FragmentBaseGuideBinding
import org.gsm.olio.util.Constants.TAG


class BaseGuideFragment : Fragment() {

    private val binding by lazy {FragmentBaseGuideBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initViews()


        return binding.root
    }

    private fun initViews(){
        binding.fragment = this@BaseGuideFragment
        binding.lifecycleOwner = this@BaseGuideFragment
        binding.viewPager.adapter = GuidePageAdapter(this@BaseGuideFragment)
        binding.dotsIndicator.attachTo(binding.viewPager)
    }

    fun startBtn(){
        Log.d(TAG, "startBtn: ${findNavController().navigate(R.id.action_baseGuideFragment_to_loginFragment)}")
    }


}