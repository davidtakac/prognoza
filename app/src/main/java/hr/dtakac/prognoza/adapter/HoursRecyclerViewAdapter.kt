package hr.dtakac.prognoza.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.databinding.CellHourBinding
import hr.dtakac.prognoza.extensions.*
import hr.dtakac.prognoza.uimodel.cell.HourUiModel

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
    init {
        binding.clHeader.setOnClickListener { onItemClickCallback.invoke(adapterPosition) }
    }

    fun bind(cellModel: HourUiModel) {
        binding.apply {
            val resources = root.context.resources
            // predefined formatting
            tvTemperature.text =
                resources.formatTemperatureValue(cellModel.temperature, cellModel.displayDataInUnit)
            tvFeelsLike.text =
                resources.formatTemperatureValue(cellModel.feelsLike, cellModel.displayDataInUnit)
            tvPrecipitationAmount.text =
                resources.formatPrecipitationValue(
                    cellModel.precipitation,
                    cellModel.displayDataInUnit
                )
            tvWind.text =
                resources.formatWindWithDirection(
                    cellModel.windSpeed,
                    cellModel.windFromCompassDirection,
                    cellModel.displayDataInUnit
                )
            tvHumidity.text = resources.formatHumidityValue(cellModel.relativeHumidity)
            tvPressure.text =
                resources.formatPressureValue(cellModel.pressure, cellModel.displayDataInUnit)
            tvDescription.text =
                resources.formatWeatherIconDescription(cellModel.weatherDescription?.descriptionResourceId)
            // other, view-specific operations
            tvPrecipitationAmount.visibility =
                if (cellModel.precipitation != null && cellModel.precipitation > 0f) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            ivWeatherIcon.setImageResource(
                cellModel.weatherDescription?.iconResourceId ?: R.drawable.ic_cloud
            )
            val time = DateUtils.formatDateTime(
                binding.root.context,
                cellModel.time.toInstant().toEpochMilli(),
                DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_TIME
            )
            tvTime.text = time
            clDetails.visibility = if (cellModel.isExpanded) View.VISIBLE else View.GONE
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