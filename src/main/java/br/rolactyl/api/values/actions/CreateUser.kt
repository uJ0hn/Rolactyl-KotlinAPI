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


    private fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^\\S+@\\S+\\.\\S+\$") // Regex para verificar o formato básico de um email

        return emailRegex.matches(email)
    }

    fun setEmail(email: String) : CreateUser {
        if(!isEmailValid(email)) throw InvalidException("O email do novo usuario precisa ser valido!")
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

        val request1 = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")
            .addHeader("action", "usercreate")
            .get()

        for(v in json) {
            request1.addHeader(v.key.toString(), v.value.toString())
        }

        val request = request1.build()

        val response : Response = client.newCall(request).execute()
        val a = response.body.string()
        val b = parse(a)!!
        if(b["error"] != "null") {
            throw InvalidException(b["error"].toString())
        }

        return api.getUser(json["name"].toString())
    }


}