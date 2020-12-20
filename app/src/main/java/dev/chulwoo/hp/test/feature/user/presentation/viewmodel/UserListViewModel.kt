package dev.chulwoo.hp.test.feature.user.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.chulwoo.hp.test.feature.user.domain.usecase.FilterUsers
import dev.chulwoo.hp.test.feature.user.domain.usecase.FilterUsersParam
import dev.chulwoo.hp.test.feature.user.domain.usecase.GetSortedUsers
import dev.chulwoo.hp.test.feature.user.presentation.model.UserListState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserListViewModel(
    private val getSortedUsers: GetSortedUsers,
    private val filterUsers: FilterUsers,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _states: MutableStateFlow<UserListState> = MutableStateFlow(UserListState.Initial)
    val states: StateFlow<UserListState> = _states

    fun load() {
        if (_states.value is UserListState.Loading) return

        viewModelScope.launch(dispatcher) {
            _states.value = UserListState.Loading
            _states.value = try {
                UserListState.Success(getSortedUsers())
            } catch (e: Exception) {
                UserListState.Failure(e)
            }
        }
    }

    fun filter(keyword: String) {
        viewModelScope.launch(dispatcher) {
            try {
                val users = filterUsers(FilterUsersParam(keyword))
                _states.value = UserListState.Success(users)
            } catch (ignore: Exception) {
            }
        }
    }
}
