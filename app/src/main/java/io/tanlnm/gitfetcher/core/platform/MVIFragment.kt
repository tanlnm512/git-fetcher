package io.tanlnm.gitfetcher.core.platform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

abstract class MVIFragment<VM : MVIViewModel<*, *, *>, VB : ViewBinding> constructor(
    bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : BaseFragment<VB>(bindingInflater) {
    protected abstract val viewModel: VM

    abstract fun subscribeUI()
    open suspend fun handleState(state: IState) {}
    open suspend fun handleEffect(effect: IEffect) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUI()
    }
}