package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import campus.tech.kakao.map.SQLiteHelper.Companion.COL_ID_2

class SQLiteDb(context: Context) {
    private val dbHelper: SQLiteHelper = SQLiteHelper.getInstance(context)
    private val database: SQLiteDatabase = dbHelper.writableDatabase

    fun insertData(name: String, address: String, category: String): Long {
        if (!isDataExists(name)) {
            val values = ContentValues().apply {
                put(SQLiteHelper.COL_NAME, name)
                put(SQLiteHelper.COL_ADDRESS, address)
                put(SQLiteHelper.COL_CATEGORY, category)
            }
            return database.insert(SQLiteHelper.TABLE_NAME, null, values)
        }
        return -1
    }

    fun isDataExists(name: String): Boolean {
        val cursor = database.query(
            SQLiteHelper.TABLE_NAME,
            arrayOf(SQLiteHelper.COL_NAME),
            "${SQLiteHelper.COL_NAME} = ?",
            arrayOf(name),
            null,
            null,
            null
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun getAllData(): List<Place> {
        val places = mutableListOf<Place>()
        val cursor = database.query(
            SQLiteHelper.TABLE_NAME,
            arrayOf(SQLiteHelper.COL_ID, SQLiteHelper.COL_NAME, SQLiteHelper.COL_ADDRESS, SQLiteHelper.COL_CATEGORY),
            null,
            null,
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteHelper.COL_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COL_NAME))
                val address = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COL_ADDRESS))
                val category = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COL_CATEGORY))
                places.add(Place(id, name, address, category))
            } while (cursor.moveToNext())
        }
        cursor.close()

        return places
    }

    fun logAllData() {
        val cursor = getAllData()
        for (place in cursor) {
            println("ID: ${place.id}, Name: ${place.name}, Address: ${place.address}, Category: ${place.category}")
        }
    }

    fun insertIntoSelectedData(name: String): Long {
        val values = ContentValues().apply {
            put(SQLiteHelper.COL_NAME_2, name)
        }
        return database.insert(SQLiteHelper.TABLE_NAME_2, null, values)
    }

    fun getAllSelectedData(): List<Pair<Int, String>> {
        val selectedData = mutableListOf<Pair<Int, String>>()
        val cursor = database.query(
            SQLiteHelper.TABLE_NAME_2,
            arrayOf(SQLiteHelper.COL_ID_2, SQLiteHelper.COL_NAME_2),
            null,
            null,
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteHelper.COL_ID_2))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COL_NAME_2))
                selectedData.add(id to name)
            } while (cursor.moveToNext())
        }
        cursor.close()

        return selectedData.reversed()
    }

    fun deleteFromSelectedData(id: Int): Int {
        return database.delete(
            SQLiteHelper.TABLE_NAME_2,
            "$COL_ID_2 = ?",
            arrayOf(id.toString())
        )
    }

}
