package com.example.countdown.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.countdown.CoroutineScopeRule
import com.example.countdown.repository.Repository
import com.example.countdown.room.entities.Countdown
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class CountdownViewModelTest {

    @get:Rule
    val coroutineScope = CoroutineScopeRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CountdownViewModel

    @Mock
    private lateinit var repository: Repository

    @Mock
    private lateinit var countdownResponseObserver: Observer<List<Countdown>>

    @Before
    fun setUp(){
        MockitoAnnotations.openMocks(this)
        viewModel = CountdownViewModel(repository, coroutineScope.dispatcherProvider)
    }



    @Test
    fun `Add item path`()  {

        val emptyList = arrayListOf<Countdown>()

        val expected = Countdown(1,"test", "25/5/2022", "18:00", "26/6/2022", "20:22", true  )


        coroutineScope.runBlockingTest {
            viewModel.insert(expected)
            viewModel.countdowns.observeForever(countdownResponseObserver)

            whenever(repository.allCountdowns).thenAnswer{
                Result.success(emptyList)
            }

            emptyList.size shouldBe 1
        }

    }


    @After
    fun destroy(){
        viewModel.countdowns.removeObserver(countdownResponseObserver)
    }
}