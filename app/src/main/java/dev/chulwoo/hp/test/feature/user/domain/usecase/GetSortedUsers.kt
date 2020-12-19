package dev.chulwoo.hp.test.feature.user.domain.usecase

import dev.chulwoo.hp.test.feature.user.domain.model.User
import dev.chulwoo.hp.test.feature.user.domain.repository.UserRepository

class GetSortedUsers(private val userRepository: UserRepository) {

    suspend operator fun invoke(): List<User> {
        return userRepository.getSortedUsers()
    }
}