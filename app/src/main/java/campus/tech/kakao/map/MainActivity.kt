package campus.tech.kakao.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.ActivityMainBinding
import campus.tech.kakao.map.databinding.ItemPlaceBinding
import campus.tech.kakao.map.databinding.ItemSavedSearchWordBinding
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.model.SavedSearchWord
import campus.tech.kakao.map.viewmodel.PlaceViewModel
import campus.tech.kakao.map.viewmodel.SavedSearchWordViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val placeViewModel by lazy { ViewModelProvider(this)[PlaceViewModel::class.java] }
    private val savedSearchWordViewModel by lazy { ViewModelProvider(this)[SavedSearchWordViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupDummyData()
        setupViews()
        observeViewModels()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            this.placeViewModel = this@MainActivity.placeViewModel
            this.savedSearchWordViewModel = this@MainActivity.savedSearchWordViewModel
            this.lifecycleOwner = this@MainActivity
        }
    }

    /**
     * RecyclerView들을 설정하는 함수.
     */
    private fun setupRecyclerViews() {
        setSearchResultRecyclerView()
        setSavedSearchWordRecyclerView()
    }

    /**
     * 테스트용 더미 데이터를 place db에 삽입하기 위한 함수.
     */
    private fun setupDummyData() {
        placeViewModel.clearAllPlaces()
        testDataInsert()
    }

    /**
     * 테스트용 더미 데이터(카페, 약국)를 삽입하는 함수
     */
    private fun testDataInsert() {
        insertPlaces("서울 성동구 성수동", "카페")
        insertPlaces("서울 강남구 대치동", "약국")
        insertPlaces("서울 강남구 수서동", "약국")
    }

    /**
     * place 데이터를 db에 삽입하는 함수.
     *
     * @param address 저장할 주소값
     * @param category 저장할 카테고리값
     */
    private fun insertPlaces(
        address: String,
        category: String,
    ) {
        repeat(20) { idx ->
            placeViewModel.insertPlace(
                Place(
                    name = "$category ${idx + 1}",
                    address = "$address ${idx + 1}",
                    category = category,
                ),
            )
        }
    }

    /**
     * view들에 필요한 작업을 처리하는 함수.
     */
    private fun setupViews() {
        setClearImageViewClickListener()
        setSearchEditText()
        setupRecyclerViews()
    }

    /**
     * 검색 EditText가 변경되면 placeViewModel을 통해 검색을 수행하도록 하는 함수.
     */
    private fun setSearchEditText() {
        binding.searchEditText.addTextChangedListener { editable ->
            val searchText = editable.toString().trim()
            placeViewModel.searchPlacesByCategory(searchText)
        }
    }

    /**
     * clearImageView의 클릭 리스너를 설정하는 함수.
     *
     * searchEditText의 text를 null로 변경.
     */
    private fun setClearImageViewClickListener() {
        binding.searchClearImageView.setOnClickListener {
            binding.searchEditText.text = null
        }
    }

    interface OnPlaceItemClickListener {
        fun onPlaceItemClicked(place: Place)
    }

    /**
     * 검색 결과를 표시하는 RecyclerView를 설정하는 함수.
     *
     * - `placeItemClickListener` : placeItem을 누르면 검색어가 저장되도록 하는 클릭 리스너 interface 구현 객체
     */
    private fun setSearchResultRecyclerView() {
        val placeItemClickListener =
            object : OnPlaceItemClickListener {
                override fun onPlaceItemClicked(place: Place) {
                    savedSearchWordViewModel.insertSearchWord(
                        SavedSearchWord(
                            name = place.name,
                            placeId = place.id,
                        ),
                    )
                }
            }
        binding.searchResultRecyclerView.adapter = ResultRecyclerViewAdapter(placeItemClickListener)
        binding.searchResultRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    class ResultRecyclerViewAdapter(private val clickListener: OnPlaceItemClickListener) :
        ListAdapter<Place, ResultRecyclerViewAdapter.PlaceViewHolder>(PlaceDiffCallback()) {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): PlaceViewHolder {
            val binding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PlaceViewHolder(binding)
        }

        override fun onBindViewHolder(
            holder: PlaceViewHolder,
            position: Int,
        ) {
            val place = getItem(position)

            holder.binding.place = place
            holder.itemView.setOnClickListener {
                clickListener.onPlaceItemClicked(place)
            }
        }

        class PlaceViewHolder(val binding: ItemPlaceBinding) : RecyclerView.ViewHolder(binding.root)

        private class PlaceDiffCallback : DiffUtil.ItemCallback<Place>() {
            override fun areItemsTheSame(
                oldItem: Place,
                newItem: Place,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Place,
                newItem: Place,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnSavedSearchWordClearImageViewClickListener {
        fun onSavedSearchWordClearImageViewClicked(savedSearchWord: SavedSearchWord)
    }

    /**
     * SavedSearchWordRecyclerView를 설정하는 함수.
     *
     * - `savedSearchWordClearImageViewClickListener` : clear 버튼을 누르면 해당 저장된 검색어가 사라지도록 하는 클릭리스너 interface 구현 객체
     */
    private fun setSavedSearchWordRecyclerView() {
        val savedSearchWordClearImageViewClickListener =
            object : OnSavedSearchWordClearImageViewClickListener {
                override fun onSavedSearchWordClearImageViewClicked(savedSearchWord: SavedSearchWord) {
                    savedSearchWordViewModel.deleteSearchWordById(savedSearchWord)
                }
            }
        binding.savedSearchWordRecyclerView.adapter =
            SavedSearchWordRecyclerViewAdapter(savedSearchWordClearImageViewClickListener)
        binding.savedSearchWordRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    class SavedSearchWordRecyclerViewAdapter(private val clickListener: OnSavedSearchWordClearImageViewClickListener) :
        ListAdapter<SavedSearchWord, SavedSearchWordRecyclerViewAdapter.SavedSearchWordViewHolder>(SavedSearchWordDiffCallback()) {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): SavedSearchWordViewHolder {
            val binding = ItemSavedSearchWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SavedSearchWordViewHolder(binding)
        }

        override fun onBindViewHolder(
            holder: SavedSearchWordViewHolder,
            position: Int,
        ) {
            val savedSearchWord = getItem(position)
            holder.binding.savedSearchWord = savedSearchWord
            holder.itemView.setOnClickListener {
                clickListener.onSavedSearchWordClearImageViewClicked(savedSearchWord)
            }
        }

        class SavedSearchWordViewHolder(
            val binding: ItemSavedSearchWordBinding,
        ) : RecyclerView.ViewHolder(binding.root)

        private class SavedSearchWordDiffCallback : DiffUtil.ItemCallback<SavedSearchWord>() {
            override fun areItemsTheSame(
                oldItem: SavedSearchWord,
                newItem: SavedSearchWord,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: SavedSearchWord,
                newItem: SavedSearchWord,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    /**
     * viewModel을 관찰하도록 하는 함수.
     */
    private fun observeViewModels() {
        observeSearchResults()
        observeSavedSearchWords()
    }

    /**
     * 검색 결과를 관찰하고, RecyclerView에 결과를 반영하는 함수.
     */
    private fun observeSearchResults() {
        placeViewModel.searchResults.observe(
            this,
        ) { places ->
            (binding.searchResultRecyclerView.adapter as? ResultRecyclerViewAdapter)?.submitList(places)
        }
    }

    /**
     * 저장된 검색어를 관찰하고, RecyclerView에 결과를 반영하는 함수.
     */
    private fun observeSavedSearchWords() {
        savedSearchWordViewModel.savedSearchWords.observe(
            this,
        ) { savedSearchWords ->
            (binding.savedSearchWordRecyclerView.adapter as? SavedSearchWordRecyclerViewAdapter)?.submitList(savedSearchWords)
        }
    }
}
