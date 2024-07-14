package campus.tech.kakao.map.model

interface LocationCallBack {
    fun onSuccess(data: List<Location>)
    fun onFailure(e: Exception)
}