package dev.chulwoo.hp.test.feature.user.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import dev.chulwoo.hp.test.feature.user.domain.model.User
import dev.chulwoo.hp.test.feature.user.domain.repository.UserRepository
import dev.chulwoo.hp.test.feature.user.domain.usecase.FilterUsers
import dev.chulwoo.hp.test.feature.user.domain.usecase.GetSortedUsers
import dev.chulwoo.hp.test.feature.user.presentation.model.UserListState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val dispatcher = TestCoroutineDispatcher()

    private lateinit var getSortedUsers: GetSortedUsers
    private lateinit var filterUsers: FilterUsers
    private lateinit var viewModel: UserListViewModel
    private lateinit var observer: Observer<UserListState>

    @Before
    fun setUp() {
        val repository = mock<UserRepository> {
            onBlocking { getSortedUsers() } doReturn listOf(User("a"), User("b"))
        }
        observer = mock {}
        getSortedUsers = GetSortedUsers(repository)
        filterUsers = FilterUsers(repository)
        viewModel = UserListViewModel(getSortedUsers, filterUsers, dispatcher)
        viewModel.states.asLiveData().observeForever(observer)
    }

    @Test
    fun testLoad() {
        dispatcher.runBlockingTest {
            verify(observer).onChanged(UserListState.Initial)
            viewModel.load()
            verify(observer).onChanged(UserListState.Loading)
            verify(observer).onChanged(UserListState.Success(listOf(User("a"), User("b"))))
        }
    }

    @Test
    fun testFilter() {
        dispatcher.runBlockingTest {
            viewModel.filter("a")
            viewModel.filter("b")

            verify(observer).onChanged(UserListState.Success(listOf(User("a"))))
            verify(observer).onChanged(UserListState.Success(listOf(User("b"))))
        }
    }
}
