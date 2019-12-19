package com.mikroskil.androiddasar.intent.model

import android.graphics.Bitmap
import android.net.Uri

object contactsData {
    var contactName : ArrayList<String> = arrayListOf()
    var contactPhone : ArrayList<String> = arrayListOf()
    var contactImage : ArrayList<String> = arrayListOf()
    var listData: ArrayList<contact>
        get() {
            val list = arrayListOf<contact>()
            for (i in contactName.indices) {
                val Contact = contact()
                Contact.contactName = contactName[i]
                Contact.contactPhoneNum = contactPhone[i]
                Contact.contactPhoto = Uri.parse(contactImage[i])
                list.add(Contact)
            }
            return list
        }
        set(data){
            for (i in data){
                contactName.add(i.contactName.toString())
                contactPhone.add(i.contactPhoneNum.toString())
                contactImage.add(i.contactPhoto.toString())
            }
    }

}