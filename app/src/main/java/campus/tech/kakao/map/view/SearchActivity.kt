package campus.tech.kakao.map.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.viewmodel.SearchViewModel

class SearchActivity : AppCompatActivity() {
    private lateinit var viewModel: SearchViewModel
    private lateinit var editText: EditText
    private lateinit var cancelBtn: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var noSearchLayout: LinearLayout
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var saveRecyclerView: RecyclerView
    private lateinit var savePlaceAdapter: SavePlaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[SearchViewModel::class.java]

        findViews()
        setCancelBtnClickListener()
        editTextWatcher()
        viewModel.showSavePlace()

        recyclerView.layoutManager = LinearLayoutManager(this)
        viewModel.places.observe(this) { places ->
            searchAdapter = SearchAdapter(places) {
                viewModel.savePlaces(it.name)
                viewModel.showSavePlace()
            }
            recyclerView.adapter = searchAdapter
            updateViewVisibility(places.isNotEmpty())
        }

        saveRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        viewModel.savePlaces.observe(this) { savePlaces ->
            savePlaceAdapter = SavePlaceAdapter(savePlaces) {
                viewModel.deleteSavedPlace(it.savePlace)
                viewModel.showSavePlace()
            }
            saveRecyclerView.adapter = savePlaceAdapter
        }

        viewModel.insertDummyData("카페", "대전 유성구 궁동", "카페")
        viewModel.insertDummyData("약국", "대전 유성구 봉명동", "약국")
    }

    private fun findViews() {
        editText = findViewById<EditText>(R.id.searchText)
        cancelBtn = findViewById<ImageView>(R.id.cancelBtn)
        recyclerView = findViewById(R.id.searchPlaceView)
        noSearchLayout = findViewById(R.id.noSearch)
        saveRecyclerView = findViewById(R.id.savePlaceView)
    }

    private fun editTextWatcher() {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    updateViewVisibility(false)
                } else {
                    viewModel.searchPlaces(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setCancelBtnClickListener() {
        cancelBtn.setOnClickListener {
            editText.setText("")
            updateViewVisibility(false)
        }
    }

    private fun updateViewVisibility(hasPlaces: Boolean) {
        if (hasPlaces) {
            recyclerView.visibility = View.VISIBLE
            noSearchLayout.visibility = View.GONE
        } else {
            recyclerView.visibility = View.GONE
            noSearchLayout.visibility = View.VISIBLE
        }
    }



}
