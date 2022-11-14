import hr.dtakac.prognoza.shared.entity.*
import kotlinx.datetime.*
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ForecastTest {
    private val timeZone = TimeZone.UTC

    @Test
    fun `throws exception when input data empty`() {
        assertFailsWith<IllegalStateException> {
            Forecast(listOf(), timeZone)
        }
    }

    @Test
    fun `only current is non-null when input data is one data point`() {
        val forecast = Forecast(listOf(getHourDatum(getStartOfDay())), timeZone)
        assertNotNull(forecast.current)
        assertNull(forecast.today)
        assertNull(forecast.coming)
    }

    @Test
    fun `only coming is null when all data points on same day`() {
        val now = getStartOfDay()
        val data = (0L..23L).map { getHourDatum(now.plus(it, DateTimeUnit.HOUR)) }
        val forecast = Forecast(data, timeZone)
        assertNotNull(forecast.current)
        assertNotNull(forecast.today)
        assertNull(forecast.coming)
    }

    @Test
    fun `none are null when data points span multiple days`() {
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