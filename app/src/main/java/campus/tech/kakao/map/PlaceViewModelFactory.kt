package campus.tech.kakao.map

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PlaceViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // modelClass 에 MainActivityViewModel이 상속되었는지?
        if (modelClass.isAssignableFrom(PlaceViewModel::class.java)) {
            // 그렇다면 뷰모델의 context 파라미터로 context 를 넘겨줌
            return PlaceViewModel(context = context) as T
        }
        // 아니라면 예외처리
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
