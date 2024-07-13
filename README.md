# android-map-search
## 🙋‍♀️ 개요
이 App은 사용자가 검색어를 입력하여 검색 결과를 확인하고, 선택된 항목을 저장할 수 있는 기능을 제공한다. 저장된 검색어 목록은 앱을 재실행해도 유지된다. 검색 데이터는 카카오로컬 API를 사용하고, 지도는 카카오지도 SDK를 사용한다. 

## ✨ 주요기능
>**STEP1_검색화면**
- 검색어를 입력하면 검색 결과가 15개 이상 표시된다
  - 검색 결과_데이터는 <ins>카카오로컬 API</ins>를 사용한다

>**STEP2_지도화면**
- 앱 처음 실행시 검색화면 X, 지도화면이 표시되도록 한다
  - 지도화면은 <ins>카카오지도 SDK</ins>를 사용한다
- `검색창 클릭`시 검색화면이으로 이동하도록 한다
- 검색화면에서 `단말기_뒤로가기`를 하면 지도화면으로 돌아온다
  

## 📱 실행화면
1. 앱 처음 실행시, 지도화면 표시


![지도화면](https://github.com/arieum/android-map-search/blob/arieum_step2/%EC%B2%AB%ED%99%94%EB%A9%B4_%EC%A7%80%EB%8F%84%ED%99%94%EB%A9%B4.png)

2. 카카오로컬 API로부터 검색결과 불러오기


![검색결과화면](https://github.com/arieum/android-map-search/blob/arieum_step2/%EA%B2%B0%EA%B3%BC%ED%99%94%EB%A9%B4fromapi.png)

3. 전체적인 앱 실행영상 + 검색화면에서 뒤로가기 누르면 다시 지도화면으로 이동하기 
![3주차과제실행영상](https://github.com/arieum/android-map-search/blob/arieum_step2/3%EC%A3%BC%EC%B0%A8%EA%B3%BC%EC%A0%9C%EC%8B%A4%ED%96%89%EC%98%81%EC%83%81.webm)

## ⚙️ 사용된 API 및 SDK
- 카카오로컬 API_Document ☞ <https://developers.kakao.com/docs/latest/ko/local/dev-guide#search-by-category>
- 카카오지도 SDK_Document ☞ <https://apis.map.kakao.com/android_v2/docs/getting-started/quickstart/>

![PrayingCatGIF](https://github.com/arieum/android-map-search/assets/143606293/a0bc3779-a19a-4e15-aae0-d7a475662aea)
