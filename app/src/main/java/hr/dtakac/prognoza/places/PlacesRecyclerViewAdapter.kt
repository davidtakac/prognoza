package hr.dtakac.prognoza.places

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.dtakac.prognoza.databinding.CellPlaceBinding
import hr.dtakac.prognoza.places.PlaceUiModel

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
    fun bind(uiModel: PlaceUiModel, placeClickListener: (String) -> Unit) {
        binding.ivSaved.visibility = if (uiModel.isSaved) {
            View.VISIBLE
        } else {
            View.GONE
        }
        binding.tvName.text = uiModel.name
        binding.tvFullName.text = uiModel.fullName
        binding.root.setOnClickListener {
            placeClickListener.invoke(uiModel.id)
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