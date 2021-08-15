package hr.dtakac.prognoza.forecast.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.common.util.*
import hr.dtakac.prognoza.databinding.CellDayBinding
import hr.dtakac.prognoza.forecast.uimodel.DayUiModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class DaysRecyclerViewAdapter : ListAdapter<DayUiModel, DayViewHolder>(DayDiffCallback()) {
    private val onItemClickCallback = object : (Int) -> Unit {
        override fun invoke(position: Int) {
            val itemAtPosition = getItem(position)
            itemAtPosition.isExpanded = !itemAtPosition.isExpanded
            notifyItemChanged(position)
        }
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val binding = CellDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(binding, onItemClickCallback)
    }
}

class DayViewHolder(
    private val binding: CellDayBinding,
    onItemClickCallback: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.clHeader.setOnClickListener { onItemClickCallback.invoke(adapterPosition) }
    }

    fun bind(uiModel: DayUiModel) {
        binding.apply {
            val resources = root.context.resources
            tvDateTime.text = resources.formatDaySummaryTime(uiModel.time)
            tvTemperatureHigh.text =
                resources.formatTemperatureValue(uiModel.highTemperature)
            tvTemperatureLow.text =
                resources.formatTemperatureValue(uiModel.lowTemperature)
            tvDescription.text =
                resources.formatRepresentativeWeatherIconDescription(uiModel.representativeWeatherIcon)
            ivWeatherIcon.setImageResource(
                uiModel.representativeWeatherIcon?.weatherIcon?.iconResourceId
                    ?: R.drawable.ic_cloud
            )
            tvPrecipitation.text =
                resources.formatTotalPrecipitation(uiModel.totalPrecipitationAmount)
            tvWind.text = resources.formatWindWithDirection(
                uiModel.maxWindSpeed,
                uiModel.windFromCompassDirection
            )
            tvHumidity.text = resources.formatHumidityValue(uiModel.maxHumidity)
            tvPressure.text = resources.formatPressureValue(uiModel.maxPressure)
            clDetails.visibility = if (uiModel.isExpanded) View.VISIBLE else View.GONE
        }
    }
}

class DayDiffCallback : DiffUtil.ItemCallback<DayUiModel>() {
    override fun areContentsTheSame(oldItem: DayUiModel, newItem: DayUiModel): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: DayUiModel, newItem: DayUiModel): Boolean {
        return oldItem.id == newItem.id
    }
}