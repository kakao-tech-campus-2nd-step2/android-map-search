package campus.tech.kakao.map.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.data.net.KakaoAPI
import campus.tech.kakao.map.PlaceApplication
import campus.tech.kakao.map.R
import campus.tech.kakao.map.domain.model.ResultSearchKeyword
import campus.tech.kakao.map.databinding.ActivityMainBinding
import campus.tech.kakao.map.view.adapter.SearchedPlaceAdapter
import campus.tech.kakao.map.view.adapter.LogAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import campus.tech.kakao.map.data.net.RetrofitApiClient
import campus.tech.kakao.map.util.PlaceMapper

class ViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var searchedPlaceAdapter: SearchedPlaceAdapter
    private lateinit var logAdapter: LogAdapter
    private lateinit var viewModel: PlaceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init(){
        initViewModel()
        initBinding()
        setupRecyclerViews()
        observeViewModel()
    }

    private fun initViewModel(){
        viewModel = ViewModelProvider(this, PlaceViewModel.provideFactory(application as PlaceApplication))
            .get(PlaceViewModel::class.java)
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

    }

    private fun setupRecyclerViews() {
        setupSearchedPlaceRecyclerView()
        setupLogRecyclerView()
    }

    private fun setupSearchedPlaceRecyclerView(){
        val searchedPlaceRecyclerView = binding.recyclerPlace
        searchedPlaceAdapter =SearchedPlaceAdapter{place -> viewModel.addLog(place) }

        searchedPlaceRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ViewActivity)
            adapter = searchedPlaceAdapter
        }
    }

    private fun setupLogRecyclerView(){
        val logRecyclerView = binding.recyclerLog
        logAdapter =LogAdapter{ id -> viewModel.removeLog(id)}
        logAdapter.submitList(viewModel.getLogs())

        logRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ViewActivity, RecyclerView.HORIZONTAL, false)
            adapter = logAdapter
        }
    }

    private fun observeViewModel(){
        viewModel.searchText.observe(this, Observer { searchText ->
            searchKeyword(searchText)
            updateHelpMessageVisibility()
        })

        viewModel.logList.observe(this, Observer { logList ->
            logAdapter.submitList(logList)
        })
    }
    private fun updateSearchedPlaceList(searchText: String){
        val searchedPlaces =viewModel.getPlaces(searchText)

        searchedPlaceAdapter.submitList(searchedPlaces)
    }
    private fun updateHelpMessageVisibility(){
        binding.tvHelpMessage.visibility =
            if (searchedPlaceAdapter.itemCount > 0) View.GONE else View.VISIBLE
    }

    private fun searchKeyword(keyword: String) {

        val retrofit =  RetrofitApiClient.api
            .getSearchKeyword(BuildConfig.KAKAO_REST_API_KEY, keyword)

        retrofit.enqueue(object: Callback<ResultSearchKeyword> {
            override fun onResponse(
                call: Call<ResultSearchKeyword>,
                response: Response<ResultSearchKeyword>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { result ->
                        val places = PlaceMapper.mapPlaces(result.documents)
                        viewModel.updatePlaces(places)
                        updateSearchedPlaceList(keyword)
                    }
                }
            }
            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                Log.w("pjh", "통신 실패: ${t.message}")
            }
        })
    }
}
