package com.mikroskil.androiddasar.intent

import android.Manifest
import android.Manifest.permission.CAMERA
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun implicit(view: View) {
        // Camera Permission
        if(ActivityCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED){
            val permission  = arrayOf(CAMERA)
            ActivityCompat.requestPermissions(
                this,
                permission,
                200
            )
            return
        }
        val cameraIntent : Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivity(cameraIntent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 200){
            if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                Toast.makeText(this@MainActivity, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
            }
            else{
                var cameraIntent : Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivity(cameraIntent)
            }
        }
    }

    fun explicit(view: View) {
        val cameraExplicit : Intent = Intent(this@MainActivity, CaptureAkarin::class.java)
        startActivity(cameraExplicit)
    }

    fun sendMail(view: View) {
        val mail = Intent(this@MainActivity, AkarinMail::class.java)
        startActivity(mail)
    }
}
