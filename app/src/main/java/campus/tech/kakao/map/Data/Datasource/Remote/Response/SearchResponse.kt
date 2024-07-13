package campus.tech.kakao.map.Data.Datasource.Remote.Response

import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject

data class SearchResponse(
    val documents: List<Document>,
    val meta: Meta
)

data class SameName(
    @SerializedName("keyword") val keyword: String,
    @SerializedName("region") val region: String,
    @SerializedName("selected_region") val selectedRegion: String
) {
    companion object{
        fun fromJSON(sameNameJsonObject : JSONObject) : SameName{
            return SameName(
                sameNameJsonObject.get("keyword").toString(),
                sameNameJsonObject.getJSONArray("region").toString(),
                sameNameJsonObject.get("selected_region").toString()
            )
        }
    }
}


data class Meta(
    @SerializedName("is_end") val isEnd: Boolean,
    @SerializedName("pageable_count") val pageableCount: Int,
    @SerializedName("same_name") val sameName: SameName,
    @SerializedName("total_count") val totalCount: Int
) {
    companion object{
        fun fromJSON(metaJsonObject : JSONObject) : Meta{
            val sameName = SameName.fromJSON(
                metaJsonObject.getJSONObject("same_name")
            )

            return Meta(
                metaJsonObject.get("is_end") as Boolean,
                metaJsonObject.get("pageable_count") as Int,
                sameName,
                metaJsonObject.get("total_count") as Int
            )
        }
    }
}

data class Document(
    @SerializedName("address_name") val addressName: String,
    @SerializedName("category_group_code") val categoryGroupCode: String,
    @SerializedName("category_group_name") val categoryGroupName: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("distance") val distance: String,
    @SerializedName("id") val id: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("place_name") val placeName: String,
    @SerializedName("place_url") val placeUrl: String,
    @SerializedName("road_address_name") val roadAddressName: String,
    @SerializedName("x") val x: String,
    @SerializedName("y") val y: String
) {
    companion object {
        fun fromJSON(documentJsonArray: JSONArray) : List<Document>{
            val result = mutableListOf<Document>()
            for (i in 0..<documentJsonArray.length()) {
                val documentJson = documentJsonArray.getJSONObject(i)
                result.add(
                    Document(
                        documentJson.get("address_name").toString(),
                        documentJson.get("category_group_code").toString(),
                        documentJson.get("category_group_name").toString(),
                        documentJson.get("category_name").toString(),
                        documentJson.get("distance").toString(),
                        documentJson.get("id").toString(),
                        documentJson.get("phone").toString(),
                        documentJson.get("place_name").toString(),
                        documentJson.get("place_url").toString(),
                        documentJson.get("road_address_name").toString(),
                        documentJson.get("x").toString(),
                        documentJson.get("y").toString()
                    )
                )
            }
            return result
        }
    }
}
