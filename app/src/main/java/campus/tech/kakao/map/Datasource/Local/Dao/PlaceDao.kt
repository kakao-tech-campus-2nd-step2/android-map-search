package campus.tech.kakao.map.Datasource.Local.Dao

import android.database.Cursor
import campus.tech.kakao.map.Model.Place

interface PlaceDao {
    fun deletePlace(name: String)
    fun getSimilarPlacesByName(name: String) : List<Place>
    fun getPlaceByName(name: String): Place
}