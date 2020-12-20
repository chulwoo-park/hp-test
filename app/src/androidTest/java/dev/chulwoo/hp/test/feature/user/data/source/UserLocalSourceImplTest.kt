package dev.chulwoo.hp.test.feature.user.data.source

import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.chulwoo.hp.test.common.CacheMissException
import dev.chulwoo.hp.test.feature.user.domain.model.User
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class UserLocalSourceImplTest {

    @Test(expected = CacheMissException::class)
    @Throws(Exception::class)
    fun testCacheMiss() {
        runBlocking {
            val localSource = UserLocalSourceImpl()
            localSource.getSortedUsers()
        }
    }

    @Test
    fun testReadWrite() {
        runBlocking {
            val localSource = UserLocalSourceImpl()
            val isSuccess = localSource.setSortedUsers(listOf(User("a"), User("b")))
            Assert.assertEquals(true, isSuccess)
            Assert.assertEquals(listOf(User("a"), User("b")), localSource.getSortedUsers())
        }
    }
}