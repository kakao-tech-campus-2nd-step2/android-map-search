package campus.tech.kakao.map

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PlaceRecyclerViewActivity : AppCompatActivity() {

    private lateinit var etSearch: EditText
    private lateinit var tvNoData: TextView
    private lateinit var rvSearchList: RecyclerView
    private lateinit var rvPlaceList: RecyclerView
    private lateinit var placeAdapter: PlaceRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etSearch = findViewById<EditText>(R.id.etSearch)
        tvNoData = findViewById<TextView>(R.id.tvNoData)
        rvPlaceList = findViewById<RecyclerView>(R.id.rvPlaceList)
        rvSearchList = findViewById<RecyclerView>(R.id.rvSearchList)

        val databaseAccess = PlaceDatabaseAccess(this)
        val placeList: List<PlaceDataModel> = databaseAccess.getAllPlace()

        placeAdapter = PlaceRecyclerViewAdapter(placeList, LayoutInflater.from(this))
        rvPlaceList.adapter = placeAdapter
        rvPlaceList.layoutManager = LinearLayoutManager(this)

        placeAdapter.updatePlace(emptyList())
        controlVisibility(emptyList())

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val keyword = s.toString()
                if (keyword.isEmpty()) {
                    placeAdapter.updatePlace(emptyList())
                    controlVisibility(emptyList())
                }
                else {
                    databaseAccess.searchPlace(keyword)
                    val searchResult = databaseAccess.searchPlace(keyword)
                    placeAdapter.updatePlace(searchResult)
                    controlVisibility(searchResult)
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    fun controlVisibility(placeList: List<PlaceDataModel>) {
        if (placeList.isEmpty()) {
            rvPlaceList.visibility = View.INVISIBLE
            tvNoData.visibility = View.VISIBLE
        }
        else {
            rvPlaceList.visibility = View.VISIBLE
            tvNoData.visibility = View.GONE
        }
    }
}