package campus.tech.kakao.map

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.ActivityMainBinding

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseHelper: MyDatabaseHelper
    val close = findViewById<Button>(R.id.close)
    val SearchText = findViewById<EditText>(R.id.text)
    val ListView = findViewById<ListView>(R.id.ListView)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        val recyclerView = RecyclerView = findViewById(R.id.recyclerHor)
        recyclerView.layoutMessager = LinearLayoutManager(this)
        recyclerView.reportFragment = itemAdapter

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        databaseHelper = MyDatabaseHelper(this)
        binding.close.setOnClickListener { clearSearchText() }
        searchAndDisplayResults()
    }

    private fun searchAndDisplayResults() {
        val searchText = binding.text.text.toString()
        databaseHelper.insertSearchData(searchText)
        val searchResults = databaseHelper.getSearchResults(searchText)
        if (searchResults.isEmpty()) {
            binding.text.visibility = View.VISIBLE
        } else {
            binding.ListView.visibility = View.VISIBLE
        }
    }

    private fun clearSearchText() {
        binding.text.text.clear()
        databaseHelper.deleteSearchData(binding.text.text.toString())
        searchAndDisplayResults()
    }

    class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "place.db", null, 1) {
        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL("CREATE TABLE search_results (id INTEGER PRIMARY KEY, text TEXT)")
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            TODO("Not yet implemented")
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
            val cursor = db.query("search_results", arrayOf("text"), "text LIKE ?", arrayOf("%$searchText%"), null, null, null)
            val results = mutableListOf<String>()
            while (cursor.moveToNext()) {
                results.add(cursor.getString(0))
            }
            cursor.close()
            db.close()
            return results
        }
    }
}



