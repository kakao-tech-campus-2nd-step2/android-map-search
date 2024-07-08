package campus.tech.kakao.map

import android.content.Context
import androidx.lifecycle.ViewModel

class SelectItemViewModel(context: Context) : ViewModel() {
    private var selectItemList: MutableList<MapItem>
    val selectItemDB = SelectItemDBHelper(context)

    init {
        selectItemList = selectItemDB.makeAllSelectItemList()
    }

    fun updateSelectItemList() {
        selectItemDB.onUpgrade(selectItemDB.writableDatabase, 1, 2)
        selectItemList = selectItemDB.makeAllSelectItemList()
    }

    fun getSelectItemList(): MutableList<MapItem> {
        return selectItemList
    }

    fun insertSelectItem(name: String, address: String, category: String, id: Int) {
        val isExist = selectItemDB.checkItemInDB(id)
        if(isExist) {
            selectItemDB.deleteSelectItem(id)
        }
        selectItemDB.insertSelectItem(name, address, category, id)
        selectItemList = selectItemDB.makeAllSelectItemList()
    }

    fun deleteSelectItem(id: Int) {
        selectItemDB.deleteSelectItem(id)
        selectItemList = selectItemDB.makeAllSelectItemList()
    }
}