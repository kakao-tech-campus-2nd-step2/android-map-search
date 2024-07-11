package campus.tech.kakao.View

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.R
import campus.tech.kakao.Model.ResultSearchKeyword
import campus.tech.kakao.Model.RetrofitClient
import campus.tech.kakao.Model.SQLiteDb
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var noResultTextView: TextView
    private lateinit var adapter: PlacesAdapter
    private lateinit var databaseHelper: SQLiteDb
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var mapView: MapView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapView = findViewById(R.id.KakaoMapView)
        searchView = findViewById(R.id.searchView)
        recyclerView = findViewById(R.id.recyclerView)
        noResultTextView = findViewById(R.id.noResultTextView)
        historyRecyclerView = findViewById(R.id.historyRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        databaseHelper = SQLiteDb(this)

        historyAdapter = HistoryAdapter(mutableListOf()) { id ->
            databaseHelper.deleteFromSelectedData(id)
            historyAdapter.removeItemById(id)
            updateHistoryData()
        }
        historyRecyclerView.adapter = historyAdapter

        adapter = PlacesAdapter(listOf()) { name ->
            val id = databaseHelper.insertIntoSelectedData(name)
            updateHistoryData()
        }
        recyclerView.adapter = adapter
        setupSearchView()
        updateHistoryData()

        startMap()
    }

    override fun onBackPressed() {
        if (mapView.visibility == View.GONE) {
            noResultTextView.visibility = View.GONE
            recyclerView.visibility = View.GONE
            mapView.visibility = View.VISIBLE
        } else {
            super.onBackPressed()
        }
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    showNoResultMessage()
                    adapter.updateData(emptyList())
                } else {
                    searchPlaces(newText)
                }
                return true
            }
        })

        // SearchView에 포커스 이벤트 리스너 추가
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // SearchView가 포커스를 받으면 recyclerView를 보이도록 설정
                recyclerView.visibility = View.VISIBLE
                historyRecyclerView.visibility = View.VISIBLE
                noResultTextView.visibility = View.VISIBLE
                mapView.visibility = View.GONE
            } else {
                // SearchView가 포커스를 잃으면 기본 상태로 설정
                recyclerView.visibility = View.GONE
                historyRecyclerView.visibility = View.GONE
                noResultTextView.visibility = View.GONE
                mapView.visibility = View.VISIBLE
            }
        }
    }

    private fun searchPlaces(query: String) {
        val apiKey = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"

        RetrofitClient.instance.searchPlaces(apiKey, query).enqueue(object : Callback<ResultSearchKeyword> {
            override fun onResponse(call: Call<ResultSearchKeyword>, response: Response<ResultSearchKeyword>) {
                if (response.isSuccessful && response.body() != null) {
                    val places = response.body()?.documents ?: emptyList()
                    if (places.isEmpty()) {
                        showNoResultMessage()
                    } else {
                        hideNoResultMessage()
                        adapter.updateData(places)
                    }
                } else {
                    showNoResultMessage()
                }
            }

            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                showNoResultMessage()
            }
        })
    }

    private fun showNoResultMessage() {
        noResultTextView.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        mapView.visibility = View.GONE
    }

    private fun hideNoResultMessage() {
        noResultTextView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        historyRecyclerView.visibility = View.VISIBLE
    }

    private fun updateHistoryData() {
        historyAdapter.updateData(databaseHelper.getAllSelectedData())
        historyAdapter.notifyDataSetChanged()
    }

    private fun startMap() {
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // 지도 API가 정상적으로 종료될 때 호출됨
            }

            override fun onMapError(error: Exception) {
                // 지도 API에서 오류가 발생할 때 호출됨
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                // 지도 API가 정상적으로 실행될 때 호출됨
            }
        })
    }

    public override fun onResume() {
        super.onResume()
        if (::mapView.isInitialized) {
            mapView.resume()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (::mapView.isInitialized) {
            mapView.pause()
        }
    }
}