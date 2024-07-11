package campus.tech.kakao.map

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.databinding.ActivityMainBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: SearchViewModel by viewModels { SearchViewModelFactory(applicationContext) }
    private lateinit var searchResultsAdapter: SearchResultsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            val searchEditText = editSearch
            val clearButton = clearButton
            val searchResultsRecyclerView = recyclerView
            val savedKeywordsRecyclerView = savedKeywordsRecyclerView
            val noResultsTextView: TextView = noResultsTextView

            searchResultsRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
            savedKeywordsRecyclerView.layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.HORIZONTAL, false)

            val savedKeywordsAdapter = SavedKeywordsAdapter(emptyList()) { keyword ->
                viewModel.deleteKeyword(keyword)
            }
            savedKeywordsRecyclerView.adapter = savedKeywordsAdapter

            searchResultsAdapter = SearchResultsAdapter(emptyList()) { keyword ->
                viewModel.saveKeyword(keyword)
            }
            searchResultsRecyclerView.adapter = searchResultsAdapter

            viewModel.searchResults.observe(this@SearchActivity) { results ->
                searchResultsAdapter.updateData(results)
                noResultsTextView.visibility = if (results.isEmpty()) View.VISIBLE else View.GONE
            }

            viewModel.savedKeywords.observe(this@SearchActivity) { keywords ->
                savedKeywordsAdapter.updateKeywords(keywords)
            }

            clearButton.setOnClickListener {
                searchEditText.text.clear()
                searchResultsAdapter.updateData(emptyList())
                noResultsTextView.visibility = View.VISIBLE
            }

            searchEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val query = s.toString()
                    if (query.isNotEmpty()) {
                        viewModel.search(query)
                        noResultsTextView.visibility = View.GONE
                    } else {
                        searchResultsAdapter.updateData(emptyList())
                        noResultsTextView.visibility = View.VISIBLE
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

}
