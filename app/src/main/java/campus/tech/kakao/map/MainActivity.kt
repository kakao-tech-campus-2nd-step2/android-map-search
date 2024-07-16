package campus.tech.kakao.map

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kakao.sdk.common.util.Utility

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
        placeList = placeRepository.insertInitialData()

        resultAdapter = RecyclerViewAdapter {
            placeRepository.insertLog(it)
            addResearchList(it)
        }
        recyclerView.adapter = resultAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        resultAdapter.submitList(placeList)

        researchList = placeRepository.getResearchEntries().toMutableList()
        tapAdapter = TapViewAdapter(researchList) {
            placeRepository.deleteResearchEntry(it)
            removeResearchList(it)
        }
        tabRecyclerView.adapter = tapAdapter
        tabRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        updateTabRecyclerViewVisibility()

        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 미사용
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 미사용
            }

            override fun afterTextChanged(s: Editable?) {
                filterList(s.toString())
            }
        })

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
            resultAdapter.submitList(filteredList.toMutableList())
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

    private fun updateTabRecyclerViewVisibility() {
        tabRecyclerView.isVisible = placeRepository.hasResearchEntries()
    }

    override fun onDestroy() {
        super.onDestroy()
        input.removeTextChangedListener(null)
    }
}

