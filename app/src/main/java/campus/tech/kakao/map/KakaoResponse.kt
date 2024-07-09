package campus.tech.kakao.map

data class KakaoResponse(
    val meta: Meta,
    val documents: List<Document>
)

data class Meta(
    val same_name: Any?,
    val pageable_count: Int,
    val total_count: Int,
    val is_end: Boolean
)

data class Document(
    val place_name: String,
    val distance: String?,
    val place_url: String,
    val category_name: String,
    val address_name: String,
    val road_address_name: String,
    val id: String,
    val phone: String?,
    val category_group_code: String,
    val category_group_name: String,
    val x: String,
    val y: String
)