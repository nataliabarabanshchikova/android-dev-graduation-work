package mobiledevcourse.nataliabarabanshchikova.graduationwork

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class CardAdapter : RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    val data = mutableListOf<Card>()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        val view = inflater.inflate(R.layout.card_layout, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.itemView.findViewById<TextView>(R.id.cardName).text = data[p1].name
        p0.itemView.findViewById<TextView>(R.id.cardDesc).text = data[p1].desc
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}