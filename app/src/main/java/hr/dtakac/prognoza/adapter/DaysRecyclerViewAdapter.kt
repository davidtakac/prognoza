package hr.dtakac.prognoza.adapter

import android.icu.text.RelativeDateTimeFormatter
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.databinding.CellDayBinding
import hr.dtakac.prognoza.model.ui.cell.DayUiModel
import hr.dtakac.prognoza.common.utils.*

class DaysRecyclerViewAdapter : ListAdapter<DayUiModel, DayViewHolder>(DayDiffCallback()) {
    private val onItemClickCallback = object : (Int) -> Unit {
        override fun invoke(position: Int) {
            val itemAtPosition = getItem(position)
            itemAtPosition.isExpanded = !itemAtPosition.isExpanded
            notifyItemChanged(position)
        }
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(getItem(position), position == 0)
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
        binding.root.setOnClickListener { onItemClickCallback.invoke(adapterPosition) }
    }

    fun bind(uiModel: DayUiModel, isTomorrow: Boolean) {
        with(binding) {
            val context = root.context
            tvDateTime.text = if (isTomorrow) {
                RelativeDateTimeFormatter.getInstance().format(
                    RelativeDateTimeFormatter.Direction.NEXT,
                    RelativeDateTimeFormatter.AbsoluteUnit.DAY
                ).replaceFirstChar { it.uppercaseChar() }
            } else {
                DateUtils.formatDateTime(
                    context,
                    uiModel.time.toInstant().toEpochMilli(),
                    DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_WEEKDAY
                )
            }
            tvTemperatureHigh.text =
                context.formatTemperatureValue(
                    uiModel.highTemperature,
                    uiModel.displayDataInUnit
                )
            tvTemperatureLow.text =
                context.formatTemperatureValue(
                    uiModel.lowTemperature,
                    uiModel.displayDataInUnit
                )
            tvDescription.text =
                context.formatRepresentativeWeatherIconDescription(uiModel.representativeWeatherDescription)
            Glide.with(binding.root)
                .load(uiModel.representativeWeatherDescription?.weatherDescription?.iconResourceId)
                .fallback(R.drawable.ic_cloud_off)
                .into(ivWeatherIcon)
            tvPrecipitation.text =
                root.context.formatTotalPrecipitation(
                    uiModel.totalPrecipitationAmount,
                    uiModel.displayDataInUnit
                )
            tvWind.text = context.formatWindWithDirection(
                uiModel.maxWindSpeed,
                uiModel.windFromCompassDirection,
                uiModel.displayDataInUnit
            )
            tvHumidity.text = context.formatHumidityValue(uiModel.maxHumidity)
            tvPressure.text =
                context.formatPressureValue(uiModel.maxPressure, uiModel.displayDataInUnit)
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