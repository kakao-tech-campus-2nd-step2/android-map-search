package campus.tech.kakao.map.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.Place
import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.Repository

class LocationViewModel(private val repository: Repository): ViewModel(){
    private val _logList = MutableLiveData<List<Location>>(repository.getLog())
    val logList: LiveData<List<Location>> get() = _logList

    val searchText = MutableLiveData<String>()

    fun clearSearch() {
        searchText.value = ""
    }
    fun findData(name: String): List<Location>{
         return repository.selectData(name)
    }
    fun saveLog(){
        logList.value?.let {
            repository.saveLog(it)
            Log.d("pjh","Log saved successfully: $it")
        } ?: repository.saveLog(emptyList())
    }
    fun addLog(location: Location) {
        val currentList = _logList.value?.toMutableList() ?: mutableListOf()
        val existingIndex = currentList.indexOfFirst { it.name == location.name }
        if (existingIndex != -1) {
            currentList.removeAt(existingIndex)
        }
        currentList.add(0, location)
        _logList.value = currentList
    }
    fun removeLog(position: Int) {
        val currentList = _logList.value?.toMutableList() ?: return
        currentList.removeAt(position)
        _logList.value = currentList
    }
    fun insertSearchedData(places: List<Place>) {
        repository.insertSearchedData(places)
    }
    fun resetData(){
        repository.deleteData()
    }
}