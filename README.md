# android-map-keyword

## 검색어 입력 화면.xml
ConstraintLayout  
    - LinearLayout "horizontal/bottom_border"(@id/input_layout)  
        - Textview "검색어를 입력해주세요" (@id/input)  
        - ImageView "X" (@id/cancle_button)  
    - LinearLayout  
        최초 : 검색어 입력전  
            - Textview "검색 결과가 없습니다"  
        검색어 클릭 로그가 있는 경우 : 검색어 입력전  
            - ScrollView[Listview/RecyclerVIew] "horizontal/tap_item"  
            - TextView "검색결과가 없습니다"  
        검색어 입력후  
            - Scrollview  
            - RecyclerView  
            - Textview isNotvisible  

## tab_card.xml
imageview - textview

## RecyclerView
    └ recycler_container.xml  
    └ Adapter.class  
    └ recycler_card.xml  

## 검색어 클릭시 -> 검색어 결과 화면.xml
