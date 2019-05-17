package mobiledevcourse.nataliabarabanshchikova.graduationwork

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

//class BoardAdapter (val boardList: List<Board>, val clickListener: (Board) -> Unit) :
//    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        val view = inflater.inflate(R.layout.board_layout, parent, false)
//        return PartViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        holder.itemView.findViewById<TextView>(R.id.boardName).text = boardList[position].name
//        holder.itemView.findViewById<TextView>(R.id.boardDesc).text = boardList[position].desc
////        holder.itemView.findViewById<TextView>(R.id.boardCreated).text = boardList[position].created
        (holder as PartViewHolder).bind(boardList[position], clickListener)
//    }
//
//    override fun getItemCount() = boardList.size
//
//    class PartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bind(board: Board, clickListener: (Board) -> Unit) {
//            itemView.setOnClickListener { clickListener(board)}
//        }
//    }
//}


class BoardAdapter : RecyclerView.Adapter<BoardAdapter.ViewHolder>() {

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
        p0.itemView.findViewById<TextView>(R.id.boardCreated).text = data[p1].dateLastActivity.toString()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}