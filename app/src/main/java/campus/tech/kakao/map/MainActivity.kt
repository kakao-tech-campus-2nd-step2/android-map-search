package campus.tech.kakao.map

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
    private var textWatcher: TextWatcher? = null

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

        //resultAdapter = RecyclerViewAdapter(placeList, placeRepository)
        resultAdapter = RecyclerViewAdapter(placeList) {
            placeRepository.insertLog(it)
            addResearchList(it)
        }
        recyclerView.adapter = resultAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        researchList = placeRepository.getResearchEntries().toMutableList()
        tapAdapter = TapViewAdapter(researchList) {
            placeRepository.deleteResearchEntry(it)
            removeResearchList(it)
        }
        tabRecyclerView.adapter = tapAdapter
        tabRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        updateTabRecyclerViewVisibility()

        textWatcher = input.addTextChangedListener(
            afterTextChanged = { s -> filterList(s.toString())}
        )

        researchCloseButton.setOnClickListener {
            input.setText("")
        }
    }


    private fun filterList(query: String) {
        val filteredList = if (query.isEmpty()) {
            emptyList<Place>()
        } else {
            placeList.filter {
                it.category.category.contains(query, ignoreCase = true) || it.name.contains(query, ignoreCase = true)
            }
        }

        if (filteredList.isEmpty()) {
            noResultTextView.isVisible = true
            recyclerView.isGone = true
        } else {
            noResultTextView.isGone = true
            recyclerView.isVisible = true
            resultAdapter.placeList = filteredList.toMutableList()
            resultAdapter.notifyDataSetChanged()
            //resultAdapter.notifyItemRangeChanged(0, filteredList.size)
            //resultAdapter.updatePlaceList(filteredList.toMutableList())
        }
    }

    private fun addResearchList(place: Place) {
        if (!researchList.contains(place)){
            researchList.add(place)
            tapAdapter.notifyItemInserted(researchList.size - 1)
            updateTabRecyclerViewVisibility()
        }
    }

    private fun removeResearchList(it: Place) {
        val position = researchList.indexOf(it)
        if (position != -1) {
            researchList.removeAt(position)
            tapAdapter.notifyItemRemoved(position)
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

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { input.removeTextChangedListener(it) }
    }
}
