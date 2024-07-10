package campus.tech.kakao.map.domain.repository

import campus.tech.kakao.map.domain.model.Place

interface PlaceRepository {
    fun getPlaces(placeName: String): List<Place>
    fun updatePlaces(places:List<Place>)
    fun addLog(placeLog: Place)
    fun removeLog(id: String)
    fun getLogs(): List<Place>

}