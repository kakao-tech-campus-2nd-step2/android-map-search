package campus.tech.kakao.map.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import campus.tech.kakao.map.LocationContract

class Repository(context: Context):
    SQLiteOpenHelper(context, LocationContract.DATABASE_NAME, null, 1) {
        //CRUD
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(LocationContract.CREATE_QUERY)
        db?.execSQL(LocationContract.CREATE_LOG_QUERY)
        initDB(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(LocationContract.DROP_QUERY)
        db?.execSQL(LocationContract.DROP_LOG_QUERY)
        onCreate(db)
    }

    private fun initDB(db: SQLiteDatabase?){
        val initData = generateInitData()

        initData.forEach {
            val values = ContentValues().apply {
                put(LocationContract.COLUMN_NAME, it.name)
                put(LocationContract.COLUMN_LOCATION, it.location)
                put(LocationContract.COLUMN_TYPE, it.type)
            }
            db?.insert(LocationContract.TABLE_NAME, null, values)
        }
    }

    private fun generateInitData(): List<Location> {
        val initData = mutableListOf<Location>()

        for (i in 1..20) {
            initData.add(Location("cafe$i", "부산시 수영구$i", "카페"))
        }
        for (i in 1..20) {
            initData.add(Location("pharmacy$i", "서울시 성동구$i", "약국"))
        }
        return initData
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
