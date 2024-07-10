package campus.tech.kakao.map

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.databinding.ActivityMainBinding
import campus.tech.kakao.map.viewmodel.SearchViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var searchHistoryAdapter: SearchHistoryRecyclerViewAdapter
    private lateinit var placeResultAdapter: PlaceResultRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = searchViewModel
        binding.lifecycleOwner = this

        setupRecyclerView()
        setupListeners()
        loadInitialData()

        // Observe the StateFlow from ViewModel
        lifecycleScope.launch {
            searchViewModel.searchResults.collect { results ->
                Log.d("MainActivity", "Search results updated: $results")
                searchHistoryAdapter.updateData(results)
            }
        }

        lifecycleScope.launch {
            searchViewModel.places.collect { places ->
                Log.d("MainActivity", "Places updated: $places")
                placeResultAdapter.updateData(places)
            }
        }
    }

    private fun setupRecyclerView() {
        searchHistoryAdapter = SearchHistoryRecyclerViewAdapter(
            mutableListOf(),
            binding.searchEditText
        )
        searchHistoryAdapter.setOnClearButtonClickedListener { searchResult ->
            searchViewModel.onClearButtonClickedFromView(searchResult)
        }
        searchHistoryAdapter.setOnItemClickedListener { searchResult ->
            searchViewModel.onItemClickedFromView(searchResult)
        }

        binding.searchHistoryRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.searchHistoryRecyclerView.adapter = searchHistoryAdapter

        placeResultAdapter = PlaceResultRecyclerViewAdapter(mutableListOf())
        binding.placeResultRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.placeResultRecyclerView.adapter = placeResultAdapter
    }

    private fun setupListeners() {
        binding.searchEditText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handleSearch(v.text.toString().trim())
                true
            } else {
                false
            }
        }

        binding.clearButton.setOnClickListener {
            binding.searchEditText.text.clear()
        }
    }

    private fun handleSearch(keyword: String) {
        Log.d("MainActivity", "Search initiated with keyword: $keyword")
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

    private fun loadInitialData() {
        searchViewModel.getAllSearchResults()
    }
}
