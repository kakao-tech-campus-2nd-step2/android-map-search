package campus.tech.kakao.map.dto

data class PlaceMeta(
    val total_count: Int,                // 검색된 전체 문서 수
    val pageable_count: Int,             // 노출 가능한 문서 수
    val is_end: Boolean,                 // 현재 페이지가 마지막 페이지인지 여부
    val same_name: RegionInfo            // 검색어의 지역 및 키워드 분석 정보
) // 검색 결과 메타데이터 클래스