package com.example.ptuxiaki.sunnybnb.Models

/**
 * Created by Pampoukidis on 16/1/2018.
 */

/* Set values in order to create empty constructor */
data class Message(var message: String? = null, var time: Long? = null, var seen: Boolean? = null, var type: String? = null, var from: String? = null)