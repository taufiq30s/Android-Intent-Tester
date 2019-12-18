package com.mikroskil.androiddasar.intent.model

import android.graphics.Bitmap

object contactsData {
    lateinit var contactName : ArrayList<String>
    lateinit var contactPhone : ArrayList<String>
    lateinit var contactImage : ArrayList<Int>
    var listData: ArrayList<contact>
        get() {
            val list = arrayListOf<contact>()
            for (i in contactName.indices) {
                val Contact = contact()
                Contact.contactName = contactName[i]
                Contact.contactPhoneNum = contactPhone[i]
                Contact.contactPhoto = contactImage[i]
                list.add(Contact)
            }
            return list
        }
        set(data){
            contactName.add(data[0].toString())
            contactPhone.add(data[1].toString())
            contactImage.add(data[2])
    }

}