package campus.tech.kakao.map

data class SearchResult(
    val meta: PlaceMeta,
    val documents: List<PlaceDocument>
)

data class PlaceMeta(
    val total_count: Int,
    val pageable_count: Int,
    val is_end: Boolean,
    val same_name: SameName
)

data class SameName(
    val region: List<String>,
    val keyword: String,
    val selected_region: String
)

data class PlaceDocument(
    val id: String,
    val place_name: String,
    val category_name: String,
    val category_group_code: String,
    val category_group_name: String,
    val phone: String,
    val address_name: String,
    val road_address_name: String,
    val x: String,
    val y: String,
    val place_url: String,
    val distance: String
)