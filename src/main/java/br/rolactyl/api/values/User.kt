package br.rolactyl.api.values

import br.rolactyl.api.utils.AbstractRest

class User : AbstractRest() {

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


}