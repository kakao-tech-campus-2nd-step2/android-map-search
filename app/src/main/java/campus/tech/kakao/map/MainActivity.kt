package campus.tech.kakao.map

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), SearchResultAdapter.OnItemClickListener, KeywordAdapter.OnKeywordRemoveListener {

    private lateinit var mapViewModel: MapViewModel
    private lateinit var etKeywords: EditText
    private lateinit var rvSearchResult: RecyclerView
    private lateinit var rvKeywords: RecyclerView
    private lateinit var tvNoResults: TextView
    private lateinit var ivClear: ImageView

    private val searchResultAdapter = SearchResultAdapter(this)
    private val keywordAdapter = KeywordAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etKeywords = findViewById(R.id.etKeywords)
        rvSearchResult = findViewById(R.id.rvSearchResult)
        rvKeywords = findViewById(R.id.rvKeywords)
        tvNoResults = findViewById(R.id.tvNoResults)
        ivClear = findViewById(R.id.ivClear)

        rvSearchResult.layoutManager = LinearLayoutManager(this)
        rvSearchResult.adapter = searchResultAdapter

        rvKeywords.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvKeywords.adapter = keywordAdapter

        mapViewModel = ViewModelProvider(this).get(MapViewModel::class.java)

        etKeywords.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val keyword = s.toString()
                if (keyword.isNotEmpty()) {
                    mapViewModel.searchPlaces(keyword)
                } else {
                    searchResultAdapter.submitList(emptyList())
                    tvNoResults.visibility = TextView.VISIBLE
                    rvSearchResult.visibility = RecyclerView.GONE
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        mapViewModel.searchResults.observe(this, Observer { results ->
            if (results.isEmpty()) {
                tvNoResults.visibility = TextView.VISIBLE
                rvSearchResult.visibility = RecyclerView.GONE
            } else {
                tvNoResults.visibility = TextView.GONE
                rvSearchResult.visibility = RecyclerView.VISIBLE
                searchResultAdapter.submitList(results)
            }
        })

        ivClear.setOnClickListener {
            etKeywords.text.clear()
        }

        loadKeywords()
    }

    override fun onItemClick(item: MapItem) {
        keywordAdapter.addKeyword(item.name)
        saveKeywords()
    }

    override fun onKeywordRemove(keyword: String) {
        saveKeywords()
    }

    private fun loadKeywords() {
        val sharedPreferences = getSharedPreferences("keywords", MODE_PRIVATE)
        val keywords = sharedPreferences.getStringSet("keywords", setOf())?.toMutableList() ?: mutableListOf()
        keywordAdapter.submitList(keywords)
    }

    private fun saveKeywords() {
        val sharedPreferences = getSharedPreferences("keywords", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("keywords", keywordAdapter.currentKeywords.toSet())
        editor.apply()
    }
}
