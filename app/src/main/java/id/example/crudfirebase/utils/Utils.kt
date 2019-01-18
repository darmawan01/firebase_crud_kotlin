package id.example.crudfirebase.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

val Context.preference get() = getSharedPreferences("pref", MODE_PRIVATE)

fun Context.saveToPref(value: Any, key: String) {
    when(value) {
        is String -> preference.edit().putString(key, value).apply()
        is Int -> preference.edit().putInt(key, value).apply()
        is Long -> preference.edit().putLong(key, value).apply()
        is Boolean -> preference.edit().putBoolean(key, value).apply()
    }
}

fun Context.getStrPref(key: String): String? = preference.getString(key, null)

val Context.email get() = getStrPref("email")

fun Context.clearPref() {
    val p = getSharedPreferences("pref", MODE_PRIVATE)
    p.edit().clear().apply()
}

fun Context.removePref(key: String) {
    val p = preference
    p.edit().remove(key).apply()
}


