package com.ichsanalfian.elog_pdam.local

import android.content.Context
import com.ichsanalfian.elog_pdam.model.UserData

class UserPreferences(context: Context) { //TODO Kelas Tambahan

    companion object {
        private const val USER_PREF = "user_pref"
        private const val USER_ID = "user id"
        private const val USER_NAME = "user name"
        private const val USER_ROLE = "user role"
        var user = UserData()
    }

    private val preferences = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)

    fun setUser(data: UserData) {
        val editor = preferences.edit()
        editor.putString(USER_ID, data.id)
        editor.putString(USER_NAME, data.username)
        editor.putString(USER_ROLE, data.role)
        editor.apply()
        user = data
    }

    fun getUser(): UserData {
        val data = UserData()
        data.id = preferences.getString(USER_ID, "")
        data.username = preferences.getString(USER_NAME, "")
        data.username = preferences.getString(USER_ROLE, "")
        user = data
        return data
    }

}