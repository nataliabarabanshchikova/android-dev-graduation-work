package mobiledevcourse.nataliabarabanshchikova.graduationwork

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
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
    private var allLists: ArrayList<List> = ArrayList()

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

        if(savedInstanceState == null || !savedInstanceState.containsKey("allLists")) {
            if (hasInternetConn(this))
                loadData(boardId)
            else
                Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show()
        } else {
            allLists = savedInstanceState.get("allLists") as ArrayList<List>
            updateUI()
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

    private fun loadData(boardId: String) = launch {
        val apiKey = MyApplication.prefs!!.apiKey
        val apiToken = MyApplication.prefs!!.apiToken
        val request = Request.Builder()
            .url(
                "https://api.trello.com/1/boards/$boardId/lists?" +
                        "cards=none&card_fields=all&filter=open&fields=all&" +
                        "key=$apiKey&" +
                        "token=$apiToken"
            )
            .build()
        val response: String = withContext(Dispatchers.IO) {
            httpClient.newCall(request).execute().body()!!.string()
        }
        val type = object : TypeToken<ArrayList<List>>() {}
        val lists = Gson().fromJson<ArrayList<List>>(response, type.type)
        allLists = ArrayList(lists)
        updateUI()
    }

    private fun updateUI() {
        allLists.forEach({
            pageAdapter.add(PageFragment.newInstance(it.id), "${it.name}")
        })
        pageAdapter.notifyDataSetChanged()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putParcelableArrayList("allLists", allLists)
    }

    override fun onDestroy() {
        rootJob.cancel()
        super.onDestroy()
    }
}
