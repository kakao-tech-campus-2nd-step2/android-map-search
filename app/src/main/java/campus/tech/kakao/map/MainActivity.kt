package campus.tech.kakao.map

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.viewmodel.SearchViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var searchHistoryRecyclerView: RecyclerView
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var adapter: SearchHistoryRecyclerViewAdapter

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
    }

    private fun setupRecyclerView() {
        adapter = SearchHistoryRecyclerViewAdapter(mutableListOf(), searchViewModel)
        searchHistoryRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        searchHistoryRecyclerView.adapter = adapter
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
                adapter.updateData(results)
            }
        }
    }

    private fun loadInitialData() {
        searchViewModel.getAllSearchResults()
        searchViewModel.getAllPlaces()
    }
}
