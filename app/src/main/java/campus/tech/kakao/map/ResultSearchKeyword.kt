package campus.tech.kakao.map

data class ResultSearchKeyword(
    var document: List<Place>
)

data class place {
    val place_name: String,
    val address_name: String,
    val category_group_name: String
}