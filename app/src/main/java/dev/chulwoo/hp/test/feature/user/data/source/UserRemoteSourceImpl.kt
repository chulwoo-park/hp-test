package dev.chulwoo.hp.test.feature.user.data.source

import android.content.res.AssetManager
import dev.chulwoo.hp.test.common.InternalServerError
import dev.chulwoo.hp.test.common.NetworkException
import dev.chulwoo.hp.test.common.PageNotFound
import dev.chulwoo.hp.test.feature.user.data.repository.RemoteUserSource
import dev.chulwoo.hp.test.feature.user.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import kotlin.random.Random


class UserRemoteSourceImpl(private val assetManager: AssetManager) : RemoteUserSource {

    override suspend fun getUsers(page: Int): List<User> {
        return withContext(Dispatchers.IO) {
            try {
                val userListJson = JSONArray(readJson("list${page + 1}.json"))
                val users = mutableListOf<User>()
                for (i in 0 until userListJson.length()) {
                    users.add(mapJsonToUser(userListJson.getJSONObject(i)))
                }
                applyNetworkLatency()
                users
            } catch (e: FileNotFoundException) {
                throw PageNotFound()
            } catch (e: IOException) {
                throw NetworkException()
            } catch (e: Exception) {
                throw InternalServerError()
            }
        }
    }

    private suspend fun applyNetworkLatency() {
        delay(Random.nextLong(500))
    }

    private fun mapJsonToUser(json: JSONObject): User {
        return User(
            id = json.getInt("id"),
            lastName = json.getString("last_name"),
            firstName = json.getString("first_name"),
            email = json.getString("email"),
        )
    }

    private suspend fun readJson(name: String): String {
        return withContext(Dispatchers.IO) {
            val inputStream: InputStream = assetManager.open(name)
            inputStream.bufferedReader().use { it.readText() }
        }
    }
}
