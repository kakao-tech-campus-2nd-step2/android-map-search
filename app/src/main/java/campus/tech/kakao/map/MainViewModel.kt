package campus.tech.kakao.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import campus.tech.kakao.map.DBHelper.PlaceDbHelper
import campus.tech.kakao.map.DBHelper.SearchWordDbHelper
import campus.tech.kakao.map.DTO.Place
import campus.tech.kakao.map.DTO.SearchWord

class MainViewModel(application: Application): AndroidViewModel(application) {
	private val placeDbHelper = PlaceDbHelper(application)
	val placeList: LiveData<List<Place>> get() =  placeDbHelper.getPlace()

	private val wordDbHelper = SearchWordDbHelper(application)
	val wordList: LiveData<List<SearchWord>> get() =  wordDbHelper.getSearchWords()

	//초기에 데이터 삽입을 위해 1번 사용
	fun insertInitData(){
		if (!placeDbHelper.existData()){
			for(i in 1..10){
				placeDbHelper.addPlace(Place("카페 $i", "남양주 $i", "카페"))
				placeDbHelper.addPlace(Place("약국 $i", "남양주 $i", "약국"))
			}
		}

	}

	fun search(query: String) {
		placeDbHelper.searchPlaceName(query)
	}

	fun addWord(place: Place){
		wordDbHelper.addWord(wordfromPlace(place))
	}

	private fun wordfromPlace(place: Place): SearchWord {
		return SearchWord(place.name, place.address, place.type)
	}
	fun deleteWord(word: SearchWord){
		wordDbHelper.deleteWord(word)
	}

	fun loadWord(){
		wordDbHelper.updateSearchWords()
	}
}