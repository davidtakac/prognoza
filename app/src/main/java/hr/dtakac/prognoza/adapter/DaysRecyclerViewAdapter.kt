package hr.dtakac.prognoza.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.databinding.CellDayBinding
import hr.dtakac.prognoza.extensions.*
import hr.dtakac.prognoza.uimodel.cell.DayCellModel

class DaysRecyclerViewAdapter : ListAdapter<DayCellModel, DayViewHolder>(DayDiffCallback()) {
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

    fun bind(cellModel: DayCellModel) {
        binding.apply {
            val resources = root.context.resources
            tvDateTime.text = if (DateUtils.isToday(cellModel.time.toInstant().toEpochMilli())) {
                resources.getString(R.string.today)
            } else {
                DateUtils.formatDateTime(
                    root.context,
                    cellModel.time.toInstant().toEpochMilli(),
                    DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_WEEKDAY
                )
            }
            tvTemperatureHigh.text =
                resources.formatTemperatureValue(cellModel.highTemperature, cellModel.unit)
            tvTemperatureLow.text =
                resources.formatTemperatureValue(cellModel.lowTemperature, cellModel.unit)
            tvDescription.text =
                resources.formatRepresentativeWeatherIconDescription(cellModel.representativeWeatherDescription)
            ivWeatherIcon.setImageResource(
                cellModel.representativeWeatherDescription?.weatherDescription?.iconResourceId
                    ?: R.drawable.ic_cloud
            )
            tvPrecipitation.text =
                resources.formatTotalPrecipitation(
                    cellModel.totalPrecipitationAmount,
                    cellModel.unit
                )
            tvWind.text = resources.formatWindWithDirection(
                cellModel.maxWindSpeed,
                cellModel.windFromCompassDirection,
                cellModel.unit
            )
            tvHumidity.text = resources.formatHumidityValue(cellModel.maxHumidity)
            tvPressure.text = resources.formatPressureValue(cellModel.maxPressure, cellModel.unit)
            clDetails.visibility = if (cellModel.isExpanded) View.VISIBLE else View.GONE
        }
    }
}

class DayDiffCallback : DiffUtil.ItemCallback<DayCellModel>() {
    override fun areContentsTheSame(oldItem: DayCellModel, newItem: DayCellModel): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: DayCellModel, newItem: DayCellModel): Boolean {
        return oldItem.id == newItem.id
    }
}