package com.wavez.trackerwater.feature.insights

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.lingvo.base_common.ui.BaseActivity
import com.wavez.trackerwater.R
import com.wavez.trackerwater.databinding.FragmentInsightDetailBinding
import com.wavez.trackerwater.extension.getSerializable
import com.wavez.trackerwater.feature.insights.adapter.InsightDetailPagerAdapter
import com.wavez.trackerwater.feature.insights.model.InsightsParts

class InsightDetailActivity : BaseActivity<FragmentInsightDetailBinding>() {
    override fun createBinding(): FragmentInsightDetailBinding {
        return FragmentInsightDetailBinding.inflate(layoutInflater)
    }

    companion object {
        private const val ARG_PARTS = "ARG_PARTS"

        private const val ARG_COLOR = "ARG_COLOR"

        private const val ARG_COLOR_TEXT = "ARG_COLOR_TEXT"

        fun newIntent(
            parts: List<InsightsParts>, colorBackground: Int, colorText: Int, context: Context
        ) = Intent(context, InsightDetailActivity::class.java).apply {
            putExtra(ARG_PARTS, ArrayList(parts))
            putExtra(ARG_COLOR, colorBackground)
            putExtra(ARG_COLOR_TEXT, colorText)
        }
    }

    private var parts = arrayListOf<InsightsParts>()

    private var color = R.color.white

    private var colorText = R.color.black


    @SuppressLint("ResourceType")
    override fun initConfig(savedInstanceState: Bundle?) {
        super.initConfig(savedInstanceState)
        parts = intent?.getSerializable(ARG_PARTS) as? ArrayList<InsightsParts> ?: arrayListOf()
        color = intent?.getIntExtra(ARG_COLOR, R.color.white) ?: R.color.white
        colorText = intent?.getIntExtra(ARG_COLOR_TEXT, R.color.black) ?: R.color.black

        binding.root.setBackgroundColor(ContextCompat.getColor(this, color))
        setStatusBarColor(color, true)

        initAdapter()
    }

    private fun initAdapter() {
        val adapter = InsightDetailPagerAdapter(colorText)
        binding.vPagerInsight.adapter = adapter
        binding.vPagerInsight.setPageTransformer(FadePageTransformer())
        adapter.setData(parts)
    }


    override fun initListener() {
        super.initListener()
        binding.btnBack.setOnClickListener {
            finish()
        }

    }
}