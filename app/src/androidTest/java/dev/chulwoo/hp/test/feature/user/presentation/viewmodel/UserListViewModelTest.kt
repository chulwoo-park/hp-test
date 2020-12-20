package dev.chulwoo.hp.test.feature.user.presentation.viewmodel

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import dev.chulwoo.hp.test.feature.user.domain.model.User
import dev.chulwoo.hp.test.feature.user.domain.usecase.FilterUsers
import dev.chulwoo.hp.test.feature.user.domain.usecase.FilterUsersParam
import dev.chulwoo.hp.test.feature.user.domain.usecase.GetSortedUsers
import dev.chulwoo.hp.test.feature.user.presentation.model.UserListState
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
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
            val states = viewModel.states.take(3).toList()
            assertThat(states[0], equalTo(UserListState.Initial))
            assertThat(states[1], equalTo(UserListState.Loading))
            assertThat(states[2], equalTo(UserListState.Success(listOf(User("a"), User("b")))))
        }
    }

    @Test
    fun testFilter() {
        runBlocking {
            viewModel.filter("a")
            var state = viewModel.states.take(1).first()
            assertThat(state, equalTo(UserListState.Success(listOf(User("a")))))

            viewModel.filter("b")
            state = viewModel.states.drop(1).take(1).first()
            assertThat(state, equalTo(UserListState.Success(listOf(User("b")))))
        }
    }
}
