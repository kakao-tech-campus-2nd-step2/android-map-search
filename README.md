# android-map-keyword STEP 1

## 개요
android-map-keyword STEP1에서는 Android Studio에서 간단한 검색창 뷰를 구현하고, 로컬 데이터베이스를 생성합니다.
- `EditText`가 아닌 `SearchView`라는 새로운 뷰를 적용해보았습니다.
- `SQLiteOpenHelper` 클래스를 오버라이딩하는 `DBHelper` 클래스를 만들고, 해당 클래스의 메소드를 통해 로컬 데이터베이스에 샘플 데이터를 추가했습니다.

## 기능
- `SearchView`를 통한 검색어 입력
- `DBHelper` - `onCreate`, `onUpgrade`, `insert` 메소드를 이용해 DB 생성, 업그레이드, 데이터 삽입이 가능
- 검색어 입력 시 실시간으로 결과 표시(STEP 2)
