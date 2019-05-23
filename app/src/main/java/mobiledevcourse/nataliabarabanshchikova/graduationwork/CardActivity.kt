package mobiledevcourse.nataliabarabanshchikova.graduationwork

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.ArrayList
import kotlin.coroutines.CoroutineContext


class CardActivity : AppCompatActivity(), CoroutineScope {

    private var currCard: CardDetail ?= null
    private val httpClient = OkHttpClient.Builder().build()

    private val rootJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + rootJob

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_detail)

        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)

        val intent = getIntent()
        val cardId = if (intent.hasExtra("id")) intent.getStringExtra("id") else ""

        if(savedInstanceState == null || !savedInstanceState.containsKey("currCard")) {
            loadData(cardId)
        } else {
            currCard = savedInstanceState.get("currCard") as CardDetail
            updateUI()
        }

        loadData(cardId)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun loadData(cardId: String) = launch {
        val apiKey = MyApplication.prefs!!.apiKey
        val apiToken = MyApplication.prefs!!.apiToken
        val request = Request.Builder()
            .url("https://api.trello.com/1/cards/$cardId?" +
                    "attachments=true&attachment_fields=all&members=false&membersVoted=false&" +
                    "checkItemStates=false&checklists=none&checklist_fields=all&board=false&list=false" +
                    "&pluginData=false&stickers=false&sticker_fields=all&customFieldItems=false&" +
                    "key=$apiKey&" +
                    "token=$apiToken")
            .build()
        val response: String = withContext(Dispatchers.IO) {
            httpClient.newCall(request).execute().body()!!.string()
        }
        val type = object : TypeToken<CardDetail>() {}
        val card = Gson().fromJson<CardDetail>(response, type.type)
        currCard = card
        updateUI()
    }

    private fun updateUI() {
        findViewById<TextView>(R.id.cardName).text = currCard!!.name

        if (currCard!!.desc.isEmpty()) {
            findViewById<TextView>(R.id.cardDesc).visibility = View.GONE
            findViewById<TextView>(R.id.cardDescLabel).visibility = View.GONE
        } else
            findViewById<TextView>(R.id.cardDesc).text = currCard!!.desc

        if (!currCard!!.attachments.isEmpty()) {
            val imageView =findViewById<ImageView>(R.id.cardImageView)
            val imageUrl = currCard!!.attachments[0].url
            Glide.with(this).load(imageUrl).into(imageView)
        } else {
            findViewById<ImageView>(R.id.cardImageView).visibility = View.GONE;
            findViewById<TextView>(R.id.cardImageLabel).visibility = View.GONE;
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putParcelable("currCard", currCard)
    }

    override fun onDestroy() {
        rootJob.cancel()
        super.onDestroy()
    }
}
