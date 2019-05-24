package mobiledevcourse.nataliabarabanshchikova.graduationwork

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import kotlinx.coroutines.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.coroutines.CoroutineContext

class AddCardActivity : AppCompatActivity(), CoroutineScope {

    private val httpClient = OkHttpClient.Builder().build()

    private val rootJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + rootJob

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)

        val intent = getIntent()
        val listId = if (intent.hasExtra("listId")) intent.getStringExtra("listId") else ""

        val btnAddCard = findViewById<Button>(R.id.btnAddCard)
        btnAddCard.setOnClickListener {
            val newCardName = findViewById<EditText>(R.id.cardName).text.toString()
            val newCardDesc = findViewById<EditText>(R.id.cardDesc).text.toString()
            addCard(listId, newCardName, newCardDesc)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun addCard(listId: String, name: String, desc: String) = launch {
        val apiKey = MyApplication.prefs!!.apiKey
        val apiToken = MyApplication.prefs!!.apiToken
        val builder = FormBody.Builder()
        builder.add("name", name)
        builder.add("desc", desc)
        val formBody = builder.build()
        val request = Request.Builder()
            .url("https://api.trello.com/1/cards/?" +
                    "idList=$listId&keepFromSource=all&" +
                    "key=$apiKey&" +
                    "token=$apiToken")
            .post(formBody)
            .build()
        val response: String = withContext(Dispatchers.IO) {
            httpClient.newCall(request).execute().body()!!.string()
        }

        val intentUpdateUI = "mobiledevcourse.nataliabarabanshchikova.graduationwork.action.UPDATE_UI"
        val broadcaster = LocalBroadcastManager.getInstance(this@AddCardActivity)
        broadcaster.sendBroadcast(Intent(intentUpdateUI))
        finish()
    }

    override fun onDestroy() {
        rootJob.cancel()
        super.onDestroy()
    }

}
