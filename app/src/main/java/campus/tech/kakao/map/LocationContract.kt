package campus.tech.kakao.map


object LocationContract{
    const val DATABASE_NAME = "location.db"

    const val TABLE_NAME: String = "DB_Location"
    const val COLUMN_NAME: String = "name"
    const val COLUMN_LOCATION: String = "location"
    const val COLUMN_TYPE: String = "type"

    const val CREATE_QUERY = "CREATE TABLE ${TABLE_NAME} (" +
            "${COLUMN_NAME} TEXT NOT NULL, " +
            "${COLUMN_LOCATION} TEXT NOT NULL, " +
            "${COLUMN_TYPE} TEXT" +
            ");"

    const val DELETE_QUERY = "DELETE FROM $TABLE_NAME"
    const val DROP_QUERY = "DROP TABLE IF EXISTS ${TABLE_NAME}"

    const val TABLE_LOG_NAME = "DB_Log"
    const val COLUMN_LOG_NAME = "log_name"

    const val CREATE_LOG_QUERY = "CREATE TABLE IF NOT EXISTS $TABLE_LOG_NAME (" +
            "$COLUMN_LOG_NAME TEXT NOT NULL" +
            ");"

    const val DROP_LOG_QUERY = "DROP TABLE IF EXISTS $TABLE_LOG_NAME"

}