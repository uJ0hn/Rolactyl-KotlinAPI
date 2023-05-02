package br.rolactyl.api.utils

import br.rolactyl.api.RLApi
import br.rolactyl.api.RLBuilder
import br.rolactyl.api.throws.InvalidException
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.simple.JSONObject
import java.lang.reflect.ParameterizedType

class RestAction<T : AbstractRest>(val api : RLApi, val jsonObject: JSONObject ,private val apiConstructor: () -> T) {


    fun queue(): T? {

        val client = OkHttpClient()
        val build = FormBody.Builder()
        for(v in jsonObject) {
            build.add(v.key.toString(), v.value.toString())
        }
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
            println(json["error"])
            return null
        }



        val api = apiConstructor()
        api.jsonObject = json
        api.api = this.api
        return api
    }


}