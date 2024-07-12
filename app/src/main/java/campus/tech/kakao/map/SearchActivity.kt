package campus.tech.kakao.map

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private val viewModel: SearchViewModel by viewModels {
        ViewModelFactory(applicationContext)
    }

    private val placeAdapter: PlaceAdapter by lazy {
        PlaceAdapter(viewModel.locationList.value ?: emptyList(),
            LayoutInflater.from(this@SearchActivity),
            object :
                PlaceAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    val item = placeAdapter.getItem(position)
                    val searchHistory = SearchHistory(item.placeName)
                    viewModel.saveSearchHistory(searchHistory)
                }
            }
        )
    }

    private val historyAdapter: HistoryAdapter by lazy {
        HistoryAdapter(
            viewModel.searchHistoryList.value ?: emptyList(),
            LayoutInflater.from(this@SearchActivity),
            object : HistoryAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    val item = viewModel.searchHistoryList.value?.get(position)
                    if (item != null) {
                        searchViewBinding.search.setText(item.searchHistory)
                    }
                }
                override fun onXMarkClick(position: Int) {
                    viewModel.deleteSearchHistory(position)
                }
            }
        )
    }

    private lateinit var searchViewBinding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchViewBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(searchViewBinding.root)

        setupRecyclerViews(searchViewBinding)
        setupSearchEditText(searchViewBinding)
        observeViewModel(searchViewBinding)

        searchViewBinding.xmark.setOnClickListener {
            searchViewBinding.search.setText("")
        }
    }

    private fun setupRecyclerViews(mainBinding: ActivitySearchBinding) {
        mainBinding.placeResult.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            adapter = placeAdapter
        }

        mainBinding.searchHistory.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = historyAdapter
        }
    }

    private fun setupSearchEditText(mainBinding: ActivitySearchBinding) {
        val searchEditText = mainBinding.search
        val handler = Handler(Looper.getMainLooper())
        val delayMillis = 800L

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val searchText = s.toString().trim()

                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    viewModel.getPlace(searchText)
                }, delayMillis)
            }
        })
    }

    private fun observeViewModel(mainBinding: ActivitySearchBinding) {
        viewModel.searchHistoryList.observe(this@SearchActivity, Observer {
            historyAdapter.setData(it)
        })
        viewModel.getSearchHistoryList()

        viewModel.locationList.observe(this@SearchActivity, Observer {
            placeAdapter.setData(it)
            mainBinding.emptyMainText.visibility = if (it.isNullOrEmpty()) View.VISIBLE else View.GONE
        })
    }
}