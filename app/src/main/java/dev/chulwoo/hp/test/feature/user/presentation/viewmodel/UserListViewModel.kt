package dev.chulwoo.hp.test.feature.user.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dev.chulwoo.hp.test.feature.user.domain.usecase.FilterUsers
import dev.chulwoo.hp.test.feature.user.domain.usecase.GetSortedUsers
import dev.chulwoo.hp.test.feature.user.presentation.model.UserListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserListViewModel(
    private val getSortedUsers: GetSortedUsers,
    private val filterUsers: FilterUsers
) : ViewModel() {

    val states: StateFlow<UserListState> = MutableStateFlow(UserListState.Initial)

    fun load() {
        TODO()
    }

    fun filter(keyword: String) {
        TODO()
    }
}
