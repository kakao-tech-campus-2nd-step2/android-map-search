package campus.tech.kakao.map.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.Domain.Model.Place
import campus.tech.kakao.map.Domain.PlaceRepository

class SearchViewModel(private val repository: PlaceRepository) : ViewModel() {

    private val _currentResult: MutableLiveData<List<Place>> = MutableLiveData()
    val currentResult : LiveData<List<Place>> = _currentResult
    private val _favoritePlace: MutableLiveData<MutableList<Place>> = MutableLiveData()
    val favoritePlace : LiveData<MutableList<Place>> = _favoritePlace

    init{
        _currentResult.value = listOf<Place>()
        _favoritePlace.value = repository.getCurrentFavorite().toMutableList()
    }

    fun searchPlace(string: String) {
        _currentResult.value = repository.getSimilarPlacesByName(string)
    }

    suspend fun searchPlaceRemote(name : String){
        _currentResult.postValue(repository.searchPlaceRemote(name))
    }

    fun addFavorite(name: String) {
        val place = _currentResult.value?.find {
            it.name == name
        }

        if(isPlaceInFavorite(name)) return

        place?.apply {
            repository.addFavorite(this)
            _favoritePlace.value?.add(this)
        }
    }

    fun deleteFromFavorite(name: String) {
        val place = favoritePlace.value?.find { it.name == name }
        favoritePlace.value?.remove(place)
        repository.deleteFavorite(name)
    }

    private fun isPlaceInFavorite(name: String): Boolean {
        return (favoritePlace.value?.find { it.name == name }) != null
    }

}