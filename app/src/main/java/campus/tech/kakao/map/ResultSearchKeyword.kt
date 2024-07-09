package campus.tech.kakao.map

import com.google.gson.annotations.SerializedName

data class ResultSearchKeyword(
    @SerializedName("documents") val documents: List<Place>
)