package campus.tech.kakao.map

import androidx.annotation.DrawableRes

data class Place(
    @DrawableRes val img: Int,
    val name: String,
    val location: String,
    val category: String
)