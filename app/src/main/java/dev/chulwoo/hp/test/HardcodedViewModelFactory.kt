package dev.chulwoo.hp.test

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.chulwoo.hp.test.feature.user.data.repository.UserRepositoryImpl
import dev.chulwoo.hp.test.feature.user.data.source.LocalUserSourceImpl
import dev.chulwoo.hp.test.feature.user.data.source.RemoteUserSourceImpl
import dev.chulwoo.hp.test.feature.user.domain.usecase.FilterUsers
import dev.chulwoo.hp.test.feature.user.domain.usecase.GetSortedUsers
import dev.chulwoo.hp.test.feature.user.presentation.viewmodel.UserListViewModel
import kotlinx.coroutines.Dispatchers

class HardcodedViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(UserListViewModel::class.java)) {
            val repository = UserRepositoryImpl(
                LocalUserSourceImpl(),
                RemoteUserSourceImpl(context.assets),
            )

            UserListViewModel(
                GetSortedUsers(repository),
                FilterUsers(repository),
                Dispatchers.IO,
            ) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
