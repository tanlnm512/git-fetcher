package io.tanlnm.gitfetcher.core.platform

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

abstract class MVIActivity<VM : MVIViewModel<*, *, *>, VB : ViewBinding> constructor(
    bindingInflater: (LayoutInflater) -> VB
) : BaseActivity<VB>(bindingInflater) {
    protected abstract val viewModel: VM

    abstract fun subscribeUI()
    open suspend fun handleState(state: IState) {}
    open suspend fun handleEffect(effect: IEffect) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeUI()
    }
}