package campus.tech.kakao.map

import androidx.annotation.DrawableRes

data class Place(
    @DrawableRes val img: Int,
    val name: String,
    val location: String,
    val category: String
) {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Place -> {
                img == other.img &&
                name == other.name &&
                location == other.location &&
                category == other.category
            }
            else -> false
        }
    }

    

}
