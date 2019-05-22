package mobiledevcourse.nataliabarabanshchikova.graduationwork

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button

class AddCardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)

        val intent = getIntent()
        val listId = if (intent.hasExtra("listId")) intent.getStringExtra("listId") else ""

        val btnAddCard = findViewById<Button>(R.id.btnAddCard)
        btnAddCard.setOnClickListener {
            addCard(listId)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun addCard(listId: String) {}
}
