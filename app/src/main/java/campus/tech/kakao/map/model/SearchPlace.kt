package campus.tech.kakao.map.model


data class SearchPlace(
    var placeInfo: List<PlaceInfo>
)

data class PlaceInfo(
    var placeName: String,
    var categoryName: String,
    var addressName: String,
    var roadAddressName: String
)