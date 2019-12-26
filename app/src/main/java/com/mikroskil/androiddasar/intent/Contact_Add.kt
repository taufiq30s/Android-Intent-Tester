package com.mikroskil.androiddasar.intent

import android.Manifest
import android.app.ActionBar
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import com.bumptech.glide.util.Util
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mikroskil.androiddasar.intent.model.contactsData
import kotlinx.android.synthetic.main.activity_contact_add.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.net.URI

class Contact_Add : AppCompatActivity() {

    // Declare Data
    var image2 : Uri? = null
    var contactData = arrayListOf<String>()
    lateinit var confirmBuilder : MaterialAlertDialogBuilder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_add)

        // Custom Toolbar
        var toolbar : androidx.appcompat.widget.Toolbar? = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        var toolbarText : TextView = findViewById(R.id.toolbar_text)
        if(toolbar!=null) {
            toolbarText.text = title
            setSupportActionBar(toolbar);
        }

        // Alert
        confirmBuilder = MaterialAlertDialogBuilder(this, R.style.Theme_MaterialComponents_Dialog_Alert)
        confirmBuilder.setMessage("Discard your changes?")
            .setTitle("Cancel")
            .setPositiveButton("OK",DialogInterface.OnClickListener {
                    DialogInterface, i -> finish()
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener{
                    DialogInterface, i -> DialogInterface.dismiss()
            })


        //Untuk Ganti Profil ( Ambil dari Galeri )
        profil.setOnClickListener{
            // Untuk memeriksa Runtime Permission
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                    // Permission Denied
                    val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //Munculkan popup untuk merequest Runtime Permission
                    requestPermissions(permission,PERMISSION_CODE)
                }
                else {
                    AksesGaleri()
                }
            }
            else{
                // Jika system OS lebih besar dari Lolipop
                AksesGaleri()
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_contact, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.saveContact) saveContact()
        else if(item.itemId == android.R.id.home){
            if(findViewById<EditText>(R.id.contactName).text.toString() != "" ||
                findViewById<EditText>(R.id.contactPhone).text.toString() != "" || image2 != null){
                val confirm = confirmBuilder.create()
                confirm.show()
                return true
            }
            else finish()

        }
        return super.onOptionsItemSelected(item)
    }

    //Akses Galeri ( Start )
    private fun AksesGaleri(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        if(intent.resolveActivity(packageManager)!=null) {
            startActivityForResult(intent, 1)
        }
    }
    companion object {
        private val PERMISSION_CODE = 110
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    AksesGaleri()
                }
                else{
                    Toast.makeText(this,"Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == 1){
            profil.setImageURI(data?.data)
            image2 = data?.data
//            val imageUri : Uri? = data!!.data
//            image = getPath(this.applicationContext, imageUri ?: Uri.parse("")) ?: "Null"
        }
    }

//     Get Path from Intent Gallery URI
    private fun getPath(context : Context, uri: Uri): String?{
        var result = ""
        var cursor : Cursor? = contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        var doc = cursor?.getString(0)
        doc = doc?.substring(doc?.lastIndexOf(":")+1)
        cursor?.close()

        cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
            MediaStore.Images.Media._ID + " = ? ", arrayOf(doc), null)

        cursor?.moveToFirst()
        val path = cursor?.getString(cursor?.getColumnIndex(MediaStore.Images.Media.DATA))
        cursor?.close()

        return path
    }

    //Akses Galeri ( End )

    fun saveContact(){
        if(findViewById<EditText>(R.id.contactName).text.toString() != "" &&
            findViewById<EditText>(R.id.contactPhone).text.toString() != "") {
            contactData.add(findViewById<EditText>(R.id.contactName).text.toString())
            contactData.add(findViewById<EditText>(R.id.contactPhone).text.toString())
//        val nameFile = contactData[0]+"_"+contactData[1]
            // Save image to app data
            contactData.add(image2.toString())
        }
//        saveImage(this.applicationContext,convertToBitmap(this.applicationContext,
//            image2?: Uri.parse("")),nameFile,"png")
//        contactData.add(nameFile)
        finish()
    }

//    private fun convertToBitmap(context: Context, uri: Uri): Bitmap?{
//        try{
//            var input = context.contentResolver.openInputStream(uri)
//            if(input == null) return null
//            return BitmapFactory.decodeStream(input)
//        }
//        catch (e : FileNotFoundException){
//            Log.e("Error Bitmap Convert", "File Not Found.\n"+e)
//        }
//        return null
//    }
//    private fun saveImage(context: Context, image: Bitmap?, name: String, extension: String){
//        val name = name + "." + extension
//        lateinit var out : FileOutputStream
//        try{
//            out = context.openFileOutput(name, Context.MODE_PRIVATE)
//            image?.compress(Bitmap.CompressFormat.PNG, 60, out)
//            out.close()
//        } catch (e : Exception){
//            e.printStackTrace()
//        }
//    }

    override fun finish() {
        var returnIntent = Intent()
        if(contactData.size == 3)
            returnIntent.putExtra("contactData", contactData)
        setResult(Activity.RESULT_OK, returnIntent)
        super.finish()
    }
}
