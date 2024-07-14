package campus.tech.kakao.map.Data.Mapper

import campus.tech.kakao.map.Data.Datasource.Remote.Response.Document
import campus.tech.kakao.map.Domain.Model.Place
import campus.tech.kakao.map.Domain.Model.PlaceCategory

class DocToPlaceMapper : EntityToModelMapper<Document, Place> {
    override fun map(document: Document): Place = Place(
            document.placeName,
            document.addressName,
            groupCodeToPlaceCategory(document.categoryGroupCode)
    )

    private fun groupCodeToPlaceCategory(code : String) : PlaceCategory {
        when(code){
            "CE7" -> return PlaceCategory.CAFE
            "PM9" -> return PlaceCategory.PHARMACY
            "FD6" -> return PlaceCategory.RESTAURANT
            else -> return PlaceCategory.ELSE
        }
    }
}