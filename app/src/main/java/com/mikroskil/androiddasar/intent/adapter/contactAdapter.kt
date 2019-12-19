package com.mikroskil.androiddasar.intent.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mikroskil.androiddasar.intent.R
import com.mikroskil.androiddasar.intent.model.contact
import org.w3c.dom.Text
import java.io.FileInputStream

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
        return listContact.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val Contact = listContact[position]
        Log.d("Check Uri", Contact.contactPhoto.toString())
        if(Contact.contactPhoto.toString() != "null") {
            Log.d("Glide", "Masuk")
            Glide.with(holder.itemView.context)
                .load(Contact.contactPhoto)
                .apply(RequestOptions())
                .into(holder.imgContactPhoto)
        }
        else
            holder.imgContactPhoto.setImageResource(R.drawable.ic_action_user)
        holder.tvContactName.text = Contact.contactName
        holder.tvContactPhone.text = Contact.contactPhoneNum

    }



}