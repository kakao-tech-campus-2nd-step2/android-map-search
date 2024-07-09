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
import campus.tech.kakao.map.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory(applicationContext)
    }

    private val placeAdapter: PlaceAdapter by lazy {
        PlaceAdapter(locationList,
            LayoutInflater.from(this@MainActivity),
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
            LayoutInflater.from(this@MainActivity),
            object : HistoryAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    val item = viewModel.searchHistoryList.value?.get(position)
                    if (item != null) {
                        mainBinding.search.setText(item.searchHistory)
                    }
                }
                override fun onXMarkClick(position: Int) {
                    viewModel.deleteSearchHistory(position)
                }
            }
        )
    }

    private lateinit var mainBinding: ActivityMainBinding


    private var placeList: List<Place> = emptyList()

    private var locationList: List<Document> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        if (placeList.isNullOrEmpty()) {
            mainBinding.emptyMainText.visibility = View.VISIBLE
        } else {
            mainBinding.emptyMainText.visibility = View.GONE
        }

        setupRecyclerViews(mainBinding)
        setupSearchEditText(mainBinding)
        observeViewModel(mainBinding)

        mainBinding.xmark.setOnClickListener {
            mainBinding.search.setText("")
        }
    }

    private fun setupRecyclerViews(mainBinding: ActivityMainBinding) {
        mainBinding.placeResult.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = placeAdapter
        }

        mainBinding.searchHistory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = historyAdapter
        }
    }

    private fun setupSearchEditText(mainBinding: ActivityMainBinding) {
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

    private fun observeViewModel(mainBinding: ActivityMainBinding) {
        viewModel.searchHistoryList.observe(this@MainActivity, Observer {
            historyAdapter.setData(it)
        })
        viewModel.getSearchHistoryList()

        viewModel.locationList.observe(this@MainActivity, Observer {
            placeAdapter.setData(it)
            mainBinding.emptyMainText.visibility = if (it.isNullOrEmpty()) View.VISIBLE else View.GONE
        })
    }
}
