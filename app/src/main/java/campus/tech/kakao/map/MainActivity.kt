package campus.tech.kakao.map

import android.util.Log
import android.view.inputmethod.EditorInfo
import android.os.Bundle
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import campus.tech.kakao.map.viewmodel.SearchViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchEditText = findViewById(R.id.searchEditText)

        // EditText에서 Enter 키를 눌렀을 때 로그를 찍고, 데이터베이스에 저장
        searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val keyword = v.text.toString()
                searchViewModel.addSearchResult(keyword)
                Log.d("testt", "Entered keyword: $keyword")
                true
            } else {
                false
            }
        }

        // 앱 시작 시 전체 데이터를 로드하여 로그에 출력
        searchViewModel.getAllSearchResults()
    }
}