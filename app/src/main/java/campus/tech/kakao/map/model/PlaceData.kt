package campus.tech.kakao.map.model


data class SearchResponse(
    val documents: List<PlaceDocument>,
)

data class PlaceDocument(
    val id: Int,
    val place_name: String,
    val address_name: String,
    val category_group_name: String
)

data class PlaceData(
    val id: Int,
    val name: String,
    val location: String,
    val category: String
)
