package campus.tech.kakao.map.dto

data class RegionInfo(
    val region: List<String>,            // 인식된 지역 리스트
    val keyword: String,                 // 검색 키워드
    val selected_region: String          // 검색에 사용된 지역 정보
) // 지역 및 키워드 분석 정보 클래스