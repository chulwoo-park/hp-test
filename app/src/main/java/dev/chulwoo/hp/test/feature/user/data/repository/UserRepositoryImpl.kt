package dev.chulwoo.hp.test.feature.user.data.repository

import dev.chulwoo.hp.test.feature.user.domain.model.User
import dev.chulwoo.hp.test.feature.user.domain.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class UserRepositoryImpl(
    private val localSource: LocalUserSource,
    private val remoteSource: RemoteUserSource
) : UserRepository {

    override suspend fun getSortedUsers(): List<User> {
        return try {
            localSource.getSortedUsers()
        } catch (e: Exception) {
            val users = coroutineScope {
                val deferreds = listOf(
                    async { remoteSource.getUsers(0) },
                    async { remoteSource.getUsers(1) }
                )
                deferreds.awaitAll().reduce { acc, list -> acc + list }
                    .sortedBy { it.firstName }
            }
            localSource.setSortedUsers(users)

            users
        }
    }
}

interface LocalUserSource {

    suspend fun setSortedUsers(users: List<User>): Boolean

    suspend fun getSortedUsers(): List<User>
}

interface RemoteUserSource {
    suspend fun getUsers(page: Int): List<User>
}