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
        getFavorite()
    }
    fun searchPlace(string: String) {
        _currentResult.value = repository.getSimilarPlacesByName(string)
    }

    fun searchPlaceRemote(name : String){
        _currentResult.postValue(repository.getPlaceByNameHTTP(name))
    }

    fun addFavorite(name: String) {
        val place = _currentResult.value?.find {
            it.name == name
        }

        if(isPlaceInFavorite(name)) return

        place?.let {
            repository.addFavorite(it)
            getFavorite()
        }
    }

    fun deleteFromFavorite(name: String) {
        repository.deleteFavorite(name)
        getFavorite()
    }

    private fun isPlaceInFavorite(name: String): Boolean {
        return (favoritePlace.value?.find { it.name == name }) != null
    }

    private fun getFavorite(){
        _favoritePlace.value = repository.getCurrentFavorite().toMutableList()
    }


}