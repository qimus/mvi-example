package ru.mvi.core.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

typealias LayoutInflateMethod = (LayoutInflater, ViewGroup?, Boolean) -> ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    private var _binding: ViewBinding? = null

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB?
        get() = _binding as VB?

    abstract val layoutInflater: LayoutInflateMethod

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = layoutInflater(inflater, container, false)
        this._binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configure(binding!!)
    }

    protected open fun configure(binding: VB) {}

    override fun onDestroyView() {
        this._binding = null
        super.onDestroyView()
    }
}
