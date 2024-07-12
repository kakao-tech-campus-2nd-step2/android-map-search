package campus.tech.kakao.map.di

import android.content.Context
import campus.tech.kakao.map.data.SavedSearchWordDBHelper
import campus.tech.kakao.map.data.repository.PlaceRepository
import campus.tech.kakao.map.data.repository.PlaceRepositoryImpl
import campus.tech.kakao.map.data.repository.SavedSearchWordRepository
import campus.tech.kakao.map.data.repository.SavedSearchWordRepositoryImpl
import campus.tech.kakao.map.ui.ViewModelFactory

class SearchActivityContainer(private val context: Context) {
    private val placeRepository: PlaceRepository by lazy {
        PlaceRepositoryImpl()
    }

    private val dbHelper: SavedSearchWordDBHelper by lazy {
        SavedSearchWordDBHelper(context.applicationContext)
    }

    private val savedSearchWordRepository: SavedSearchWordRepository by lazy {
        SavedSearchWordRepositoryImpl(dbHelper)
    }

    private val viewModelFactory: ViewModelFactory by lazy {
        ViewModelFactory(placeRepository, savedSearchWordRepository)
    }

    fun provideViewModelFactory(): ViewModelFactory {
        return viewModelFactory
    }
}
