package campus.tech.kakao.map

import android.provider.DocumentsContract.Document
import com.google.gson.annotations.SerializedName


data class PlaceResponse(
    @SerializedName("documents") val documents: List<Documents>,
    @SerializedName("meta") val meta: Meta
)

data class Meta(
    @SerializedName("is_end") val isEnd: Boolean,
    @SerializedName("pageable_count") val pageableCount: Int,
    @SerializedName("same_name") val sameName: SameName,
    @SerializedName("total_count") val totalCount: Int
)

data class SameName(
    @SerializedName("keyword") val keyword: String,
    @SerializedName("region") val region: List<Any>,
    @SerializedName("selected_region") val selectedRegion: String
)


data class Documents(
    val id: String,
    val place_name: String,
    val category_name: String,
    val category_group_code: String,
    val category_group_name: String,
    val phone: String,
    val address_name: String,
    val road_address_name: String,
    val x: String,
    val y: String,
    val place_url: String,
    val distance: String,
)