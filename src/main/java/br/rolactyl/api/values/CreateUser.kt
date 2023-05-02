package br.rolactyl.api.values

import br.rolactyl.api.RLBuilder
import br.rolactyl.api.throws.InvalidException
import br.rolactyl.api.utils.AbstractRest
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.simple.JSONObject

class CreateUser {

    private val json = JSONObject()

    fun setName(name: String) : CreateUser {
        json["name"] = name
        return this
    }

    fun setEmail(email: String) : CreateUser{
        json["email"] = email
        return this
    }

    fun setPassword(password: String) : CreateUser{
        json["password"] = password
        return this
    }

    fun isAdmin(i: Boolean) : CreateUser{
        val a = if(i) 1 else 2
        json["admin"] = a
        return this
    }

    fun complete() {
        val client = OkHttpClient()
        val build = FormBody.Builder()
        for(v in json) {
            build.add(v.key.toString(), v.value.toString())
        }
        build.add("action", "usercreate")
        val requestBody = build.build()
        val request = Request.Builder()
            .url(RLBuilder.url)
            .addHeader("Authorization", "Bearer ${RLBuilder.token}")
            .post(requestBody)
            .build()

        val response : Response = client.newCall(request).execute()
        val a = response.body.string()
        val json = AbstractRest.parse(a)!!
        if(json["error"] != "null") {
            throw InvalidException(json["error"].toString())
        }
    }


}