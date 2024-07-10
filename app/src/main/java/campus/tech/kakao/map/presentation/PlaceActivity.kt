package campus.tech.kakao.map.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.data.network.RetrofitObject
import campus.tech.kakao.map.data.repository.PlaceRepositoryImpl
import campus.tech.kakao.map.databinding.ActivityPlaceBinding
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.repository.PlaceRepository

class PlaceActivity : AppCompatActivity() {
    private lateinit var placeViewModel: PlaceViewModel
    private lateinit var placeAdapter: PlaceAdapter
    private lateinit var historyAdapter: SearchHistoryAdapter

    private lateinit var binding: ActivityPlaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViewModel()
        initializeAdapters()
        initializeRecyclerView()

        setUpSearchEditText()
        setUpRemoveButton()
    }

    private fun showEmptyMessage() {
        binding.emptyMessage.visibility = TextView.VISIBLE
        binding.placeRecyclerView.visibility = RecyclerView.GONE
    }

    private fun clearSearchEditText() {
        binding.searchEditText.text.clear()
    }

    private fun showRecyclerView(places: List<Place>) {
        binding.emptyMessage.visibility = TextView.GONE
        binding.placeRecyclerView.visibility = RecyclerView.VISIBLE
        placeAdapter.updateData(places)
    }


    private fun initializeViewModel() {
        val placeRepository = PlaceRepositoryImpl(context = this)

        placeViewModel = ViewModelProvider(
            this,
            PlaceViewModelFactory(placeRepository)
        )[PlaceViewModel::class.java]

        placeViewModel.places.observe(this) { places ->
            updateUI(places)
        }

        placeViewModel.searchHistory.observe(this) { history ->
            historyAdapter.updateData(history)
        }
        placeViewModel.loadSearchHistory()
    }

    private fun updateUI(places: List<Place>) {
        if (places.isEmpty()) {
            showEmptyMessage()
        } else {
            showRecyclerView(places)
        }
    }

    private fun initializeRecyclerView() {
        binding.placeRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.placeRecyclerView.adapter = placeAdapter

        binding.historyRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.historyRecyclerView.adapter = historyAdapter
    }

    private fun initializeAdapters() {
        placeAdapter = PlaceAdapter { place ->
            placeViewModel.saveSearchQuery(place)
        }

        historyAdapter = SearchHistoryAdapter(
            historyList = emptyList(),
            onDeleteClick = { query ->
                placeViewModel.removeSearchQuery(query)
            },
            onItemClick = { query ->
                binding.searchEditText.setText(query)
                placeViewModel.searchPlaces(query)
            }
        )
    }

    private fun setUpSearchEditText() {
        binding.searchEditText.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(
                    p0: CharSequence?, p1: Int, p2: Int, p3: Int,
                ) {
                    if (p0.isNullOrBlank()) {
                        showEmptyMessage()
                        return
                    }
                    placeViewModel.searchPlaces(query = p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {}
            },
        )
    }

    private fun setUpRemoveButton() {
        binding.cancelButton.setOnClickListener {
            clearSearchEditText()
            showEmptyMessage()
        }
    }
}
