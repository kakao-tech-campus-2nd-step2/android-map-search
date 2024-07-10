package campus.tech.kakao.map

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PlaceActivity : AppCompatActivity() {
    private lateinit var placeViewModel: PlaceViewModel
    private lateinit var placeViewModelFactory: PlaceViewModelFactory
    private lateinit var searchEditText: EditText
    private lateinit var removeButton: ImageButton
    private lateinit var emptyMessage: TextView
    private lateinit var placeRecyclerView: RecyclerView
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var placeAdapter: PlaceAdapter
    private lateinit var historyAdapter: SearchHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)

        initializeViews()
        initializeViewModel()
        initializeRecyclerView()

        setUpSearchEditText()
        setUpRemoveButton()
    }

    private fun showEmptyMessage() {
        emptyMessage.visibility = TextView.VISIBLE
        placeRecyclerView.visibility = RecyclerView.GONE
    }

    private fun clearSearchEditText() {
        searchEditText.text.clear()
    }

    private fun showRecyclerView(places: List<Place>) {
        emptyMessage.visibility = TextView.GONE
        placeRecyclerView.visibility = RecyclerView.VISIBLE
        placeAdapter.updateData(places)
    }

    private fun initializeViews() {
        searchEditText = findViewById(R.id.searchEditText)
        removeButton = findViewById(R.id.cancelButton)
        placeRecyclerView = findViewById(R.id.placeRecyclerView)
        emptyMessage = findViewById(R.id.emptyMessage)
        historyRecyclerView = findViewById(R.id.historyRecyclerView)
        placeAdapter =
            PlaceAdapter { place ->
                placeViewModel.saveSearchQuery(place.name)
            }
    }

    private fun initializeViewModel() {
        placeViewModelFactory = PlaceViewModelFactory(this)
        placeViewModel =
            ViewModelProvider(this, placeViewModelFactory).get(PlaceViewModel::class.java)

        historyAdapter =
            SearchHistoryAdapter(emptyList(), placeViewModel::removeSearchQuery) { query ->
                searchEditText.setText(query)
                placeViewModel.searchPlaces(query)
            }

        placeViewModel.places.observe(this) { places ->
            updateUI(places)
        }

        placeViewModel.searchHistory.observe(this) { history ->
            historyAdapter.updateData(history)
        }
        placeViewModel.loadSearchHistory()
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
        placeRecyclerView.layoutManager = LinearLayoutManager(this)
        placeRecyclerView.adapter = placeAdapter

        historyRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        historyRecyclerView.adapter = historyAdapter
    }

    private fun setUpSearchEditText() {
        searchEditText.addTextChangedListener(
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
                    placeViewModel.searchPlaces(p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {
                    //
                }
            },
        )
    }

    private fun setUpRemoveButton() {
        removeButton.setOnClickListener {
            clearSearchEditText()
            showEmptyMessage()
        }
    }
}
