package dev.chulwoo.hp.test.feature.user.data.source

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.chulwoo.hp.test.common.PageNotFound
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class UserRemoteSourceImplTest {

    @Test(expected = PageNotFound::class)
    @Throws(Exception::class)
    fun testInvalidPage() {
        runBlocking {
            val context: Context = ApplicationProvider.getApplicationContext()
            val remoteSource = UserRemoteSourceImpl(context.assets)
            remoteSource.getUsers(3)
        }
    }

    @Test
    fun testJsonRead() {
        runBlocking {
            val context: Context = ApplicationProvider.getApplicationContext()
            val remoteSource = UserRemoteSourceImpl(context.assets)

            var users = remoteSource.getUsers(0)
            Assert.assertEquals("Ilyse", users[0].firstName)
            Assert.assertEquals("Fee", users[1].firstName)
            Assert.assertEquals("Goldy", users[470].firstName)

            users = remoteSource.getUsers(1)
            Assert.assertEquals("Gladys", users[0].firstName)
            Assert.assertEquals("Earle", users[1].firstName)
            Assert.assertEquals("Mathew", users[470].firstName)
        }
    }
}
