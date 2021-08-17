package hr.dtakac.prognoza

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import hr.dtakac.prognoza.common.TEST_PLACE_ID
import hr.dtakac.prognoza.common.util.toDayUiModel
import hr.dtakac.prognoza.database.entity.ForecastHour
import hr.dtakac.prognoza.forecast.uimodel.DayUiModel
import hr.dtakac.prognoza.forecast.uimodel.RepresentativeWeatherIcon
import hr.dtakac.prognoza.forecast.uimodel.WEATHER_ICONS
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import java.time.ZonedDateTime
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class DayUiModelMappingTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val coroutineDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = TestCoroutineScope(coroutineDispatcher)

    @Test
    fun toDayUiModel_mapsNormalValues() = coroutineScope.runBlockingTest {
        // Arrange
        val start = ZonedDateTime.now()
        val hours = getForecastHours_withNormalValues(start)
        // Act
        val actual = hours.toDayUiModel(this)
        // Assert
        val expected = getExpectedDayUiModel_forNormalValues(start)
        assertTrue {
            actual == expected
        }
    }

    @Test
    fun toDayUiModel_mapsAllNullValues() = coroutineScope.runBlockingTest {
        // Arrange
        val start = ZonedDateTime.now()
        val nullHours = getForecastHours_withAllNullValues(start)
        // Act
        val actual = nullHours.toDayUiModel(this)
        // Assert
        val expected = getExpectedDayUiModel_forAllNullValues(start)
        assertTrue {
            actual == expected
        }
    }

    @Test
    fun toDayUiModel_isResistantToNullValues() = coroutineScope.runBlockingTest {
        // Arrange
        val start = ZonedDateTime.now()
        val hours = getForecastHours_withNormalValues(start) + getForecastHours_withAllNullValues(start)
        // Act
        val actual = hours.toDayUiModel(this)
        // Assert
        val expected = getExpectedDayUiModel_forNormalValues(start)
        assertTrue {
            actual == expected
        }
    }

    private fun getForecastHours_withNormalValues(start: ZonedDateTime) = listOf(
        ForecastHour(
            time = start,
            placeId = TEST_PLACE_ID,
            temperature = 18f,
            symbolCode = "clearsky_day",
            precipitationProbability = null,
            precipitationAmount = 0f,
            windSpeed = 15.4f,
            windFromDirection = 230f,
            relativeHumidity = 90f,
            pressure = 1020f
        ),
        ForecastHour(
            time = start.plusHours(1L),
            placeId = TEST_PLACE_ID,
            temperature = 23.56f,
            symbolCode = "clearsky_night",
            precipitationProbability = null,
            precipitationAmount = 7.87f,
            windSpeed = 1.2f,
            windFromDirection = 100f,
            relativeHumidity = 10f,
            pressure = 1019f
        ),
        ForecastHour(
            time = start.plusHours(2L),
            placeId = TEST_PLACE_ID,
            temperature = 33f,
            symbolCode = "clearsky_day",
            precipitationProbability = null,
            precipitationAmount = 6.3f,
            windSpeed = 13.3f,
            windFromDirection = 200f,
            relativeHumidity = 50f,
            pressure = 1000f
        ),
    )

    private fun getForecastHours_withAllNullValues(time: ZonedDateTime): List<ForecastHour> {
        val nullHour = ForecastHour(
            time = time,
            placeId = TEST_PLACE_ID,
            temperature = null,
            symbolCode = null,
            precipitationProbability = null,
            precipitationAmount = null,
            windSpeed = null,
            windFromDirection = null,
            relativeHumidity = null,
            pressure = null
        )
        return listOf(nullHour, nullHour, nullHour)
    }

    private fun getExpectedDayUiModel_forNormalValues(start: ZonedDateTime): DayUiModel =
        DayUiModel(
            id = "$TEST_PLACE_ID-$start",
            time = start,
            representativeWeatherIcon = RepresentativeWeatherIcon(
                weatherIcon = WEATHER_ICONS["clearsky_day"]!!,
                isMostly = false
            ),
            lowTemperature = 18f,
            highTemperature = 33f,
            maxWindSpeed = 15.4f,
            windFromCompassDirection = R.string.direction_sw,
            totalPrecipitationAmount = 6.3f + 7.87f + 0f,
            maxHumidity = 90f,
            maxPressure = 1020f,
            isExpanded = false
        )

    private fun getExpectedDayUiModel_forAllNullValues(start: ZonedDateTime) = DayUiModel(
        id = "$TEST_PLACE_ID-$start",
        time = start,
        representativeWeatherIcon = null,
        lowTemperature = null,
        highTemperature = null,
        maxWindSpeed = null,
        windFromCompassDirection = null,
        totalPrecipitationAmount = 0f,
        maxHumidity = null,
        maxPressure = null,
        isExpanded = false
    )
}