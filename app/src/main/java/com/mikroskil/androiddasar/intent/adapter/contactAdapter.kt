package com.mikroskil.androiddasar.intent.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mikroskil.androiddasar.intent.R
import com.mikroskil.androiddasar.intent.model.contact
import org.w3c.dom.Text

class contactAdapter(private val listContact: ArrayList<contact>): RecyclerView.Adapter<contactAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvContactName : TextView = itemView.findViewById(R.id.tv_contact_name)
        var tvContactPhone : TextView = itemView.findViewById(R.id.tv_contact_phone)
        var imgContactPhoto : ImageView = itemView.findViewById(R.id.img_contact_photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.contact_list, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}