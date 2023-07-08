import hr.dtakac.prognoza.shared.entity.*
import kotlinx.datetime.*
import kotlin.test.*

class ForecastTest {
    private val timeZone = TimeZone.UTC

    @Test
    fun `throws exception when input data empty`() {
        assertFailsWith<IllegalStateException> {
            Forecast(listOf(), timeZone)
        }
    }

    @Test
    fun `when data spans 0-23hr forecast has today and coming`() {
        val start = getStartOfDay()
        val data = (0..23).map { getHourDatum(start.plus(it, DateTimeUnit.HOUR)) }
        val forecast = Forecast(data, timeZone)
        assertNotNull(forecast.current)
        assertEquals(
            expected = Forecast.dayStartHour - 1,
            actual = (forecast.today?.hourly?.size ?: 0)
        )
        assertEquals(
            expected = 1,
            actual = forecast.coming?.size ?: 0
        )
    }

    @Test
    fun `when data is 0-4hr forecast has only today`() {
        val start = getStartOfDay()
        val data = (0 until Forecast.dayStartHour).map { getHourDatum(start.plus(it, DateTimeUnit.HOUR)) }
        val forecast = Forecast(data, timeZone)
        assertNotNull(forecast.current)
        assertEquals(
            expected = Forecast.dayStartHour - 1,
            actual = (forecast.today?.hourly?.size ?: 0)
        )
        assertNull(forecast.coming)
    }

    @Test
    fun `when data is a single hour forecast has only current`() {
        val forecast = Forecast(listOf(getHourDatum(getStartOfDay())), timeZone)
        assertNotNull(forecast.current)
        assertNull(forecast.today)
        assertNull(forecast.coming)
    }

    private fun getHourDatum(start: Instant): Hour = Hour(
        startUnixSecond = start.toEpochMilliseconds(),
        endEpochMillis = start.plus(1, DateTimeUnit.HOUR).toEpochMilliseconds(),
        temperature = Temperature(0.0, TemperatureUnit.DegreeCelsius),
        precipitation = Length(0.0, LengthUnit.Millimetre),
        wind = Wind(Speed(0.0, SpeedUnit.KilometrePerHour), Angle(45.0, AngleUnit.Degree)),
        pressureAtSeaLevel = Pressure(0.25, PressureUnit.Millibar),
        description = Description.CLEAR_SKY_DAY,
        relativeHumidity = Percentage(0.5, PercentageUnit.Fraction)
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