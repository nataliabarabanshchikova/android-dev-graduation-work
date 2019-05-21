package mobiledevcourse.nataliabarabanshchikova.graduationwork

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.*
import kotlin.coroutines.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, CoroutineScope  {

    val adapter = BoardAdapter({board : Board -> boardItemClicked(board)})

    private val httpClient = OkHttpClient.Builder().build()

    private val rootJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + rootJob

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

        loadData()
        val boardList = findViewById<RecyclerView>(R.id.boardList)
        boardList.layoutManager = LinearLayoutManager(this)
        boardList.adapter = adapter
    }

    private fun loadData() = launch {
        val request = Request.Builder()
            .url("https://api.trello.com/1/members/nataliabarabanschikova/boards" +
                     "?filter=all&fields=all&lists=none&memberships=none&organization=false&organization_fields=name%2CdisplayName&" +
                     "key=315325026f4dcdd9fa31100def752a22&" +
                     "token=8a6d287dfa9d6e8709f2423aba6c3cd6d29e576c83fa86582933afd699cfb794")
            .build()
        val response: String = withContext(Dispatchers.IO) {
            httpClient.newCall(request).execute().body()!!.string()
        }
        val type = object : TypeToken<ArrayList<Board>>() {}
        val boards = Gson().fromJson<ArrayList<Board>>(response, type.type)
        adapter.data.clear()
        adapter.data.addAll(boards)
        adapter.notifyDataSetChanged()
    }

    private fun boardItemClicked(board: Board) {
        val intent = Intent(this, BoardDetailsActivity::class.java)
        intent.putExtra("id", board.id)
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
//        savedInstanceState.putParcelableArrayList("boards", boards)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onDestroy() {
        rootJob.cancel()
        super.onDestroy()
    }
}
