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
    private val isPositionExpanded = mutableMapOf<Int, Boolean>()

    override fun onBindViewHolder(holder: HourViewHolder, position: Int) {
        holder.binding.clHeader.setOnClickListener {
            isPositionExpanded[position] = !(isPositionExpanded[position] ?: false)
            notifyItemChanged(position)
        }
        holder.bind(getItem(position), isPositionExpanded[position] ?: false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourViewHolder {
        val binding = CellHourBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HourViewHolder(binding)
    }

    fun submitListActual(list: List<HourUiModel>) {
        super.submitList(list)
        isPositionExpanded.clear()
    }
}

class HourViewHolder(
    val binding: CellHourBinding
) : RecyclerView.ViewHolder(binding.root) {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())

    fun bind(uiModel: HourUiModel, isExpanded: Boolean) {
        val resources = binding.root.context.resources
        // predefined formatting
        binding.tvTemperature.text = resources.formatTemperatureValue(uiModel.temperature)
        binding.tvFeelsLike.text = resources.formatFeelsLikeDescription(uiModel.feelsLike)
        binding.tvPrecipitationAmount.text =
            resources.formatPrecipitationValue(uiModel.precipitation)
        binding.tvWind.text =
            resources.formatWindDescription(uiModel.windSpeed, uiModel.windFromCompassDirection)
        binding.tvHumidity.text = resources.formatHumidityDescription(uiModel.relativeHumidity)
        binding.tvPressure.text = resources.formatPressureDescription(uiModel.pressure)
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
        binding.clDetails.visibility = if (isExpanded) View.VISIBLE else View.GONE
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