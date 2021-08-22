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
import hr.dtakac.prognoza.uimodel.cell.HourCellModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class HoursRecyclerViewAdapter : ListAdapter<HourCellModel, HourViewHolder>(HourDiffCallback()) {
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

    fun bind(cellModel: HourCellModel) {
        binding.apply {
            val resources = root.context.resources
            // predefined formatting
            tvTemperature.text = resources.formatTemperatureValue(cellModel.temperature)
            tvFeelsLike.text = resources.formatTemperatureValue(cellModel.feelsLike)
            tvPrecipitationAmount.text =
                resources.formatPrecipitationValue(cellModel.precipitation)
            tvWind.text =
                resources.formatWindWithDirection(cellModel.windSpeed, cellModel.windFromCompassDirection)
            tvHumidity.text = resources.formatHumidityValue(cellModel.relativeHumidity)
            tvPressure.text = resources.formatPressureValue(cellModel.pressure)
            tvDescription.text =
                resources.formatWeatherIconDescription(cellModel.weatherDescription?.descriptionResourceId)
            tvPrecipitationAmount.text =
                resources.formatPrecipitationValue(cellModel.precipitation)
            // other, view-specific operations
            tvPrecipitationAmount.apply {
                visibility = if (cellModel.precipitation.isPrecipitationAmountSignificant()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
            ivWeatherIcon.setImageResource(
                cellModel.weatherDescription?.iconResourceId ?: R.drawable.ic_cloud
            )
            tvTime.text = cellModel.time
                .withZoneSameInstant(ZoneId.systemDefault())
                .format(dateTimeFormatter)
            clDetails.visibility = if (cellModel.isExpanded) View.VISIBLE else View.GONE
        }
    }
}

class HourDiffCallback : DiffUtil.ItemCallback<HourCellModel>() {
    override fun areContentsTheSame(oldItem: HourCellModel, newItem: HourCellModel): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: HourCellModel, newItem: HourCellModel): Boolean {
        return oldItem.id == newItem.id
    }
}