package mobiledevcourse.nataliabarabanshchikova.graduationwork

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView

class PageFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_lists, container, false)
        val listId = getArguments()!!.getString(LIST_ID)

        val tvHello: TextView = view.findViewById(R.id.tv_hello)
        tvHello.text = "Fragment $listId"
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
}