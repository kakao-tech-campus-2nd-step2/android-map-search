package campus.tech.kakao.map

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<StoreInfo>>(emptyList())
    val searchResults: StateFlow<List<StoreInfo>> get() = _searchResults
    private val _savedSearchWords = MutableStateFlow<List<SearchWord>>(emptyList())
    val savedSearchWords: StateFlow<List<SearchWord>> get() = _savedSearchWords
    init{
        setDefaultData()
        getSavedSearchWords()
    }

    private fun setDefaultData() {
        searchRepository.setDefaultData()
    }
    fun getSearchResults(searchWord: SearchWord) {
        _searchResults.value = searchRepository.getSearchResults(searchWord)
    }
    fun saveSearchWord(searchWord: SearchWord){
        searchRepository.saveSearchWord(searchWord)
        getSavedSearchWords()
    }
    fun getSavedSearchWords(){
        _savedSearchWords.value = searchRepository.getSavedSearchWords()
    }

    fun delSavedSearchWord(searchWord: SearchWord){
        searchRepository.delSavedSearchWord(searchWord)
        getSavedSearchWords()
    }
}