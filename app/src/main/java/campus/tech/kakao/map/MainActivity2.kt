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

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        databaseHelper = MyDatabaseHelper(this)

        // 검색어 삭제 버튼 클릭 리스너 추가
        binding.close.setOnClickListener { clearSearchText() }

        // 초기 검색 결과 표시
        searchAndDisplayResults()
    }

    private fun searchAndDisplayResults() {
        val searchText = binding.text.text.toString()

        // 검색어를 데이터베이스에 저장
        databaseHelper.insertSearchData(searchText)

        // 검색 결과 가져오기
        val searchResults = databaseHelper.getSearchResults(searchText)

        // 검색 결과가 없다면 "검색 결과가 없습니다" 메시지 표시
        if (searchResults.isEmpty()) {
            binding.text.visibility = View.VISIBLE
        } else {
            // 검색 결과 화면에 표시
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
            // 검색 결과를 저장할 테이블 생성
            db?.execSQL("CREATE TABLE search_results (id INTEGER PRIMARY KEY, text TEXT)")
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            TODO("Not yet implemented")
        }

        // 검색어 저장
        fun insertSearchData(searchText: String) {
            val db = writableDatabase
            db.execSQL("INSERT INTO search_results (text) VALUES (?)", arrayOf(searchText))
            db.close()
        }

        // 검색어 삭제
        fun deleteSearchData(searchText: String) {
            val db = writableDatabase
            db.execSQL("DELETE FROM search_results WHERE text = ?", arrayOf(searchText))
            db.close()
        }

        // 검색 결과 가져오기
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



