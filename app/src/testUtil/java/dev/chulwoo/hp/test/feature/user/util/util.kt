package dev.chulwoo.hp.test.feature.user.util

import dev.chulwoo.hp.test.feature.user.domain.model.User

fun mockUser(name: String): User {
    return User(0, "", name, "")
}
