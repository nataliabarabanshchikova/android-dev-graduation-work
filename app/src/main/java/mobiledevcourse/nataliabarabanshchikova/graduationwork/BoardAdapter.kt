package mobiledevcourse.nataliabarabanshchikova.graduationwork

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.text.SimpleDateFormat


class BoardAdapter (val clickListener: (Board) -> Unit): RecyclerView.Adapter<BoardAdapter.ViewHolder>() {

    val data = mutableListOf<Board>()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        val view = inflater.inflate(R.layout.board_layout, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.itemView.findViewById<TextView>(R.id.boardName).text = data[p1].name
        p0.itemView.findViewById<TextView>(R.id.boardDesc).text = data[p1].desc
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm");
        val date = dateFormat.format(data[p1].dateLastActivity)
        p0.itemView.findViewById<TextView>(R.id.boardCreated).text = date
        (p0 as ViewHolder).bind(data[p1], clickListener)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(board: Board, clickListener: (Board) -> Unit) {
            itemView.setOnClickListener { clickListener(board)}
        }
    }
}