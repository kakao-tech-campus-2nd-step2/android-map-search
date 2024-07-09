package campus.tech.kakao.map

import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Search_Activity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var savedSearchRecyclerView: RecyclerView
    private lateinit var searchResultAdapter: PlaceAdapter
    private lateinit var savedSearchAdapter: SavedSearchAdapter
    private lateinit var databaseHelper: MyDatabaseHelper
    private lateinit var noResultTextView : TextView
    private lateinit var kakaoApiService: KakaoApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchView = findViewById(R.id.search_text)
        searchRecyclerView = findViewById(R.id.RecyclerVer)
        savedSearchRecyclerView = findViewById(R.id.recyclerHor)
        noResultTextView = findViewById(R.id.nosearch)

        databaseHelper = MyDatabaseHelper(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        kakaoApiService = retrofit.create(KakaoApiService::class.java)

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
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = kakaoApiService.searchAddress(searchText)
                val places = response.documents.map { document ->
                    mapOf(
                        MapContract.COLUMN_NAME to document.placeName,
                        MapContract.COLUMN_ADDRESS to document.addressName,
                        MapContract.COLUMN_CATEGORY to document.categoryName
                    )
                }

                savePlacesToDatabase(places)

                if (places.isEmpty()) {
                    searchRecyclerView.visibility = RecyclerView.GONE
                    noResultTextView.visibility = View.VISIBLE
                } else {
                    searchRecyclerView.visibility = RecyclerView.VISIBLE
                    noResultTextView.visibility = View.GONE
                    searchResultAdapter.updateData(places)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun savePlacesToDatabase(places: List<Map<String, String>>) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val db = databaseHelper.writableDatabase
                db.beginTransaction()
                try {
                    db.delete(MapContract.TABLE_CAFE, null, null)
                    db.delete(MapContract.TABLE_PHARMACY, null, null)

                    places.forEach { place ->
                        if (place[MapContract.COLUMN_CATEGORY] == "카페") {
                            db.insert(MapContract.TABLE_CAFE, null, getContentValues(place))
                        } else if (place[MapContract.COLUMN_CATEGORY] == "약국") {
                            db.insert(MapContract.TABLE_PHARMACY, null, getContentValues(place))
                        }
                    }

                    db.setTransactionSuccessful()
                } finally {
                    db.endTransaction()
                    db.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getContentValues(place: Map<String, String>): ContentValues {
        return ContentValues().apply {
            put(MapContract.COLUMN_NAME, place[MapContract.COLUMN_NAME])
            put(MapContract.COLUMN_ADDRESS, place[MapContract.COLUMN_ADDRESS])
            put(MapContract.COLUMN_CATEGORY, place[MapContract.COLUMN_CATEGORY])
        }
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
