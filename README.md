# 💁‍♀️android-map-keyword
이 App은 USER가 검색어를 입력하면, 관련된 검색결과들이 화면에 표시되고, 클릭시 검색어를 저장할 수 있다. 

저장된 검색어는 앱을 재실행해도 유지된다.

## 기능✍️
1. **검색어 입력**
   - 검색어_입력란에 검색어(장소이름 혹은 장소카테고리)를 입력한다.
   - 검색어_입력란 옆에 X 버튼을 누르면 입력한 검색어는 삭제된다.
  
     
2. **검색결과 목록**
   - 입력한 검색어에 대한 검색결과 목록이 표시된다.
       - category 입력시 _category와 관련된 모든 장소가_ 표시된다
       - 상세이름(name) 입력시 _해당 장소만_ 표시된다
   - 검색 결과 목록은 **세로 스크롤**로 표시된다.
   - 검색 결과 목록 中 특정 Card를 선택할 수 있다.
  
           
3. **사용자_검색어 저장**
    - 선택한 card는 검색어_저장목록에 저장된다
    - 검색어_저장목록은 **가로 스크롤**로 표시된다
    - 저장된 검색어의 X 버튼을 누르면 검색어_저장목록에서 삭제된다
    - 검색어_저장목록은 앱을 재실행해도 유지된다

***
## 실행화면✍️
1. 첫 검색화면

   
![첫검색화면](https://github.com/arieum/android-map-keyword/blob/arieum_step1/%EC%B2%AB%20%EA%B2%80%EC%83%89%ED%99%94%EB%A9%B4.png)


2. 검색어 입력시 ver1.카테고리

   
![검색어입력ver1](https://github.com/arieum/android-map-keyword/blob/arieum_step1/%EA%B2%80%EC%83%89%EC%96%B4%EC%9E%85%EB%A0%A5%EC%8B%9C.png)

3. 검색어 입력시 ver2.상세입력검색

   
![검색어입력ver2](https://github.com/arieum/android-map-keyword/blob/arieum_step1/%EC%83%81%EC%84%B8%EC%9D%B4%EB%A6%84%EA%B2%80%EC%83%89%EA%B0%80%EB%8A%A5.png)

4. 카드클릭시_tabRecyclerView_등록

   
![카드클릭시](https://github.com/arieum/android-map-keyword/blob/arieum_step1/%EC%B9%B4%EB%93%9C%ED%81%B4%EB%A6%AD%EC%8B%9C.png)

5. 앱_재실행시에도, 검색기록 유지

   
![검색기록유지_동영상](https://github.com/arieum/android-map-keyword/blob/arieum_step1/%EA%B2%80%EC%83%89%EA%B2%B0%EA%B3%BC%EC%9C%A0%EC%A7%80.webm)

***
## 세부구현_명세
🔗activity_main.xml


- 초기 모든화면 UI를 구성 (ConstraintLayout)
      - 검색어 입력을 위한 EditText

      - 검색어 입력_삭제를 위한 ImageView

      - 검색어_저장목록을 위한 RecyclerView

      - 검색결과를 불러오기 전 빈 화면을 나타내는 TextView

      - 검색어 입력시 검색_결과화면을 나타내는 RecyclerVIew   

🔗place_card.xml

- 검색_결과화면에 나타낼 각 아이템별 cardview[카드뷰] (LinearLayout)
      - 장소_사진을 보여주기 위한 ImageVIew


      - 장소_이름을 보여주기 위한 TextVIew


      - 장소_위치를 보여주기 위한 TextView


🔗tab_card.xml

- 검색어_저장화면에 나타낼 각 아이템별 cardview[카드뷰] (LinearLayout)
    - 취소버튼 ImageView
 
    - 장소_이름을 보여주기 위한 TextView


🧾MainActivity
- UI 초기화
- 변경된 데이터를 update하여 뷰 처리


🧾RecylcerViewAdapter
- 검색결과를 표시하는 RecyclerVIew의 어답터


🧾TabViewAdapter
- 저장된_검색어 목록을 표시하는 RecyclerView의 어답터 


🧾PlaceRepository
- DB와의 상호작용을 관리하는 클래


🧾PlaceDbHelper
- SQLiteOpenHelper를 상속받아 DB를 생성하고 관리


🏷️ Place.kt
- 데이터 틀 생성
- Property
    - img
    - name
    - location
    - category


🏷️ MyPlaceContract
- DB를 위한 상수 설정
- DB : MyPlace
    - Table1 : place (장소에 대한 모든 객체가 들어있음)
    - Tabale2 : research (사용자가 검색한 기록_객체만 들어있음)



