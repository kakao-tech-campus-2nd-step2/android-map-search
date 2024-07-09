package campus.tech.kakao.map.data.network.dto

import com.google.gson.annotations.SerializedName

data class PlaceMeta(
    @SerializedName("same_name") val sameName: Boolean,
    @SerializedName("pageable_count") val pageableCount: Int,
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("is_end") val isEnd: Boolean,
)
