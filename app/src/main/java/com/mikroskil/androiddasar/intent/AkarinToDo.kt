package com.mikroskil.androiddasar.intent

import android.annotation.TargetApi
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.custom_dialog_reminder.*
import java.text.DateFormatSymbols
import java.util.*
import java.util.zip.Inflater
import javax.xml.datatype.DatatypeConstants.MONTHS

class AkarinToDo : AppCompatActivity() {

    lateinit var todoAlert : AlertDialog
    lateinit var custLayoutInflater: LayoutInflater
    lateinit var addToDoLayout : View
    lateinit var builder: AlertDialog.Builder
    lateinit var tvDate : TextView
    lateinit var tvTime : TextView


    val now = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_akarin_to_do)

        val lvTodo = findViewById<ListView>(R.id.lvToDo)
        lvTodo.visibility = View.GONE

        // Initialize Custom Dialog and its element
        custLayoutInflater = layoutInflater // Initialize LayoutInflater
        addToDoLayout = custLayoutInflater.inflate(R.layout.custom_dialog_reminder,null)
        tvDate = addToDoLayout.findViewById<TextView>(R.id.tvDate)
        tvTime = addToDoLayout.findViewById<TextView>(R.id.tvTime)

        // Repeat Spinner Adapter
        val repeatAdapter = ArrayAdapter.createFromResource(this, R.array.repeat_condition,
            android.R.layout.simple_spinner_dropdown_item)
        addToDoLayout.findViewById<Spinner>(R.id.spRepeat).adapter = repeatAdapter
    }

    fun AddRiminder(view: View) {
        val ic_remind = addToDoLayout.findViewById<ImageView>(R.id.remindMic)
        val remind = addToDoLayout.findViewById<EditText>(R.id.remind)
        builder = AlertDialog.Builder(this)
        todoAlert = builder.create()
        if(addToDoLayout.parent != null)
            (addToDoLayout.parent as ViewGroup).removeView(addToDoLayout) // Kalau udah pernah buka dialog, reset cust. layout
        todoAlert.setView(addToDoLayout)
        todoAlert.setTitle("Add Reminder")
        todoAlert.show()
    }

    fun setDate(view: View) {
        val tahun = now.get(Calendar.YEAR)
        val bulan = now.get(Calendar.MONTH)
        val hari = now.get(Calendar.DAY_OF_MONTH)
        val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener {
                datePicker, year, monthOfYear, dayOfMonth ->
            tvDate.setText("$dayOfMonth - " + DateFormatSymbols.getInstance().months[monthOfYear] +  " - $year")},
            tahun, bulan, hari ).show()
    }
    fun setTime(view: View) {
        val jam = now.get(Calendar.HOUR)
        val menit = now.get(Calendar.MINUTE)
        val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener {
                TimePicker, hour, minutes ->
            tvTime.setText("$hour:$minutes")
        }, jam, menit, true).show()
    }
    fun cancelTodo(view: View){
        todoAlert.dismiss()
    }
    fun saveTodo(view: View){
        // Menampilkan Alert Dialog
        var builder = AlertDialog.Builder(this, R.style.Theme_MaterialComponents_Dialog_Alert).setTitle("To-Do Remember")
        builder.setMessage("Fitur ini saat ini masih belum tersedia.\nSilahkan coba di lain kesempatan.")

        builder.setPositiveButton("OK", DialogInterface.OnClickListener {
                dialogInterface, i -> todoAlert.dismiss()
        })

        var dialog = builder.create() // Membuat builder

        dialog.show() // Menampilkan Alert Dialog

    }
}
