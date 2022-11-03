package hr.dtakac.prognoza.data

import com.squareup.sqldelight.ColumnAdapter
import hr.dtakac.prognoza.entities.forecast.ForecastDescription
import hr.dtakac.prognoza.entities.forecast.units.*
import java.time.ZonedDateTime

val zonedDateTimeSqlAdapter = object : ColumnAdapter<ZonedDateTime, String> {
    override fun decode(databaseValue: String): ZonedDateTime {
        return ZonedDateTime.parse(databaseValue)
    }

    override fun encode(value: ZonedDateTime): String {
        return value.toString()
    }
}

val temperatureSqlAdapter = object : ColumnAdapter<Temperature, Double> {
    override fun decode(databaseValue: Double): Temperature {
        return Temperature(databaseValue, TemperatureUnit.C)
    }

    override fun encode(value: Temperature): Double {
        return value.celsius
    }
}

val forecastDescriptionSqlAdapter = object : ColumnAdapter<ForecastDescription, String> {
    override fun decode(databaseValue: String): ForecastDescription {
        return ForecastDescription.valueOf(databaseValue)
    }

    override fun encode(value: ForecastDescription): String {
        return value.name
    }
}

val lengthSqlAdapter = object : ColumnAdapter<Length, Double> {
    override fun decode(databaseValue: Double): Length {
        return Length(databaseValue, LengthUnit.MM)
    }

    override fun encode(value: Length): Double {
        return value.millimeters
    }
}

val speedSqlAdapter = object : ColumnAdapter<Speed, Double> {
    override fun decode(databaseValue: Double): Speed {
        return Speed(databaseValue, SpeedUnit.MPS)
    }

    override fun encode(value: Speed): Double {
        return value.metersPerSecond
    }
}

val angleSqlAdapter = object : ColumnAdapter<Angle, Double> {
    override fun decode(databaseValue: Double): Angle {
        return Angle(databaseValue, AngleUnit.DEG)
    }

    override fun encode(value: Angle): Double {
        return value.degrees
    }
}

val percentageSqlAdapter = object : ColumnAdapter<Percentage, Double> {
    override fun decode(databaseValue: Double): Percentage {
        return Percentage(databaseValue, PercentageUnit.PERCENT)
    }

    override fun encode(value: Percentage): Double {
        return value.percent
    }
}

val pressureSqlAdapter = object : ColumnAdapter<Pressure, Double> {
    override fun decode(databaseValue: Double): Pressure {
        return Pressure(databaseValue, PressureUnit.HPA)
    }

    override fun encode(value: Pressure): Double {
        return value.hectoPascal
    }
}

val temperatureUnitSqlAdapter = object : ColumnAdapter<TemperatureUnit, Int> {
    override fun decode(databaseValue: Int): TemperatureUnit {
        return TemperatureUnit.values()[databaseValue]
    }

    override fun encode(value: TemperatureUnit): Int {
        return value.ordinal
    }
}

val lengthUnitSqlAdapter = object : ColumnAdapter<LengthUnit, Int> {
    override fun decode(databaseValue: Int): LengthUnit {
        return LengthUnit.values()[databaseValue]
    }

    override fun encode(value: LengthUnit): Int {
        return value.ordinal
    }
}

val speedUnitSqlAdapter = object : ColumnAdapter<SpeedUnit, Int> {
    override fun decode(databaseValue: Int): SpeedUnit {
        return SpeedUnit.values()[databaseValue]
    }

    override fun encode(value: SpeedUnit): Int {
        return value.ordinal
    }
}

val pressureUnitSqlAdapter = object : ColumnAdapter<PressureUnit, Int> {
    override fun decode(databaseValue: Int): PressureUnit {
        return PressureUnit.values()[databaseValue]
    }

    override fun encode(value: PressureUnit): Int {
        return value.ordinal
    }
}