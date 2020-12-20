package dev.chulwoo.hp.test.feature.user.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import dev.chulwoo.hp.test.feature.user.domain.repository.UserRepository
import dev.chulwoo.hp.test.feature.user.domain.usecase.FilterUsers
import dev.chulwoo.hp.test.feature.user.domain.usecase.GetSortedUsers
import dev.chulwoo.hp.test.feature.user.presentation.model.UserListState
import dev.chulwoo.hp.test.feature.user.util.MainCoroutineScopeRule
import dev.chulwoo.hp.test.feature.user.util.mockUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class UserListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineScopeRule()

    private lateinit var getSortedUsers: GetSortedUsers
    private lateinit var filterUsers: FilterUsers
    private lateinit var viewModel: UserListViewModel
    private lateinit var observer: Observer<UserListState>

    @Before
    fun setUp() {
        val repository = mock<UserRepository> {
            onBlocking { getSortedUsers() } doReturn listOf(mockUser("a"), mockUser("b"))
        }
        observer = mock {}
        getSortedUsers = GetSortedUsers(repository)
        filterUsers = FilterUsers(repository)
        viewModel = UserListViewModel(getSortedUsers, filterUsers, mainCoroutineRule.dispatcher)
        viewModel.states.asLiveData().observeForever(observer)
    }

    @Test
    fun testLoad() {
        mainCoroutineRule.dispatcher.runBlockingTest {
            verify(observer).onChanged(UserListState.Initial)
            viewModel.load()
            verify(observer).onChanged(UserListState.Loading)
            verify(observer).onChanged(UserListState.Success(listOf(mockUser("a"), mockUser("b"))))
        }
    }

    @Test
    fun testFilter() {
        mainCoroutineRule.dispatcher.runBlockingTest {
            verify(observer).onChanged(UserListState.Initial)
            viewModel.filter("a")
            verify(observer).onChanged(UserListState.Success(listOf(mockUser("a"))))
            viewModel.filter("b")
            verify(observer).onChanged(UserListState.Success(listOf(mockUser("b"))))
        }
    }

}
