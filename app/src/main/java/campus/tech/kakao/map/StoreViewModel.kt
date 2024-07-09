package campus.tech.kakao.map
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StoreViewModel(private val storeDao: StoreDao) : ViewModel() {

    private val _searchResults = MutableLiveData<List<StoreEntity>>()
    val searchResults: LiveData<List<StoreEntity>> = _searchResults

    private val _savedSearches = MutableLiveData<List<StoreEntity>>()
    val savedSearches: LiveData<List<StoreEntity>> = _savedSearches

    init {
        loadSavedSearches()
    }

    private fun loadSavedSearches() {
        viewModelScope.launch(Dispatchers.IO) {
            val savedSearches = storeDao.getSavedSearches()
            _savedSearches.postValue(savedSearches)
        }
    }

    fun search(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val results = storeDao.search("%$query%")
            _searchResults.postValue(results)
        }
    }

    fun saveSearch(store: StoreEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentSavedSearches = _savedSearches.value.orEmpty().toMutableList()
            if (currentSavedSearches.contains(store)) {
                currentSavedSearches.remove(store)
            }
            currentSavedSearches.add(0, store) // 리스트의 가장 앞에 추가 -> 최신순
            _savedSearches.postValue(currentSavedSearches)
        }
    }

    fun removeSavedSearch(store: StoreEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentSavedSearches = _savedSearches.value.orEmpty().toMutableList()
            currentSavedSearches.remove(store)
            _savedSearches.postValue(currentSavedSearches)
        }
    }

}
