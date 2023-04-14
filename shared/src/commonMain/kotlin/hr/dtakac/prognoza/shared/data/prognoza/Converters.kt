package hr.dtakac.prognoza.shared.data.prognoza

import com.squareup.sqldelight.ColumnAdapter
import hr.dtakac.prognoza.shared.entity.*
import kotlinx.datetime.TimeZone

internal val temperatureSqlAdapter = object : ColumnAdapter<Temperature, Double> {
    override fun decode(databaseValue: Double): Temperature = Temperature(databaseValue, TemperatureUnit.DegreeCelsius)
    override fun encode(value: Temperature): Double = value.celsius
}

internal val lengthSqlAdapter = object : ColumnAdapter<Length, Double> {
    override fun decode(databaseValue: Double): Length = Length(databaseValue, LengthUnit.Millimetre)
    override fun encode(value: Length): Double = value.millimetre
}

internal val speedSqlAdapter = object : ColumnAdapter<Speed, Double> {
    override fun decode(databaseValue: Double): Speed = Speed(databaseValue, SpeedUnit.MetrePerSecond)
    override fun encode(value: Speed): Double = value.metrePerSecond
}

internal val angleSqlAdapter = object : ColumnAdapter<Angle, Double> {
    override fun decode(databaseValue: Double): Angle = Angle(databaseValue, AngleUnit.Degree)
    override fun encode(value: Angle): Double = value.degree
}

internal val percentageSqlAdapter = object : ColumnAdapter<Percentage, Double> {
    override fun decode(databaseValue: Double): Percentage = Percentage(databaseValue, PercentageUnit.Percent)
    override fun encode(value: Percentage): Double = value.percent
}

internal val pressureSqlAdapter = object : ColumnAdapter<Pressure, Double> {
    override fun decode(databaseValue: Double): Pressure = Pressure(databaseValue, PressureUnit.Millibar)
    override fun encode(value: Pressure): Double = value.millibar
}

internal val timeZoneAdapter = object : ColumnAdapter<TimeZone, String> {
    override fun decode(databaseValue: String): TimeZone = TimeZone.of(databaseValue)
    override fun encode(value: TimeZone): String = value.id
}