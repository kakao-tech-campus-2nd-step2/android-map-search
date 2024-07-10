package campus.tech.kakao.map

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var placeAdapter: PlaceAdapter
    private lateinit var savedSearchAdapter: SavedSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val search = findViewById<EditText>(R.id.search)
        val closeIcon = findViewById<ImageView>(R.id.close_icon)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val savedSearch = findViewById<RecyclerView>(R.id.saved_search)

        val dbManager = DatabaseManager(context = this)

        placeAdapter = PlaceAdapter(emptyList()){ place ->
            dbManager.insertSavedPlace(place.id, place.name)
            updateSavedSearch(dbManager)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = placeAdapter

        //저장된 검색어 adapter, recyclerView
        savedSearchAdapter = SavedSearchAdapter(emptyList()){ savedSearch ->
            dbManager.deleteSavedPlace(savedSearch.id)
            updateSavedSearch(dbManager)
        }
        savedSearch.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)    //마지막 인자는 역순으로 배치 여부
        savedSearch.adapter = savedSearchAdapter

        //검색어 지우기
        closeIcon.setOnClickListener {
            search.text.clear()
        }

        //검색창에 텍스트가 바뀔 때마다 감지해서 검색
        search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // 텍스트 변경 후 호출
                val query = s.toString()
                searchPlaces(query)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        })


        updateSavedSearch(dbManager)


    } //onCreate

    private fun searchPlaces(query: String) {
        val call = RetrofitInstance.api.searchKeyword(query) //API 요청

        call.enqueue(object : Callback<KakaoSearchResponse> { //비동기 API 요청

            override fun onResponse(call: Call<KakaoSearchResponse>, response: Response<KakaoSearchResponse>) {

                if (response.isSuccessful) {    //성공했을 때
                    val places = response.body()?.documents?.map { document ->
                        Place(
                            id = document.id.toInt(),
                            name = document.place_name,
                            address = document.address_name,
                            kind = document.category_name
                        )
                    } ?: emptyList()
                    placeAdapter.updateData(places)
                }
                else {  //실패했을 때
                    Log.e("API_ERROR", "Error: ${response.errorBody()?.string()}")
                }

            }

            override fun onFailure(call: Call<KakaoSearchResponse>, t: Throwable) { //실패 했을 때
                Log.e("API_ERROR", "Failure: ${t.message}")
            }
        })
    }


    //저장된 검색어 업데이트
    private fun updateSavedSearch(dbManager: DatabaseManager) {
        val savedSearches = dbManager.getSavedSearches()
        savedSearchAdapter.updateData(savedSearches)
    }
}
