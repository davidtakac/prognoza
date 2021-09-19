package hr.dtakac.prognoza

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import hr.dtakac.prognoza.common.TEST_PLACE_ID
import hr.dtakac.prognoza.utils.toDayUiModel
import hr.dtakac.prognoza.dbmodel.ForecastTimeSpan
import hr.dtakac.prognoza.uimodel.MeasurementUnit
import hr.dtakac.prognoza.uimodel.cell.DayUiModel
import hr.dtakac.prognoza.uimodel.RepresentativeWeatherDescription
import hr.dtakac.prognoza.uimodel.WEATHER_ICONS
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
        val hours = getForecastTimeSpans_withNormalValues(start)
        // Act
        val actual = hours.toDayUiModel(this, MeasurementUnit.METRIC)
        // Assert
        val expected = getExpectedDayUiModel_forNormalValues(start)
        assertTrue { actual == expected }
    }

    @Test
    fun toDayUiModel_mapsNullValues() = coroutineScope.runBlockingTest {
        // Arrange
        val start = ZonedDateTime.now()
        val nullHours = getForecastTimeSpans_withAllNullValues(start)
        // Act
        val actual = nullHours.toDayUiModel(this, MeasurementUnit.METRIC)
        // Assert
        val expected = getExpectedDayUiModel_forAllNullValues(start)
        assertTrue { actual == expected }
    }

    @Test
    fun toDayUiModel_isResistantToNullValues() = coroutineScope.runBlockingTest {
        // Arrange
        val start = ZonedDateTime.now()
        val hours = getForecastTimeSpans_withNormalValues(start) +
                getForecastTimeSpans_withAllNullValues(start)
        // Act
        val actual = hours.toDayUiModel(this, MeasurementUnit.METRIC)
        // Assert
        val expected = getExpectedDayUiModel_forNormalValues(start)
        assertTrue { actual == expected }
    }

    private fun getForecastTimeSpans_withNormalValues(start: ZonedDateTime) = listOf(
        ForecastTimeSpan(
            startTime = start,
            placeId = TEST_PLACE_ID,
            instantTemperature = 18f,
            symbolCode = "clearsky_day",
            precipitationProbability = null,
            precipitationAmount = 0f,
            instantWindSpeed = 15.4f,
            instantWindFromDirection = 230f,
            instantRelativeHumidity = 90f,
            instantAirPressureAtSeaLevel = 1020f
        ),
        ForecastTimeSpan(
            startTime = start.plusHours(1L),
            placeId = TEST_PLACE_ID,
            instantTemperature = 23.56f,
            symbolCode = "clearsky_night",
            precipitationProbability = null,
            precipitationAmount = 7.87f,
            instantWindSpeed = 1.2f,
            instantWindFromDirection = 100f,
            instantRelativeHumidity = 10f,
            instantAirPressureAtSeaLevel = 1019f
        ),
        ForecastTimeSpan(
            startTime = start.plusHours(2L),
            placeId = TEST_PLACE_ID,
            instantTemperature = 33f,
            symbolCode = "clearsky_day",
            precipitationProbability = null,
            precipitationAmount = 6.3f,
            instantWindSpeed = 13.3f,
            instantWindFromDirection = 200f,
            instantRelativeHumidity = 50f,
            instantAirPressureAtSeaLevel = 1000f
        ),
    )

    private fun getForecastTimeSpans_withAllNullValues(time: ZonedDateTime): List<ForecastTimeSpan> {
        val nullHour = ForecastTimeSpan(
            startTime = time,
            placeId = TEST_PLACE_ID,
            instantTemperature = null,
            symbolCode = null,
            precipitationProbability = null,
            precipitationAmount = null,
            instantWindSpeed = null,
            instantWindFromDirection = null,
            instantRelativeHumidity = null,
            instantAirPressureAtSeaLevel = null
        )
        return listOf(nullHour, nullHour, nullHour)
    }

    private fun getExpectedDayUiModel_forNormalValues(start: ZonedDateTime): DayUiModel =
        DayUiModel(
            id = "$TEST_PLACE_ID-$start",
            time = start,
            representativeWeatherDescription = RepresentativeWeatherDescription(
                weatherDescription = WEATHER_ICONS["clearsky_day"]!!,
                isMostly = false
            ),
            lowTemperature = 18f,
            highTemperature = 33f,
            maxWindSpeed = 15.4f,
            windFromCompassDirection = R.string.direction_sw,
            totalPrecipitationAmount = 6.3f + 7.87f + 0f,
            maxHumidity = 90f,
            maxPressure = 1020f,
            isExpanded = false,
            displayDataInUnit = MeasurementUnit.METRIC
        )

    private fun getExpectedDayUiModel_forAllNullValues(start: ZonedDateTime) = DayUiModel(
        id = "$TEST_PLACE_ID-$start",
        time = start,
        representativeWeatherDescription = null,
        lowTemperature = null,
        highTemperature = null,
        maxWindSpeed = null,
        windFromCompassDirection = null,
        totalPrecipitationAmount = 0f,
        maxHumidity = null,
        maxPressure = null,
        isExpanded = false,
        displayDataInUnit = MeasurementUnit.METRIC
    )
}