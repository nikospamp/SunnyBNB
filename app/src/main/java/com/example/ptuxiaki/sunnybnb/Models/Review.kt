package com.example.ptuxiaki.sunnybnb.Models

import java.util.*

data class Review(var body: String? = null,
                  var uid: String? = null,
                  var time: Any? = null,
                  var stars: Int? = null,
                  var title: String? = null) {

    fun toMap(): Map<String, Any?> {
        val userObject = HashMap<String, Any?>()

        userObject["body"] = body
        userObject["uid"] = uid
        userObject["time"] = time
        userObject["stars"] = stars
        userObject["title"] = title

        return userObject
    }

}