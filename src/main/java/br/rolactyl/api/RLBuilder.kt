package br.rolactyl.api

class RLBuilder(private val url : String,private val token : String) {

    companion object {
        lateinit var url : String
        lateinit var token : String
    }

    fun build() : RLApi {
        RLBuilder.url = this.url
        RLBuilder.token = this.token
        return RLApi()
    }


}