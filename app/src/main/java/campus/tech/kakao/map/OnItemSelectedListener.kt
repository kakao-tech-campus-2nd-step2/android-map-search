package campus.tech.kakao.map

interface OnItemSelectedListener {
    fun insertSavedLocation(title: String)
    fun deleteSavedLocation(item: SavedLocation)
}