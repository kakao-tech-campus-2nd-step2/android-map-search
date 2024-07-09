package com.example.kakaotechcampus_2nd_step2_android.viewmodel.keyword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kakaotechcampus_2nd_step2_android.model.Item
import com.example.kakaotechcampus_2nd_step2_android.repository.keyword.KeywordRepository
import com.example.kakaotechcampus_2nd_step2_android.viewmodel.OnKeywordItemClickListener
import com.example.kakaotechcampus_2nd_step2_android.viewmodel.OnSearchItemClickListener

class KeywordViewModel(private val keywordRepository: KeywordRepository) : ViewModel(),
    OnSearchItemClickListener, OnKeywordItemClickListener {
    private val _keyword = MutableLiveData<List<String>>()
    val keyword: LiveData<List<String>>
        get() = _keyword

    private fun updateKeywordHistory(keyword: String) {
        keywordRepository.delete(keyword)
        keywordRepository.update(keyword)
        _keyword.value = keywordRepository.read()
    }

    private fun deleteKeywordHistory(keyword: String) {
        keywordRepository.delete(keyword)
        _keyword.value = keywordRepository.read()
    }

    fun readKeywordHistory() {
        _keyword.value = keywordRepository.read()
    }

    override fun onSearchItemClick(item: Item) {
        updateKeywordHistory(item.place)
    }

    override fun onKeywordItemDeleteClick(keyword: String) {
        deleteKeywordHistory(keyword)
    }
}
