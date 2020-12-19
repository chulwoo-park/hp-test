package dev.chulwoo.hp.test.feature.user.domain.repository

import dev.chulwoo.hp.test.feature.user.domain.model.User

interface UserRepository {
    suspend fun getSortedUsers(): List<User>
}