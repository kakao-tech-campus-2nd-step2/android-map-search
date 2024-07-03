package campus.tech.kakao.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PlaceRecyclerViewActivity : AppCompatActivity() {

    private lateinit var tvNoData: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvNoData = findViewById<TextView>(R.id.tvNoData)

        val databaseAccess = PlaceDatabaseAccess(this)
        val placeList: List<PlaceDataModel> = databaseAccess.getAllPlace()

        val recyclerView = findViewById<RecyclerView>(R.id.rvPlaceList)
        recyclerView.adapter = PlaceRecyclerViewAdapter(placeList, LayoutInflater.from(this))
        recyclerView.layoutManager = LinearLayoutManager(this)

        hasPlaceData(placeList)
    }

    fun hasPlaceData(placeList: List<PlaceDataModel>) {
        if (placeList.isEmpty()) {
            tvNoData.visibility = View.VISIBLE
        }
        else {
            tvNoData.visibility = View.GONE
        }
    }
}