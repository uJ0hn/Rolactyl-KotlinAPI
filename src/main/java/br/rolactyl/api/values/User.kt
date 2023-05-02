package br.rolactyl.api.values

import br.rolactyl.api.utils.AbstractRest

class User : AbstractRest() {

    fun getName() : String{
        return jsonObject!!["name"].toString()
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