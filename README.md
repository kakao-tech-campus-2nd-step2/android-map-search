# android-map-keyword

###1단계###

1. main ui 만들기
   1. 검색어 입력 창 (editText)
   2. 검색어 삭제 x 표시
   3. 검색결과를 보여주는 뷰 (ListView or recyclerView)
   4. 아이템뷰 만들기

2. SQLite 설계
   1. SQLiteOpenHelper 클래스 생성
   2. SQLiteDatabase 클래스 생성
      1. insert함수 작성
      2. select함수 작성

3. 리스트뷰 어댑터 설계

###2단계###

1. 리스트뷰 -> 리사이클러뷰로 변경
2. 검색어 저장 리스트뷰 설계
3. (close_icon)버튼 기능 제작
4. (리사이클러뷰) 뷰에 클릭리스너를 달아서 검색어 저장 리스트뷰에 추가
5. 저장된 검색어 데이터 베이스 설계