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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
                searchKeyword(query)
                Log.d("testt","검색하기")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        })


        updateSavedSearch(dbManager)


    } //onCreate

    private fun searchKeyword(keyword : String){
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(KakaoAPI::class.java)   // 통신 인터페이스를 객체로 생성
        val call = api.searchKeyword("KakaoAK $KAKAO_REST_API_KEY", keyword)   // 검색 조건 입력

        call.enqueue(object : Callback<KakaoSearchResponse> { //비동기 API 요청
            override fun onResponse(call: Call<KakaoSearchResponse>, response: Response<KakaoSearchResponse>) {
                if (response.isSuccessful) {
                    val places = response.body()?.documents?.map { document ->
                        Place(
                            id = document.id.toInt(),
                            name = document.place_name,
                            address = document.address_name,
                            kind = document.category_name
                        )
                    } ?: emptyList()
                    placeAdapter.updateData(places) //새 데이터 설정
                    Log.d("testt","api 완료")
                } else {
                    Log.d("testt", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<KakaoSearchResponse>, t: Throwable) {
                Log.d("testt", "Failure: ${t.message}")
            }
        })
    }
    companion object{
        const val BASE_URL = "https://dapi.kakao.com/"
        const val KAKAO_REST_API_KEY = "5c9da7f5fde44dde9598baa428277ec4"
    }


    //저장된 검색어 업데이트
    private fun updateSavedSearch(dbManager: DatabaseManager) {
        val savedSearches = dbManager.getSavedSearches()
        savedSearchAdapter.updateData(savedSearches)
    }
}
