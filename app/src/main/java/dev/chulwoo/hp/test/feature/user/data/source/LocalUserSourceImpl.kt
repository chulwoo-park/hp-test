package dev.chulwoo.hp.test.feature.user.data.source

import dev.chulwoo.hp.test.common.CacheMissException
import dev.chulwoo.hp.test.feature.user.data.repository.LocalUserSource
import dev.chulwoo.hp.test.feature.user.domain.model.User

class LocalUserSourceImpl : LocalUserSource {

    private var cache: List<User>? = null

    override suspend fun setSortedUsers(users: List<User>): Boolean {
        cache = users
        return true
    }

    override suspend fun getSortedUsers(): List<User> {
        return cache ?: throw CacheMissException()
    }
}
