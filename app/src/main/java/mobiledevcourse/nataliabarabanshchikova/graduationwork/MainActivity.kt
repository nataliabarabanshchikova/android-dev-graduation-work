package mobiledevcourse.nataliabarabanshchikova.graduationwork

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlin.coroutines.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, CoroutineScope  {

    val adapter = BoardAdapter({board : Board -> boardItemClicked(board)})
    private var allBoards: ArrayList<Board> = ArrayList()
    private val httpClient = OkHttpClient.Builder().build()

    private val rootJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + rootJob

    private var boardDatabase: BoardDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        boardDatabase = BoardDatabase.getDatabase(this)!!

        val boardList = findViewById<RecyclerView>(R.id.boardList)
        boardList.layoutManager = LinearLayoutManager(this)
        boardList.adapter = adapter

        if(savedInstanceState == null || !savedInstanceState.containsKey("allBoards")) {
            if (hasInternetConn(this))
                loadData()
            else
                GetBoardsTask(this@MainActivity).execute()
        } else {
            allBoards = savedInstanceState.get("allBoards") as ArrayList<Board>
            updateUI()
        }

    }

    private fun hasInternetConn(context: MainActivity) : Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }

    private fun loadData() = launch {
        val apiKey = MyApplication.prefs!!.apiKey
        val apiToken = MyApplication.prefs!!.apiToken
        val request = Request.Builder()
            .url("https://api.trello.com/1/members/nataliabarabanschikova/boards" +
                     "?filter=all&fields=all&lists=none&memberships=none&organization=false&organization_fields=name%2CdisplayName&" +
                     "key=$apiKey&" +
                     "token=$apiToken")
            .build()
        val response: String = withContext(Dispatchers.IO) {
            httpClient.newCall(request).execute().body()!!.string()
        }
        val type = object : TypeToken<ArrayList<Board>>() {}
        val boards = Gson().fromJson<ArrayList<Board>>(response, type.type)
        allBoards = ArrayList(boards)
        updateUI()
        SaveBoardsTask(this@MainActivity).execute()
    }

    private class SaveBoardsTask(var context: MainActivity) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            context.boardDatabase!!.boardDao().deleteAll()
            context.allBoards.forEach {
                context.boardDatabase!!.boardDao().insert(it)
            }
            return true
        }
        override fun onPostExecute(bool: Boolean?) {
            if (bool!!)
                Toast.makeText(context, "All boards added to app database", Toast.LENGTH_LONG).show()
        }
    }

    private class GetBoardsTask(var context: MainActivity) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            context.boardDatabase!!.boardDao().getAll().forEach {
                context.allBoards.add(it)
            }
            context.updateUI()
            return true
        }
        override fun onPostExecute(bool: Boolean?) {
            if (bool!!)
                Toast.makeText(context, "Get boards data from database", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateUI() {
        adapter.data.clear()
        adapter.data.addAll(allBoards)
        adapter.notifyDataSetChanged()
    }

    private fun boardItemClicked(board: Board) {
        val intent = Intent(this, BoardDetailsActivity::class.java)
        intent.putExtra("boardId", board.id)
        intent.putExtra("boardName", board.name)
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_board -> {
                val intent = Intent(this, AddBoardActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_boards -> {
            }
            R.id.nav_close_boards -> {
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putParcelableArrayList("allBoards", allBoards)
    }

    override fun onDestroy() {
        rootJob.cancel()
        super.onDestroy()
    }
}
