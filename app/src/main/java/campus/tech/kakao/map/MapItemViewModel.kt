package campus.tech.kakao.map

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel

class MapItemViewModel(context: Context) : ViewModel() {
    private var mapItemList: MutableList<MapItem>
    val mapItemDB = MapItemDbHelper(context)

    init {
        mapItemList = mapItemDB.searchMapItem("")
    }

    fun updateMapItemList() {
        mapItemDB.onUpgrade(mapItemDB.writableDatabase, 1, 2)
    }

    fun getMapItemList(): MutableList<MapItem> {
        //mapItemList = mapItemDB.searchMapItem(category)
        Log.d("uin", "" + mapItemList.size)
        return mapItemList
    }

    fun searchMapItem(category: String): MutableList<MapItem> {
        mapItemList = mapItemDB.searchMapItem(category)
        return mapItemList
    }

    fun printAllMapItemList() {
        mapItemDB.printAllMapItemList()
    }


}