package dev.chulwoo.hp.test.feature.user.domain

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import dev.chulwoo.hp.test.feature.user.domain.model.User
import dev.chulwoo.hp.test.feature.user.domain.repository.UserRepository
import dev.chulwoo.hp.test.feature.user.domain.usecase.FilterUsers
import dev.chulwoo.hp.test.feature.user.domain.usecase.FilterUsersParam
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class FilterUsersTest {

    @Test
    fun `Given some keyword When called FilterUsers then return filtered user list by keyword`() {
        runBlocking {
            // given
            val repository = mock<UserRepository> {
                onBlocking { getSortedUsers() } doReturn listOf(
                    User("hello"),
                    User("home"),
                    User("hope"),
                    User("orange"),
                    User("word"),
                    User("world"),
                )
            }

            val filterUsers = FilterUsers(repository)

            // when
            var filteredUsers = filterUsers(FilterUsersParam(keyword = "h"))
            // then
            assertThat(filteredUsers, equalTo(listOf(User("hello"), User("home"), User("hope"))))

            // when
            filteredUsers = filterUsers(FilterUsersParam(keyword = "ho"))
            // then
            assertThat(filteredUsers, equalTo(listOf(User("home"), User("hope"))))

            // when
            filteredUsers = filterUsers(FilterUsersParam(keyword = "word"))
            // then
            assertThat(filteredUsers, equalTo(listOf(User("word"))))

            // when
            filteredUsers = filterUsers(FilterUsersParam(keyword = "or"))
            // then
            assertThat(filteredUsers, equalTo(listOf(User("orange"), User("word"), User("world"))))
        }
    }

    @Test
    fun `Given some keyword with uppercase When called FilterUsers then filter list without case sensitivity`() {
        runBlocking {
            // given
            val repository = mock<UserRepository> {
                onBlocking { getSortedUsers() } doReturn listOf(
                    User("hello"),
                    User("Home"),
                    User("hope"),
                    User("orange"),
                    User("word"),
                    User("world"),
                )
            }

            val filterUsers = FilterUsers(repository)

            // when
            var filteredUsers = filterUsers(FilterUsersParam(keyword = "h"))
            // then
            assertThat(filteredUsers, equalTo(listOf(User("hello"), User("Home"), User("hope"))))

            // when
            filteredUsers = filterUsers(FilterUsersParam(keyword = "ho"))
            // then
            assertThat(filteredUsers, equalTo(listOf(User("Home"), User("hope"))))

            // when
            filteredUsers = filterUsers(FilterUsersParam(keyword = "word"))
            // then
            assertThat(filteredUsers, equalTo(listOf(User("word"))))

            // when
            filteredUsers = filterUsers(FilterUsersParam(keyword = "or"))
            // then
            assertThat(filteredUsers, equalTo(listOf(User("orange"), User("word"), User("world"))))
        }
    }
}
