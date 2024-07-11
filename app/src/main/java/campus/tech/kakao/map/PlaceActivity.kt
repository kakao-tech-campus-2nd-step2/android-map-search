package campus.tech.kakao.map

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
    private lateinit var rvPlaceList: RecyclerView
    private lateinit var rvSearchList: RecyclerView
    private lateinit var placeAdapter: PlaceRecyclerViewAdapter
    private lateinit var searchAdapter: SearchRecyclerViewAdapter
    private val placeDatabaseAccess = PlaceDatabaseAccess(this, "Place.db")
    private val searchDatabaseAccess = PlaceDatabaseAccess(this, "Search.db")

    private lateinit var retrofit: Retrofit
    private lateinit var retrofitLocalService: RetrofitLocalService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_layout)

        etSearch = findViewById<EditText>(R.id.etSearch)
        btnErase = findViewById<ImageButton>(R.id.btnErase)
        tvNoData = findViewById<TextView>(R.id.tvNoData)
        rvPlaceList = findViewById<RecyclerView>(R.id.rvPlaceList)
        rvSearchList = findViewById<RecyclerView>(R.id.rvSearchList)

        val placeList: MutableList<PlaceDataModel> = placeDatabaseAccess.getAllPlace()
        val searchList: MutableList<PlaceDataModel> = searchDatabaseAccess.getAllPlace()
        var keywordList: MutableList<PlaceDataModel> = mutableListOf()

        // Retrofit 객체
        retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.BASE_URL))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofitLocalService = retrofit.create(RetrofitLocalService::class.java)

        // Search 어댑터
        searchAdapter = searchRecyclerViewAdapter(searchList)
        rvSearchList.adapter = searchAdapter
        rvSearchList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Place 어댑터
        placeAdapter = placeRecyclerViewAdapter(keywordList, searchList)
        rvPlaceList.adapter = placeAdapter
        rvPlaceList.layoutManager = LinearLayoutManager(this)

        controlPlaceVisibility(keywordList)
        controlSearchVisibility(searchList)

        btnErase.setOnClickListener {
            etSearch.setText("")
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val category = s.toString()
                if (category.isNotEmpty()) {
                    searchPlace(category)
                    Log.d("success", "onTextChanged")
                }
                else {
                    keywordList.clear()
                    placeAdapter.updateData(keywordList)
                    controlPlaceVisibility(keywordList)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun searchPlace(categoryName: String) {
        val call = retrofitLocalService.searchPlaceByCategory("KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}", CategoryGroupCodes.getCodeByName(categoryName) ?: "")
        call.enqueue(object : Callback<SearchResult> {
            override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                if (response.isSuccessful) {
                    var categoryList: MutableList<PlaceDataModel> = mutableListOf()
                    val places = response.body()?.documents
                    places?.let {
                        for (placeInfo in places) {
                            val place = PlaceDataModel(
                                name = placeInfo.place_name,
                                category = placeInfo.category_group_name,
                                address = placeInfo.address_name
                            )
                            addPlaceList(categoryList, place)
                        }
                    }
                    placeAdapter.updateData(categoryList)
                    controlPlaceVisibility(categoryList)
                    Log.d("API response", "Success: $places")
                }
                else {
                    val errorBody = response.errorBody()?.string()
                    Log.d("API response", "Error response: $errorBody")
                }
            }

            override fun onFailure(call: Call<SearchResult>, throwable: Throwable) {
                Log.w("API response", "Failure: $throwable")
            }
        })
    }


    private fun includeKeywordList(s: CharSequence?, placeList: MutableList<PlaceDataModel>): MutableList<PlaceDataModel> {
        var keywordList = placeList
        val keyword = s.toString()
        keywordList = placeDatabaseAccess.searchPlaceName(keyword)
        placeAdapter.updateData(keywordList)
        return keywordList
    }

    private fun includeCategoryList(s: CharSequence?, placeList: MutableList<PlaceDataModel>): MutableList<PlaceDataModel> {
        var keywordList = placeList
        val keyword = s.toString()
        keywordList = placeDatabaseAccess.searchPlaceCategory(keyword)
        placeAdapter.updateData(keywordList)
        return keywordList
    }

    private fun placeRecyclerViewAdapter(placeList: MutableList<PlaceDataModel>, searchList: MutableList<PlaceDataModel>) =
        PlaceRecyclerViewAdapter(placeList, onItemClick = { place ->
            if (place in searchList) {
                removePlaceRecord(searchList, place)
            }
            addPlaceRecord(searchList, place)
            controlSearchVisibility(searchList)
        })

    private fun searchRecyclerViewAdapter(searchList: MutableList<PlaceDataModel>) =
        SearchRecyclerViewAdapter(searchList, onItemClick = { place ->
            removePlaceRecord(searchList, place)
            controlSearchVisibility(searchList)
        })

    // 검색 저장 기록 조작
    private fun addPlaceRecord(searchList: MutableList<PlaceDataModel>, place: PlaceDataModel) {
        searchList.add(place)
        searchDatabaseAccess.insertPlace(place)
        searchAdapter.notifyDataSetChanged()
    }

    private fun removePlaceRecord(searchList: MutableList<PlaceDataModel>, place: PlaceDataModel) {
        val index = searchList.indexOf(place)
        searchList.removeAt(index)
        searchDatabaseAccess.deletePlace(place.name)
        searchAdapter.notifyDataSetChanged()
    }

    // 장소 목록 조작
    private fun addPlaceList(placeList: MutableList<PlaceDataModel>, place: PlaceDataModel) {
        placeList.add(place)
        placeDatabaseAccess.insertPlace(place)
        placeAdapter.notifyDataSetChanged()
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

    private fun controlSearchVisibility(searchList: List<PlaceDataModel>) {
        if (searchList.isEmpty()) {
            rvSearchList.visibility = View.GONE
        }
        else {
            rvSearchList.visibility = View.VISIBLE
        }
    }
}