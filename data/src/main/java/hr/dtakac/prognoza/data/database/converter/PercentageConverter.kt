package hr.dtakac.prognoza.data.database.converter

import androidx.room.TypeConverter
import hr.dtakac.prognoza.entities.forecast.units.Percentage
import hr.dtakac.prognoza.entities.forecast.units.PercentageUnit

object PercentageConverter {
    @JvmStatic
    @TypeConverter
    fun fromDouble(value: Double?): Percentage? = value?.let {
        Percentage(it, PercentageUnit.PERCENT)
    }

    @JvmStatic
    @TypeConverter
    fun toDouble(percentage: Percentage?): Double? = percentage?.percent
}