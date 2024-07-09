package campus.tech.kakao.map.data.repository

import campus.tech.kakao.map.model.Place

interface PlaceRepository {
    fun getPlacesByCategory(
        categoryInput: String,
        callback: (List<Place>) -> Unit,
    )
}
