package com.example.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val isVerified: Boolean = true,
    val membershipTierName: String = "PUBLIC",
    val registeredAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "class_enrollments")
data class ClassEnrollmentEntity(
    @PrimaryKey val classId: String,
    val enrolledAtMillis: Long = System.currentTimeMillis(),
    val lastViewedSlideIndex: Int = 0
)

@Entity(tableName = "saved_articles")
data class SavedArticleEntity(
    @PrimaryKey val articleId: String,
    val savedAtMillis: Long = System.currentTimeMillis()
)

@Dao
interface BidarshoDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun observeUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUserProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET membershipTierName = :tierName WHERE id = 1")
    suspend fun updateMembershipTier(tierName: String)

    @Query("DELETE FROM user_profile")
    suspend fun clearUserProfile()

    @Query("SELECT * FROM class_enrollments")
    fun observeEnrollments(): Flow<List<ClassEnrollmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertEnrollment(enrollment: ClassEnrollmentEntity)

    @Query("DELETE FROM class_enrollments")
    suspend fun clearEnrollments()

    @Query("SELECT * FROM saved_articles")
    fun observeSavedArticles(): Flow<List<SavedArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticle(article: SavedArticleEntity)

    @Query("DELETE FROM saved_articles WHERE articleId = :articleId")
    suspend fun removeSavedArticle(articleId: String)

    @Query("DELETE FROM saved_articles")
    suspend fun clearSavedArticles()
}

@Database(
    entities = [
        UserProfileEntity::class,
        ClassEnrollmentEntity::class,
        SavedArticleEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BidarshoDatabase : RoomDatabase() {
    abstract fun bidarshoDao(): BidarshoDao

    companion object {
        @Volatile
        private var INSTANCE: BidarshoDatabase? = null

        fun getInstance(context: Context): BidarshoDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    BidarshoDatabase::class.java,
                    "bidarsho_app.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
