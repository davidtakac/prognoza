package hr.dtakac.prognoza.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

abstract class CoroutineScopeViewModel(
    coroutineScope: CoroutineScope?
) : ViewModel() {
    protected val coroutineScope = coroutineScope ?: viewModelScope

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }
}