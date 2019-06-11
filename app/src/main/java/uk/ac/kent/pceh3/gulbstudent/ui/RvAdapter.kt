package uk.ac.kent.pceh3.gulbstudent.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import uk.ac.kent.pceh3.gulbstudent.R
import uk.ac.kent.pceh3.gulbstudent.model.Deal

/**
 * Created by pceh3 on 11/06/2019.
 */
class RvAdapter(val dealList: ArrayList<Deal>) : RecyclerView.Adapter<RvAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0?.context).inflate(R.layout.adapter_item_layout, p0, false)
        return ViewHolder(v)
    }
    override fun getItemCount(): Int {
        return dealList.size
    }
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        p0.code?.text = dealList[p1].code
        p0.description?.text = dealList[p1].description
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val code = itemView.findViewById<TextView>(R.id.code)
        val description = itemView.findViewById<TextView>(R.id.description)

    }
}