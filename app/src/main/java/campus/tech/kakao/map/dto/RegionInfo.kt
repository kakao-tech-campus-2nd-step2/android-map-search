package campus.tech.kakao.map.dto

data class RegionInfo(
    val region: List<String>,
    val keyword: String,
    val selectedRegion: String
) // 지역 및 키워드 분석 정보 클래스