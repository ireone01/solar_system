package com.wavez.trackerwater.feature.onboarding.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.R
import com.wavez.trackerwater.databinding.FragmentOnboading7Binding

class FragmentOnboarding7 : BaseFragment<FragmentOnboading7Binding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnboading7Binding
        get() = FragmentOnboading7Binding::inflate


    override fun initListener() {
        super.initListener()

        var btnClicked: LinearLayout? = null
        var btnClickedText : TextView?  = null
        binding.llayoutBtn1.setOnClickListener {
            if(btnClicked != binding.llayoutBtn1) {
                checkClicked(btnClicked, binding.llayoutBtn1, btnClickedText)
                binding.tvLlayoutBtn1.setTextColor(resources.getColor(R.color.primary_50))
                btnClicked = binding.llayoutBtn1
                btnClickedText = binding.tvLlayoutBtn1
            }
        }
        binding.llayoutBtn2.setOnClickListener {
            if (btnClicked != binding.llayoutBtn2) {
                checkClicked(btnClicked, binding.llayoutBtn2, btnClickedText)
                binding.tvLlayoutBtn2.setTextColor(resources.getColor(R.color.primary_50))
                btnClicked = binding.llayoutBtn2
                btnClickedText = binding.tvLlayoutBtn2
            }
        }
        binding.llayoutBtn3.setOnClickListener {
            if(btnClicked != binding.llayoutBtn3) {
                checkClicked(btnClicked, binding.llayoutBtn3, btnClickedText)
                binding.tvLlayoutBtn3.setTextColor(resources.getColor(R.color.primary_50))
                btnClicked = binding.llayoutBtn3
                btnClickedText = binding.tvLlayoutBtn3
            }
        }

    }

    private fun checkClicked(btnOld: LinearLayout? = null, btnNew: LinearLayout , btnclickedText : TextView? = null ) {
        btnNew.isSelected = true
        btnOld?.isSelected = false
        btnclickedText?.setTextColor(resources.getColor(R.color.blue_end))
    }

}