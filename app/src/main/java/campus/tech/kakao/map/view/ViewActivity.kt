package campus.tech.kakao.map.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMainBinding
import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.Repository
import campus.tech.kakao.map.view.adapter.LocationAdapter
import campus.tech.kakao.map.view.adapter.LogAdapter
import campus.tech.kakao.map.viewmodel.LocationViewModel
import campus.tech.kakao.map.viewmodel.LocationViewModelFactory

class ViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: LocationViewModel
    private lateinit var locationAdapter: LocationAdapter
    private lateinit var logAdapter: LogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun onStop() {
        super.onStop()
        saveLog()
    }
    private fun saveLog(){
        viewModel.saveLog()
    }

    private fun init(){
        initBinding()
        initViewModel()
        setupRecyclerViews()
        observeViewModel()
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
    }

    private fun initViewModel() {
        val factory = createViewModelFactory()
        viewModel = ViewModelProvider(this,factory).get(LocationViewModel::class.java)
        binding.viewModel = viewModel
    }

    private fun createViewModelFactory(): LocationViewModelFactory{
        val repository = createRepository()
        return LocationViewModelFactory(repository)
    }

    private fun createRepository() = Repository(this)

    private fun setupRecyclerViews() {
        setupLocationRecyclerView()
        setupLogRecyclerView()
    }

    private fun setupLocationRecyclerView(){
        initLocationAdapter()
        configureLocationRecyclerView()
    }

    private fun initLocationAdapter() {
        locationAdapter = LocationAdapter { location -> viewModel.addLog(location) }
    }

    private fun configureLocationRecyclerView(){
        val locationRecyclerView = binding.recyclerLocation

        locationRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ViewActivity)
            adapter = locationAdapter
        }
    }

    private fun setupLogRecyclerView(){
        initLogAdapter()
        configureLogRecyclerView()
    }

    private fun initLogAdapter() {
        logAdapter = LogAdapter{position -> viewModel.removeLog(position)}
    }

    private fun configureLogRecyclerView(){
        val logRecyclerView = binding.recyclerLog

        logRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ViewActivity, RecyclerView.HORIZONTAL, false)
            adapter = logAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.searchText.observe(this, Observer { searchText ->
            updateLocationList(searchText)
            updateHelpMessageVisibility()
        })

        viewModel.logList.observe(this, Observer { logList ->
            logAdapter.submitList(logList)
        })
    }

    private fun updateLocationList(searchText: String){
        val foundLocations =viewModel.findData(searchText)

        locationAdapter.submitList(foundLocations)
    }

    private fun updateHelpMessageVisibility(){
        binding.tvHelpMessage.visibility =
            if (locationAdapter.itemCount > 0) View.GONE else View.VISIBLE
    }
}
