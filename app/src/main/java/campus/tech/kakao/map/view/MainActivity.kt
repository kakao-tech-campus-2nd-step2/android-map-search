package campus.tech.kakao.map.view

//import DBHelper
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
import campus.tech.kakao.map.viewmodel.PlaceAdapter
import campus.tech.kakao.map.viewmodel.PlaceViewModel
import campus.tech.kakao.map.viewmodel.PlaceViewModelFactory
import campus.tech.kakao.map.viewmodel.SavedPlaceAdapter

class MainActivity : AppCompatActivity() {

    /** 생성 완료 후 주석 */
//    private lateinit var dbHelper: DBHelper


    private val viewModel: PlaceViewModel by viewModels { PlaceViewModelFactory(this) }
    private lateinit var placeAdapter: PlaceAdapter
    private lateinit var savedPlaceAdapter: SavedPlaceAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** 생성 완료 후 주석 */
//        dbHelper = DBHelper(this)
//        for (i in 1..20) { dbHelper.insertData("약국$i", "서울시 서대문구$i") }
//        for (i in 1..20) { dbHelper.insertData("카페$i", "서울시 동대문구$i") }


        val searchEditText: EditText = findViewById(R.id.searchEditText)
        val clearButton: ImageButton = findViewById(R.id.clearButton)
        val recyclerViewSearchResults: RecyclerView = findViewById(R.id.recyclerViewSearchResults)
        val recyclerViewSavedSearches: RecyclerView = findViewById(R.id.recyclerViewSavedSearches)

        recyclerViewSearchResults.layoutManager = LinearLayoutManager(this)
        recyclerViewSavedSearches.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        viewModel.places.observe(this, Observer { places ->
            placeAdapter = PlaceAdapter(places) { place ->
                viewModel.addSavedQuery(place.name)
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
                    viewModel.loadPlaces(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        clearButton.setOnClickListener {
            searchEditText.text.clear()
        }
    }
}
