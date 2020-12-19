package dev.chulwoo.hp.test.feature.user.domain

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import dev.chulwoo.hp.test.feature.user.domain.model.User
import dev.chulwoo.hp.test.feature.user.domain.repository.UserRepository
import dev.chulwoo.hp.test.feature.user.domain.usecase.FilterUsers
import dev.chulwoo.hp.test.feature.user.domain.usecase.FilterUsersParam
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class FilterUsersTest {

    @Test
    fun `Given some keyword When called FilterUsers then return filtered user list by keyword`() =
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
            Assert.assertEquals(listOf(User("hello"), User("home"), User("hope")), filteredUsers)

            // when
            filteredUsers = filterUsers(FilterUsersParam(keyword = "ho"))
            // then
            Assert.assertEquals(listOf(User("home"), User("hope")), filteredUsers)

            // when
            filteredUsers = filterUsers(FilterUsersParam(keyword = "word"))
            // then
            Assert.assertEquals(listOf(User("word")), filteredUsers)

            // when
            filteredUsers = filterUsers(FilterUsersParam(keyword = "or"))
            // then
            Assert.assertEquals(listOf(User("world"), User("word"), User("orange")), filteredUsers)
        }
}