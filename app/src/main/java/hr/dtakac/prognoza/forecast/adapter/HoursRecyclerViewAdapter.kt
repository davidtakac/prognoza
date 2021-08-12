package hr.dtakac.prognoza.forecast.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.common.util.*
import hr.dtakac.prognoza.databinding.CellHourBinding
import hr.dtakac.prognoza.forecast.uimodel.HourUiModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class HoursRecyclerViewAdapter : ListAdapter<HourUiModel, HourViewHolder>(HourDiffCallback()) {
    override fun onBindViewHolder(holder: HourViewHolder, position: Int) {
        holder.binding.clHeader.setOnClickListener {
            val itemAtPosition = getItem(position)
            itemAtPosition.isExpanded = !itemAtPosition.isExpanded
            notifyItemChanged(position)
        }
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourViewHolder {
        val binding = CellHourBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HourViewHolder(binding)
    }
}

class HourViewHolder(
    val binding: CellHourBinding
) : RecyclerView.ViewHolder(binding.root) {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())

    fun bind(uiModel: HourUiModel) {
        val resources = binding.root.context.resources
        // predefined formatting
        binding.tvTemperature.text = resources.formatTemperatureValue(uiModel.temperature)
        binding.tvFeelsLike.text = resources.formatTemperatureValue(uiModel.feelsLike)
        binding.tvPrecipitationAmount.text =
            resources.formatPrecipitationValue(uiModel.precipitation)
        binding.tvWind.text =
            resources.formatWindWithDirection(uiModel.windSpeed, uiModel.windFromCompassDirection)
        binding.tvHumidity.text = resources.formatHumidityValue(uiModel.relativeHumidity)
        binding.tvPressure.text = resources.formatPressureValue(uiModel.pressure)
        binding.tvDescription.text =
            resources.formatWeatherIconDescription(uiModel.weatherIcon?.descriptionResourceId)
        binding.tvPrecipitationAmount.text =
            resources.formatPrecipitationValue(uiModel.precipitation)
        // other, view-specific operations
        binding.tvPrecipitationAmount.apply {
            visibility = if (uiModel.precipitation.isPrecipitationAmountSignificant()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        binding.ivWeatherIcon.setImageResource(
            uiModel.weatherIcon?.iconResourceId ?: R.drawable.ic_cloud
        )
        binding.tvTime.text = uiModel.time
            .withZoneSameInstant(ZoneId.systemDefault())
            .format(dateTimeFormatter)
        binding.clDetails.visibility = if (uiModel.isExpanded) View.VISIBLE else View.GONE
    }
}

class HourDiffCallback : DiffUtil.ItemCallback<HourUiModel>() {
    override fun areContentsTheSame(oldItem: HourUiModel, newItem: HourUiModel): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: HourUiModel, newItem: HourUiModel): Boolean {
        return oldItem.id == newItem.id
    }
}