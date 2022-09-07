package hr.dtakac.prognoza.data.database.forecast.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters
import hr.dtakac.prognoza.data.database.converter.*
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import hr.dtakac.prognoza.entities.forecast.units.*
import java.time.ZonedDateTime

@Entity(
    tableName = "forecast",
    primaryKeys = ["startTime", "endTime", "latitude", "longitude"]
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