package hr.dtakac.prognoza.forecast.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.common.isPrecipitationAmountSignificant
import hr.dtakac.prognoza.common.isWindSpeedSignificant
import hr.dtakac.prognoza.databinding.CellHourBinding
import hr.dtakac.prognoza.forecast.uimodel.HourUiModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

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
        binding.tvTemperature.text = if (uiModel.temperature == null) {
            resources.getString(R.string.placeholder_temperature)
        } else {
            resources.getString(
                R.string.template_temperature_universal,
                uiModel.temperature
            )
        }
        binding.tvFeelsLike.text = resources.getString(
            R.string.template_feels_like,
            if (uiModel.feelsLike == null) {
                resources.getString(R.string.placeholder_temperature)
            } else {
                resources.getString(R.string.template_temperature_universal, uiModel.temperature)
            }
        )
        binding.tvPrecipitationAmount.apply {
            if (uiModel.precipitation.isPrecipitationAmountSignificant()) {
                visibility = View.VISIBLE
                text = resources.getString(
                    R.string.template_precipitation_metric,
                    uiModel.precipitation
                )
            } else {
                visibility = View.GONE
                text = null
            }
        }
        binding.ivWeatherIcon.setImageResource(
            uiModel.weatherIcon?.iconResourceId ?: R.drawable.ic_cloud
        )
        binding.tvDescription.text = if (uiModel.weatherIcon?.descriptionResourceId == null) {
            resources.getString(R.string.placeholder_description)
        } else {
            resources.getString(uiModel.weatherIcon.descriptionResourceId)
        }
        binding.tvTime.text = uiModel.time
            .withZoneSameInstant(ZoneId.systemDefault())
            .format(dateTimeFormatter)
        binding.tvWind.text = resources.getString(
            R.string.template_wind_description,
            if (uiModel.windSpeed.isWindSpeedSignificant()) {
                resources.getString(R.string.template_wind_metric, uiModel.windSpeed)
            } else {
                resources.getString(R.string.placeholder_wind_speed)
            },
            resources.getString(
                uiModel.windFromCompassDirection ?: R.string.placeholder_wind_direction
            )
        )
        binding.tvHumidity.text = if (uiModel.relativeHumidity != null) {
            resources.getString(
                R.string.template_humidity_description,
                resources.getString(
                    R.string.template_humidity_universal,
                    uiModel.relativeHumidity.roundToInt()
                )
            )
        } else {
            resources.getString(R.string.placeholder_humidity)
        }
        binding.tvPressure.text = resources.getString(
            R.string.template_pressure_description,
            if (uiModel.pressure == null) resources.getString(R.string.placeholder_precipitation)
            else resources.getString(
                R.string.template_pressure_metric,
                uiModel.pressure.roundToInt()
            )
        )
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