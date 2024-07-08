package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log

data class SelectItem(
    val id: Int,
    val name: String
)

object SelectItemDB : BaseColumns {
    const val TABLE_NAME = "selectItem"
    const val TABLE_COLUMN_ID = "id"
    const val TABLE_COLUMN_NAME = "name"
    const val TABLE_COLUMN_ADDRESS = "address"
    const val TABLE_COLUMN_CATEGORY = "category"
    const val TABLE_COLUMN_MAP_ITEM_ID = "mapItemID"
}

class SelectItemDBHelper(context: Context) : SQLiteOpenHelper(context, "selectItem.db", null, 1) {
    private val wDb = writableDatabase
    private val rDb = readableDatabase
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE ${SelectItemDB.TABLE_NAME} (" +
                    "${SelectItemDB.TABLE_COLUMN_ID} Integer primary key autoincrement," +
                    "${SelectItemDB.TABLE_COLUMN_NAME} varchar(15) not null," +
                    "${SelectItemDB.TABLE_COLUMN_ADDRESS} varchar(30) not null," +
                    "${SelectItemDB.TABLE_COLUMN_CATEGORY} varchar(10) not null," +
                    "${SelectItemDB.TABLE_COLUMN_MAP_ITEM_ID} Integer not null," +
                    "FOREIGN KEY(${SelectItemDB.TABLE_COLUMN_MAP_ITEM_ID})" +
                    " REFERENCES ${MapItemDB.TABLE_NAME}(${MapItemDB.TABLE_COLUMN_ID})" +
                    ");"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${SelectItemDB.TABLE_NAME}")
        onCreate(db)
    }

    fun insertSelectItem(name: String, address: String, category: String, id: Int) {
        val values = ContentValues()
        values.put(SelectItemDB.TABLE_COLUMN_NAME, name)
        values.put(SelectItemDB.TABLE_COLUMN_ADDRESS, address)
        values.put(SelectItemDB.TABLE_COLUMN_CATEGORY, category)
        values.put(SelectItemDB.TABLE_COLUMN_MAP_ITEM_ID, id)

        wDb.insert(SelectItemDB.TABLE_NAME, null, values)
    }

    fun deleteSelectItem(id: Int) {
        wDb.delete(
            SelectItemDB.TABLE_NAME,
            "${SelectItemDB.TABLE_COLUMN_MAP_ITEM_ID}=?",
            arrayOf(id.toString())
        )
    }

    fun makeAllSelectItemList(): MutableList<MapItem> {
        val cursor = rDb.rawQuery(
            "Select * from ${SelectItemDB.TABLE_NAME} order by ${SelectItemDB.TABLE_COLUMN_ID} desc",
            null
        )
        val selectItemList = mutableListOf<MapItem>()
        while (cursor.moveToNext()) {
            selectItemList.add(
                MapItem(
                    cursor.getInt(cursor.getColumnIndexOrThrow(SelectItemDB.TABLE_COLUMN_MAP_ITEM_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SelectItemDB.TABLE_COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SelectItemDB.TABLE_COLUMN_ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SelectItemDB.TABLE_COLUMN_CATEGORY))
                )
            )
        }
        cursor.close()

        return selectItemList
    }

    fun checkItemInDB(id : Int) : Boolean {
        val cursor = rDb.rawQuery(
            "Select * from ${SelectItemDB.TABLE_NAME} where ${SelectItemDB.TABLE_COLUMN_MAP_ITEM_ID} = ?",
            arrayOf(id.toString())
        )
        if(cursor.getCount() > 0) {
            cursor.close()
            return true
        } else {
            cursor.close()
            return false
        }
    }
}