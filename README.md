# android-map-search

- 카카오 테크 캠퍼스 과제(위치 검색 - 네트워크 통신) 수행을 위한 저장소입니다.

## 동작 영상
![week3_step1](https://github.com/kakao-tech-campus-2nd-step2/android-map-search/assets/102301656/b46ccdb7-7f91-452d-a7e1-f5a0c73edeb9)

## flow chart

<img src=https://github.com/ichanguk/android-map-search/blob/step1/image/map_search_flow_chart.png width="600" height="250">

## feature

### 1단계 - 카카오 로컬 API

1. 검색어를 입력하면 검색 결과(15개 이상) 목록이 표시된다.
    - 리사이클러뷰 사용(세로 스크롤)

2. 입력한 검색어는 X를 눌러서 삭제할 수 있다.

3. 검색 결과 목록에서 하나의 항목을 선택할 수 있다.
    - 선택된 항목은 검색어 저장 목록에 추가된다.
    - 리사이클러뷰 사용(가로 스크롤)

4. 저장된 검색어는 X를 눌러서 삭제할 수 있다.

5. 이미 검색어 저장 목록에 있는 검색어를 검색 결과 목록에서 선택한 경우 기존 검색어는 삭제하고 다시 추가한다.

### 2단계 - 카카오 지도 API

1. 앱을 처음 실행하면 지도 화면이 표시된다.

2. 검색창을 선택하면 검색 화면으로 이동한다.
    - 검색 화면에서 뒤로 가기를 하면 지도 화면으로 돌아온다.