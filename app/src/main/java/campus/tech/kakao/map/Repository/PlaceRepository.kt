package campus.tech.kakao.map.Repository

import campus.tech.kakao.map.Model.Place

interface PlaceRepository {
    fun getCurrentFavorite(): MutableList<Place>
    fun getSimilarPlacesByName(name: String): List<Place>?
    fun addFavorite(name: String)
    fun getPlaceByName(name: String): Place
    fun deleteFavorite(name: String)
    suspend fun a(name : String) : List<Place>?
}