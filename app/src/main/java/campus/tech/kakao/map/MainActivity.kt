package campus.tech.kakao.map

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
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

        searchViewModel.getAllSearchResults()
        searchViewModel.getAllPlaces()

        // insertBulkData()
    }

    private fun insertBulkData() {
        val places = listOf(
            Triple("cafe99thStreet1", "대전광역시 유성구 대학로 99", "카페"),
            Triple("cafe99thStreet2", "대전광역시 유성구 대학로 99", "카페"),
            Triple("cafe99thStreet3", "대전광역시 유성구 대학로 99", "카페"),
            Triple("cafe99thStreet4", "대전광역시 유성구 대학로 99", "카페"),
            Triple("cafecafe", "대전광역시 유성구 대학로 2", "카페"),
            Triple("cafe99", "대전광역시 유성구 대학로 3", "카페"),
            Triple("MegaCoffee", "대전광역시 유성구 대학로 4", "카페"),
            Triple("starbucks", "대전광역시 유성구 대학로 5", "카페"),
            Triple("CaffeBene", "대전광역시 유성구 대학로 6", "카페"),
            Triple("composeCoffee", "대전광역시 유성구 대학로 7", "카페"),
            Triple("EdiyaCoffee", "대전광역시 유성구 대학로 8", "카페"),
            Triple("cinema 1", "대전광역시 유성구 궁동 1", "영화관"),
            Triple("cinema 2", "대전광역시 유성구 궁동 2", "영화관"),
            Triple("cinema 3", "대전광역시 유성구 궁동 3", "영화관"),
            Triple("cinema 4", "대전광역시 유성구 궁동 4", "영화관"),
            Triple("cinema 5", "대전광역시 유성구 궁동 5", "영화관"),
            Triple("cinema 6", "대전광역시 유성구 궁동 6", "영화관"),
            Triple("cinema 7", "대전광역시 유성구 궁동 7", "영화관"),
            Triple("cinema 8", "대전광역시 유성구 궁동 8", "영화관"),
            Triple("cinema 9", "대전광역시 유성구 궁동 9", "영화관"),
            Triple("cinema 10", "대전광역시 유성구 궁동 10", "영화관"),
        )

        lifecycleScope.launch {
            places.forEach { place ->
                searchViewModel.addPlace(place.first, place.second, place.third)
            }
            Log.d("testt", "Bulk data insertion completed.")
        }
    }
}
