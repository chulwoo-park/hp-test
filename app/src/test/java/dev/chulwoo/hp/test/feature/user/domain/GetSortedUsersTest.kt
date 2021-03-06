package dev.chulwoo.hp.test.feature.user.domain

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import dev.chulwoo.hp.test.feature.user.domain.repository.UserRepository
import dev.chulwoo.hp.test.feature.user.domain.usecase.GetSortedUsers
import dev.chulwoo.hp.test.feature.user.util.mockUser
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class GetSortedUsersTest {

    @Test
    fun `Given sorted user list When called GetSortedUsers then return sorted user list`() =
        runBlocking {
            // given
            val repository = mock<UserRepository> {
                onBlocking { getSortedUsers() } doReturn listOf(
                    mockUser("a"),
                    mockUser("b"),
                    mockUser("c")
                )
            }

            val getUsers = GetSortedUsers(repository)

            // when
            val users = getUsers()

            // then
            verify(repository).getSortedUsers()
            assertThat(users, equalTo(listOf(mockUser("a"), mockUser("b"), mockUser("c"))))
        }
}
