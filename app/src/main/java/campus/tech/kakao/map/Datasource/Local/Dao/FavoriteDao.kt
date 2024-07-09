package campus.tech.kakao.map.Datasource.Local.Dao

import campus.tech.kakao.map.Model.Place

interface FavoriteDao {
    fun getCurrentFavorite(): MutableList<Place>
    fun deleteFavorite(name: String)
    fun addFavorite(place: Place)
}