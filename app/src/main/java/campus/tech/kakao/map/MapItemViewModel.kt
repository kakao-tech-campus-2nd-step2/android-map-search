package campus.tech.kakao.map

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapItemViewModel(context: Context) : ViewModel() {
    val mapItemDB = MapItemDbHelper(context)

    //private var mapItemList: MutableList<MapItem>
    //private var selectItemList: MutableList<MapItem>

    private val _mapItemList: MutableLiveData<List<MapItem>> = MutableLiveData(listOf<MapItem>())
    val mapItemList : LiveData<List<MapItem>> get() = _mapItemList

    private val _selectItemList: MutableLiveData<List<MapItem>> = MutableLiveData(listOf<MapItem>())
    val selectItemList : LiveData<List<MapItem>> get() = _selectItemList

    init {
        //updateMapItemDB()
        mapItemDB.searchMapItem("")
        _selectItemList.postValue(mapItemDB.makeAllSelectItemList())
    }

    fun updateMapItemDB() {
        mapItemDB.onUpgrade(mapItemDB.writableDatabase, 1, 2)
        _selectItemList.postValue(mapItemDB.makeAllSelectItemList())
    }

    fun searchMapItem(category: String) {
        _mapItemList.postValue(mapItemDB.searchMapItem(category))
    }

    fun insertSelectItem(mapItem: MapItem) {
        val id = mapItem.id
        val isExist = mapItemDB.checkItemInDB(id)
        if(isExist) {
            mapItemDB.deleteSelectItem(id)
        }
        mapItemDB.insertSelectItem(mapItem.name, mapItem.address, mapItem.category, id)
        _selectItemList.postValue(mapItemDB.makeAllSelectItemList())
    }

    fun deleteSelectItem(id: Int) {
        mapItemDB.deleteSelectItem(id)
        _selectItemList.postValue(mapItemDB.makeAllSelectItemList())
    }
}