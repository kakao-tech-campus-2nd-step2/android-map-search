# android-map-search

# 1단계
## 기능 요구 사항
- 검색어를 입력하면 검색 결과가 15개 이상 표시된다.
- 검색 결과 목록은 세로 스크롤이 된다.
- 입력한 검색어는 X를 눌러서 삭제할 수 있다.
- 검색 결과 목록에서 하나의 항목을 선택할 수 있다.
- 선택된 항목은 검색어 저장 목록에 추가된다.
- 저장된 검색어 목록은 가로 스크롤이 된다.
- 저장된 검색어는 X를 눌러서 삭제할 수 있다.
- 저장된 검색어는 앱을 재실행하여도 유지된다.

## 프로그래밍 요구 사항
- 검색 데이터는 카카오로컬 API를 사용한다.
- 카카오 API 사용을 위한 앱 키를 외부에 노출하지 않는다.
- 가능한 MVVM 아키텍처 패턴을 적용하도록 한다.
- 코드 컨벤션을 준수하며 프로그래밍한다.

## 구현할 기능 목록
- KAKAO Search Service 구현
- SearchResultRepository 리스트를 KAKAO Search Service를 통해 불러오도록 변경

# 2단계

## 구현할 기능 목록
- 카카오 맵 API와 연동하여 맵을 보여주는 기능 추가
- 검색 창 선택시 검색 화면으로 전환하는 기능 추가
- 뒤로가기 콜백 처리