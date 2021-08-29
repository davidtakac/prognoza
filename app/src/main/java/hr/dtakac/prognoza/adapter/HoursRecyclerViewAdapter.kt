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

    fun bind(uiModel: HourUiModel) {
        binding.apply {
            val resources = root.context.resources
            // predefined formatting
            tvTemperature.text =
                resources.formatTemperatureValue(uiModel.temperature, uiModel.displayDataInUnit)
            tvFeelsLike.text =
                resources.formatTemperatureValue(uiModel.feelsLike, uiModel.displayDataInUnit)
            tvPrecipitationAmount.text =
                root.context.formatPrecipitationValue(
                    uiModel.precipitation,
                    uiModel.displayDataInUnit
                )
            tvWind.text =
                resources.formatWindWithDirection(
                    uiModel.windSpeed,
                    uiModel.windFromCompassDirection,
                    uiModel.displayDataInUnit
                )
            tvHumidity.text = resources.formatHumidityValue(uiModel.relativeHumidity)
            tvPressure.text =
                resources.formatPressureValue(uiModel.pressure, uiModel.displayDataInUnit)
            tvDescription.text =
                resources.formatWeatherIconDescription(uiModel.weatherDescription?.descriptionResourceId)
            // other, view-specific operations
            tvPrecipitationAmount.visibility =
                if (uiModel.precipitation != null && uiModel.precipitation > 0f) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            ivWeatherIcon.setImageResource(
                uiModel.weatherDescription?.iconResourceId ?: R.drawable.ic_cloud
            )
            val time = DateUtils.formatDateTime(
                binding.root.context,
                uiModel.time.toInstant().toEpochMilli(),
                DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_TIME
            )
            tvTime.text = time
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