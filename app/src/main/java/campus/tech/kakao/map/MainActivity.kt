package campus.tech.kakao.map

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.viewmodel.SearchViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var resultPlaceDataTextView: TextView
    private lateinit var searchHistoryRecyclerView: RecyclerView
    private lateinit var placeResultRecyclerView: RecyclerView
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var searchHistoryAdapter: SearchHistoryRecyclerViewAdapter
    private lateinit var placeResultAdapter: PlaceResultRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupRecyclerView()
        setupListeners()
        observeViewModel()
        loadInitialData()
    }

    private fun initializeViews() {
        searchEditText = findViewById(R.id.searchEditText)
        clearButton = findViewById(R.id.clearButton)
        searchHistoryRecyclerView = findViewById(R.id.searchHistoryRecyclerView)
        placeResultRecyclerView = findViewById(R.id.placeResultRecyclerView)
        resultPlaceDataTextView = findViewById(R.id.resultPlaceDataTextView)
    }

    private fun setupRecyclerView() {
        searchHistoryAdapter = SearchHistoryRecyclerViewAdapter(mutableListOf(), searchViewModel, searchEditText)
        searchHistoryRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        searchHistoryRecyclerView.adapter = searchHistoryAdapter

        placeResultAdapter = PlaceResultRecyclerViewAdapter(mutableListOf())
        placeResultRecyclerView.layoutManager = LinearLayoutManager(this)
        placeResultRecyclerView.adapter = placeResultAdapter
    }

    private fun setupListeners() {
        searchEditText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handleSearch(v.text.toString().trim())
                true
            } else {
                false
            }
        }

        clearButton.setOnClickListener {
            searchEditText.text.clear()
        }
    }

    private fun handleSearch(keyword: String) {
        if (keyword.isNotEmpty()) {
            searchViewModel.addSearchResult(keyword)
            searchViewModel.searchPlaces(keyword)
        } else {
            showEmptySearchToast()
        }
    }

    private fun showEmptySearchToast() {
        Toast.makeText(this, "검색어를 입력해 주세요", Toast.LENGTH_SHORT).show()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            searchViewModel.searchResults.collect { results ->
                searchHistoryAdapter.updateData(results)
            }
        }

        lifecycleScope.launch {
            searchViewModel.places.collect { places ->
                if (places.isEmpty()) {
                    resultPlaceDataTextView.visibility = View.VISIBLE
                    placeResultRecyclerView.visibility = View.GONE
                } else {
                    resultPlaceDataTextView.visibility = View.GONE
                    placeResultRecyclerView.visibility = View.VISIBLE
                    placeResultAdapter.updateData(places)
                }
            }
        }
    }

    private fun loadInitialData() {
        searchViewModel.getAllSearchResults()
        // searchViewModel.getAllPlaces()       // case: 첫 화면에 모든 장소 출력
    }
}
