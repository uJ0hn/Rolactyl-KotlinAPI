package br.rolactyl.api.values.actions

import br.rolactyl.api.throws.InvalidException
import br.rolactyl.api.utils.AbstractRest
import br.rolactyl.api.utils.RestAction
import br.rolactyl.api.values.Node
import br.rolactyl.api.values.Server
import br.rolactyl.api.values.User
import okhttp3.*
import org.json.simple.JSONObject

class CreateServer(private val url: String, private val token: String) : AbstractRest() {

    private val json = JSONObject()

    fun setName(name : String) : CreateServer {
        json["name"] = name
        return this
    }

    fun setNode(node : Node) : CreateServer {
        json["node"] = node.getId()
        return this
    }

    fun setRam(ram : Int) : CreateServer {
        json["ram"] = ram
        return this
    }

    fun setOwner(user: User) : CreateServer {
        json["owner"] = user.getName()
        return this
    }

    fun setType(type: Server.Type) : CreateServer {
        json["type"] = type.type
        return this
    }

    fun setPort(port : Int) : CreateServer {
        json["port"] = port
        return this
    }

    fun setJava(java: Server.Java) : CreateServer {
        json["java"] = java.int
        return this
    }

    fun setMySQL(boolean: Boolean): CreateServer {
        json["mysql"] = boolean.toString()
        return this
    }

    fun complete(): RestAction<Server> {
        val client = OkHttpClient()
        val build = FormBody.Builder()
        for(v in json) {
            build.add(v.key.toString(), v.value.toString())
        }
        build.add("action", "createserver")
        val requestBody = build.build()
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")
            .post(requestBody)
            .build()

        val response : Response = client.newCall(request).execute()
        val a = response.body.string()
        val json = parse(a)!!
        if(json["error"] != "null" && json["error"].toString().length != 5) {
            throw InvalidException(json["error"].toString())
        }

        return api.getServer(json["error"].toString())
    }



}