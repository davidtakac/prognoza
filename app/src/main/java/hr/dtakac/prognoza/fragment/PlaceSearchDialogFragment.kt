package hr.dtakac.prognoza.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import hr.dtakac.prognoza.BUNDLE_KEY_PLACE_PICKED
import hr.dtakac.prognoza.PLACE_SEARCH_REQUEST_KEY
import hr.dtakac.prognoza.adapter.PlacesRecyclerViewAdapter
import hr.dtakac.prognoza.common.MarginItemDecoration
import hr.dtakac.prognoza.databinding.FragmentPlaceSearchBinding
import hr.dtakac.prognoza.extensions.toPx
import hr.dtakac.prognoza.viewmodel.PlacesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

class PlaceSearchDialogFragment : DialogFragment() {
    private val adapter = PlacesRecyclerViewAdapter { viewModel.select(it) }
    private val viewModel by viewModel<PlacesViewModel>()
    private var _binding: FragmentPlaceSearchBinding? = null
    private val binding: FragmentPlaceSearchBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaceSearchBinding.inflate(inflater)
        setDialogPosition()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initializeRecyclerView()
        initializeSearchField()
        if (viewModel.places.value.isNullOrEmpty()) {
            viewModel.showPlaces()
        }
    }

    private fun observeViewModel() {
        viewModel.places.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.apply {
                if (isLoading) show() else hide()
            }
        }
        viewModel.placeSelectedEvent.observe(viewLifecycleOwner) {
            if (!it.isConsumed) {
                notifyParentPlaceWasPicked()
            }
        }
        viewModel.message.observe(viewLifecycleOwner) {
            if (!it.isConsumed) {
                Snackbar.make(
                    binding.root,
                    resources.getString(it.getValue()),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun initializeRecyclerView() {
        binding.rvResults.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvResults.adapter = adapter
        binding.rvResults.addItemDecoration(MarginItemDecoration())
        binding.rvResults.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun initializeSearchField() {
        binding.etSearch.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.showPlaces(textView.text.toString())
                true
            } else {
                false
            }
        }
        binding.etSearch.addTextChangedListener {
            if (it.isNullOrBlank()) {
                viewModel.showPlaces()
            }
        }
    }

    private fun notifyParentPlaceWasPicked() {
        val result = Bundle().apply { putBoolean(BUNDLE_KEY_PLACE_PICKED, true) }
        parentFragmentManager.apply {
            setFragmentResult(PLACE_SEARCH_REQUEST_KEY, result)
        }
    }

    // dialog specific shenanigans, useful stackoverflow article:
    // https://stackoverflow.com/questions/9698410/position-of-dialogfragment-in-android
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, 324.toPx.roundToInt())
    }

    private fun setDialogPosition() {
        val window = dialog?.window
        window?.setGravity(Gravity.TOP or Gravity.START)
    }
}