package hr.dtakac.prognoza

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import hr.dtakac.prognoza.fakes.FakeDispatcherProvider
import hr.dtakac.prognoza.fakes.FakeForecastRepository
import hr.dtakac.prognoza.fakes.FakeForecastService
import hr.dtakac.prognoza.fakes.FakePreferencesRepository
import hr.dtakac.prognoza.forecast.viewmodel.TodayFragmentViewModel
import hr.dtakac.prognoza.repository.forecast.CachedSuccess
import hr.dtakac.prognoza.repository.forecast.Empty
import hr.dtakac.prognoza.repository.forecast.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class TodayFragmentViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val coroutineDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = TestCoroutineScope(coroutineDispatcher)
    private val repository = FakeForecastRepository()
    private val preferencesRepository = FakePreferencesRepository()
    private val dispatcherProvider = FakeDispatcherProvider(coroutineDispatcher)

    // class under test
    private lateinit var viewModel: TodayFragmentViewModel

    @Before
    fun setup() {
        viewModel = TodayFragmentViewModel(
            coroutineScope,
            repository,
            dispatcherProvider,
            preferencesRepository
        )
    }

    @After
    fun teardown() {
        coroutineScope.cleanupTestCoroutines()
    }

    @Test
    fun getForecast_showsForecastForToday() {
        // Arrange
        assertTrue {
            viewModel.forecast.value == null
        }
        repository.typeOfResultToReturn = Success::class.java
        // Act
        viewModel.getForecast()
        // Assert
        val today = FakeForecastService.startOfData
        val hours = viewModel.forecast.value?.otherHours
        val firstHour = hours?.getOrNull(0)
        val lastHour = hours?.getOrNull(hours.lastIndex)
        assertTrue("Is the first hour equal to today") {
            firstHour?.time == today
        }
        assertTrue("Is the last hour in tomorrow") {
            lastHour?.time?.minusDays(1)?.toLocalDate() == today.toLocalDate()
        }
        assertTrue("Is the last hour set to 6AM") {
            lastHour?.time?.hour == 6
        }
    }

    @Test
    fun getForecast_showsEmptyScreen() {
        // Arrange
        assertTrue {
            viewModel.emptyScreen.value == null
        }
        repository.typeOfResultToReturn = Empty::class.java
        // Act
        viewModel.getForecast()
        // Assert
        assertTrue("Is empty screen shown") {
            viewModel.emptyScreen.value != null
        }
    }

    @Test
    fun getForecast_showsCachedResultsNotification() {
        // Arrange
        assertTrue {
            viewModel.cachedResultsMessage.value == null
        }
        assertTrue {
            viewModel.forecast.value == null
        }
        repository.typeOfResultToReturn = CachedSuccess::class.java
        // Act
        viewModel.getForecast()
        // Assert
        assertTrue("Is forecast shown") {
            viewModel.forecast.value != null
        }
        assertTrue("Is cached results notification shown") {
            viewModel.cachedResultsMessage.value != null
        }
    }
}