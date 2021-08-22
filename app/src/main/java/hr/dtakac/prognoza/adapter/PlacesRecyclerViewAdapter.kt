package hr.dtakac.prognoza.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.dtakac.prognoza.R
import hr.dtakac.prognoza.databinding.CellPlaceBinding
import hr.dtakac.prognoza.uimodel.cell.PlaceCellModel

class PlacesRecyclerViewAdapter(
    private val placeClickListener: (String) -> Unit
) : ListAdapter<PlaceCellModel, PlaceViewHolder>(PlaceDiffCallback()) {
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
    fun bind(cellModel: PlaceCellModel, placeClickListener: (String) -> Unit) {
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

class PlaceDiffCallback : DiffUtil.ItemCallback<PlaceCellModel>() {
    override fun areContentsTheSame(oldItem: PlaceCellModel, newItem: PlaceCellModel): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: PlaceCellModel, newItem: PlaceCellModel): Boolean {
        return oldItem == newItem
    }
}