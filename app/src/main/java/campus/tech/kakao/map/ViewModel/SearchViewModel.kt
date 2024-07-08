package campus.tech.kakao.map.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.Model.Place
import campus.tech.kakao.map.Repository.PlaceRepository

class SearchViewModel(private val repository: PlaceRepository) : ViewModel() {

    private val _currentResult: MutableLiveData<List<Place>> = MutableLiveData()
    val currentResult : LiveData<List<Place>> = _currentResult
    private val _favoritePlace: MutableLiveData<MutableList<Place>> = MutableLiveData()
    val favoritePlace : LiveData<MutableList<Place>> = _favoritePlace


    init {
        repository.favoritePlace.observeForever{
            _favoritePlace.value = it
        }
        repository.currentResult.observeForever{
            _currentResult.value = it
        }
    }

    fun searchPlace(string: String) {
        repository.getSimilarPlacesByName(string)
    }

    suspend fun searchPlaceRemote(name : String){
        repository.searchPlaceRemote(name)
    }

    fun addFavorite(name: String) {
        repository.addFavorite(name)
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