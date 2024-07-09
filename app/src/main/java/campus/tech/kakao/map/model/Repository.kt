package campus.tech.kakao.map.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import campus.tech.kakao.map.LocationContract
import campus.tech.kakao.map.Place

class Repository(context: Context):
    SQLiteOpenHelper(context, LocationContract.DATABASE_NAME, null, 1) {
        //CRUD
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(LocationContract.CREATE_QUERY)
        db?.execSQL(LocationContract.CREATE_LOG_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(LocationContract.DROP_QUERY)
        db?.execSQL(LocationContract.DROP_LOG_QUERY)
        onCreate(db)
    }

    fun insertSearchedData(places: List<Place>){
        val db = writableDatabase
        db.execSQL(LocationContract.DELETE_QUERY)
        places.forEach {
            val values = ContentValues().apply {
                put(LocationContract.COLUMN_NAME, it.place_name)
                put(LocationContract.COLUMN_LOCATION, it.address_name)
                put(LocationContract.COLUMN_TYPE, it.category_group_name)
            }
            db.insert(LocationContract.TABLE_NAME, null, values)
        }
    }
    fun selectData(newText: String): List<Location>{
        val locations = mutableListOf<Location>()
        val cursor = readableDatabase.query(
            LocationContract.TABLE_NAME,
            null, "${LocationContract.COLUMN_NAME} LIKE ?", arrayOf("${newText}%"), null, null, null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val name = it.getString(it.getColumnIndexOrThrow(LocationContract.COLUMN_NAME))
                val location = it.getString(it.getColumnIndexOrThrow(LocationContract.COLUMN_LOCATION))
                val type = it.getString(it.getColumnIndexOrThrow(LocationContract.COLUMN_TYPE))
                locations.add(Location(name, location, type))
            }
        }
        return locations
    }
    fun deleteData(){
        writableDatabase.execSQL(LocationContract.DELETE_QUERY)
    }

    fun saveLog(locationLog: List<Location>) {
        writableDatabase.execSQL(LocationContract.DROP_LOG_QUERY)
        writableDatabase.execSQL(LocationContract.CREATE_LOG_QUERY)

        locationLog.forEach {
            val values = ContentValues().apply {
                put(LocationContract.COLUMN_LOG_NAME, it.name)
            }
            writableDatabase.insert(LocationContract.TABLE_LOG_NAME, null, values)
        }
    }

    fun getLog():List<Location> {
        val logList = mutableListOf<Location>()
        val cursor = readableDatabase.query(
            LocationContract.TABLE_LOG_NAME,
            null, null, null, null, null, null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val name = it.getString(it.getColumnIndexOrThrow(LocationContract.COLUMN_LOG_NAME))
                logList.add(Location(name, "", ""))
            }
        }
        return logList
    }
}
