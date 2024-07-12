package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.dto.Place
import campus.tech.kakao.map.repository.KakaoRepository

class SearchActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var resultRecyclerView: RecyclerView
    private lateinit var searchHistoryRecyclerView: RecyclerView
    private lateinit var noResults: TextView
    private lateinit var resultRecyclerViewAdapter: ResultRecyclerViewAdapter
    private lateinit var searchHistoryRecyclerViewAdapter: SearchHistoryRecyclerViewAdapter
    private lateinit var placeList: List<Place>
    private lateinit var searchHistoryList: MutableList<Place>
    private lateinit var kakaoRepository: KakaoRepository
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchView = findViewById(R.id.search_view)
        resultRecyclerView = findViewById(R.id.recycler_view)
        searchHistoryRecyclerView = findViewById(R.id.horizontal_recycler_view)
        noResults = findViewById(R.id.no_results)
        backButton = findViewById(R.id.back_button)

        placeList = emptyList()
        kakaoRepository = (application as KyleMaps).kakaoRepository

        val searchHistoryDB = SearchHistoryDBHelper(this)
        searchHistoryList = searchHistoryDB.getAllSearchHistory()

        resultRecyclerViewAdapter = ResultRecyclerViewAdapter(
            places = emptyList(),
            onItemClick = { place ->
                searchHistoryDB.insertSearchHistory(place)
                updateSearchHistoryRecyclerView(place)
            }
        )
        resultRecyclerView.adapter = resultRecyclerViewAdapter
        resultRecyclerView.layoutManager = LinearLayoutManager(this)

        searchHistoryRecyclerViewAdapter = SearchHistoryRecyclerViewAdapter(
            searchHistory = searchHistoryList,
            onItemClick = { index ->
                searchView.setQuery(searchHistoryList[index].placeName, true)
                searchView.clearFocus()
                searchView.isIconified = false
            },
            onItemDelete = { index ->
                if (index >= 0 && index < searchHistoryList.size) {
                    val deletedItemName = searchHistoryList[index].placeName
                    searchHistoryList.removeAt(index)
                    searchHistoryDB.deleteSearchHistoryByName(deletedItemName)
                    searchHistoryRecyclerViewAdapter.notifyItemRemoved(index)
                }
            }
        )
        searchHistoryRecyclerView.adapter = searchHistoryRecyclerViewAdapter
        searchHistoryRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchPlaces(it) }
                return true
            }
        })

        backButton.setOnClickListener {
            goBackToMap()
        }
    }

    private fun searchPlaces(query: String) {

        kakaoRepository.searchPlaces(query) { places ->
            runOnUiThread {
                placeList = places
                resultRecyclerViewAdapter.setPlaces(places)
                showNoResultsMessage(places.isEmpty())
            }
        }
    }


    private fun showNoResultsMessage(show: Boolean) {
        if (show) {
            noResults.visibility = TextView.VISIBLE
            resultRecyclerView.visibility = RecyclerView.GONE
        } else {
            noResults.visibility = TextView.GONE
            resultRecyclerView.visibility = RecyclerView.VISIBLE
        }
    }

    private fun updateSearchHistoryRecyclerView(place: Place) {
        searchHistoryList.add(place)
        searchHistoryRecyclerViewAdapter.notifyDataSetChanged()
    }

    private fun goBackToMap() {
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
    }
}
