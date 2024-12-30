package com.lingvo.base_common.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding

abstract class BaseDialogFragment<VB : ViewBinding> : DialogFragment() {

    // Abstract property for ViewBinding
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    // Abstract method for initializing the binding
    abstract fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    // Flag to control whether the dialog should be full-screen
    open val enableFullScreenMode: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize binding and return the root view
        _binding = initializeBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI components, observers, and listeners
        initConfig(view, savedInstanceState)
        initView(view, savedInstanceState)
        initObserver()
        initListener()
    }

    override fun onStart() {
        super.onStart()

        // Adjust the dialog's window size if full-screen is enabled
        if (enableFullScreenMode) {
            dialog?.window?.apply {
                setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    /**
     * Initializes configuration settings.
     * Subclasses can override this method for specific configuration setup.
     */
    open fun initConfig(view: View, savedInstanceState: Bundle?) {}

    /**
     * Initialize and configure UI components.
     * Subclasses can override this method for specific setup.
     */
    open fun initView(view: View, savedInstanceState: Bundle?) {}

    /**
     * Observe LiveData or other reactive components.
     * Subclasses can override this method to handle state changes.
     */
    open fun initObserver() {}

    /**
     * Set up click listeners or other interaction handlers.
     * Subclasses can override this method for specific interactions.
     */
    open fun initListener() {}

    /**
     * Handle cleanup of resources or listeners.
     * Subclasses can override this method for specific cleanup actions.
     */
    open fun releaseResources() {}

    override fun onDestroyView() {
        super.onDestroyView()
        // Perform cleanup actions
        releaseResources()
        _binding = null // Release binding reference to prevent memory leaks
    }

    override fun show(fragmentManager: FragmentManager, tag: String?) {
        if (fragmentManager.findFragmentByTag(tag) != null) return
        super.show(fragmentManager, tag)
    }
}
