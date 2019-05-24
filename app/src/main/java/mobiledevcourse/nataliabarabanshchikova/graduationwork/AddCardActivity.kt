package mobiledevcourse.nataliabarabanshchikova.graduationwork

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
            if (hasInternetConn(this)) {
                val newCardName = findViewById<EditText>(R.id.cardName).text.toString()
                val newCardDesc = findViewById<EditText>(R.id.cardDesc).text.toString()
                addCard(listId, newCardName, newCardDesc)
            } else
                Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show()
        }
    }

    private fun hasInternetConn(context: Context) : Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected == true
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
        finish()
    }

    override fun onDestroy() {
        rootJob.cancel()
        super.onDestroy()
    }

}
