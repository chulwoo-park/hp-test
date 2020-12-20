package dev.chulwoo.hp.test.feature.user.presentation.model

import dev.chulwoo.hp.test.feature.user.domain.model.User

sealed class UserListState {

    object Initial : UserListState()
    object Loading : UserListState()
    data class Success(val users: List<User>) : UserListState()
    data class Failure(val t: Throwable) : UserListState()
}
