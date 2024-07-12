package campus.tech.kakao.map.dto

data class Place(
    val addressName: String,
    val categoryGroupCode: String,
    val categoryGroupName: String,
    val categoryName: String,
    val distance: String,
    val id: String,
    val phone: String,
    val placeName: String,
    val placeUrl: String,
    val roadAddressName: String,
    val x: String,
    val y: String
) // 장소 정보 클래스