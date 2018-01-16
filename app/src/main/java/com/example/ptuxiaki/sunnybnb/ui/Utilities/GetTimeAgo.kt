package com.example.ptuxiaki.sunnybnb.ui.Utilities

/**
 * Created by Pampoukidis on 11/1/2018.
 */
class GetTimeAgo {

    fun getTimeAgo(givenTime: Long): String? {
        var time = givenTime
        if (time < 1000000000000L) {
            time *= 1000
        }

        val now = System.currentTimeMillis()
        if (time > now || time <= 0) {
            return null
        }

        val diff = now - time
        return when {
            diff < MINUTE_MILLIS -> "just now"
            diff < 2 * MINUTE_MILLIS -> "a minute ago"
            diff < 50 * MINUTE_MILLIS -> (diff / MINUTE_MILLIS).toString() + " minutes ago"
            diff < 90 * MINUTE_MILLIS -> "an hour ago"
            diff < 24 * HOUR_MILLIS -> (diff / HOUR_MILLIS).toString() + " hours ago"
            diff < 48 * HOUR_MILLIS -> "yesterday"
            else -> (diff / DAY_MILLIS).toString() + " days ago"
        }
    }

    companion object {
        private val SECOND_MILLIS = 1000
        private val MINUTE_MILLIS = 60 * SECOND_MILLIS
        private val HOUR_MILLIS = 60 * MINUTE_MILLIS
        private val DAY_MILLIS = 24 * HOUR_MILLIS
    }
}