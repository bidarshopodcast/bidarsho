package com.example.data

import com.example.model.MembershipTier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BidarshoRepository(private val dao: BidarshoDao) {

    val userProfile: Flow<UserProfileEntity?> = dao.observeUserProfile()

    val enrollments: Flow<Map<String, ClassEnrollmentEntity>> =
        dao.observeEnrollments().map { list -> list.associateBy { it.classId } }

    val savedArticleIds: Flow<Set<String>> =
        dao.observeSavedArticles().map { list -> list.map { it.articleId }.toSet() }

    suspend fun registerUser(
        fullName: String,
        email: String,
        phoneNumber: String
    ) {
        dao.upsertUserProfile(
            UserProfileEntity(
                id = 1,
                fullName = fullName.trim(),
                email = email.trim(),
                phoneNumber = phoneNumber.trim(),
                isVerified = true,
                membershipTierName = MembershipTier.PUBLIC.name
            )
        )
    }

    suspend fun updateMembershipTier(tier: MembershipTier) {
        dao.updateMembershipTier(tier.name)
    }

    suspend fun enrollInClass(classId: String) {
        dao.upsertEnrollment(
            ClassEnrollmentEntity(
                classId = classId,
                enrolledAtMillis = System.currentTimeMillis(),
                lastViewedSlideIndex = 0
            )
        )
    }

    suspend fun updateSlideProgress(classId: String, slideIndex: Int) {
        dao.upsertEnrollment(
            ClassEnrollmentEntity(
                classId = classId,
                lastViewedSlideIndex = slideIndex
            )
        )
    }

    suspend fun toggleSaveArticle(articleId: String, currentlySaved: Boolean) {
        if (currentlySaved) {
            dao.removeSavedArticle(articleId)
        } else {
            dao.saveArticle(SavedArticleEntity(articleId = articleId))
        }
    }

    suspend fun signOut() {
        dao.clearUserProfile()
        dao.clearEnrollments()
        dao.clearSavedArticles()
    }
}
