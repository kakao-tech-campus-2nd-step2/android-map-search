package campus.tech.kakao.map.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.databinding.ActivitySearchBinding
import campus.tech.kakao.map.adapter.search.SearchAdapter
import campus.tech.kakao.map.api.KakaoLocalApi
import campus.tech.kakao.map.viewmodel.search.SearchViewModel
import campus.tech.kakao.map.viewmodel.search.SearchViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import campus.tech.kakao.map.model.Item
import campus.tech.kakao.map.viewmodel.OnSearchItemClickListener

class SearchActivity : AppCompatActivity(), OnSearchItemClickListener {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrofit 초기화
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(KakaoLocalApi::class.java)

        // ViewModel 초기화
        searchViewModel = ViewModelProvider(this, SearchViewModelFactory(api))[SearchViewModel::class.java]

        // RecyclerView 설정
        searchAdapter = SearchAdapter(this)
        binding.searchResultView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchAdapter
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }

        // 검색 입력 설정
        binding.searchTextInput.doAfterTextChanged {
            searchViewModel.searchLocationData(it.toString())
        }

        // 데이터 관찰하여 UI 업데이트
        searchViewModel.items.observe(this) {
            searchAdapter.submitList(it)
            binding.searchResultView.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            binding.emptyView.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onSearchItemClick(item: Item) {
        // 검색 항목 클릭 시 수행할 작업
    }
}
