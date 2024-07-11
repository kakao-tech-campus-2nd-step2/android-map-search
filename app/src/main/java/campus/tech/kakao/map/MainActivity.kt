package campus.tech.kakao.map

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SearchViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val searchRepository = SearchRepository()
        val savedSearchKeywordRepository = SavedSearchKeywordRepository(this)
        val viewModelProviderFactory =
            SearchViewModelFactory(searchRepository, savedSearchKeywordRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory)[SearchViewModel::class.java]

        delSearchKeywordListener()
        detectSearchWindowChangedListener()
        displaySearchResults()
        displaySavedSearchKeywords()
    }

    private fun delSearchKeywordListener() {
        binding.delSearchKeyword.setOnClickListener {
            binding.searchWindow.text = null
        }
    }

    private fun detectSearchWindowChangedListener() {
        binding.searchWindow.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchKeyWord = SearchKeyword(s.toString())
                viewModel.getSearchResults(searchKeyWord)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun displaySearchResults() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchResults.collect {
                    if (it.isEmpty()) {
                        showView(binding.emptySearchResults, true)
                        showView(binding.searchResultsList, false)
                    } else {
                        showView(binding.emptySearchResults, false)
                        showView(binding.searchResultsList, true)
                        binding.searchResultsList.adapter =
                            SearchResultsAdapter(it, layoutInflater, viewModel::saveSearchKeyword)
                        binding.searchResultsList.layoutManager =
                            LinearLayoutManager(this@MainActivity)
                    }
                }
            }
        }
    }

    private fun displaySavedSearchKeywords() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.savedSearchKeywords.collect {
                    binding.savedSearchKeywordsList.adapter =
                        SavedSearchKeywordsAdapter(it, layoutInflater, viewModel::delSavedSearchKeyword)
                    binding.savedSearchKeywordsList.layoutManager = LinearLayoutManager(
                        this@MainActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                }
            }
        }
    }

    private fun showView(view: View, isShow: Boolean) {
        view.visibility = if (isShow) View.VISIBLE else View.GONE
    }
}
