package campus.tech.kakao.map

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlaceActivity : AppCompatActivity() {

    private lateinit var etSearch: EditText
    private lateinit var btnErase: ImageButton
    private lateinit var tvNoData: TextView
    private lateinit var rvSearchList: RecyclerView
    private lateinit var rvPlaceList: RecyclerView
    private lateinit var placeAdapter: PlaceRecyclerViewAdapter
    private lateinit var searchAdapter: SearchRecyclerViewAdapter
    private val placeDatabaseAccess = PlaceDatabaseAccess(this)
    private val searchDatabaseAccess = SearchDatabaseAccess(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_layout)

        etSearch = findViewById<EditText>(R.id.etSearch)
        btnErase = findViewById<ImageButton>(R.id.btnErase)
        tvNoData = findViewById<TextView>(R.id.tvNoData)
        rvPlaceList = findViewById<RecyclerView>(R.id.rvPlaceList)
        rvSearchList = findViewById<RecyclerView>(R.id.rvSearchList)

        val placeList: List<PlaceDataModel> = emptyList()
        val searchList: List<PlaceDataModel> = emptyList()

        // Search list adapter
        searchAdapter = SearchRecyclerViewAdapter(searchList, LayoutInflater.from(this), this, )
        rvSearchList.adapter = searchAdapter
        rvSearchList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Place list adapter
        placeAdapter = PlaceRecyclerViewAdapter(placeList, LayoutInflater.from(this), this, searchAdapter)
        rvPlaceList.adapter = placeAdapter
        rvPlaceList.layoutManager = LinearLayoutManager(this)

        btnErase.setOnClickListener {
            etSearch.setText("")
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val keyword = s.toString()
                if (keyword.isEmpty()) {
                    placeAdapter.updatePlace(emptyList())
                    controlPlaceVisibility(emptyList())
                }
                else {
                    placeDatabaseAccess.searchPlace(keyword)
                    val searchResult = placeDatabaseAccess.searchPlace(keyword)
                    placeAdapter.updatePlace(searchResult)
                    controlPlaceVisibility(searchResult)
                    searchKeyword(keyword)
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        val searchHistory = searchDatabaseAccess.getAllSearch()
        searchAdapter.updateSearch(searchHistory)
    }


    private fun controlPlaceVisibility(placeList: List<PlaceDataModel>) {
        if (placeList.isEmpty()) {
            rvPlaceList.visibility = View.INVISIBLE
            tvNoData.visibility = View.VISIBLE
        }
        else {
            rvPlaceList.visibility = View.VISIBLE
            tvNoData.visibility = View.GONE
        }
    }

    fun controlSearchVisibility(placeList: List<PlaceDataModel>) {
        if (placeList.isEmpty()) {
            rvSearchList.visibility = View.GONE
        }
        else {
            rvSearchList.visibility = View.VISIBLE
        }
    }

    private fun searchKeyword(keyword: String) {
        // Retrofit 객체 생성
        val retrofitLocalService = Retrofit.Builder()
            .baseUrl(getString(R.string.BASE_URL))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitLocalService::class.java)

        retrofitLocalService.requestPlace("KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}", keyword, 2, 15).enqueue(object :
            Callback<SearchResult> {
            override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("apiTest", "$body")
                    body?.let {
                        for (placeInfo in it.documents) {
                            val place = PlaceDataModel(
                                name = placeInfo.place_name,
                                category = placeInfo.category_group_name,
                                address = placeInfo.address_name
                            )
                            placeDatabaseAccess.insertPlace(place)
                        }
                    }
                }
                else {
                    val errorBody = response.errorBody()?.string()
                    Log.d("apiTest", "Error response: $errorBody")

                }
            }

            override fun onFailure(call: Call<SearchResult>, throwable: Throwable) {
                Log.w("apiTest", "$throwable")
            }
        })
    }


}