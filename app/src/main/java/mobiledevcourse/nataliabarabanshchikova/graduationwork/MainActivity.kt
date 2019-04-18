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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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

        val testBoardData = createTestData()
        val boardList = findViewById<RecyclerView>(R.id.boardList)
        boardList.layoutManager = LinearLayoutManager(this)
        boardList.adapter = BoardAdapter(testBoardData, {board : Board -> boardItemClicked(board)})
    }

    private fun createTestData() : List<Board> {
        val boardList = ArrayList<Board>()
        boardList.add(Board(1, "Board #1", "Description for board #1", "2019-04-18"))
        boardList.add(Board(2, "Board #2", "Description for board #2", "2019-04-18"))
        boardList.add(Board(3, "Board #3", "Description for board #3", "2019-04-18"))
        boardList.add(Board(4, "Board #4", "Description for board #4", "2019-04-18"))
        boardList.add(Board(5, "Board #5", "Description for board #5", "2019-04-18"))
        return boardList
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
            R.id.action_settings -> {
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
}
