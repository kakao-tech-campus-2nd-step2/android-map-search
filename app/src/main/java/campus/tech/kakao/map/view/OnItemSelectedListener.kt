package campus.tech.kakao.map.view

import campus.tech.kakao.map.model.SavedLocation

interface OnItemSelectedListener {
    fun insertSavedLocation(title: String)
    fun deleteSavedLocation(item: SavedLocation)
}