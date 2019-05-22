package mobiledevcourse.nataliabarabanshchikova.graduationwork

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.ArrayList
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.*
import kotlinx.coroutines.*

class BoardDetailsActivity : AppCompatActivity(), CoroutineScope {

    private val pageAdapter = PageAdapter(supportFragmentManager)

    private val httpClient = OkHttpClient.Builder().build()

    private val rootJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + rootJob

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)

        val intent = getIntent()
        val boardId = if (intent.hasExtra("boardId")) intent.getStringExtra("boardId") else ""
        val boardName = if (intent.hasExtra("boardName")) intent.getStringExtra("boardName") else ""

        val actionbar = supportActionBar
        supportActionBar!!.title = boardName
        supportActionBar!!.setElevation(0f)
        actionbar!!.setDisplayHomeAsUpEnabled(true)

        val view_pager = findViewById<ViewPager>(R.id.view_pager)
        val tabs = findViewById<TabLayout>(R.id.tabs)
        view_pager.adapter = pageAdapter
        tabs.setupWithViewPager(view_pager)

        loadData(boardId)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun loadData(boardId: String) = launch {
        val apiKey = MyApplication.prefs!!.apiKey
        val apiToken = MyApplication.prefs!!.apiToken
        val request = Request.Builder()
            .url("https://api.trello.com/1/boards/$boardId/lists?" +
                    "cards=none&card_fields=all&filter=open&fields=all&" +
                    "key=$apiKey&" +
                    "token=$apiToken")
            .build()
        val response: String = withContext(Dispatchers.IO) {
            httpClient.newCall(request).execute().body()!!.string()
        }
        val type = object : TypeToken<ArrayList<List>>() {}
        val lists = Gson().fromJson<ArrayList<List>>(response, type.type)
        lists.forEach({
            pageAdapter.add(PageFragment.newInstance(it.id), "${it.name}")
        })
        pageAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        rootJob.cancel()
        super.onDestroy()
    }
}
