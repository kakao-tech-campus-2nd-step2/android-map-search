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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Search_Activity : AppCompatActivity() {

    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var savedSearchRecyclerView: RecyclerView
    private lateinit var searchResultAdapter: PlaceAdapter
    private lateinit var savedSearchAdapter: MyDatabaseHelper.SavedSearchAdapter
    private lateinit var databaseHelper: MyDatabaseHelper
    private lateinit var placeDatabase: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchRecyclerView = findViewById(R.id.RecyclerVer)
        savedSearchRecyclerView = findViewById(R.id.recyclerHor)

        databaseHelper = MyDatabaseHelper(this)
        placeDatabase = Database(this)

        searchResultAdapter = PlaceAdapter(emptyList())
        savedSearchAdapter = MyDatabaseHelper.SavedSearchAdapter(this, emptyList()) { searchText ->
            searchAndDisplayResults(searchText)
        }

        searchRecyclerView.layoutManager = LinearLayoutManager(this)
        savedSearchRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        searchRecyclerView.adapter = searchResultAdapter
        savedSearchRecyclerView.adapter = savedSearchAdapter

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
            val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_view, parent, false)
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
            private val deleteButton: Button = itemView.findViewById(R.id.delete)

            fun bind(place: Map<String, String>) {
                val name = place["name"] ?: ""
                nameTextView.text = name
                deleteButton.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val updatedData = data.toMutableList()
                        updatedData.removeAt(position)
                        data = updatedData.toList()
                        notifyDataSetChanged()
                    }
                }

                itemView.setOnClickListener {
                    val searchText = "$name"
                    databaseHelper.insertSearchData(searchText)
                    val savedSearches = databaseHelper.getSavedSearches()
                    savedSearchAdapter.updateData(savedSearches)
                }
            }
        }
    }
}





