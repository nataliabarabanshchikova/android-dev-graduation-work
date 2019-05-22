package mobiledevcourse.nataliabarabanshchikova.graduationwork

import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context) {
    val API_KEY = "API_KEY"
    val API_TOKEN = "API_TOKEN"
    val PREFS_FILENAME = "mobiledevcourse.nataliabarabanshchikova.graduationwork.localsettings"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);

    var apiKey: String
        get() = prefs.getString(API_KEY, "")
        set(value) = prefs.edit().putString(API_KEY, value).apply()

    var apiToken: String
        get() = prefs.getString(API_TOKEN, "")
        set(value) = prefs.edit().putString(API_TOKEN, value).apply()
}