package br.rolactyl.api.values

import br.rolactyl.api.RLBuilder
import br.rolactyl.api.throws.InvalidException
import br.rolactyl.api.utils.AbstractRest
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.simple.JSONObject

class CreateServer : AbstractRest() {

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

    fun setPort(port : Int) : CreateServer{
        json["port"] = port
        return this
    }

    fun complete() {
        val client = OkHttpClient()
        val build = FormBody.Builder()
        for(v in json) {
            build.add(v.key.toString(), v.value.toString())
        }
        build.add("action", "createserver")
        val requestBody = build.build()
        val request = Request.Builder()
            .url(RLBuilder.url)
            .addHeader("Authorization", "Bearer ${RLBuilder.token}")
            .post(requestBody)
            .build()

        val response : Response = client.newCall(request).execute()
        val a = response.body.string()
        val json = parse(a)!!
        if(json["error"] != "null") {
            throw InvalidException(json["error"].toString())
        }
    }



}