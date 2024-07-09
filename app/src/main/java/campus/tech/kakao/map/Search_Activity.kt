package campus.tech.kakao.map

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Search_Activity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var savedSearchRecyclerView: RecyclerView
    private lateinit var searchResultAdapter: PlaceAdapter
    private lateinit var savedSearchAdapter: SavedSearchAdapter
    private lateinit var databaseHelper: MyDatabaseHelper
    private lateinit var noResultTextView: TextView
    private lateinit var kakaoApiService: KakaoApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // 초기화
        searchView = findViewById(R.id.search_text)
        searchRecyclerView = findViewById(R.id.RecyclerVer)
        savedSearchRecyclerView = findViewById(R.id.recyclerHor)
        noResultTextView = findViewById(R.id.nosearch)

        // 데이터베이스 헬퍼 초기화 -> 초기화를 하지 않으면 에러 발생
        databaseHelper = MyDatabaseHelper(this)

        // Kakao API 서비스 초기화
        try {
            kakaoApiService = createKakaoApiService()
        } catch (e: Exception) {
            Log.e("Search_Activity", "Error initializing KakaoApiService", e)
            return
        }

        // 검색 결과 어댑터, 저장된 검색 어댑터 초기화
        searchResultAdapter = PlaceAdapter(emptyList())
        savedSearchAdapter = SavedSearchAdapter(this, emptyList()) { searchText ->
            searchAndDisplayResults(searchText)
        }

        // RecyclerView의 레이아웃 매니저를 설정
        searchRecyclerView.layoutManager = LinearLayoutManager(this)
        savedSearchRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // RecyclerView에 어댑터를 설정
        searchRecyclerView.adapter = searchResultAdapter
        savedSearchRecyclerView.adapter = savedSearchAdapter

        // SearchView에 텍스트 변경 리스너를 설정
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchAndDisplayResults(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchText = newText ?: ""
                searchAndDisplayResults(searchText)
                return true
            }
        })

        // 초기 검색 결과를 표시
        searchAndDisplayResults("")
    }

    // Kakao API 서비스를 생성하는 메서드
    private fun createKakaoApiService(): KakaoApiService {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(KakaoApiService::class.java)
    }

    // 검색어를 입력받아 결과를 표시하는 메서드
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

                // 검색 결과를 데이터베이스에 저장
                savePlacesToDatabase(places)

                // 검색 결과가 없는 경우 "검색결과가 없습니다."
                if (places.isEmpty()) {
                    searchRecyclerView.visibility = RecyclerView.GONE
                    noResultTextView.visibility = RecyclerView.VISIBLE
                } else {
                    searchRecyclerView.visibility = RecyclerView.VISIBLE
                    noResultTextView.visibility = RecyclerView.GONE
                    searchResultAdapter.updateData(places)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 검색 결과를 데이터베이스에 저장하는 메서드
    private fun savePlacesToDatabase(places: List<Map<String, String>>) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val db = databaseHelper.writableDatabase
                db.beginTransaction()
                try {
                    // 기존 데이터를 삭제하고 새 데이터를 삽입
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

    // ContentValues를 생성하는 메서드
    private fun getContentValues(place: Map<String, String>): ContentValues {
        return ContentValues().apply {
            put(MapContract.COLUMN_NAME, place[MapContract.COLUMN_NAME])
            put(MapContract.COLUMN_ADDRESS, place[MapContract.COLUMN_ADDRESS])
            put(MapContract.COLUMN_CATEGORY, place[MapContract.COLUMN_CATEGORY])
        }
    }

    // 검색 결과를 표시하기 위한 PlaceAdapter 클래스
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

        // PlaceViewHolder 클래스
        inner class PlaceViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
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
