package campus.tech.kakao.map.data.network.dto

import com.google.gson.annotations.SerializedName

data class Place(
    @SerializedName("id") val placeId: String,
    @SerializedName("place_name") val placeName: String,
    @SerializedName("address_name") val address: String,
    @SerializedName("category_group_name") val category: String?,
)
