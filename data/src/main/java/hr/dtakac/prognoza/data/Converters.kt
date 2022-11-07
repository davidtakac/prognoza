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
        return Temperature(databaseValue, TemperatureUnit.C)
    }

    override fun encode(value: Temperature): Double {
        return value.celsius
    }
}

val descriptionSqlAdapter = object : ColumnAdapter<Description, Long> {
    override fun decode(databaseValue: Long): Description {
        return Description.values()[databaseValue.toInt()]
    }

    override fun encode(value: Description): Long {
        return value.ordinal.toLong()
    }
}

val moodSqlAdapter = object : ColumnAdapter<Mood, Long> {
    override fun decode(databaseValue: Long): Mood {
        return Mood.values()[databaseValue.toInt()]
    }

    override fun encode(value: Mood): Long {
        return value.ordinal.toLong()
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

val temperatureUnitSqlAdapter = object : ColumnAdapter<TemperatureUnit, Long> {
    override fun decode(databaseValue: Long): TemperatureUnit {
        return TemperatureUnit.values()[databaseValue.toInt()]
    }

    override fun encode(value: TemperatureUnit): Long {
        return value.ordinal.toLong()
    }
}

val lengthUnitSqlAdapter = object : ColumnAdapter<LengthUnit, Long> {
    override fun decode(databaseValue: Long): LengthUnit {
        return LengthUnit.values()[databaseValue.toInt()]
    }

    override fun encode(value: LengthUnit): Long {
        return value.ordinal.toLong()
    }
}

val speedUnitSqlAdapter = object : ColumnAdapter<SpeedUnit, Long> {
    override fun decode(databaseValue: Long): SpeedUnit {
        return SpeedUnit.values()[databaseValue.toInt()]
    }

    override fun encode(value: SpeedUnit): Long {
        return value.ordinal.toLong()
    }
}

val pressureUnitSqlAdapter = object : ColumnAdapter<PressureUnit, Long> {
    override fun decode(databaseValue: Long): PressureUnit {
        return PressureUnit.values()[databaseValue.toInt()]
    }

    override fun encode(value: PressureUnit): Long {
        return value.ordinal.toLong()
    }
}