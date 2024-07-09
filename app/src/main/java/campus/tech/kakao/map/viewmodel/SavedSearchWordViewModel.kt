package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.repository.SavedSearchWordRepository
import campus.tech.kakao.map.model.SavedSearchWord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SavedSearchWordViewModel(private val repository: SavedSearchWordRepository) : ViewModel() {
    private val _savedSearchWords = MutableLiveData<List<SavedSearchWord>>()
    val savedSearchWords: LiveData<List<SavedSearchWord>> get() = _savedSearchWords

    init {
        getAllSearchWords()
    }

    fun insertSearchWord(searchWord: SavedSearchWord) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertOrUpdateSearchWord(searchWord)
            getAllSearchWords()
        }
    }

    fun deleteSearchWordById(searchWord: SavedSearchWord) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSearchWordById(searchWord.id)
            val currentList = _savedSearchWords.value ?: emptyList()
            _savedSearchWords.postValue(currentList - searchWord)
        }
    }

    private fun getAllSearchWords() {
        viewModelScope.launch(Dispatchers.IO) {
            val searchWords = repository.getAllSearchWords()
            _savedSearchWords.postValue(searchWords)
        }
    }
}
