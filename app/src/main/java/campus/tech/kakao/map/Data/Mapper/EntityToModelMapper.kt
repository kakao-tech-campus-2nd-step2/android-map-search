package campus.tech.kakao.map.Data.Mapper

interface EntityToModelMapper<Document,Place> {
    fun map(document : Document) : Place
}