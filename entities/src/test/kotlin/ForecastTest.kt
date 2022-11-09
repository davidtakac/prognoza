import hr.dtakac.prognoza.entities.forecast.Description
import hr.dtakac.prognoza.entities.forecast.Forecast
import hr.dtakac.prognoza.entities.forecast.ForecastDatum
import hr.dtakac.prognoza.entities.forecast.Wind
import hr.dtakac.prognoza.entities.forecast.units.*
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ForecastTest {
    @BeforeTest
    fun `set time zone to UTC`() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    @Test
    fun `when input data empty, throws exception`() {
        assertFailsWith<IllegalStateException> {
            Forecast(listOf())
        }
    }

    @Test
    fun `when input data is only one data point, only current is non-null`() {
        val forecast = Forecast(listOf(getHourDatum(getStartOfDay())))
        assertNotNull(forecast.current)
        assertNull(forecast.today)
        assertNull(forecast.coming)
    }

    @Test
    fun `when all data points on same day, only coming is null`() {
        val now = getStartOfDay()
        val data = (0L..23L).map { getHourDatum(now.plusHours(it)) }
        val forecast = Forecast(data)
        assertNotNull(forecast.current)
        assertNotNull(forecast.today)
        assertNull(forecast.coming)
    }

    @Test
    fun `when data points span multiple days, none are null`() {
        val now = getStartOfDay()
        val data = (0L..23L).map { getHourDatum(now.plusHours(it)) } +
                (0L..23L).map { getHourDatum(now.plusDays(1L).plusHours(it)) } +
                (0L..23L).map { getHourDatum(now.plusDays(2L).plusHours(it)) }
        val forecast = Forecast(data)
        assertNotNull(forecast.current)
        assertNotNull(forecast.today)
        assertNotNull(forecast.coming)
    }

    private fun getHourDatum(start: ZonedDateTime): ForecastDatum = ForecastDatum(
        start = start,
        end = start.plusHours(1L),
        temperature = Temperature(0.0, TemperatureUnit.DEGREE_CELSIUS),
        precipitation = Length(0.0, LengthUnit.MILLIMETER),
        wind = Wind(Speed(0.0, SpeedUnit.KILOMETER_PER_HOUR), Angle(45.0, AngleUnit.DEGREE)),
        airPressure = Pressure(0.25, PressureUnit.MILLIBAR),
        description = Description.CLEAR_SKY_DAY,
        humidity = Percentage(0.5, PercentageUnit.FRACTION)
    )

    private fun getStartOfDay() = Instant
        .now()
        .atZone(ZoneId.systemDefault())
        .truncatedTo(ChronoUnit.DAYS)
}