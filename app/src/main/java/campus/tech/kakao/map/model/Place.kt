package campus.tech.kakao.map.model

data class Place(
    val id: Long = 0,
    val name: String,
    val address: String? = null,
    val category: String? = null,
)
