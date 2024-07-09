package campus.tech.kakao.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.data.SavedSearchWordDBHelper
import campus.tech.kakao.map.data.repository.PlaceRepositoryImpl
import campus.tech.kakao.map.data.repository.SavedSearchWordRepositoryImpl
import campus.tech.kakao.map.databinding.ActivityMainBinding
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.model.SavedSearchWord
import campus.tech.kakao.map.viewmodel.PlaceViewModel
import campus.tech.kakao.map.viewmodel.SavedSearchWordViewModel
import campus.tech.kakao.map.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var placeViewModel: PlaceViewModel
    private lateinit var savedSearchWordViewModel: SavedSearchWordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModels()
        setupViews()
        setupObservers()
    }

    /**
     * 사용할 ViewModel을 설정하는 함수
     *
     * - `placeRepository` : 장소 데이터를 제공 Repository
     * - `dbHelper` : 저장된 검색어 데이터베이스를 관리하는 Helper
     * - `savedSearchWordRepository` : 저장된 검색어 데이터를 제공 Repository
     * - `viewModelFactory` : ViewModel 인스턴스를 생성하고 제공하는 Factory
     */
    private fun setupViewModels() {
        val placeRepository = PlaceRepositoryImpl()
        val dbHelper = SavedSearchWordDBHelper(applicationContext)
        val savedSearchWordRepository = SavedSearchWordRepositoryImpl(dbHelper)
        val viewModelFactory = ViewModelFactory(placeRepository, savedSearchWordRepository)

        placeViewModel = ViewModelProvider(this, viewModelFactory)[PlaceViewModel::class.java]
        savedSearchWordViewModel = ViewModelProvider(this, viewModelFactory)[SavedSearchWordViewModel::class.java]
    }

    /**
     * RecyclerView들을 설정하는 함수.
     */
    private fun setupRecyclerViews() {
        setSearchResultRecyclerView()
        setSavedSearchWordRecyclerView()
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
            val categoryInput = editable.toString().trim()
            placeViewModel.searchPlacesByCategory(categoryInput)
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
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
            return PlaceViewHolder(itemView)
        }

        override fun onBindViewHolder(
            holder: PlaceViewHolder,
            position: Int,
        ) {
            val place = getItem(position)
            holder.bind(place)
            holder.itemView.setOnClickListener {
                clickListener.onPlaceItemClicked(place)
            }
        }

        class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(place: Place) {
                itemView.findViewById<TextView>(R.id.place_name_text_view).text = place.name
                itemView.findViewById<TextView>(R.id.place_category_text_view).text = place.category
                itemView.findViewById<TextView>(R.id.place_address_text_view).text = place.address
            }
        }

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
        ListAdapter<SavedSearchWord, SavedSearchWordRecyclerViewAdapter.SavedSearchWordViewHolder>(
            SavedSearchWordDiffCallback(),
        ) {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): SavedSearchWordViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_saved_search_word, parent, false)
            return SavedSearchWordViewHolder(itemView)
        }

        override fun onBindViewHolder(
            holder: SavedSearchWordViewHolder,
            position: Int,
        ) {
            val savedSearchWord = getItem(position)
            holder.bind(savedSearchWord)
            holder.itemView.setOnClickListener {
                clickListener.onSavedSearchWordClearImageViewClicked(savedSearchWord)
            }
        }

        class SavedSearchWordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(savedSearchWord: SavedSearchWord) {
                itemView.findViewById<TextView>(R.id.saved_search_word_text_view).text =
                    savedSearchWord.name
            }
        }

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
    private fun setupObservers() {
        collectSearchResults()
        collectSavedSearchWords()
    }

    /**
     * 검색 결과를 관찰하고, RecyclerView에 결과를 반영하는 함수.
     */
    private fun collectSearchResults() {
        lifecycleScope.launch {
            placeViewModel.searchResults.collect { places ->
                (binding.searchResultRecyclerView.adapter as? ResultRecyclerViewAdapter)?.submitList(places)
                binding.noSearchResultTextView.visibility = if (places.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    /**
     * 저장된 검색어를 관찰하고, RecyclerView에 결과를 반영하는 함수.
     */
    private fun collectSavedSearchWords() {
        lifecycleScope.launch {
            savedSearchWordViewModel.savedSearchWords.collect { savedSearchWords ->
                (binding.savedSearchWordRecyclerView.adapter as? SavedSearchWordRecyclerViewAdapter)?.submitList(
                    savedSearchWords,
                )
                binding.savedSearchWordRecyclerView.visibility =
                    if (savedSearchWords.isEmpty()) View.GONE else View.VISIBLE
            }
        }
    }
}
