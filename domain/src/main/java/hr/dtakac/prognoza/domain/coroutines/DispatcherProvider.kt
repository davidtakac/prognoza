package hr.dtakac.prognoza.domain.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatcherProvider {
    val io: CoroutineDispatcher
    val compute: CoroutineDispatcher
    val main: CoroutineDispatcher
}