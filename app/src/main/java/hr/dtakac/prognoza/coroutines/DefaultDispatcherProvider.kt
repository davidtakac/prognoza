package hr.dtakac.prognoza.coroutines

import hr.dtakac.prognoza.domain.coroutines.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultDispatcherProvider : DispatcherProvider {
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO
    override val compute: CoroutineDispatcher
        get() = Dispatchers.Default
    override val main: CoroutineDispatcher
        get() = Dispatchers.Main
}