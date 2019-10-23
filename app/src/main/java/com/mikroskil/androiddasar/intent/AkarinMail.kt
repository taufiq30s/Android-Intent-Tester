package com.mikroskil.androiddasar.intent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_akarin_mail.*

class AkarinMail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_akarin_mail)
    }

    override fun onCreateOptionsMenu(menu : Menu): Boolean{
        menuInflater.inflate(R.menu.menu_mail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item : MenuItem) : Boolean{
        if(item.itemId == R.id.send){
            val to = addressDest.text.toString()
            val Subject = subject.text.toString()
            val message = compose.text

            val email = Intent(Intent.ACTION_SEND)
            val address : Array<String> = arrayOf(to) // Email
            email.putExtra(Intent.EXTRA_EMAIL, address)
            email.putExtra(Intent.EXTRA_SUBJECT, Subject)
            email.putExtra(Intent.EXTRA_TEXT, message)

            // email client
            email.setType("message/rfc822")
            startActivity(Intent.createChooser(email, "Choose an Email client"))
        }
        return super.onOptionsItemSelected(item)
    }
}
