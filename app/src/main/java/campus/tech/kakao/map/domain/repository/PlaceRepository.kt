package campus.tech.kakao.map.domain.repository

import campus.tech.kakao.map.domain.model.Place

interface PlaceRepository{
    fun searchPlaces(apiKey: String, query: String, callback: (List<Place>?) -> Unit)
}
