package mobiledevcourse.nataliabarabanshchikova.graduationwork

import android.app.Application

class MyApplication : Application() {
    var apiKey = "315325026f4dcdd9fa31100def752a22"
    var apiToken = "8a6d287dfa9d6e8709f2423aba6c3cd6d29e576c83fa86582933afd699cfb794"

    companion object {
        var prefs: Prefs? = null
    }

    override fun onCreate() {
        prefs = Prefs(applicationContext)
        prefs!!.apiKey = apiKey
        prefs!!.apiToken = apiToken
        super.onCreate()
    }
}