package br.rolactyl.api

import br.rolactyl.api.throws.InvalidException
import br.rolactyl.api.utils.AbstractRest
import br.rolactyl.api.utils.RestAction
import br.rolactyl.api.values.*
import br.rolactyl.api.values.actions.CreateServer
import br.rolactyl.api.values.actions.CreateUser
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.simple.JSONObject

class RLApi(private var url : String,private val token : String) {



    init {
        if(url.endsWith("/")) url.dropLast(1)
        url += "/api/v1"
        val client = OkHttpClient()
        val build = FormBody.Builder()
        val requestBody = build.build()
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer ${token}")
            .get()
            .build()

        val response : Response = client.newCall(request).execute()
        println(response)
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
            val api = RLApi("http://localhost:8080", "bSdY2VXRd3Xcjuo")
        }
    }

    fun getUser(user: String): RestAction<User> {
        val j = JSONObject()
        j["action"] = "getuser"
        j["user"] = user
        return RestAction(url, token, this, j) { User(token, url) }
    }

    fun createServer() : CreateServer {
        val createServer = CreateServer(url, token)
        createServer.api = this
        return createServer
    }

    fun createUser() : CreateUser {
        val cu = CreateUser(url, token)
        cu.api = this
        return cu
    }

    fun getServer(id : String) : RestAction<Server> {
        val j = JSONObject()
        j["action"] = "getserver"
        j["id"] = id
        return RestAction(url, token, this, j) {Server()}
    }

    fun getNode(id : Int) : RestAction<Node> {
        val j = JSONObject()
        j["action"] = "getnode"
        j["id"] = id
        return RestAction(url, token, this, j) { Node() }
    }
    
}
