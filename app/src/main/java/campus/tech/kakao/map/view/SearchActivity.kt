package campus.tech.kakao.map.view

import android.content.ContentValues
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.databinding.ActivitySearchBinding
import campus.tech.kakao.map.adapter.keyword.KeywordAdapter
import campus.tech.kakao.map.adapter.search.SearchAdapter
import campus.tech.kakao.map.repository.location.LocationContract
import campus.tech.kakao.map.repository.location.LocationDbHelper
import campus.tech.kakao.map.viewmodel.keyword.KeywordViewModel
import campus.tech.kakao.map.viewmodel.keyword.KeywordViewModelFactory
import campus.tech.kakao.map.viewmodel.search.SearchViewModel
import campus.tech.kakao.map.viewmodel.search.SearchViewModelFactory

class SearchActivity : AppCompatActivity() {
    private val dbHelper by lazy { LocationDbHelper(this) }
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var keywordViewModel: KeywordViewModel
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var keywordAdapter: KeywordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModels()
        setupBinding()
        makeLocationData()
        setupSearchResultViewRecyclerView()
        setupKeywordHistoryView()
        setupEditText()
        bindingSearchResult()
        bindingKeywordHistory()
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }

    private fun setupViewModels() {
        searchViewModel =
            ViewModelProvider(this, SearchViewModelFactory(this))[SearchViewModel::class.java]
        keywordViewModel =
            ViewModelProvider(this, KeywordViewModelFactory(this))[KeywordViewModel::class.java]

    }

    private fun setupBinding() {
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun makeLocationData() {
        if (isCreatedLocationData()) return

        val db = dbHelper.writableDatabase
        ContentValues().apply {
            for (i in 1..15) {
                put(LocationContract.PLACE_NAME, "약국$i")
                put(LocationContract.ADDRESS_NAME, "서울 강남구 대치동 $i")
                put(LocationContract.CATEGORY_GROUP_NAME, "약국")
                db.insert(LocationContract.TABLE_NAME, null, this)
            }
            for (i in 1..15) {
                put(LocationContract.PLACE_NAME, "카페$i")
                put(LocationContract.ADDRESS_NAME, "서울 성동구 성수동 $i")
                put(LocationContract.CATEGORY_GROUP_NAME, "카페")
                db.insert(LocationContract.TABLE_NAME, null, this)
            }
        }

        checkLocationDataCreated()
    }

    private fun isCreatedLocationData(): Boolean {
        val sharedPreference = getSharedPreferences("db_update", MODE_PRIVATE)
        return sharedPreference.getBoolean("exist", false)
    }

    private fun checkLocationDataCreated() {
        val sharedPreference = getSharedPreferences("db_update", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreference.edit()
        editor.putBoolean("exist", true)
        editor.apply()
    }

    private fun setupSearchResultViewRecyclerView() {
        searchAdapter = SearchAdapter(keywordViewModel)
        binding.searchResultView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchAdapter
        }

        val dividerItemDecoration = DividerItemDecoration(
            binding.searchResultView.context,
            (binding.searchResultView.layoutManager as LinearLayoutManager).orientation
        )
        binding.searchResultView.addItemDecoration(dividerItemDecoration)
    }

    private fun setupKeywordHistoryView() {
        keywordAdapter = KeywordAdapter(keywordViewModel)
        binding.keywordHistoryView.apply {
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = keywordAdapter
        }
        keywordViewModel.readKeywordHistory()
    }

    private fun setupEditText() {
        binding.searchTextInput.setOnFocusChangeListener { _, _ ->
            keywordViewModel.readKeywordHistory()
        }

        binding.searchTextInput.doAfterTextChanged {
            binding.searchTextInput.text.toString().let {
                searchViewModel.searchLocationData(it)
            }
        }

        binding.deleteTextInput.setOnClickListener {
            binding.searchTextInput.setText("")
        }
    }

    private fun bindingSearchResult() {
        searchViewModel.items.observe(this) {
            if (it.isEmpty()) {
                binding.searchResultView.visibility = View.GONE
                binding.emptyView.visibility = View.VISIBLE
            } else {
                searchAdapter.submitList(it)
                binding.searchResultView.visibility = View.VISIBLE
                binding.emptyView.visibility = View.GONE
            }
        }
    }

    private fun bindingKeywordHistory() {
        keywordViewModel.keyword.observe(this) {
            keywordAdapter.submitList(it)
        }
    }
}