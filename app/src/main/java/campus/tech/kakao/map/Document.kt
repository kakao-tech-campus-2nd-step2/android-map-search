package campus.tech.kakao.map

import com.google.gson.annotations.SerializedName

data class Document(
    @SerializedName("address_name") val addressName: String,
    @SerializedName("road_address") val roadAddress: RoadAddress?,
    @SerializedName("place_name") val placeName: String?
)