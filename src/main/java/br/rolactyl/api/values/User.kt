package br.rolactyl.api.values

import br.rolactyl.api.utils.AbstractRest
import br.rolactyl.api.values.actions.ChangeUserPass

class User(private val token: String, private val url: String) : AbstractRest() {

    fun getName() : String{
        return jsonObject!!["name"].toString()
    }

    @Suppress("UNCHECKED_CAST")
    fun getServers() : List<Server> {
        val list = ArrayList<Server>()
        val a = jsonObject!!["servers"] as List<String>
        for(b in a) list.add(api.getServer(b).queue()!!)
        return list
    }

    fun getEmail() : String {
        return jsonObject!!["email"].toString()
    }

    fun isAdmin() : Boolean {
        return jsonObject!!["admin"] as Boolean
    }

    fun getAtualToken() : String {
        return jsonObject!!["token"].toString()
    }

    fun changeUserPass(newpass: String) : User {
        ChangeUserPass(url, token)
            .username(this)
            .newpassword(newpass)
            .complete()
        return this
    }


}