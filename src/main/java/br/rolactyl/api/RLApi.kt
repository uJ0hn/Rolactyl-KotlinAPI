package br.rolactyl.api

import br.rolactyl.api.throws.InvalidException
import br.rolactyl.api.utils.AbstractRest
import br.rolactyl.api.utils.RestAction
import br.rolactyl.api.values.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.simple.JSONObject
import java.util.Timer
import kotlin.concurrent.schedule

class RLApi {

    init {
        if(RLBuilder.url.endsWith("/")) RLBuilder.url.dropLast(1)
        RLBuilder.url += "/api/v1"
        val client = OkHttpClient()
        val build = FormBody.Builder()
        val requestBody = build.build()
        val request = Request.Builder()
            .url(RLBuilder.url)
            .addHeader("Authorization", "Bearer ${RLBuilder.token}")
            .post(requestBody)
            .build()

        val response : Response = client.newCall(request).execute()
        val json = AbstractRest.parse(response.body.string())!!
        if(json["error"] != "null") {
            throw InvalidException(json["error"].toString())
        } else {
            println(json["success"])
        }
    }

    companion object {
        @JvmStatic
        fun main(args :Array<String>) {
            val api = RLBuilder("http://localhost:8080", "fdgtr6hr6rfghrhfghfth").build()
            api.createUser()
                .setName("xerequinha")
                .setEmail("xerequinha@gmail.com")
                .setPassword("xerequinha")
                .isAdmin(false)
                .complete()
            Timer().schedule(250L) {
                api.createServer()
                    .setName("Xereca")
                    .setOwner(api.getUser("xerequinha").queue()!!)
                    .setNode(api.getNode(1).queue()!!)
                    .setRam(1024)
                    .complete()
            }
        }
    }

    fun getUser(user: String): RestAction<User> {
        val j = JSONObject()
        j["action"] = "getuser"
        j["user"] = user
        return RestAction(this, j) { User() }
    }

    fun createServer() : CreateServer {
        return CreateServer()
    }

    fun createUser() : CreateUser {
        return CreateUser()
    }

    fun getServer(id : String) : RestAction<Server> {
        val j = JSONObject()
        j["action"] = "getserver"
        j["id"] = id
        return RestAction(this, j) {Server()}
    }

    fun getNode(id : Int) : RestAction<Node> {
        val j = JSONObject()
        j["action"] = "getnode"
        j["id"] = id
        return RestAction(this, j) { Node() }
    }


}
