package com.mikroskil.androiddasar.intent

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AkarinToast : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_akarin_toast)
    }

    fun startToaster(view: View) {
        val layoutInflater: LayoutInflater = layoutInflater // Initialize LayoutInflater
        /*  first parameter is the layout you made
            second parameter is the root view in that xml
        */
        val layout: View = layoutInflater.inflate(
            R.layout.akarin_toast,
            findViewById<ViewGroup>(R.id.akarinToaster)
        )
        layout.findViewById<TextView>(R.id.tvMessage).text =
            "Hello " + findViewById<EditText>(R.id.pesan).text // Set Message
        val toast = Toast(this) // Initialize Toast
        toast.duration = Toast.LENGTH_LONG // Set Duration
        toast.setGravity(Gravity.BOTTOM, 0, 200)
        toast.view = layout // Set Toast Layout
        toast.show() // Show Toast


    }
}
