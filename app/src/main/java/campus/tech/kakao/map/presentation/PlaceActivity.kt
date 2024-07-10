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

    private lateinit var binding: ActivityPlaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViewModel()
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
        val placeRepository = PlaceRepositoryImpl()

        placeViewModel = ViewModelProvider(
            this,
            PlaceViewModelFactory(placeRepository)
        ).get(PlaceViewModel::class.java)
        placeViewModel.places.observe(this) { places ->
            Log.d("PlaceActivity", "Places observed: $places")
            updateUI(places)
        }
    }

    private fun updateUI(place: List<Place>) {
        placeViewModel.places.observe(this) { places ->
            if (places.isEmpty()) {
                showEmptyMessage()
            } else {
                showRecyclerView(places)
            }
        }
    }

    private fun initializeRecyclerView() {
        placeAdapter = PlaceAdapter { place ->
            placeViewModel.saveSearchQuery(place.placeName)
        }
        binding.placeRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.placeRecyclerView.adapter = placeAdapter
    }

    private fun setUpSearchEditText() {
        binding.searchEditText.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    p0: CharSequence?,
                    p1: Int,
                    p2: Int,
                    p3: Int,
                ) {
                    //
                }

                override fun onTextChanged(
                    p0: CharSequence?,
                    p1: Int,
                    p2: Int,
                    p3: Int,
                ) {
                    Log.d("PlaceActivity", "Search query: ${p0.toString()}")
                    placeViewModel.searchPlaces(query = p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {
                    //
                }
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
