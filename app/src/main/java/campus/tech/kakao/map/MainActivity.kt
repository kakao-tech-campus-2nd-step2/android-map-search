package campus.tech.kakao.map

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import campus.tech.kakao.map.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {



    private lateinit var database: StoreDatabase
    private lateinit var storeDao: StoreDao
    private lateinit var storeAdapter: StoreAdapter
    private lateinit var savedSearchAdapter: SavedSearchAdapter
    private lateinit var binding: ActivityMainBinding
    private val storeViewModel: StoreViewModel by viewModels { StoreViewModelFactory(storeDao) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Room 데이터베이스 초기화 및 임시 데이터 삽입
        initDatabase()

        // RecyclerView 설정
        setupRecyclerView()

        // EditText 텍스트 변경 이벤트 처리
        setupSearchEditText()

        // 상단 X 버튼 클릭 이벤트 처리
        binding.clearBtn.setOnClickListener {
            clearSearch()
        }

        // 저장된 검색어 RecyclerView 설정
        setupSavedSearchRecyclerView()
    }





    /**
     * 데이터베이스 파일이 존재하는지 확인하고
     * 데이터베이스 초기화
     * 데이터베이스가 존재하지 않는 경우에만 샘플 데이터 삽입
     */
    private fun initDatabase() {
        val dbFile = applicationContext.getDatabasePath("store-database")
        val dbExists = dbFile.exists()

        database = Room.databaseBuilder(
            applicationContext,
            StoreDatabase::class.java,
            "store-database"
        ).build()
        storeDao = database.storeDao()

        if (!dbExists) {
            lifecycleScope.launch(Dispatchers.IO) {
                storeDao.insert(StoreEntity(name = "약국1", location = "서울시 성동구1"))
                storeDao.insert(StoreEntity(name = "약국2", location = "서울시 성동구2"))
                storeDao.insert(StoreEntity(name = "약국3", location = "서울시 성동구3"))
                storeDao.insert(StoreEntity(name = "약국4", location = "서울시 성동구4"))
                storeDao.insert(StoreEntity(name = "약국5", location = "서울시 성동구5"))
                storeDao.insert(StoreEntity(name = "약국6", location = "서울시 성동구6"))
                storeDao.insert(StoreEntity(name = "약국7", location = "서울시 성동구7"))
                storeDao.insert(StoreEntity(name = "약국8", location = "서울시 성동구8"))
                storeDao.insert(StoreEntity(name = "약국9", location = "서울시 성동구9"))
                storeDao.insert(StoreEntity(name = "약국10", location = "서울시 성동구10"))
                storeDao.insert(StoreEntity(name = "약국11", location = "서울시 성동구11"))
                storeDao.insert(StoreEntity(name = "약국12", location = "서울시 성동구12"))
                storeDao.insert(StoreEntity(name = "약국13", location = "서울시 성동구13"))
                storeDao.insert(StoreEntity(name = "약국14", location = "서울시 성동구14"))
                storeDao.insert(StoreEntity(name = "약국15", location = "서울시 성동구15"))
                storeDao.insert(StoreEntity(name = "약국16", location = "서울시 성동구16"))
                storeDao.insert(StoreEntity(name = "약국17", location = "서울시 성동구17"))
                storeDao.insert(StoreEntity(name = "약국18", location = "서울시 성동구18"))
                storeDao.insert(StoreEntity(name = "약국19", location = "서울시 성동구19"))
                storeDao.insert(StoreEntity(name = "약국20", location = "서울시 성동구20"))
                storeDao.insert(StoreEntity(name = "카페1", location = "서울시 성동구1"))
                storeDao.insert(StoreEntity(name = "카페2", location = "서울시 성동구2"))
                storeDao.insert(StoreEntity(name = "카페3", location = "서울시 성동구3"))
                storeDao.insert(StoreEntity(name = "카페4", location = "서울시 성동구4"))
                storeDao.insert(StoreEntity(name = "카페5", location = "서울시 성동구5"))
                storeDao.insert(StoreEntity(name = "카페6", location = "서울시 성동구6"))
                storeDao.insert(StoreEntity(name = "카페7", location = "서울시 성동구7"))
                storeDao.insert(StoreEntity(name = "카페8", location = "서울시 성동구8"))
                storeDao.insert(StoreEntity(name = "카페9", location = "서울시 성동구9"))
                storeDao.insert(StoreEntity(name = "카페10", location = "서울시 성동구10"))
            }
        }
    }




    private fun setupRecyclerView() {
        storeAdapter = StoreAdapter { store ->
            storeViewModel.saveSearch(store)
        }

        binding.recyclerViewSearchResults.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storeAdapter
        }

        storeViewModel.searchResults.observe(this) { results ->
            storeAdapter.updateSearchResults(results)
            binding.noResultsTextView.visibility = if (results.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupSavedSearchRecyclerView() {
        savedSearchAdapter = SavedSearchAdapter { store ->
            storeViewModel.removeSavedSearch(store)
        }

        binding.recyclerViewSavedSearches.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
            adapter = savedSearchAdapter
        }

        storeViewModel.savedSearches.observe(this) { savedSearches ->
            savedSearchAdapter.updateSavedSearches(savedSearches)
        }

    }

    private fun setupSearchEditText() {
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            performSearch(text.toString())
        }

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(binding.searchEditText.text.toString())
                true
            } else {
                false
            }
        }
    }

    private fun performSearch(query: String) {
        if (query.isNotEmpty()) {
            storeViewModel.search(query)
        } else {
            clearSearch()
        }
    }

    private fun clearSearch() {
        binding.searchEditText.text.clear()
        resetRecyclerView()
    }

    private fun resetRecyclerView() {
        storeAdapter.updateSearchResults(emptyList())
        binding.noResultsTextView.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        database.close()
    }
}
