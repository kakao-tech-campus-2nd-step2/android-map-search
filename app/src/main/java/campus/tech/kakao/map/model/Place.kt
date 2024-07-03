package campus.tech.kakao.map.model

import android.provider.BaseColumns

object Place: BaseColumns {
    const val TABLE_NAME = "places"
    const val COLUMN_ID = "id"
    const val COLUMN_NAME = "name"
    const val COLUMN_LOCATION = "location"
    const val COLUMN_CATEGORY = "category"
}