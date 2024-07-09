package campus.tech.kakao.map.model

import android.provider.BaseColumns

object LocationContract : BaseColumns {
    const val ID = BaseColumns._ID
    const val TABLE_NAME = "LOCATION"
    const val COLUMN_NAME = "name"
    const val COLUMN_ADDRESS = "address"
    const val COLUMN_CATEGORY = "category"
}