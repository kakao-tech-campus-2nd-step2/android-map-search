package campus.tech.kakao.map.DTO

import com.google.gson.annotations.SerializedName

data class Meta(
	@SerializedName("total_count")
	val totalCount: Int,
	@SerializedName("pageable_count")
	val pageableCount: Int,
	@SerializedName("is_end")
	val isEnd: Boolean,
	@SerializedName("same_name")
	val sameName: SameName
)
