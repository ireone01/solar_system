package com.lingvo.base_common.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetFragment<VB : ViewBinding> : BottomSheetDialogFragment() {

    // Abstract property for binding to be initialized in subclasses
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    // Abstract method for initializing the binding
    abstract fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?): VB

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

    /**
     * Initializes configuration settings.
     * Subclasses can override this method for specific configuration setup.
     */
    open fun initConfig(view: View, savedInstanceState: Bundle?) {}

    /**
     * Initializes the UI components.
     * Subclasses can override this method for specific view setup.
     */
    open fun initView(view: View, savedInstanceState: Bundle?) {}

    /**
     * Initializes reactive observers.
     * Subclasses can override this method to set up state observers.
     */
    open fun initObserver() {}

    /**
     * Sets up listeners for user interactions.
     * Subclasses can override this method to handle specific interactions.
     */
    open fun initListener() {}

    /**
     * Cleans up resources or listeners.
     * Subclasses can override this method to define specific cleanup logic.
     */
    open fun release() {}

    override fun onDestroyView() {
        super.onDestroyView()
        // Perform cleanup actions
        release()
        _binding = null // Release binding reference to prevent memory leaks
    }
}
