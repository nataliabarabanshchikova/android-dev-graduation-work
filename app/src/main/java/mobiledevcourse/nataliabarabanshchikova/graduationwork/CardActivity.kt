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

        loadData(cardId)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun loadData(cardId: String) = launch {
        val request = Request.Builder()
            .url("https://api.trello.com/1/cards/$cardId?attachments=true&attachment_fields=all&" +
                    "members=false&membersVoted=false&checkItemStates=false&checklists=none&checklist_fields=all&b" +
                    "oard=false&list=false&pluginData=false&stickers=false&sticker_fields=all&customFieldItems=false&" +
                    "key=315325026f4dcdd9fa31100def752a22&" +
                    "token=8a6d287dfa9d6e8709f2423aba6c3cd6d29e576c83fa86582933afd699cfb794")
            .build()
        val response: String = withContext(Dispatchers.IO) {
            httpClient.newCall(request).execute().body()!!.string()
        }
        val type = object : TypeToken<CardDetail>() {}
        val card = Gson().fromJson<CardDetail>(response, type.type)
        updateUI(card)
    }

    private fun updateUI(card: CardDetail) {
        findViewById<TextView>(R.id.cardName).text = card.name

        if (card.desc.isEmpty()) {
            findViewById<TextView>(R.id.cardDesc).visibility = View.GONE
            findViewById<TextView>(R.id.cardDescLabel).visibility = View.GONE
        } else
            findViewById<TextView>(R.id.cardDesc).text = card.desc

        if (!card.attachments.isEmpty()) {
            val imageView = ImageView(this)
            Glide.with(this).load(card.attachments[0].url).into(imageView)
            findViewById<LinearLayout>(R.id.cardImage).addView(imageView)
        } else {
            findViewById<LinearLayout>(R.id.cardImage).visibility = View.GONE;
            findViewById<TextView>(R.id.cardImageLabel).visibility = View.GONE;
        }
    }

    override fun onDestroy() {
        rootJob.cancel()
        super.onDestroy()
    }
}
