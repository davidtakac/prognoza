package hr.dtakac.prognoza.forecast.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.dtakac.prognoza.IMAGE_PLACEHOLDER
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.databinding.CellHourBinding
import hr.dtakac.prognoza.forecast.uimodel.HourUiModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class HoursRecyclerViewAdapter : RecyclerView.Adapter<HourViewHolder>() {
    var data: List<HourUiModel> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: HourViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourViewHolder {
        val binding = CellHourBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HourViewHolder(binding)
    }
}

class HourViewHolder(
    private val binding: CellHourBinding
) : RecyclerView.ViewHolder(binding.root) {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())

    fun bind(uiModel: HourUiModel) {
        val resources = binding.root.context.resources
        binding.tvTemperature.text = if (uiModel.temperature == null) {
            resources.getString(R.string.temperature_placeholder)
        } else {
            resources.getString(
                R.string.template_degrees,
                uiModel.temperature
            )
        }
        binding.tvPrecipitationAmount.apply {
            if (uiModel.precipitationAmount ?: 0f != 0f) {
                text = resources.getString(R.string.template_mm, uiModel.precipitationAmount)
                visibility = View.VISIBLE
            } else {
                text = null
                visibility = View.GONE
            }
        }
        binding.ivWeatherIcon.setImageResource(uiModel.weatherIcon?.iconResourceId ?: IMAGE_PLACEHOLDER)
        binding.tvTime.text = uiModel.time
            .withZoneSameInstant(ZoneId.systemDefault())
            .format(dateTimeFormatter)
    }
}