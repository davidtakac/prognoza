package hr.dtakac.prognoza.fakes

import hr.dtakac.prognoza.core.coroutines.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher

class FakeDispatcherProvider(
    private val dispatcher: CoroutineDispatcher
) : DispatcherProvider {
    override val default: CoroutineDispatcher
        get() = dispatcher
    override val io: CoroutineDispatcher
        get() = dispatcher
    override val main: CoroutineDispatcher
        get() = dispatcher
}