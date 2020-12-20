package dev.chulwoo.hp.test.feature.user.data.source

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.chulwoo.hp.test.common.PageNotFound
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
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
            assertThat(users[0].firstName, equalTo("Ilyse"))
            assertThat(users[1].firstName, equalTo("Fee"))
            assertThat(users[470].firstName, equalTo("Goldy"))

            users = remoteSource.getUsers(1)
            assertThat(users[0].firstName, equalTo("Gladys"))
            assertThat(users[1].firstName, equalTo("Earle"))
            assertThat(users[470].firstName, equalTo("Mathew"))
        }
    }
}
