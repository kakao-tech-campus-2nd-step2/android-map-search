package campus.tech.kakao.map.Repository

import androidx.lifecycle.LiveData
import campus.tech.kakao.map.Model.Place

interface PlaceRepository {
    val currentResult : LiveData<List<Place>>
    val favoritePlace : LiveData<MutableList<Place>>
    fun getCurrentFavorite()
    fun getSimilarPlacesByName(name: String)
    fun addFavorite(name: String)
    fun getPlaceByName(name: String): Place
    fun deleteFavorite(name: String)
    suspend fun searchPlaceRemote(name : String)
}