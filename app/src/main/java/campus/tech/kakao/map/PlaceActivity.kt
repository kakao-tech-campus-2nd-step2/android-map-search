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

class PlaceActivity : AppCompatActivity() {

    private lateinit var etSearch: EditText
    private lateinit var btnErase: ImageButton
    private lateinit var tvNoData: TextView
    private lateinit var rvPlaceList: RecyclerView
    private lateinit var rvSearchList: RecyclerView
    private lateinit var placeAdapter: PlaceRecyclerViewAdapter
    private lateinit var searchAdapter: SearchRecyclerViewAdapter
    private val searchDatabaseAccess = PlaceDatabaseAccess(this, "Search.db")

    private lateinit var placeRepository: PlaceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_layout)

        etSearch = findViewById<EditText>(R.id.etSearch)
        btnErase = findViewById<ImageButton>(R.id.btnErase)
        tvNoData = findViewById<TextView>(R.id.tvNoData)
        rvPlaceList = findViewById<RecyclerView>(R.id.rvPlaceList)
        rvSearchList = findViewById<RecyclerView>(R.id.rvSearchList)

        val searchList: MutableList<PlaceDataModel> = searchDatabaseAccess.getAllPlace()
        val keywordList: MutableList<PlaceDataModel> = mutableListOf()

        placeRepository = PlaceRepository()

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
        placeRepository.searchPlace(categoryName,
            onSuccess = { categoryList ->
                placeAdapter.updateData(categoryList)
                controlPlaceVisibility(categoryList)
            },
            onFailure = { throwable ->
                Log.w("API response", "Failure: $throwable")
            }
        )
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

    // visibility 조작
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