package br.rolactyl.api.values

import br.rolactyl.api.utils.AbstractRest

class Node : AbstractRest() {

    fun getId() : Long {
        return jsonObject!!["id"] as Long
    }

    fun getIp() : String {
        return jsonObject!!["ip"].toString()
    }

    fun getUrl() : String {
        return jsonObject!!["url"].toString()
    }

    fun getStatus() : String {
        return jsonObject!!["status"].toString()
    }

    @Suppress("UNCHECKED_CAST")
    fun getServers() : List<Server> {
        val list = ArrayList<Server>()
        val a = jsonObject!!["servers"] as List<String>
        for(b in a) list.add(api.getServer(b).queue()!!)
        return list
    }

}