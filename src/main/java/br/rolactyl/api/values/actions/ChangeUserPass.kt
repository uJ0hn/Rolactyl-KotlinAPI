package br.rolactyl.api.values.actions

import br.rolactyl.api.throws.InvalidException
import br.rolactyl.api.utils.AbstractRest
import br.rolactyl.api.utils.RestAction
import br.rolactyl.api.values.Server
import br.rolactyl.api.values.User
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.simple.JSONObject

class ChangeUserPass(private val url: String, private val token: String) : AbstractRest() {

    private val json = JSONObject()

    fun username(user: User) : ChangeUserPass{
        json["user"] = user.getName()
        return this
    }

    fun newpassword(string: String) : ChangeUserPass{
        json["newpass"] = string
        return this
    }

    fun complete() {
        val client = OkHttpClient()

        val request1 = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")
            .addHeader("action", "changeuserpass")
            .get()

        for(v in json) {
            request1.addHeader(v.key.toString(), v.value.toString())
        }
        val request = request1.build()

        val response : Response = client.newCall(request).execute()
        val a = response.body.string()
        val json = parse(a)!!
        if(json["error"] != "null" && json["error"].toString().length != 5) {
            throw InvalidException(json["error"].toString())
        }
    }


}