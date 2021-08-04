package hr.dtakac.prognoza.forecast.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.dtakac.prognoza.common.IMAGE_PLACEHOLDER
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.databinding.CellHourBinding
import hr.dtakac.prognoza.forecast.uimodel.HourUiModel
import hr.dtakac.prognoza.common.isPrecipitationAmountSignificant
import hr.dtakac.prognoza.common.isWindSpeedSignificant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class HoursRecyclerViewAdapter : ListAdapter<HourUiModel, HourViewHolder>(HourDiffCallback()) {
    override fun onBindViewHolder(holder: HourViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourViewHolder {
        val binding = CellHourBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HourViewHolder(binding)
    }
}

class HourViewHolder(
    private val binding: CellHourBinding
) : RecyclerView.ViewHolder(binding.root) {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())

    fun bind(uiModel: HourUiModel) {
        val resources = binding.root.context.resources
        binding.tvTemperature.text = if (uiModel.temperature == null) {
            resources.getString(R.string.placeholder_temperature)
        } else {
            resources.getString(
                R.string.template_temperature,
                uiModel.temperature
            )
        }
        binding.windAndPrecipitation.tvPrecipitationAmount.text =
            if (uiModel.precipitationAmount.isPrecipitationAmountSignificant()) {
                resources.getString(R.string.template_precipitation, uiModel.precipitationAmount)
            } else {
                resources.getString(R.string.placeholder_precipitation)
            }
        binding.ivWeatherIcon.setImageResource(
            uiModel.weatherIcon?.iconResourceId ?: IMAGE_PLACEHOLDER
        )
        binding.tvDescription.text = if (uiModel.weatherIcon?.descriptionResourceId == null) {
            resources.getString(R.string.placeholder_weather_description)
        } else {
            resources.getString(uiModel.weatherIcon.descriptionResourceId)
        }
        binding.tvTime.text = uiModel.time
            .withZoneSameInstant(ZoneId.systemDefault())
            .format(dateTimeFormatter)
        if (uiModel.windSpeed.isWindSpeedSignificant()) {
            binding.windAndPrecipitation.tvWindSpeed.text =
                resources.getString(R.string.template_wind_speed, uiModel.windSpeed)
            binding.windAndPrecipitation.ivWindFromDirection.visibility = View.VISIBLE
            binding.windAndPrecipitation.ivWindFromDirection.rotation = uiModel.windFromDirection ?: 0f
        } else {
            binding.windAndPrecipitation.tvWindSpeed.text = resources.getString(R.string.placeholder_wind_speed)
            binding.windAndPrecipitation.ivWindFromDirection.visibility = View.INVISIBLE
        }
    }
}

class HourDiffCallback : DiffUtil.ItemCallback<HourUiModel>() {
    override fun areContentsTheSame(oldItem: HourUiModel, newItem: HourUiModel): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: HourUiModel, newItem: HourUiModel): Boolean {
        return oldItem == newItem
    }
}