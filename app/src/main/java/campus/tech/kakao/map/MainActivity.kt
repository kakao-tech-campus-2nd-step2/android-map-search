package campus.tech.kakao.map

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.provider.BaseColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var input: EditText
    private lateinit var researchCloseButton: ImageView
    private lateinit var tabRecyclerView: RecyclerView
    private lateinit var noResultTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var placeRepository: PlaceRepository
    private var placeList = mutableListOf<Place>()
    private var researchList = mutableListOf<Place>()
    private lateinit var resultAdapter: RecyclerViewAdapter
    private lateinit var tapAdapter: TapViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        input = findViewById(R.id.input)
        researchCloseButton = findViewById(R.id.close_button)
        noResultTextView = findViewById(R.id.no_result_textview)
        recyclerView = findViewById(R.id.recyclerView)
        tabRecyclerView = findViewById(R.id.tab_recyclerview)

        placeRepository = PlaceRepository(this)
        placeRepository.reset()
        placeRepository.insertInitialData()
        placeList = placeRepository.returnPlaceList()

        resultAdapter = RecyclerViewAdapter(placeList, LayoutInflater.from(this), placeRepository)
        recyclerView.adapter = resultAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        researchList = placeRepository.getResearchEntries().toMutableList()
        tapAdapter = TapViewAdapter(researchList, LayoutInflater.from(this), placeRepository)
        tabRecyclerView.adapter = tapAdapter
        tabRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        updateTabRecyclerViewVisibility()

        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterList(s.toString())
            }
        })

        researchCloseButton.setOnClickListener {
            input.setText("")
        }
    }

    private fun filterList(query: String) {
        val filteredList = placeList.filter {
            it.category.contains(query, ignoreCase = true) || it.name.contains(query, ignoreCase = true)
        }

        if (filteredList.isEmpty()) {
            noResultTextView.isVisible = true
            recyclerView.isGone = true
        } else {
            noResultTextView.isGone = true
            recyclerView.isVisible = true
            resultAdapter.placeList = filteredList.toMutableList()
            resultAdapter.notifyDataSetChanged()
        }
    }

    fun addResearchList(place: Place) {
        if (!researchList.contains(place)){
            researchList.add(place)
            tapAdapter.notifyDataSetChanged()
            updateTabRecyclerViewVisibility()
        }
    }

    fun updateTabRecyclerViewVisibility() {
        if (placeRepository.hasResearchEntries()) {
            tabRecyclerView.isVisible = true
        } else {
            tabRecyclerView.isGone = true
        }
    }
}