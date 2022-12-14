package com.youssef.weather.framework.presentation.features.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.youssef.weather.R

abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!

    abstract fun bindViews()

    @LayoutRes
    abstract fun getLayoutResId(): Int

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        bindViews()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)
        return binding.root
    }

    protected fun handleError(throwable: Throwable) {
        handleError(getErrorMessage(throwable))
    }

    protected fun handleError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
            .show()
    }

    protected fun getErrorMessage(throwable: Throwable): String =
        throwable.message ?: getInternetConnectionErrorMessage()

    private fun getInternetConnectionErrorMessage() =
        requireContext().getString(R.string.please_check_your_internet_connection)


    protected fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
            .show()
    }

    protected fun showMessage(@StringRes messageId: Int) {
        showMessage(getString(messageId))
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}