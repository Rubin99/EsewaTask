package com.esewa.esewatask.adapter

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.esewa.esewatask.R

class NameListViewHolder (private val itemView: View) : RecyclerView.ViewHolder(itemView){

    var imvPrompt: ImageView? = itemView.findViewById(R.id.imvProfile)
    var txvNameOutput: TextView = itemView.findViewById(R.id.txvNameOutput)
    val txvAgeOutput: TextView = itemView.findViewById(R.id.txvAgeOutput)
    val txvSexOutput: TextView = itemView.findViewById(R.id.txvSexOutput)

}
