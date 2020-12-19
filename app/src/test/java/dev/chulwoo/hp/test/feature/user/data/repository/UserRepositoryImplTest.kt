package dev.chulwoo.hp.test.feature.user.data.repository

import com.nhaarman.mockitokotlin2.*
import dev.chulwoo.hp.test.feature.user.domain.model.User
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class UserRepositoryImplTest {

    @Test
    fun `Given error on local When getSortedUsers Then retrieve data from remote`() {
        runBlocking {
            // given
            val localSource = mock<LocalUserSource> {
                onBlocking { getSortedUsers() } doAnswer { throw Exception() }
            }
            val remoteSource = mock<RemoteUserSource> {
                onBlocking { getUsers(any()) } doReturn listOf()
            }
            val repository = UserRepositoryImpl(localSource, remoteSource)

            // when
            repository.getSortedUsers()

            // then
            verify(remoteSource, times(2)).getUsers(any())
        }
    }

    @Test
    fun `Given local data When getSortedUsers Then return local data`() {
        runBlocking {
            // given
            val localSource = mock<LocalUserSource> {
                onBlocking { getSortedUsers() } doReturn listOf(User("local"))
            }
            val remoteSource = mock<RemoteUserSource> {
                onBlocking { getUsers(any()) } doReturn listOf(User("remote"))
            }
            val repository = UserRepositoryImpl(localSource, remoteSource)

            // when
            val users = repository.getSortedUsers()

            // then
            Assert.assertEquals(listOf(User("local")), users)
        }
    }

    @Test
    fun `When getSortedUsers Then combine data from first and second remote page`() {
        runBlocking {
            // given
            val localSource = mock<LocalUserSource> {
                onBlocking { getSortedUsers() } doAnswer { throw Exception() }
            }
            val remoteSource = mock<RemoteUserSource> {
                onBlocking { getUsers(any()) } doReturn listOf()
            }
            val repository = UserRepositoryImpl(localSource, remoteSource)

            // when
            repository.getSortedUsers()

            // then
            verify(remoteSource).getUsers(0)
            verify(remoteSource).getUsers(1)
        }
    }

    @Test
    fun `Given two lists on remote When getSortedUsers Then return combined and sorted list`() =
        runBlocking {
            // given
            val localSource = mock<LocalUserSource> {
                onBlocking { getSortedUsers() } doAnswer { throw Exception() }
            }

            // given
            val remoteSource = mock<RemoteUserSource> {
                onBlocking { getUsers(0) } doReturn listOf(User("b"), User("a"), User("d"))
                onBlocking { getUsers(1) } doReturn listOf(User("c"), User("f"), User("e"))
            }

            val repository = UserRepositoryImpl(localSource, remoteSource)

            // when
            val users = repository.getSortedUsers()

            // then
            Assert.assertEquals(
                listOf(
                    User("a"),
                    User("b"),
                    User("c"),
                    User("d"),
                    User("e"),
                    User("f")
                ), users
            )
        }


    @Test
    fun `Given error on local When getSortedUsers Then save combined data to local`() {
        runBlocking {
            // given
            val localSource = mock<LocalUserSource> {
                onBlocking { getSortedUsers() } doAnswer { throw Exception() }
            }
            val remoteSource = mock<RemoteUserSource> {
                onBlocking { getUsers(0) } doReturn listOf(User("b"), User("a"), User("d"))
                onBlocking { getUsers(1) } doReturn listOf(User("c"), User("f"), User("e"))
            }
            val repository = UserRepositoryImpl(localSource, remoteSource)

            // when
            repository.getSortedUsers()

            // then
            verify(localSource).setSortedUsers(
                listOf(
                    User("a"),
                    User("b"),
                    User("c"),
                    User("d"),
                    User("e"),
                    User("f")
                )
            )
        }
    }
}