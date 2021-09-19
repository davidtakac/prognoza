package hr.dtakac.prognoza

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import hr.dtakac.prognoza.fakes.*
import hr.dtakac.prognoza.repomodel.CachedSuccess
import hr.dtakac.prognoza.repomodel.Empty
import hr.dtakac.prognoza.repomodel.Success
import hr.dtakac.prognoza.viewmodel.TodayFragmentViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.ZoneId
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class TodayForecastFragmentViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val coroutineDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = TestCoroutineScope(coroutineDispatcher)
    private val forecastRepository = FakeForecastRepository()
    private val preferencesRepository = FakePreferencesRepository()
    private val dispatcherProvider = FakeDispatcherProvider(coroutineDispatcher)
    private val placeRepository = FakePlaceRepository()
    private val timeProvider = FakeForecastTimeProvider()

    // class under test
    private lateinit var viewModel: TodayFragmentViewModel

    @Before
    fun setup() {
        viewModel = TodayFragmentViewModel(
            coroutineScope,
            preferencesRepository,
            placeRepository,
            forecastRepository,
            timeProvider,
            dispatcherProvider
        )
    }

    @After
    fun teardown() {
        coroutineScope.cleanupTestCoroutines()
    }

    @Test
    fun getForecast_whenSuccess_showsForecast() {
        // Arrange
        assertTrue {
            viewModel.forecast.value == null
        }
        forecastRepository.typeOfResultToReturn = Success::class.java
        // Act
        viewModel.getForecast()
        // Assert
        val hours = viewModel.forecast.value?.otherHours
        val firstHour = hours?.getOrNull(0)
        val lastHour = hours?.getOrNull(hours.lastIndex)
        assertTrue("First hour is start of today") {
            firstHour?.time == timeProvider.todayStart
        }
        assertTrue("Last hour is tomorrow") {
            lastHour?.time?.dayOfYear == timeProvider.tomorrowStart.dayOfYear
        }
        assertTrue("Last hour is at 6AM") {
            lastHour?.time?.withZoneSameInstant(ZoneId.systemDefault())?.hour == 6
        }
    }

    @Test
    fun getForecast_whenEmpty_showsEmptyScreen() {
        // Arrange
        assertTrue {
            viewModel.emptyScreen.value == null
        }
        forecastRepository.typeOfResultToReturn = Empty::class.java
        // Act
        viewModel.getForecast()
        // Assert
        assertTrue("Is forecast not shown") {
            viewModel.forecast.value == null
        }
        assertTrue("Is empty screen shown") {
            viewModel.emptyScreen.value != null
        }
    }

    @Test
    fun getForecast_whenCached_showsMessageAndForecast() {
        // Arrange
        assertTrue {
            viewModel.outdatedForecastMessage.value == null
        }
        assertTrue {
            viewModel.forecast.value == null
        }
        forecastRepository.typeOfResultToReturn = CachedSuccess::class.java
        // Act
        viewModel.getForecast()
        // Assert
        assertTrue("Is forecast shown") {
            viewModel.forecast.value != null
        }
        assertTrue("Is cached results notification shown") {
            viewModel.outdatedForecastMessage.value != null
        }
    }
}