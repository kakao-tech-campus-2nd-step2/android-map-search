package campus.tech.kakao.map.data.network

import campus.tech.kakao.map.data.network.dto.Place
import campus.tech.kakao.map.data.network.dto.PlaceMeta

data class PlaceResponse(
    val meta: PlaceMeta,
    val documents: List<Place>,
)
