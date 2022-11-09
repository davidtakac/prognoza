package hr.dtakac.prognoza.data

import com.squareup.sqldelight.ColumnAdapter
import hr.dtakac.prognoza.entities.forecast.Description
import hr.dtakac.prognoza.entities.forecast.Mood
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
        return Temperature(databaseValue, TemperatureUnit.DEGREE_CELSIUS)
    }

    override fun encode(value: Temperature): Double {
        return value.celsius
    }
}

val descriptionSqlAdapter = object : ColumnAdapter<Description, String> {
    override fun decode(databaseValue: String): Description {
        return Description.valueOf(databaseValue)
    }

    override fun encode(value: Description): String {
        return value.name
    }
}

val moodSqlAdapter = object : ColumnAdapter<Mood, String> {
    override fun decode(databaseValue: String): Mood {
        return Mood.valueOf(databaseValue)
    }

    override fun encode(value: Mood): String {
        return value.name
    }
}

val lengthSqlAdapter = object : ColumnAdapter<Length, Double> {
    override fun decode(databaseValue: Double): Length {
        return Length(databaseValue, LengthUnit.MILLIMETRE)
    }

    override fun encode(value: Length): Double {
        return value.millimeters
    }
}

val speedSqlAdapter = object : ColumnAdapter<Speed, Double> {
    override fun decode(databaseValue: Double): Speed {
        return Speed(databaseValue, SpeedUnit.METRE_PER_SECOND)
    }

    override fun encode(value: Speed): Double {
        return value.metersPerSecond
    }
}

val angleSqlAdapter = object : ColumnAdapter<Angle, Double> {
    override fun decode(databaseValue: Double): Angle {
        return Angle(databaseValue, AngleUnit.DEGREE)
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
        return Pressure(databaseValue, PressureUnit.MILLIBAR)
    }

    override fun encode(value: Pressure): Double {
        return value.millibar
    }
}

val temperatureUnitSqlAdapter = object : ColumnAdapter<TemperatureUnit, String> {
    override fun decode(databaseValue: String): TemperatureUnit {
        return TemperatureUnit.valueOf(databaseValue)
    }

    override fun encode(value: TemperatureUnit): String {
        return value.name
    }
}

val lengthUnitSqlAdapter = object : ColumnAdapter<LengthUnit, String> {
    override fun decode(databaseValue: String): LengthUnit {
        return LengthUnit.valueOf(databaseValue)
    }

    override fun encode(value: LengthUnit): String {
        return value.name
    }
}

val speedUnitSqlAdapter = object : ColumnAdapter<SpeedUnit, String> {
    override fun decode(databaseValue: String): SpeedUnit {
        return SpeedUnit.valueOf(databaseValue)
    }

    override fun encode(value: SpeedUnit): String {
        return value.name
    }
}

val pressureUnitSqlAdapter = object : ColumnAdapter<PressureUnit, String> {
    override fun decode(databaseValue: String): PressureUnit {
        return PressureUnit.valueOf(databaseValue)
    }

    override fun encode(value: PressureUnit): String {
        return value.name
    }
}