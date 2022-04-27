package ru.mvi.core.utils

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> SharedPreferences.getModel(key: String): T? {
    val json = getString(key, null) ?: return null

    val tokenType = object : TypeToken<T>() {}.type

    return Gson().fromJson(json, tokenType)
}

inline fun <reified T> SharedPreferences.saveModel(key: String, value: T?) {
    val editor = edit()
    if (value == null) {
        editor.remove(key)
    } else {
        editor.putString(key, Gson().toJson(value))
    }
    editor.apply()
}
