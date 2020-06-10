package kz.evilteamgenius.chessapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.evilteamgenius.chessapp.R


class ItemDesignListDialogFragment(val listeren: ListenerOfChoose) : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item_list_dialog_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<RecyclerView>(R.id.list).layoutManager = LinearLayoutManager(context)
        view.findViewById<RecyclerView>(R.id.list).adapter = ItemDesignAdapter()
    }

    private inner class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.fragment_item_list_dialog_list_dialog_item, parent, false)) {

        internal val text: TextView = itemView.findViewById(R.id.text)
    }

    private inner class ItemDesignAdapter : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.text.text = (position + 1).toString()
            when (position) {
                0 -> {
                    holder.text.background = resources.getDrawable(R.drawable.back_grad)
                }
                1 -> {
                    holder.text.background = resources.getDrawable(R.drawable.back_grad_1)
                }
                2 -> {
                    holder.text.background = resources.getDrawable(R.drawable.back_grad_3)
                }
            }
            holder.text.setOnClickListener {
                listeren.selectStyle(position)
                dismiss()
            }

        }

        override fun getItemCount(): Int {
            return 3
        }
    }

    interface ListenerOfChoose {
        fun selectStyle(position: Int)
    }
}