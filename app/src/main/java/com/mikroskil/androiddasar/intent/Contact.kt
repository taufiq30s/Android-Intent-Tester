package com.mikroskil.androiddasar.intent

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mikroskil.androiddasar.intent.adapter.contactAdapter
import com.mikroskil.androiddasar.intent.model.contact
import com.mikroskil.androiddasar.intent.model.contactsData


class Contact : AppCompatActivity() {

    var REQUEST_CODE = 0
    private lateinit var rvContact : RecyclerView
    private var contact_list : ArrayList<contact> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        rvContact = findViewById(R.id.rvContact)
        rvContact.setHasFixedSize(true)
        var decoration = DividerItemDecoration(applicationContext, VERTICAL)
        rvContact.addItemDecoration(decoration)

        contact_list.addAll(contact_list)
        showContact()
    }

    fun addContact(view: View) {
        val contactAdd = Intent(this@Contact, Contact_Add::class.java)
        startActivityForResult(contactAdd, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            lateinit var dataList : contact
            val item = data!!.getStringArrayListExtra("contactData")
            if(item.size > 0){
                dataList = contact(item[0],
                    item[1], Uri.parse(item[2]))
                val list : ArrayList<contact> = arrayListOf(dataList)
                contact_list.addAll(contact_list.size,list)
                contactsData.listData = contact_list
            }

        }
        showContact()
    }

    private fun showContact(){
        rvContact.layoutManager = LinearLayoutManager(this)
        val listAdapter = contactAdapter(contactsData.listData)
        rvContact.adapter = listAdapter
    }


}
