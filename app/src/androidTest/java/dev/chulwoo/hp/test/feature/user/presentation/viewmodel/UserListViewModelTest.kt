package dev.chulwoo.hp.test.feature.user.presentation.viewmodel

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import dev.chulwoo.hp.test.feature.user.domain.model.User
import dev.chulwoo.hp.test.feature.user.domain.usecase.FilterUsers
import dev.chulwoo.hp.test.feature.user.domain.usecase.FilterUsersParam
import dev.chulwoo.hp.test.feature.user.domain.usecase.GetSortedUsers
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Test

class UserListViewModelTest {
    private lateinit var getSortedUsers: GetSortedUsers
    private lateinit var filterUsers: FilterUsers
    private lateinit var viewModel: UserListViewModel

    @Before
    fun setUp() {
        getSortedUsers = mock {
            onBlocking { invoke() } doReturn listOf(User("a"), User("b"))
        }
        filterUsers = mock {
            onBlocking { invoke(FilterUsersParam("a")) } doReturn listOf(User("a"))
            onBlocking { invoke(FilterUsersParam("b")) } doReturn listOf(User("b"))
        }
        viewModel = UserListViewModel(getSortedUsers, filterUsers)
    }

    @Test
    fun testLoad() {
        runBlocking {
            viewModel.load()
            assertThat(viewModel.users.value, equalTo(listOf(User("a"), User("b"))))
        }
    }

    @Test
    fun testFilter() {
        runBlocking {
            viewModel.filter("a")
            assertThat(viewModel.users.value, equalTo(listOf(User("a"))))

            viewModel.filter("b")
            assertThat(viewModel.users.value, equalTo(listOf(User("b"))))
        }
    }
}
