package campus.tech.kakao.map.Data.Datasource.Remote.Response

import org.json.JSONArray
import org.json.JSONObject

data class SearchResponse(
    val documents: List<Document>,
    val meta: Meta
)

data class SameName(
    val keyword: String,
    val region: String,
    val selected_region: String
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
    val is_end: Boolean,
    val pageable_count: Int,
    val same_name: SameName,
    val total_count: Int
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
    val address_name: String,
    val category_group_code: String,
    val category_group_name: String,
    val category_name: String,
    val distance: String,
    val id: String,
    val phone: String,
    val place_name: String,
    val place_url: String,
    val road_address_name: String,
    val x: String,
    val y: String
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
