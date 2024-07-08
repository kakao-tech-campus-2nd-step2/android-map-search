package campus.tech.kakao.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import campus.tech.kakao.map.DBHelper.SearchWordDbHelper
import campus.tech.kakao.map.DTO.Document
import campus.tech.kakao.map.DTO.SearchWord

class MainViewModel(application: Application): AndroidViewModel(application) {

	private val wordDbHelper = SearchWordDbHelper(application)
	val wordList: LiveData<List<SearchWord>> get() =  wordDbHelper.getSearchWords()

	private val retrofitData = RetrofitData()
	val documentList: LiveData<List<Document>> get() = retrofitData.getDocuments()

	fun addWord(document: Document){
		wordDbHelper.addWord(wordfromDocument(document))
	}

	private fun wordfromDocument(document: Document): SearchWord {
		return SearchWord(document.place_name, document.category_group_name, document.address_name)
	}
	fun deleteWord(word: SearchWord){
		wordDbHelper.deleteWord(word)
	}

	fun loadWord(){
		wordDbHelper.updateSearchWords()
	}

	fun searchLocalAPI(query: String){
		retrofitData.searchPlace(query)
	}
}