package campus.tech.kakao.map.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.Model.Place
import campus.tech.kakao.map.Repository.PlaceRepository
import campus.tech.kakao.map.Repository.PlaceRepositoryImpl

class SearchViewModel(private val repository: PlaceRepository) : ViewModel() {

    private val _currentResult: MutableLiveData<List<Place>> = MutableLiveData()
    val currentResult : LiveData<List<Place>> = _currentResult
    private val _favoritePlace: MutableLiveData<MutableList<Place>> = MutableLiveData()
    val favoritePlace : LiveData<MutableList<Place>> = _favoritePlace


    init {
        _favoritePlace.value = repository.getCurrentFavorite()
        _currentResult.value = listOf<Place>()
    }

    fun searchPlace(string: String) {
        _currentResult.value = if (string.isEmpty()) listOf<Place>()
        else repository.getSimilarPlacesByName(string)
    }
    fun addFavorite(name: String) {
        repository.addFavorite(name)
        val place = repository.getPlaceByName(name)

        if (favoritePlace.value == null) {
            _favoritePlace.value = mutableListOf<Place>(place)
        } else {
            if (isPlaceInFavorite(name)) return
            // favoritePlace.value에 바로 add할 시 Adapter에서 변화를 감지 못함
            val favorites = favoritePlace.value ?: mutableListOf<Place>()
            favorites.add(place)
            _favoritePlace.value = favorites
        }
    }

    fun deleteFromFavorite(name: String) {
        val place = favoritePlace.value?.find { it.name == name }
        favoritePlace.value?.remove(place)
        repository.deleteFavorite(name)
    }

    private fun getCurrentFavorite(): MutableList<Place> {
        return repository.getCurrentFavorite()
    }

    private fun isPlaceInFavorite(name: String): Boolean {
        return (favoritePlace.value?.find { it.name == name }) != null
    }

}