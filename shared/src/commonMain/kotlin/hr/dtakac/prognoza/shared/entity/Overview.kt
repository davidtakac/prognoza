package hr.dtakac.prognoza.shared.entity

class Overview(private val forecast: Forecast) {
    val temperature: Temperature by lazy { forecast.hours[0].temperature }
    val minTemperature: Temperature by lazy { forecast.days[0].minimumTemperature }
    val maxTemperature: Temperature by lazy { forecast.days[0].maximumTemperature }
    val wmoCode: Int by lazy { forecast.hours[0].wmoCode }

}