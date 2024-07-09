package campus.tech.kakao.map

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchView = findViewById(R.id.searchView)
        recyclerView = findViewById(R.id.recyclerView)
        noResultTextView = findViewById(R.id.noResultTextView)
        historyRecyclerView = findViewById(R.id.historyRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)

        databaseHelper = SQLiteDb(this)
        historyRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

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
                showNoResultMessage() } }) }

    private fun showNoResultMessage() {
        noResultTextView.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun hideNoResultMessage() {
        noResultTextView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun updateHistoryData() {
        historyAdapter.updateData(databaseHelper.getAllSelectedData())
    }
}
