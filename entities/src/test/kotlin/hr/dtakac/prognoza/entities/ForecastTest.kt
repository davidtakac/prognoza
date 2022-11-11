package hr.dtakac.prognoza.entities

import hr.dtakac.prognoza.entities.forecast.Description
import hr.dtakac.prognoza.entities.forecast.Forecast
import hr.dtakac.prognoza.entities.forecast.ForecastDatum
import hr.dtakac.prognoza.entities.forecast.Wind
import hr.dtakac.prognoza.entities.forecast.units.*
import kotlinx.datetime.*
import org.testng.annotations.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ForecastTest {
    private val timeZone = TimeZone.UTC

    @Test
    fun `when input data empty, throws exception`() {
        assertFailsWith<IllegalStateException> {
            Forecast(listOf(), timeZone)
        }
    }

    @Test
    fun `when input data is only one data point, only current is non-null`() {
        val forecast = Forecast(listOf(getHourDatum(getStartOfDay())), timeZone)
        assertNotNull(forecast.current)
        assertNull(forecast.today)
        assertNull(forecast.coming)
    }

    @Test
    fun `when all data points on same day, only coming is null`() {
        val now = getStartOfDay()
        val data = (0L..23L).map { getHourDatum(now.plus(it, DateTimeUnit.HOUR)) }
        val forecast = Forecast(data, timeZone)
        assertNotNull(forecast.current)
        assertNotNull(forecast.today)
        assertNull(forecast.coming)
    }

    @Test
    fun `when data points span multiple days, none are null`() {
        val now = getStartOfDay()
        val data = (0L..23L).map { getHourDatum(now.plus(it, DateTimeUnit.HOUR)) } +
                (0L..23L).map { getHourDatum(now.plus(1, DateTimeUnit.DAY, timeZone).plus(it, DateTimeUnit.HOUR)) } +
                (0L..23L).map { getHourDatum(now.plus(2, DateTimeUnit.DAY, timeZone).plus(it, DateTimeUnit.HOUR)) }
        val forecast = Forecast(data, timeZone)
        assertNotNull(forecast.current)
        assertNotNull(forecast.today)
        assertNotNull(forecast.coming)
    }

    private fun getHourDatum(start: Instant): ForecastDatum = ForecastDatum(
        startEpochMillis = start.toEpochMilliseconds(),
        endEpochMillis = start.plus(1, DateTimeUnit.HOUR).toEpochMilliseconds(),
        temperature = Temperature(0.0, TemperatureUnit.DEGREE_CELSIUS),
        precipitation = Length(0.0, LengthUnit.MILLIMETRE),
        wind = Wind(Speed(0.0, SpeedUnit.KILOMETRE_PER_HOUR), Angle(45.0, AngleUnit.DEGREE)),
        airPressure = Pressure(0.25, PressureUnit.MILLIBAR),
        description = Description.CLEAR_SKY_DAY,
        humidity = Percentage(0.5, PercentageUnit.FRACTION)
    )

    private fun getStartOfDay() = LocalDateTime(
        hour = 0,
        minute = 0,
        year = 1997,
        monthNumber = 1,
        dayOfMonth = 1,
        second = 0,
        nanosecond = 0
    ).toInstant(timeZone)
}