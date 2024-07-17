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
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var input: EditText
    private lateinit var researchCloseButton: ImageView
    private lateinit var tabRecyclerView: RecyclerView
    private lateinit var noResultTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var placeRepository: PlaceRepository
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

        resultAdapter = RecyclerViewAdapter {
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

        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 미사용
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 미사용
            }

            override fun afterTextChanged(s: Editable?) {
                // ★ 여기서 구현해야함
                // 유저입력값 = s.toString
                // s.toString에 따라 retrofit 서비스가 쏘는 값(category_group query)이 달라져야함
                // s.toString(카페) -> retorifit(카페) -> ★ db에 넣을 필요 없다 -> recyclerview.submitlist(list)
                // s.toString(약국) -> retrofit(약국) -> ★ db에 넣을 필요 없다 -> recyclerview.submitlist(list)
                // val list는 한번만 선언하고 매 카테고리마다 쓰면 된다
                val query = s.toString()
                if (query.isNotEmpty()) {
                    placeRepository.insertData2ResultView(query) { updateRecyclerView(it) }
                } else {
                    resultAdapter.submitList(emptyList())
                    noResultTextView.isVisible = true
                    recyclerView.isGone = true
                }
            }
        })

        researchCloseButton.setOnClickListener {
            input.setText("")
        }
    }

    private fun updateRecyclerView(list: List<Place>) {
        if (list.isEmpty()) {
            noResultTextView.isVisible = true
            recyclerView.isGone = true
        } else {
            noResultTextView.isGone = true
            recyclerView.isVisible = true
            resultAdapter.submitList(list)
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

