package hr.dtakac.prognoza.data.database.forecast

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters
import hr.dtakac.prognoza.data.database.converter.*
import hr.dtakac.prognoza.entities.forecast.ForecastDatum
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import hr.dtakac.prognoza.entities.forecast.units.*
import hr.dtakac.prognoza.entities.forecast.wind.Wind
import java.time.ZonedDateTime

@Entity(
    tableName = "forecast",
    primaryKeys = ["start_time", "end_time", "latitude", "longitude"]
)
@TypeConverters(
    IsoLocalDateTimeConverter::class,
    TemperatureConverter::class,
    LengthConverter::class,
    SpeedConverter::class,
    AngleConverter::class,
    PercentageConverter::class,
    PressureConverter::class
)
data class ForecastDbModel(
    @ColumnInfo(name = "start_time")
    val startTime: ZonedDateTime,
    @ColumnInfo(name = "end_time")
    val endTime: ZonedDateTime,
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double,
    @ColumnInfo(name = "temperature")
    val temperature: Temperature,
    @ColumnInfo(name = "forecast_description")
    val forecastDescription: ForecastDescription,
    @ColumnInfo(name = "precipitation")
    val precipitation: Length,
    @ColumnInfo(name = "wind")
    val wind: Speed,
    @ColumnInfo(name = "wind_from_direction")
    val windFromDirection: Angle,
    @ColumnInfo(name = "humidity")
    val humidity: Percentage,
    @ColumnInfo(name = "air_pressure_at_sea_level")
    val airPressureAtSeaLevel: Pressure
)

fun ForecastDbModel.toEntity(): ForecastDatum = ForecastDatum(
    start = startTime,
    end = endTime,
    temperature = temperature,
    precipitation = precipitation,
    wind = Wind(speed = wind, fromDirection = windFromDirection),
    airPressure = airPressureAtSeaLevel,
    description = forecastDescription,
    humidity = humidity
)

fun ForecastDatum.toDbModel(
    latitude: Double,
    longitude: Double
): ForecastDbModel = ForecastDbModel(
    startTime = start,
    endTime = end,
    latitude = latitude,
    longitude = longitude,
    temperature = temperature,
    forecastDescription = description,
    precipitation = precipitation,
    wind = wind.speed,
    windFromDirection = wind.fromDirection,
    humidity = humidity,
    airPressureAtSeaLevel = airPressure
)