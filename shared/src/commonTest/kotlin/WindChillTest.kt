import hr.dtakac.prognoza.shared.entity.*
import kotlin.test.Test
import kotlin.test.assertEquals

class WindChillTest {
    private val tolerance = 0.0001

    @Test // By chart from here https://www.weather.gov/media/lsx/wcm/Winter2008/Wind_Chill.pdf
    fun `calculates wind chill when wind and temperature are per NWS chart`() {
        val windSpeed = Speed(20.0, SpeedUnit.MILE_PER_HOUR)
        val temperaturesFahrenheit = (-45..40 step 5).map {
            Temperature(it.toDouble(), TemperatureUnit.DEGREE_FAHRENHEIT)
        }

        val expectedWindChillTempsFahrenheit = listOf(
            -81.0, -74.0, -68.0, -61.0, -55.0, -48.0, -42.0,
            - 35.0, - 29.0, -22.0, -15.0, -9.0, -2.0, 4.0,
            11.0, 17.0, 24.0, 31.0
        )
        val actualWindChillTempsFahrenheit = temperaturesFahrenheit.map {
            calculateWindChill(it, windSpeed).fahrenheit
        }

        for (i in expectedWindChillTempsFahrenheit.indices) {
            assertEquals(
                expected = expectedWindChillTempsFahrenheit[i],
                actual = actualWindChillTempsFahrenheit[i],
                // The chart displays rounded temperatures
                absoluteTolerance = 1.0
            )
        }
    }

    @Test
    fun `same as air temperature when wind speed 60mph and air temperature 80F`() {
        val windSpeed = Speed(60.0, SpeedUnit.MILE_PER_HOUR)
        val temperature = Temperature(80.0, TemperatureUnit.DEGREE_FAHRENHEIT)
        assertEquals(
            expected = temperature.fahrenheit,
            actual = calculateWindChill(temperature, windSpeed).fahrenheit,
            absoluteTolerance = tolerance
        )
    }

    @Test
    fun `same as air temperature when wind speed 2mph and air temperature 10F`() {
        val windSpeed = Speed(2.0, SpeedUnit.MILE_PER_HOUR)
        val temperature = Temperature(10.0, TemperatureUnit.DEGREE_FAHRENHEIT)
        assertEquals(
            expected = temperature.fahrenheit,
            actual = calculateWindChill(temperature, windSpeed).fahrenheit,
            absoluteTolerance = tolerance
        )
    }

    @Test
    fun `same as air temperature when wind speed 2mph and air temperature 85F`() {
        val windSpeed = Speed(2.0, SpeedUnit.MILE_PER_HOUR)
        val temperature = Temperature(85.0, TemperatureUnit.DEGREE_FAHRENHEIT)
        assertEquals(
            expected = temperature.fahrenheit,
            actual = calculateWindChill(temperature, windSpeed).fahrenheit,
            absoluteTolerance = tolerance
        )
    }
}