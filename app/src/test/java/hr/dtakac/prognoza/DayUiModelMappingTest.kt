package hr.dtakac.prognoza

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import hr.dtakac.prognoza.common.TEST_PLACE
import hr.dtakac.prognoza.utils.toDayUiModel
import hr.dtakac.prognoza.data.database.forecast.ForecastTimeSpan
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
        val actual = hours.toDayUiModel(this, MeasurementUnit.METRIC, TEST_PLACE)
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
        val actual = nullHours.toDayUiModel(this, MeasurementUnit.METRIC, TEST_PLACE)
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
        val actual = hours.toDayUiModel(this, MeasurementUnit.METRIC, TEST_PLACE)
        // Assert
        val expected = getExpectedDayUiModel_forNormalValues(start)
        assertTrue { actual == expected }
    }

    private fun getForecastTimeSpans_withNormalValues(start: ZonedDateTime) = listOf(
        hr.dtakac.prognoza.data.database.forecast.ForecastTimeSpan(
            startTime = start,
            endTime = start.plusHours(1L),
            placeId = TEST_PLACE.id,
            instantTemperature = 18.0,
            symbolCode = "clearsky_night",
            precipitationProbability = null,
            precipitationAmount = 0.0,
            instantWindSpeed = 15.4,
            instantWindFromDirection = 230.0,
            instantRelativeHumidity = 90.0,
            instantAirPressureAtSeaLevel = 1020.0,
            airTemperatureMin = 18.0,
            airTemperatureMax = 18.0,
        ),
        hr.dtakac.prognoza.data.database.forecast.ForecastTimeSpan(
            startTime = start.plusHours(1L),
            endTime = start.plusHours(2L),
            placeId = TEST_PLACE.id,
            instantTemperature = 23.56,
            symbolCode = "clearsky_night",
            precipitationProbability = null,
            precipitationAmount = 7.87,
            instantWindSpeed = 1.2,
            instantWindFromDirection = 100.0,
            instantRelativeHumidity = 10.0,
            instantAirPressureAtSeaLevel = 1019.0,
            airTemperatureMin = 23.56,
            airTemperatureMax = 23.56,
        ),
        hr.dtakac.prognoza.data.database.forecast.ForecastTimeSpan(
            startTime = start.plusHours(2L),
            endTime = start.plusHours(3L),
            placeId = TEST_PLACE.id,
            instantTemperature = 33.0,
            symbolCode = "fair_night",
            precipitationProbability = null,
            precipitationAmount = 6.3,
            instantWindSpeed = 13.3,
            instantWindFromDirection = 200.0,
            instantRelativeHumidity = 50.0,
            instantAirPressureAtSeaLevel = 1000.0,
            airTemperatureMin = 33.0,
            airTemperatureMax = 33.0,
        ),
    )

    private fun getForecastTimeSpans_withAllNullValues(time: ZonedDateTime): List<hr.dtakac.prognoza.data.database.forecast.ForecastTimeSpan> {
        val nullHour = hr.dtakac.prognoza.data.database.forecast.ForecastTimeSpan(
            startTime = time,
            endTime = null,
            placeId = TEST_PLACE.id,
            instantTemperature = null,
            symbolCode = null,
            precipitationProbability = null,
            precipitationAmount = null,
            instantWindSpeed = null,
            instantWindFromDirection = null,
            instantRelativeHumidity = null,
            instantAirPressureAtSeaLevel = null,
            airTemperatureMin = null,
            airTemperatureMax = null
        )
        return listOf(nullHour, nullHour, nullHour)
    }

    private fun getExpectedDayUiModel_forNormalValues(start: ZonedDateTime): DayUiModel =
        DayUiModel(
            id = "${TEST_PLACE.id}-$start",
            time = start,
            representativeWeatherDescription = RepresentativeWeatherDescription(
                weatherDescription = WEATHER_ICONS["clearsky_night"]!!,
                isMostly = true
            ),
            lowTemperature = 18.0,
            highTemperature = 33.0,
            maxWindSpeed = 15.4,
            windFromCompassDirection = R.string.direction_sw,
            totalPrecipitationAmount = 6.3 + 7.87 + 0,
            maxHumidity = 90.0,
            maxPressure = 1020.0,
            isExpanded = false,
            displayDataInUnit = MeasurementUnit.METRIC
        )

    private fun getExpectedDayUiModel_forAllNullValues(start: ZonedDateTime) = DayUiModel(
        id = "${TEST_PLACE.id}-$start",
        time = start,
        representativeWeatherDescription = null,
        lowTemperature = null,
        highTemperature = null,
        maxWindSpeed = null,
        windFromCompassDirection = null,
        totalPrecipitationAmount = 0.0,
        maxHumidity = null,
        maxPressure = null,
        isExpanded = false,
        displayDataInUnit = MeasurementUnit.METRIC
    )
}