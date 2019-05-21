package mobiledevcourse.nataliabarabanshchikova.graduationwork

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.ArrayList
import kotlin.coroutines.CoroutineContext

class PageFragment : Fragment(), CoroutineScope {

    val adapter = CardAdapter({card : Card -> cardItemClicked(card)})

    private val httpClient = OkHttpClient.Builder().build()

    private val rootJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + rootJob

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_lists, container, false)
        val listId = getArguments()!!.getString(LIST_ID)

        loadData(listId)
        val cardList = view.findViewById<RecyclerView>(R.id.cardList)
        cardList.layoutManager = LinearLayoutManager(context)
        cardList.adapter = adapter

        return view
    }

    companion object {
        val LIST_ID = "LIST_ID"
        fun newInstance(listId: String): PageFragment {
            val fragment = PageFragment()
            val args = Bundle()
            args.putString(LIST_ID, listId)
            fragment.setArguments(args)
            return fragment
        }
    }

    private fun loadData(listId: String) = launch {
        val request = Request.Builder()
            .url("https://api.trello.com/1/lists/$listId/cards?cards=none&card_fields=all&filter=open&fields=all&" +
                    "key=315325026f4dcdd9fa31100def752a22&" +
                    "token=8a6d287dfa9d6e8709f2423aba6c3cd6d29e576c83fa86582933afd699cfb794")
            .build()
        val response: String = withContext(Dispatchers.IO) {
            httpClient.newCall(request).execute().body()!!.string()
        }
        val type = object : TypeToken<ArrayList<Card>>() {}
        val cards = Gson().fromJson<ArrayList<Card>>(response, type.type)
        adapter.data.clear()
        adapter.data.addAll(cards)
        adapter.notifyDataSetChanged()
    }

    private fun cardItemClicked(card: Card) {
        val intent = Intent(context, CardActivity::class.java)
        intent.putExtra("id", card.id)
        startActivity(intent)
    }
}