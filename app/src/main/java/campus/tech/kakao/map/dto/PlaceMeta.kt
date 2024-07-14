package campus.tech.kakao.map.dto

data class PlaceMeta(
    val totalCount: Int,
    val pageableCount: Int,
    val isEnd: Boolean,
    val sameName: RegionInfo
) // 검색 결과 메타데이터 클래스