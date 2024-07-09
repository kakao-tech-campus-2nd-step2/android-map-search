package campus.tech.kakao.map

data class ResultSearchKeyword(
    var documents: List<Place>
)

data class Place(
    var place_name: String,
    var address_name: String,
    var category_group_name: String
)

