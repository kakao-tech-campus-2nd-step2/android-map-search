package campus.tech.kakao.map

data class KakaoMapProductResponse(
    val documents: List<Document>
)

data class Document(
    val place_name: String,
    val address_name: String,
    val category_name: String
)
