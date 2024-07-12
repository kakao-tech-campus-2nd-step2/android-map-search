package campus.tech.kakao.map

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.ViewModelFactory.LocationViewModelFactory
import campus.tech.kakao.map.ViewModelFactory.SavedLocationViewModelFactory

class MainActivity : AppCompatActivity(), OnItemSelectedListener {

    private lateinit var locationViewModel: LocationViewModel
    private lateinit var locationAdapter: LocationAdapter
    private lateinit var locationRecyclerView: RecyclerView

    private lateinit var savedLocationViewModel: SavedLocationViewModel
    private lateinit var savedLocationAdapter: SavedLocationAdapter
    private lateinit var savedLocationRecyclerView: RecyclerView

    private lateinit var locationDbHelper: LocationDbHelper
    private lateinit var locationDbAccessor: LocationLocalRepository
    private lateinit var locationRemoteRepository: LocationRemoteRepository

    private lateinit var clearButton: ImageView
    private lateinit var searchEditText: EditText
    private lateinit var noResultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
//        locationViewModel.insertLocation()

        setupSearchEditText()
        setupClearButton()
        setupViewModels()
        setupRecyclerViews()
    }

    private fun getKakaoRestApiKey(): String {
        val kakaoRestApiKey = "KakaoAK " + getString(R.string.KAKAO_REST_API_KEY)
        return kakaoRestApiKey
    }

    private fun initViews() {
        locationDbHelper = LocationDbHelper(this)
        locationDbAccessor = LocationLocalRepository(locationDbHelper)
        locationRemoteRepository = LocationRemoteRepository()

        locationViewModel = ViewModelProvider(this, LocationViewModelFactory(locationDbAccessor, locationRemoteRepository)
        ).get(LocationViewModel::class.java)
        locationRecyclerView = findViewById(R.id.locationRecyclerView)

        savedLocationViewModel = ViewModelProvider(this, SavedLocationViewModelFactory(locationDbAccessor)
        ).get(SavedLocationViewModel::class.java)
        savedLocationRecyclerView = findViewById(R.id.savedLocationRecyclerView)

        clearButton = findViewById(R.id.clearButton)
        searchEditText = findViewById(R.id.searchEditText)
        noResultTextView = findViewById(R.id.NoResultTextView)
    }

    private fun setupSearchEditText() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                locationViewModel.searchLocationsFromKakaoAPI(getKakaoRestApiKey(),query){
                    if (locationViewModel.getSearchedLocationsSize() > 0) {
                        noResultTextView.visibility = View.GONE
                    } else {
                        noResultTextView.visibility = View.VISIBLE
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupClearButton() {
        clearButton.setOnClickListener {
            searchEditText.setText("")
        }
    }

    private fun setupViewModels() {
        savedLocationViewModel.setSavedLocation()
        observeSavedLocationViewModel()

        locationViewModel.setLocationsFromKakaoAPI()
        observeLocationsViewModel()
    }

    private fun observeSavedLocationViewModel() {
        savedLocationViewModel.savedLocation.observe(this, Observer {
            savedLocationAdapter.submitList(it?.toList() ?: emptyList())
            if (it.size > 0) {
                savedLocationRecyclerView.visibility = View.VISIBLE
            } else {
                savedLocationRecyclerView.visibility = View.GONE
            }
        })
    }

    private fun observeLocationsViewModel() {
        locationViewModel.searchedLocations.observe(this, Observer {
            locationAdapter.submitList(it?.toList() ?: emptyList())
        })
    }

    private fun setupRecyclerViews() {
        locationAdapter = LocationAdapter(this)
        locationRecyclerView.layoutManager = LinearLayoutManager(this)
        locationRecyclerView.adapter = locationAdapter

        savedLocationAdapter = SavedLocationAdapter(this)
        savedLocationRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        savedLocationRecyclerView.adapter = savedLocationAdapter
    }

    override fun insertSavedLocation(title: String) {
        savedLocationViewModel.insertSavedLocation(title)
    }

    override fun deleteSavedLocation(item: SavedLocation) {
        savedLocationViewModel.deleteSavedLocation(item)
    }
}
