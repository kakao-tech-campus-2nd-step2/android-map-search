package campus.tech.kakao.map

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MapAccess(context: Context) {

    private val dbHelper = SQLiteHelper(context)

    //검색어 기반 항목 검색 suspend 함수
    suspend fun searchItems(query: String): List<MapItem> {
        //코루틴 문맥 전환 - IO 스레드 기반 실행
        return withContext(Dispatchers.IO) {
            val cursor = dbHelper.searchItems(query)
            val items = mutableListOf<MapItem>()

            //만약 검색결과가 있으면 커서를 첫 번째 행으로 이동
            if (cursor.moveToFirst()) {

                //마지막 행까지 커서 이동해서 계속 반복하기
                do {

                    //현재 위치한 행의 데이터 가져오기
                    val item = MapItem(
                        cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteHelper.COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COL_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COL_ADDRESS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COL_CATEGORY))
                    )

                    //생성된 MapItem객체 리스트 추가
                    items.add(item)
                } while (cursor.moveToNext())
            }
            //더이상 없을 경우 닫아서 메모리 낭비 방지
            cursor.close()
            items
        }
    }
}