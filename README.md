# android-map-keyword
# 기능 구현 내용
1. 검색어 입력 및 검색 결과를 표시할 기본 레이아웃을 구현한다. 
2. 검색에 사용될 데이터를 로컬 데이터베이스에 생성한다. 
# 프로그래밍 요구 사항
1. 검색 데이터 저장은 SQLite를 사용한다. 
2. 가능한 MVVM 아키텍쳐 패턴을 적용하도록 한다.
# 구현 과정
1. database.kt파일을 생성하여 데이터베이스 생성 및 관리
2. SearchRepo에서 데이터 CRUD 작업 
3. SearchViewModel에서 데이터 처리 및 제공
4. MainActivity에서 데이터 활용
5. "검색 결과가 없습니다." 단어의 경우 visibility로 처리, 단어와 연관된 database가 없을때 보여짐
6. ListView를 사용해서 데이터베이스에서 결과를 가져와 표시해줌
# 소감 및 의문점
1. 안드로이드 에뮬레이터가 컴퓨터 사양이 부족한지 자꾸 꺼졌다가 켜집니다.
2. intent-filter 문제인 것 같았는데 android:exported="true" 로 해봐도 똑같습니다..
3. database.kt의 내용을 Mainactivity.kt에도 똑같은 코드를 쓴것 같아서 이걸 어떻게 수정을 해야할 지 모르겠습니다.
4. database.kt에서 정의한 함수를 Mainactivity.kt에서 못받는 것 같습니다. 