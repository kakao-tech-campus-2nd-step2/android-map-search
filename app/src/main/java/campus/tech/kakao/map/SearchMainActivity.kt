package campus.tech.kakao.map

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView

class SearchMainActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var noResults: TextView
    private lateinit var dbHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchView = findViewById(R.id.search_view)
        noResults = findViewById(R.id.no_results)
        dbHelper = DBHelper(this, 1)
        dbHelper.insert("카카오 카페", "카페", "금정구 1")
        dbHelper.insert("카카오 식당", "식당", "금정구 2")
        dbHelper.insert("카카오 포차", "주점", "금정구 3")
        dbHelper.insert("테크 카페", "카페", "금정구 4")
        dbHelper.insert("테크 식당", "식당", "금정구 5")
        dbHelper.insert("테크 포차", "주점", "금정구 6")
        dbHelper.insert("캠퍼스 카페", "카페", "금정구 7")
        dbHelper.insert("캠퍼스 식당", "식당", "금정구 8")
        dbHelper.insert("캠퍼스 포차", "주점", "금정구 9")

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
}
