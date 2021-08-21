package hr.dtakac.prognoza.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.extensions.*
import hr.dtakac.prognoza.databinding.CellHourBinding
import hr.dtakac.prognoza.uimodel.HourUiModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class HoursRecyclerViewAdapter : ListAdapter<HourUiModel, HourViewHolder>(HourDiffCallback()) {
    private val onItemClickCallback = object : (Int) -> Unit {
        override fun invoke(position: Int) {
            val itemAtPosition = getItem(position)
            itemAtPosition.isExpanded = !itemAtPosition.isExpanded
            notifyItemChanged(position)
        }
    }

    override fun onBindViewHolder(holder: HourViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourViewHolder {
        val binding = CellHourBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HourViewHolder(binding, onItemClickCallback)
    }
}

class HourViewHolder(
    private val binding: CellHourBinding,
    onItemClickCallback: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())

    init {
        binding.clHeader.setOnClickListener { onItemClickCallback.invoke(adapterPosition) }
    }

    fun bind(uiModel: HourUiModel) {
        binding.apply {
            val resources = root.context.resources
            // predefined formatting
            tvTemperature.text = resources.formatTemperatureValue(uiModel.temperature)
            tvFeelsLike.text = resources.formatTemperatureValue(uiModel.feelsLike)
            tvPrecipitationAmount.text =
                resources.formatPrecipitationValue(uiModel.precipitation)
            tvWind.text =
                resources.formatWindWithDirection(uiModel.windSpeed, uiModel.windFromCompassDirection)
            tvHumidity.text = resources.formatHumidityValue(uiModel.relativeHumidity)
            tvPressure.text = resources.formatPressureValue(uiModel.pressure)
            tvDescription.text =
                resources.formatWeatherIconDescription(uiModel.weatherIcon?.descriptionResourceId)
            tvPrecipitationAmount.text =
                resources.formatPrecipitationValue(uiModel.precipitation)
            // other, view-specific operations
            tvPrecipitationAmount.apply {
                visibility = if (uiModel.precipitation.isPrecipitationAmountSignificant()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
            ivWeatherIcon.setImageResource(
                uiModel.weatherIcon?.iconResourceId ?: R.drawable.ic_cloud
            )
            tvTime.text = uiModel.time
                .withZoneSameInstant(ZoneId.systemDefault())
                .format(dateTimeFormatter)
            clDetails.visibility = if (uiModel.isExpanded) View.VISIBLE else View.GONE
        }
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