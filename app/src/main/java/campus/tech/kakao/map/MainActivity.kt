package campus.tech.kakao.map

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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
        databaseHelper = SQLiteDb(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PlacesAdapter(listOf()) { name ->
            databaseHelper.insertIntoSelectedData(name)
            updateHistory()
        }
        recyclerView.adapter = adapter

        historyRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val historyList = databaseHelper.getAllSelectedData()
        historyAdapter = HistoryAdapter(historyList.toMutableList()) { id ->
            val deletedRows = databaseHelper.deleteFromSelectedData(id)
            if (deletedRows > 0) {
                historyAdapter.removeItemById(id)
            } else { }
        }
        historyRecyclerView.adapter = historyAdapter

        setupSearchView()
        checkRun()
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

    private fun checkRun() {
        val checkRun = getSharedPreferences("", Context.MODE_PRIVATE)
            .getBoolean("First", true)
        if (checkRun) {
            insertData()
            getSharedPreferences("", Context.MODE_PRIVATE).edit().putBoolean("First", false).apply()
            databaseHelper.logAllData()
        }
    }

    private fun insertData() {
        val dbHelper = SQLiteDb(this)
        for (i in 1..10) {
            dbHelper.insertData("카페 $i", "서울 성동구 성수동 $i", "카페")
            dbHelper.insertData("약국 $i", "서울 강남구 대치동 $i", "약국")
        }
    }

    private fun searchPlaces(query: String) {
        val places = getPlacesFromDatabase(query)
        if (places.isEmpty()) {
            showNoResultMessage()
            recyclerView.visibility = View.GONE
        } else {
            hideNoResultMessage()
            recyclerView.visibility = View.VISIBLE
            adapter.updateData(places)
        }
    }

    private fun getPlacesFromDatabase(query: String): List<Place> {
        return databaseHelper.getAllData().filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.address.contains(query, ignoreCase = true) ||
                    it.category.contains(query, ignoreCase = true)
        }
    }

    private fun showNoResultMessage() {
        noResultTextView.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun hideNoResultMessage() {
        noResultTextView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun updateHistory() {
        val history = databaseHelper.getAllSelectedData()
        historyAdapter.updateData(history)
    }
}
