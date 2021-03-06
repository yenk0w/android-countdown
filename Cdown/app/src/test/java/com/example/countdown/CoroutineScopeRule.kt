package com.example.countdown

import com.example.countdown.utils.CoroutineDispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class CoroutineScopeRule(
    private val dispatcher: TestCoroutineDispatcher =
        TestCoroutineDispatcher(),
    var dispatcherProvider: CoroutineDispatcherProvider =
        CoroutineDispatcherProvider()
): TestWatcher(), TestCoroutineScope by TestCoroutineScope(dispatcher) {


    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
        dispatcherProvider = CoroutineDispatcherProvider(
            main = dispatcher,
            default = dispatcher,
            io = dispatcher
        )
    }

    override fun finished(description: Description?) {
        super.finished(description)
        cleanupTestCoroutines()
        Dispatchers.resetMain()
    }
}

