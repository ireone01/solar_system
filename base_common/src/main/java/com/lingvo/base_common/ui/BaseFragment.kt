package com.lingvo.base_common.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    // Abstract property for layout resource ID
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    // Property to hold the binding
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using ViewBinding
        _binding = bindingInflater(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize UI components, observers, and listeners
        initConfig(view, savedInstanceState)
        initView(view, savedInstanceState)
        initObserver()
        initListener()
    }

    open fun initConfig(view: View, savedInstanceState: Bundle?) {}

    /**
     * Initialize and configure UI components.
     * Subclasses can override for specific setup.
     * @param view The root view of the fragment.
     */
    open fun initView(view: View, savedInstanceState: Bundle?) {}

    /**
     * Observe LiveData or other reactive components.
     * Subclasses can override to handle state changes.
     */
    open fun initObserver() {}

    /**
     * Set up click listeners or other interaction handlers.
     * Subclasses can override for specific interactions.
     */
    open fun initListener() {}

    /**
     * Clean up resources or stop tasks.
     * Subclasses can override to handle specific cleanup actions.
     */
    open fun release() {}

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up binding to prevent memory leaks
        _binding = null
        // Perform cleanup actions defined in the clearResources method
        release()
    }
}
