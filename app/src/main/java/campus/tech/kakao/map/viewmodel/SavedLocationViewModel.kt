package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.repository.LocationLocalRepository
import campus.tech.kakao.map.model.SavedLocation

class SavedLocationViewModel(private val locationDbAccessor: LocationLocalRepository) : ViewModel() {
    private val _savedLocation = MutableLiveData<MutableList<SavedLocation>>()
    val savedLocation: LiveData<MutableList<SavedLocation>> get() = _savedLocation

    fun setSavedLocation() {
        _savedLocation.value = locationDbAccessor.getSavedLocationAll()
    }
    fun insertSavedLocation(title: String) {
        locationDbAccessor.insertSavedLocation(title)
        val savedLocation = SavedLocation(title)

        val currentList = _savedLocation.value ?: mutableListOf()
        if (!currentList.contains(savedLocation)) {
            currentList.add(savedLocation)
            _savedLocation.value = currentList
        }
    }
    fun deleteSavedLocation(savedLocation: SavedLocation) {
        locationDbAccessor.deleteSavedLocation(savedLocation.title)
        val currentList = _savedLocation.value ?: return
        if (currentList.remove(savedLocation)) {
            _savedLocation.value = currentList
        }
    }
}
