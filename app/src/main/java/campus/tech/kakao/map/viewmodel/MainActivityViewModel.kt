package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.model.SavedPlace
import campus.tech.kakao.map.repository.KakaoLocalRepository
import campus.tech.kakao.map.repository.PlaceRepository
import campus.tech.kakao.map.repository.SavedPlaceRepository


class MainActivityViewModel(
    private val placeRepository: PlaceRepository,
    private val savedPlaceRepository: SavedPlaceRepository,
    private val kakaoLocalRepository : KakaoLocalRepository
) : ViewModel() {
    private val _place = MutableLiveData<List<Place>>()
    private val _savedPlace = MutableLiveData<List<SavedPlace>>()
    val place: LiveData<List<Place>> get() = _place
    val savedPlace: LiveData<List<SavedPlace>> get() = _savedPlace
    init{
        getSavedPlace()
        getPlaceWithCategory("")
    }
    fun getPlace() {
        _place.postValue(placeRepository.getAllPlace())
    }

    fun getPlaceWithCategory(category: String) {
        _place.postValue(placeRepository.getPlaceWithCategory(category))
    }

    fun getSavedPlace() {
        _savedPlace.postValue(savedPlaceRepository.getAllSavedPlace())
    }

    fun savePlace(place: Place) {
        savedPlaceRepository.writePlace(place)
        getSavedPlace()
    }

    fun deleteSavedPlace(savedPlace: SavedPlace){
        savedPlaceRepository.deleteSavedPlace(savedPlace)
        getSavedPlace()
    }

    fun getKakaoLocalData(text:String){
    }


}