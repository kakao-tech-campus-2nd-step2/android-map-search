package campus.tech.kakao.map
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoreDao {
    @Query("SELECT * FROM store WHERE name LIKE :query OR location LIKE :query")
    fun search(query: String): List<StoreEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(store: StoreEntity)

    @Query("SELECT * FROM store WHERE saved = 1")
    fun getSavedSearches(): List<StoreEntity>

    @Query("UPDATE store SET saved = :saved WHERE id = :id")
    fun updateSavedStatus(id: Int, saved: Boolean)
}
