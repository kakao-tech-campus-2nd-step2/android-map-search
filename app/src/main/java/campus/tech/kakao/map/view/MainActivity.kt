package campus.tech.kakao.map.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.model.DBHelper
import campus.tech.kakao.map.viewmodel.PlaceAdapter
import campus.tech.kakao.map.viewmodel.PlaceViewModel
import campus.tech.kakao.map.viewmodel.PlaceViewModelFactory
import campus.tech.kakao.map.viewmodel.SavedPlaceAdapter

class MainActivity : AppCompatActivity() {

    private val viewModel: PlaceViewModel by viewModels { PlaceViewModelFactory(DBHelper(this)) }
    private lateinit var placeAdapter: PlaceAdapter
    private lateinit var savedPlaceAdapter: SavedPlaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchEditText: EditText = findViewById(R.id.searchEditText)
        val clearButton: ImageButton = findViewById(R.id.clearButton)
        val recyclerViewSearchResults: RecyclerView = findViewById(R.id.recyclerViewSearchResults)
        val recyclerViewSavedSearches: RecyclerView = findViewById(R.id.recyclerViewSavedSearches)

        recyclerViewSearchResults.layoutManager = LinearLayoutManager(this)
        recyclerViewSavedSearches.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        viewModel.places.observe(this, Observer { places ->
            placeAdapter = PlaceAdapter(places) { place ->
                viewModel.addSavedQuery(place.placeName)
            }
            recyclerViewSearchResults.adapter = placeAdapter
        })

        viewModel.savedQueries.observe(this, Observer { queries ->
            savedPlaceAdapter = SavedPlaceAdapter(queries) { query ->
                viewModel.removeSavedQuery(query)
            }
            recyclerViewSavedSearches.adapter = savedPlaceAdapter
        })

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrBlank()) {
                    val selectedCategoryCode = "YOUR_SELECTED_CATEGORY_CODE_HERE" // 사용자가 선택한 카테고리 코드
                    viewModel.loadPlaces(s.toString(), selectedCategoryCode)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        clearButton.setOnClickListener {
            searchEditText.text.clear()
        }
    }
}
