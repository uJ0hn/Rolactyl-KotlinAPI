package br.rolactyl.api.utils

import br.rolactyl.api.RLApi
import br.rolactyl.api.throws.InvalidException
import okhttp3.*
import org.json.simple.JSONObject
import java.lang.reflect.ParameterizedType

class RestAction<T : AbstractRest>(private val url: String,
                                   private val token : String,
                                    private val api : RLApi,
                                   private val jsonObject: JSONObject,
                                   private val apiConstructor: () -> T) {


    fun queue(): T? {

        val client = OkHttpClient()
        val build = FormBody.Builder()
        for(v in jsonObject) {
            build.add(v.key.toString(), v.value.toString())
        }
        val requestBody = build.build()
        val request1 = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")
            .get()

        for(v in jsonObject) {
            request1.addHeader(v.key.toString(), v.value.toString())
        }
        val request = request1.build()
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