package br.rolactyl.api.utils

import br.rolactyl.api.RLApi
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.json.simple.parser.ParseException

abstract class AbstractRest {


    companion object {

        fun parse(string: String) : JSONObject? {
            val parser = JSONParser()
            return try {
                val jsonObject = parser.parse(string) as JSONObject
                jsonObject
            } catch (e: ParseException) {
                null
            }
        }

        fun formaterJson(jsonObject: JSONObject): String {
            val jsonString = jsonObject.toJSONString()
            return jsonString.replace("{", "{\n ")
                .replace("}", "\n}")
                .replace(",", ",\n ")
        }

        fun sendAction(url : String, params: JSONObject) : String {
            val client = OkHttpClient()

            val a = Request.Builder()
                .url(url)
                .get()

            for(p in params) {
                a.addHeader(p.key.toString(), p.value.toString())
            }

            val request = a.build()

            val response = client.newCall(request).execute()
            return response.body.string()
        }

    }

    var jsonObject : JSONObject? = null

    lateinit var api : RLApi

    fun getParameters() : JSONObject{
        return jsonObject!!
    }

}