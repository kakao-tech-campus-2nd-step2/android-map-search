package ksc.campus.tech.kakao.map.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ksc.campus.tech.kakao.map.BuildConfig
import ksc.campus.tech.kakao.map.models.SearchKeywordRepository
import ksc.campus.tech.kakao.map.models.SearchResult
import ksc.campus.tech.kakao.map.models.SearchResultRepository


class SearchActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val searchResultRepository: SearchResultRepository =
        SearchResultRepository.getInstance()
    private val keywordRepository: SearchKeywordRepository =
        SearchKeywordRepository.getInstance(application)

    private val _searchText: MutableLiveData<String> = MutableLiveData("")
    private val _activeContent: MutableLiveData<ContentType> = MutableLiveData(ContentType.MAP)

    val searchResult: LiveData<List<SearchResult>>
        get() = searchResultRepository.searchResult
    val keywords: LiveData<List<String>>
        get() = keywordRepository.keywords
    val searchText: LiveData<String>
        get() = _searchText
    val activeContent: LiveData<ContentType>
        get() = _activeContent

    init {
        keywordRepository.getKeywords()
    }

    private fun search(query: String) {
        searchResultRepository.search(query, BuildConfig.KAKAO_REST_API_KEY)
        switchContent(ContentType.SEARCH_LIST)
    }

    private fun addKeyword(keyword: String) {
        keywordRepository.addKeyword(keyword)
    }

    private fun deleteKeyword(keyword: String) {
        keywordRepository.deleteKeyword(keyword)
    }

    fun clickSearchResultItem(selectedItem: SearchResult) {
        addKeyword(selectedItem.name)
    }

    fun submitQuery(value: String) {
        search(value)
    }

    fun clickKeywordDeleteButton(keyword: String) {
        deleteKeyword(keyword)
    }

    fun clickKeyword(keyword: String) {
        _searchText.postValue(keyword)
        search(keyword)
    }

    fun switchContent(type: ContentType) {
        _activeContent.postValue(type)
    }

    enum class ContentType { MAP, SEARCH_LIST }
}