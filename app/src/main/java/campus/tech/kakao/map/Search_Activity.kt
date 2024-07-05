package campus.tech.kakao.map

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class Search_Activity : AppCompatActivity() {

    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var savedSearchRecyclerView: RecyclerView
    private lateinit var searchResultAdapter: PlaceAdapter
    private lateinit var savedSearchAdapter: SavedSearchAdapter
    private lateinit var databaseHelper: MyDatabaseHelper
    private lateinit var placeDatabase: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchView = findViewById(R.id.search_text)
        searchRecyclerView = findViewById(R.id.RecyclerVer)
        savedSearchRecyclerView = findViewById(R.id.recyclerHor)

        databaseHelper = MyDatabaseHelper(this)
        placeDatabase = Database(this)

        searchResultAdapter = PlaceAdapter(emptyList())
        savedSearchAdapter = SavedSearchAdapter(this, emptyList()) { searchText ->
            searchAndDisplayResults(searchText)
        }

        searchRecyclerView.layoutManager = LinearLayoutManager(this)
        savedSearchRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        searchRecyclerView.adapter = searchResultAdapter
        savedSearchRecyclerView.adapter = savedSearchAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchText = newText ?: ""
                searchAndDisplayResults(searchText)
                return true
            }
        })

        searchAndDisplayResults("")
    }

    private fun searchAndDisplayResults(searchText: String) {
        val searchResults = placeDatabase.searchPlaces(searchText)
        searchResultAdapter.updateData(searchResults)

        val savedSearches = databaseHelper.getSavedSearches()
        savedSearchAdapter.updateData(savedSearches)
    }

    inner class PlaceAdapter(private var data: List<Map<String, String>>) :
        RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_place_item, parent, false)
            return PlaceViewHolder(view)
        }

        override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
            val place = data[position]
            holder.bind(place)
        }

        override fun getItemCount(): Int = data.size

        fun updateData(newData: List<Map<String, String>>) {
            data = newData
            notifyDataSetChanged()
        }

        inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val nameTextView: TextView = itemView.findViewById(R.id.name)
            private val addressTextView: TextView = itemView.findViewById(R.id.place)
            private val categoryTextView: TextView = itemView.findViewById(R.id.category)

            fun bind(place: Map<String, String>) {
                val name = place[MapContract.COLUMN_NAME] ?: ""
                val address = place[MapContract.COLUMN_ADDRESS] ?: ""
                val category = place[MapContract.COLUMN_CATEGORY] ?: ""

                nameTextView.text = name
                addressTextView.text = address
                categoryTextView.text = category
            }
        }
    }
}



