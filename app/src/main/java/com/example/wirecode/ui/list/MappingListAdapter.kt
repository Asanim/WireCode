package com.example.wirecode.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wirecode.ExpandableTextView
import com.example.wirecode.R
import com.example.wirecode.database.Mapping

class MappingListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<MappingListAdapter.MappingViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var mappings = emptyList<Mapping>() // Cached copy of words

    inner class MappingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val mappingTitleView: TextView = itemView.findViewById(R.id.xmapping_title)
        val mappingPinView: TextView = itemView.findViewById(R.id.xmapping_title)
        val mappingPinExpandableView: ExpandableTextView = itemView.findViewById(R.id.xpin_mappings_expandable)

        override fun onClick(v: View) {
            MappingListAdapter.clickListener!!.onItemClick(adapterPosition, v)
        }
        init {
            itemView.setOnClickListener(this)
        }
    }

    fun setOnItemClickListener(clickListener: ClickListener?) {
        Companion.clickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(position: Int, v: View?)
    }

    companion object {
        private var clickListener: ClickListener? = null
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MappingViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)

        return MappingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MappingViewHolder, position: Int) {
        val current = mappings[position]
        holder.mappingTitleView.text = current.partNumber.toString()
        //holder.mappingPinView.text = current.pinMapping.toString()
        var mappingString = ""
        current.pinMapping.forEachIndexed() { count , it ->
            var i = count +1
            mappingString += "Pin $i: " + it + "\n"
        }

        holder.mappingPinExpandableView.text = mappingString
    }

    internal fun setWords(words: List<Mapping>) {
        this.mappings = words
        notifyDataSetChanged()
    }

    override fun getItemCount() = mappings.size
}
