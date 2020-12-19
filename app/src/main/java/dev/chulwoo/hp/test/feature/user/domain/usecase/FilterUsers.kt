package dev.chulwoo.hp.test.feature.user.domain.usecase

import dev.chulwoo.hp.test.feature.user.domain.model.User
import dev.chulwoo.hp.test.feature.user.domain.repository.UserRepository

data class FilterUsersParam(val keyword: String)

class FilterUsers(private val userRepository: UserRepository) {

    suspend operator fun invoke(param: FilterUsersParam): List<User> {
        val users = userRepository.getSortedUsers()
        return users.filter { it.firstName.contains(param.keyword) }
    }
}