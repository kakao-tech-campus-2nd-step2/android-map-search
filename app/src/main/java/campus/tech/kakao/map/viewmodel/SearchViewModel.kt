package campus.tech.kakao.map.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.model.SavePlace
import campus.tech.kakao.map.repository.SearchRepository

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val searchRepo: SearchRepository = SearchRepository(application)
    private val _places: MutableLiveData<List<Place>> = MutableLiveData()
    private val _savePlaces: MutableLiveData<List<SavePlace>> = MutableLiveData()

    val places: LiveData<List<Place>> get() = _places
    val savePlaces: LiveData<List<SavePlace>> get() = _savePlaces


    fun insertDummyData(name: String, address: String, category: String) {
        searchRepo.insertPlaceDummyData(name, address, category)
    }

    fun searchPlaces(placeCategory: String) {
        _places.value = searchRepo.getSearchPlaces(placeCategory)
    }

    fun savePlaces(placeName: String) {
        searchRepo.savePlaces(placeName)
    }

    fun showSavePlace() {
        _savePlaces.value = searchRepo.showSavePlace()
    }

    fun deleteSavedPlace(savedPlaceName: String) {
        searchRepo.deleteSavedPlace(savedPlaceName)
    }
}