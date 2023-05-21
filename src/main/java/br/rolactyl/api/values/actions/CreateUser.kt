package br.rolactyl.api.values.actions

import br.rolactyl.api.throws.InvalidException
import br.rolactyl.api.utils.AbstractRest
import br.rolactyl.api.utils.RestAction
import br.rolactyl.api.values.User
import okhttp3.*
import org.json.simple.JSONObject

class CreateUser(private val url: String,private val token: String ) : AbstractRest(){

    private val json = JSONObject()

    fun setName(name: String) : CreateUser {
        json["name"] = name
        return this
    }

    fun setEmail(email: String) : CreateUser {
        json["email"] = email
        return this
    }

    fun setPassword(password: String) : CreateUser {
        json["password"] = password
        return this
    }

    fun isAdmin(i: Boolean) : CreateUser {
        val a = if(i) 1 else 2
        json["admin"] = a
        return this
    }

    fun complete() : RestAction<User>{
        val client = OkHttpClient()
        val build = FormBody.Builder()
        for(v in json) {
            build.add(v.key.toString(), v.value.toString())
        }
        build.add("action", "usercreate")
        val requestBody = build.build()
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")
            .post(requestBody)
            .build()

        val response : Response = client.newCall(request).execute()
        val a = response.body.string()
        val b = parse(a)!!
        if(b["error"] != "null") {
            throw InvalidException(b["error"].toString())
        }

        return api.getUser(json["name"].toString())
    }


}