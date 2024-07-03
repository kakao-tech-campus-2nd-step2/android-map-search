package campus.tech.kakao.map

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var input: EditText
    private lateinit var researchCloseButton: ImageView
    private lateinit var scrollviewContainer: LinearLayout
    private lateinit var resultLayout: LinearLayout
    private lateinit var noResultTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var placeRepository: PlaceRepository
    private var placeList = mutableListOf<Place>()
    private lateinit var adapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        input = findViewById(R.id.input)
        researchCloseButton = findViewById(R.id.close_button)
        scrollviewContainer = findViewById(R.id.tab_container)
        noResultTextView = findViewById(R.id.no_result_textview)
        recyclerView = findViewById(R.id.recyclerView)

        placeRepository = PlaceRepository(this)

        placeRepository.reset()
        placeRepository.insertInitialData()

        placeList = placeRepository.returnPlaceList()

        adapter = RecyclerViewAdapter(placeList, LayoutInflater.from(this))
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

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
            it.category == query
        }

        if (filteredList.isEmpty()) {
            noResultTextView.isVisible = true
            recyclerView.isGone = true
        } else {
            noResultTextView.isGone = true
            recyclerView.isVisible = true
            adapter.placeList = filteredList.toMutableList()
            adapter.notifyDataSetChanged()
        }
    }

}