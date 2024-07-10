# android-map-search

## 기능 요구 사항
### 이전 구현 기능
- 검색 결과 목록은 세로 스크롤이 된다. 
- 입력한 검색어는 X를 눌러서 삭제할 수 있다. 
- 검색 결과 목록에서 하나의 항목을 선택할 수 있다. 
- 선택된 항목은 검색어 저장 목록에 추가된다. 
- 저장된 검색어 목록은 가로 스크롤이 된다. 
- 저장된 검색어는 X를 눌러서 삭제할 수 있다. 
- 저장된 검색어는 앱을 재실행하여도 유지된다.
### 1단계 구현 목록
- [x] 검색 결과를 15개 이상 표시
- [x] 서버 응답에 대한 DTO 구현
- [x] Retrofit 구현
- [x] 카카오 로컬 API 사용
### 2단계 구현 목록
- [ ] 카카오 SDK 적용
- [ ] 앱 실행 시 지도 화면 표시
- [ ] 검색 창을 선택하면 검색화면으로 이동
- [ ] 검색 화면에서 뒤로가기를 하면 지도 화면으로 복귀