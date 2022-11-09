package hr.dtakac.prognoza.entities

import hr.dtakac.prognoza.entities.forecast.calculateWindChill
import hr.dtakac.prognoza.entities.forecast.units.Speed
import hr.dtakac.prognoza.entities.forecast.units.SpeedUnit
import hr.dtakac.prognoza.entities.forecast.units.Temperature
import hr.dtakac.prognoza.entities.forecast.units.TemperatureUnit
import org.testng.annotations.Test
import kotlin.test.assertEquals

class WindChillTest {
    @Test // By chart from here https://www.weather.gov/media/lsx/wcm/Winter2008/Wind_Chill.pdf
    fun `when wind and temperature are per NWS chart, wind chill is correct`() {
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
}