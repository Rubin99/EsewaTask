package com.esewa.esewatask.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.esewa.esewatask.R

/**
 * Created by Rubin on 9/9/2022
 */
class NameListAdapter(val context: Context, var nameDataList: MutableList<NameData>): RecyclerView.Adapter<NameListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.names_item, parent, false)
        return NameListViewHolder(view)
    }

    override fun onBindViewHolder(holder: NameListViewHolder, position: Int) {
        var nameData = nameDataList[position]
        holder.txvNameOutput.text = nameData.name
        holder.txvAgeOutput.text = nameData.age.toString()
        holder.txvSexOutput.text = nameData.sex
    }

    override fun getItemCount(): Int {
        return nameDataList.size
    }

    // Add tthis funciton in adapter
    fun filterList(filteredUsersList: ArrayList<NameData>) {
        this.nameDataList = filteredUsersList // Change new List and Notify
        notifyDataSetChanged()
    }
}