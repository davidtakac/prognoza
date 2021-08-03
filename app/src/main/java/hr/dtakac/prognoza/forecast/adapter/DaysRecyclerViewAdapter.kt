package hr.dtakac.prognoza.forecast.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.dtakac.prognoza.IMAGE_PLACEHOLDER
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.database.entity.isPrecipitationAmountSignificant
import hr.dtakac.prognoza.database.entity.isWindSpeedSignificant
import hr.dtakac.prognoza.databinding.CellDayBinding
import hr.dtakac.prognoza.forecast.uimodel.DayUiModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class DaysRecyclerViewAdapter : ListAdapter<DayUiModel, DayViewHolder>(DayDiffCallback()) {
    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val binding = CellDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(binding)
    }
}

class DayViewHolder(
    private val binding: CellDayBinding
) : RecyclerView.ViewHolder(binding.root) {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("EE, d LLLL", Locale.getDefault())

    fun bind(uiModel: DayUiModel) {
        val resources = binding.root.context
        binding.tvDateTime.text =
            uiModel.time.withZoneSameInstant(ZoneId.systemDefault()).format(dateTimeFormatter)
        binding.tvTemperatureHigh.text =
            resources.getString(R.string.template_temperature, uiModel.highTemperature)
        binding.tvTemperatureLow.text =
            resources.getString(R.string.template_temperature, uiModel.lowTemperature)
        binding.tvDescription.text =
            resources.getString(uiModel.weatherIcon?.descriptionResourceId ?: IMAGE_PLACEHOLDER)
        binding.ivWeatherIcon.setImageResource(
            uiModel.weatherIcon?.iconResourceId ?: IMAGE_PLACEHOLDER
        )
        binding.windAndPrecipitation.tvPrecipitationAmount.text =
            if (uiModel.precipitationAmount.isPrecipitationAmountSignificant()) {
                resources.getString(R.string.template_precipitation, uiModel.precipitationAmount)
            } else {
                resources.getString(R.string.placeholder_precipitation)
            }
        binding.windAndPrecipitation.tvWindSpeed.text =
            if (uiModel.maxWindSpeed.isWindSpeedSignificant()) {
                resources.getString(R.string.template_wind_speed, uiModel.maxWindSpeed)
            } else {
                resources.getString(R.string.placeholder_wind_speed)
            }
        binding.windAndPrecipitation.ivWindFromDirection.visibility = View.GONE
    }
}

class DayDiffCallback : DiffUtil.ItemCallback<DayUiModel>() {
    override fun areContentsTheSame(oldItem: DayUiModel, newItem: DayUiModel): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: DayUiModel, newItem: DayUiModel): Boolean {
        return oldItem == newItem
    }
}