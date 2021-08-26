package hr.dtakac.prognoza.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.databinding.CellPlaceBinding
import hr.dtakac.prognoza.uimodel.cell.PlaceUiModel

class PlacesRecyclerViewAdapter(
    private val placeClickListener: (String) -> Unit
) : ListAdapter<PlaceUiModel, PlaceViewHolder>(PlaceDiffCallback()) {
    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(getItem(position), placeClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = CellPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }
}

class PlaceViewHolder(
    private val binding: CellPlaceBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(cellModel: PlaceUiModel, placeClickListener: (String) -> Unit) {
        binding.ivSaved.setImageResource(R.drawable.ic_history)
        binding.ivSaved.visibility = if (cellModel.isSaved) {
            View.VISIBLE
        } else {
            View.GONE
        }
        binding.tvName.text = cellModel.name
        binding.tvFullName.text = cellModel.fullName
        binding.root.setOnClickListener {
            placeClickListener.invoke(cellModel.id)
        }
    }
}

class PlaceDiffCallback : DiffUtil.ItemCallback<PlaceUiModel>() {
    override fun areContentsTheSame(oldItem: PlaceUiModel, newItem: PlaceUiModel): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: PlaceUiModel, newItem: PlaceUiModel): Boolean {
        return oldItem == newItem
    }
}