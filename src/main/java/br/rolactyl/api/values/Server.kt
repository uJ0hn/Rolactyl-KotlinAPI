package br.rolactyl.api.values

import br.rolactyl.api.utils.AbstractRest
import org.json.simple.JSONObject

class Server : AbstractRest() {

    fun getId() : String {
        return jsonObject!!["id"].toString()
    }

    fun getName() : String {
        return jsonObject!!["name"].toString()
    }

    fun getRam() : Long {
        return jsonObject!!["ram"] as Long
    }

    fun getPort() : Long {
        return jsonObject!!["port"] as Long
    }

    fun getStatus() : Status {
        return Status.valueOf(jsonObject!!["status"].toString().uppercase())
    }

    fun getIp() : String {
        return jsonObject!!["ip"].toString()
    }

    fun getNode() : Node {
        return api.getNode((jsonObject!!["node"] as Long).toInt()).queue()!!
    }

    fun getOwner() : User {
        return api.getUser(jsonObject!!["owner"].toString()).queue()!!
    }

    fun getType(): Type {
        return Type.values().filter { it.type == jsonObject!!["type"].toString() }[0]
    }

    fun getJar() : String {
        return jsonObject!!["jar"].toString()
    }

    fun sendAction(action: Action) {
        val json = JSONObject()
        json["id"] = getId()
        json["token"] = api.getUser("admin").queue()!!.getAtualToken()
        json["action"] = action.action
        sendAction(getNode().getUrl() + "/manager", json)
    }

    enum class Action(val action : String) {
        START("start"),
        STOP("stop"),
        KILL("kill")
    }

    enum class Type(val type : String) {
        SPIGOT("spigot"),
        BUNGEE("bungee")
    }

    enum class Status(val status : String) {
        ONLINE("online"),
        OFFLINE("offline"),
        DESLIGADO("offline"),
        LIGADO("online")
    }

    enum class Java(val int: Int) {
        JAVA8(8),
        JAVA11(11),
        JAVA17(17)
    }


    companion object {
    }

}