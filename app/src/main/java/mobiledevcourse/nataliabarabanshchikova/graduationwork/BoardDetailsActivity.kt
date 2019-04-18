package mobiledevcourse.nataliabarabanshchikova.graduationwork

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class BoardDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)

        val intent = getIntent()
        val boardName = if (intent.hasExtra("boardName")) intent.getStringExtra("boardName") else ""
        val boardDesc = if (intent.hasExtra("boardDesc")) intent.getStringExtra("boardDesc") else ""
        val boardCreated = if (intent.hasExtra("boardCreated")) intent.getStringExtra("boardCreated") else ""

        findViewById<TextView>(R.id.boardName).text = boardName
        findViewById<TextView>(R.id.boardDesc).text = boardDesc
        findViewById<TextView>(R.id.boardCreated).text = boardCreated
    }
}
