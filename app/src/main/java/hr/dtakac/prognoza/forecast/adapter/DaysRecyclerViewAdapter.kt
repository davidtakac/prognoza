package hr.dtakac.prognoza.forecast.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.databinding.CellDayBinding
import hr.dtakac.prognoza.forecast.uimodel.DayUiModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class DaysRecyclerViewAdapter : RecyclerView.Adapter<DayViewHolder>() {
    var data: List<DayUiModel> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val binding = CellDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(binding)
    }
}

class DayViewHolder(
    private val binding: CellDayBinding
) : RecyclerView.ViewHolder(binding.root) {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd LLLL", Locale.getDefault())

    fun bind(uiModel: DayUiModel) {
        val resources = binding.root.context
        binding.tvDateTime.text = uiModel.time.withZoneSameInstant(ZoneId.systemDefault()).format(dateTimeFormatter)
        binding.tvTemperatureHigh.text = resources.getString(R.string.template_degrees, uiModel.highTemperature)
        binding.tvTemperatureLow.text = resources.getString(R.string.template_degrees, uiModel.lowTemperature)
        binding.tvDescription.text = resources.getString(uiModel.weatherIcon.descriptionResourceId)
        binding.ivWeatherIcon.setImageResource(uiModel.weatherIcon.iconResourceId)
    }
}