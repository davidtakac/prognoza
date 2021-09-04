package hr.dtakac.prognoza.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
        with(binding) {
            val context = root.context
            // predefined formatting
            tvTemperature.text =
                context.formatTemperatureValue(uiModel.temperature, uiModel.displayDataInUnit)
            tvFeelsLike.text =
                context.formatTemperatureValue(uiModel.feelsLike, uiModel.displayDataInUnit)
            tvPrecipitationAmount.text =
                root.context.formatPrecipitationValue(
                    uiModel.precipitationAmount,
                    uiModel.displayDataInUnit
                )
            tvWind.text =
                context.formatWindWithDirection(
                    uiModel.windSpeed,
                    uiModel.windFromCompassDirection,
                    uiModel.displayDataInUnit
                )
            tvHumidity.text = context.formatHumidityValue(uiModel.relativeHumidity)
            tvPressure.text =
                context.formatPressureValue(uiModel.airPressureAtSeaLevel, uiModel.displayDataInUnit)
            // other, view-specific operations
            tvPrecipitationAmount.visibility =
                if (uiModel.precipitationAmount != null && uiModel.precipitationAmount > 0f) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            Glide.with(binding.root)
                .load(uiModel.weatherDescription?.iconResourceId)
                .fallback(R.drawable.ic_cloud_off)
                .into(ivWeatherIcon)
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