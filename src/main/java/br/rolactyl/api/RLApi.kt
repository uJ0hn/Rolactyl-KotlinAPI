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
            val api = RLApi("http://168.75.110.133:8080", "uirrj5mCc4LhXga")
            val server = api.createServer().setName("Murillinho")
                .setNode(api.getNode(2).queue()!!)
                .setOwner(api.getUser("admin").queue()!!)
                .setJava(Server.Java.JAVA11)
                .setRam(4096)
                .setMySQL(false)
                .setType(Server.Type.BUNGEE)
                .complete()
            println(server.queue()!!.getName())
        }
    }

    fun getUser(user: String): RestAction<User> {
        val j = JSONObject()
        j["action"] = "getuser"
        j["user"] = user
        return RestAction(url, token, this, j) { User() }
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
