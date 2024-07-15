package campus.tech.kakao.map.view.search

import campus.tech.kakao.map.model.SavedLocation

interface OnItemSelectedListener {
    fun insertSavedLocation(title: String)
    fun deleteSavedLocation(item: SavedLocation)
}