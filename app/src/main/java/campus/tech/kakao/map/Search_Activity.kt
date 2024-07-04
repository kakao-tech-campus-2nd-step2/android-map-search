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
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class Search_Activity : AppCompatActivity() {
    private lateinit var databaseHelper: MyDatabaseHelper
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var savedSearchRecyclerView: RecyclerView
    private lateinit var searchText: androidx.appcompat.widget.SearchView
    private lateinit var savedSearchAdapter: MyDatabaseHelper.SearchAdapter
    private lateinit var searchResultAdapter: MyDatabaseHelper.SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        databaseHelper = MyDatabaseHelper(this)
        searchText = findViewById(R.id.search_text)
        searchRecyclerView = findViewById(R.id.RecyclerVer)
        savedSearchRecyclerView = findViewById(R.id.recyclerHor)

        savedSearchAdapter = MyDatabaseHelper.SearchAdapter(this, emptyList()) { searchText ->
            searchAndDisplayResults(searchText)
        }

        searchResultAdapter = MyDatabaseHelper.SearchAdapter(this, emptyList()) { searchText ->
            searchAndDisplayResults(searchText)
        }
        searchRecyclerView.adapter = searchResultAdapter
        savedSearchRecyclerView.adapter = savedSearchAdapter

        searchText.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchAndDisplayResults(query ?: "")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchAndDisplayResults(newText ?: "")
                return false
            }
        })
    }

    private fun searchAndDisplayResults(searchText: String) {
        val searchResults = databaseHelper.getSearchResults(searchText)
        searchRecyclerView.visibility = if (searchResults.isEmpty()) View.GONE else View.VISIBLE
        searchResultAdapter.updateData(searchResults)

        val savedSearches = databaseHelper.getSavedSearches()
        savedSearchRecyclerView.visibility = if (savedSearches.isEmpty()) View.GONE else View.VISIBLE
        savedSearchAdapter.updateData(savedSearches)
    }
}

class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "History.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE search_results (id INTEGER PRIMARY KEY, text TEXT)")
        db?.execSQL("CREATE TABLE saved_searches (id INTEGER PRIMARY KEY, text TEXT)")
        db?.execSQL("CREATE TABLE cafe (id INTEGER PRIMARY KEY, name TEXT, location TEXT)")
        addDummySearchData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS search_results")
        db?.execSQL("DROP TABLE IF EXISTS saved_searches")
        db?.execSQL("DROP TABLE IF EXISTS cafe")
        onCreate(db)
    }

    private fun addDummySearchData(db: SQLiteDatabase?) {
        val dummySearchData = listOf(
            "Android", "Kotlin", "Kakao", "약국", "카페", "아무거나", "일단 테스트"
        )
        for (searchText in dummySearchData) {
            db?.execSQL("INSERT INTO search_results (text) VALUES (?)", arrayOf(searchText))
        }
    }

    fun insertSearchData(searchText: String) {
        val db = writableDatabase
        db.execSQL("INSERT INTO search_results (text) VALUES (?)", arrayOf(searchText))
        db.close()
    }

    fun deleteSearchData(searchText: String) {
        val db = writableDatabase
        db.execSQL("DELETE FROM search_results WHERE text = ?", arrayOf(searchText))
        db.close()
    }

    fun getSearchResults(searchText: String): List<String> {
        val db = readableDatabase
        val cursor = db.query(
            "search_results",
            arrayOf("text"),
            "text LIKE ?",
            arrayOf("%$searchText%"),
            null,
            null,
            null
        )
        val results = mutableListOf<String>()
        while (cursor.moveToNext()) {
            results.add(cursor.getString(0))
        }
        cursor.close()
        db.close()
        return results
    }

    fun getSavedSearches(): List<String> {
        val db = readableDatabase
        val cursor = db.query("saved_searches", arrayOf("text"), null, null, null, null, null)
        val results = mutableListOf<String>()
        while (cursor.moveToNext()) {
            results.add(cursor.getString(0))
        }
        cursor.close()
        db.close()
        return results
    }

    class SearchAdapter(
        private val context: Context,
        private var data: List<String>,
        private val onItemClick: (String) -> Unit
    ) : RecyclerView.Adapter<SearchItemViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): SearchItemViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.activity_item_view, parent, false)
            return SearchItemViewHolder(view, onItemClick)
        }

        override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
            holder.bind(data[position])
        }

        override fun getItemCount() = data.size

        fun updateData(newData: List<String>) {
            data = newData
            notifyDataSetChanged()
        }
    }

    class SearchItemViewHolder(itemView: View, private val onItemClick: (String) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.result)
        private val deleteButton: Button = itemView.findViewById(R.id.delete)

        fun bind(searchText: String) {
            textView.text = searchText
            deleteButton.setOnClickListener {
                onItemClick(searchText)
            }
        }
    }
}




