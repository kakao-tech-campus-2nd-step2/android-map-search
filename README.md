# android-map-search
## KakaoTechCampus 2기 3주차 과제 : 위치검색 - 네트워크 통신 

### 기능 구현
- 카카오 지도 SDK 키 해시 등록하기
- Application 파일 작성하기
- build.gradle 의존성 파일 추가하기
- 지도 MainActivity 기능 구현
- 지도 레이아웃 구현
- 기존 MainActivity 화면 -> SearchActivity
- AndroidManifest.xml 파일 기능 작성하기

### 디렉토리 구조
```bash
app
├── build
├── src
│   ├── androidTest
│   │   └── java
│   │       └── campus.tech.kakao,map
│   │           └── .gitkeep
│   ├── main
│   │   ├── java
│   │   │   └── campus.tech.kakao
│   │   │       │── map
│   │   │       │   ├── MainActivity.kt
│   │   │       │   ├── MapAccess.kt
│   │   │       │   ├── MapItem.kt
│   │   │       │   ├── MapViewModel.kt
│   │   │       │   └── SelectedAdapter.kt
│   │   │       │   ├── SelectedAdapter.kt
│   │   │       │   └── SQLiteHelper.kt
│   │   │       │   └── KakaoApiService.kt
│   │   │       │   └── KakaoSearchResponse.kt
│   │   │       │   └── RetrofitInstance.kt
│   │   │       │   └── SearchActivity.kt
│   │   │       └── MyApplication.kt
│   │   └── res
│   │       ├── drawable
│   │       │   ├── id_delete.xml
│   │       │   ├── ic_launcher_background.xml
│   │       │   ├── ic_launcher_foreground.xml
│   │       │   └── ic_location_marker.xml
│   │       │   └── search_background.xml
│   │       ├── layout
│   │       │   ├── activity_main.xml
│   │       │   ├── activity_search.xml
│   │       │   ├── item_search_result.xml
│   │       │   └── item_selected.xml
│   │       │── values
│   │       │   ├── colors.xml
│   │       │   ├── strings.xml
│   │       │   └── themes.xml
│   │       └── AndroidManifest.xml
├── .gitignore
├── build.gradle.kts
└── proguard-rules.pro
```

### 구현 화면
<img src="https://github.com/YJY1220/DATA/assets/93771689/e6a77da3-1559-49e5-93c8-3fd51e642a66" width="200" height="400"/>
<img src="https://github.com/YJY1220/DATA/assets/93771689/f2ad4062-304d-48b4-87c6-6294f312cd57" width="200" height="400"/>
<img src="https://github.com/YJY1220/DATA/assets/93771689/8a0d0d57-38c7-4ef0-9420-9e58fc812627" width="200" height="400"/>
<img src="https://github.com/YJY1220/DATA/assets/93771689/8f0c63d7-4be8-4fa3-84f7-9d8469b9f0a5" width="200" height="400"/>
<img src="https://github.com/YJY1220/DATA/assets/93771689/aa55af71-0e89-4e12-9060-aabecf30ff6f" width="200" height="400"/>

### 실행영상
https://youtube.com/shorts/7CwPrAi0BnU?feature=share