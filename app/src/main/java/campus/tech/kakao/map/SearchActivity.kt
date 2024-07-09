package campus.tech.kakao.map

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var resultRecyclerView: RecyclerView
    private lateinit var searchHistoryRecyclerView: RecyclerView
    private lateinit var noResults: TextView
    private lateinit var resultRecyclerViewAdapter: ResultRecyclerViewAdapter
    private lateinit var searchHistoryRecyclerViewAdapter: SearchHistoryRecyclerViewAdapter
    private lateinit var placeList: List<Place>
    private lateinit var searchHistoryList: MutableList<Place>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchView = findViewById(R.id.search_view)
        resultRecyclerView = findViewById(R.id.recycler_view)
        searchHistoryRecyclerView = findViewById(R.id.horizontal_recycler_view)
        noResults = findViewById(R.id.no_results)

        val placeDB = PlaceDBHelper(this)
        val searchHistoryDB = SearchHistoryDBHelper(this)

        placeList = placeDB.getAllPlaces()
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
                searchView.setQuery(searchHistoryList[index].name, true)
                searchView.clearFocus()

                searchView.isIconified = false
            },
            onItemDelete = { index ->
                if (index >= 0 && index < searchHistoryList.size) {
                    val deletedItemName = searchHistoryList[index].name
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
                filterPlaces(newText)
                return true
            }
        })
    }

    private fun filterPlaces(query: String?) {
        val filteredList = if (query.isNullOrBlank()) emptyList() else placeList.filter { place -> matchesQuery(place, query) }
        resultRecyclerViewAdapter.setPlaces(filteredList)
        showNoResultsMessage(filteredList.isEmpty())
    }

    private fun matchesQuery(place: Place, searchQuery: String): Boolean {
        val queryLowercase = searchQuery.lowercase()
        return place.name.lowercase().contains(queryLowercase) ||
                place.category.lowercase().contains(queryLowercase) ||
                place.address.lowercase().contains(queryLowercase)
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
}

