package com.mikroskil.androiddasar.intent

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.GestureDetectorCompat
import kotlinx.android.synthetic.main.activity_akarin_gesture_detecion.*
import org.w3c.dom.Text

class AkarinGestureDetecion : AppCompatActivity(), GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    // Declaration & Initialized
    var mDetector: GestureDetectorCompat? = null
    var triggerCond = "showPress"

    lateinit var triggerStatus : TextView
    lateinit var triggerXCoor : TextView
    lateinit var triggerYCoor : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_akarin_gesture_detecion)

        triggerStatus = findViewById<TextView>(R.id.triggerStatus)
        triggerXCoor = findViewById<TextView>(R.id.xPosition)
        triggerYCoor = findViewById<TextView>(R.id.yPosition)

        mDetector = GestureDetectorCompat(this, this)
        mDetector?.setOnDoubleTapListener(this)
        findViewById<TextView>(R.id.trigger).text = "Show Press Trigger"

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.showPress -> triggerOption("showPress")
            R.id.singleTapUp -> triggerOption("singleTapUp")
            R.id.down -> triggerOption("down")
            R.id.fling -> triggerOption("fling")
            R.id.scroll -> triggerOption("scroll")
            R.id.longPress -> triggerOption("longPress")
            R.id.doubleTap -> triggerOption("doubleTap")
            R.id.doubleTapEvent -> triggerOption("doubleTapEvent")
            R.id.singleTapConfirmed -> triggerOption("singleTapConfirmed")
        }
        return super.onOptionsItemSelected(item)
    }

    fun triggerOption(option: String){
        triggerCond = option
        if(option == "fling" || option == "scroll"){
            findViewById<TextView>(R.id.xPositionCaption).visibility = View.VISIBLE
            findViewById<TextView>(R.id.xPosition).visibility = View.VISIBLE
            findViewById<TextView>(R.id.yPositionCaption).visibility = View.VISIBLE
            findViewById<TextView>(R.id.yPosition).visibility = View.VISIBLE
        }
        else {
            findViewById<TextView>(R.id.xPositionCaption).visibility = View.GONE
            findViewById<TextView>(R.id.xPosition).visibility = View.GONE
            findViewById<TextView>(R.id.yPositionCaption).visibility = View.GONE
            findViewById<TextView>(R.id.yPosition).visibility = View.GONE
        }
        if(option == "doubleTapEvent" || option == "singleTapConfirmed"){
            findViewById<ImageView>(R.id.akariGesture).visibility = View.VISIBLE
        }
        else
            findViewById<ImageView>(R.id.akariGesture).visibility = View.GONE

        triggerStatus.text = getString(R.string.falseCondition)
        triggerStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light))
        xPosition.text = "0"
        yPosition.text = "0"
        findViewById<ImageView>(R.id.akariGesture).setImageResource(R.drawable.hide_akarin_special)
        findViewById<RelativeLayout>(R.id.relativeLayout).setBackgroundColor(ContextCompat
            .getColor(this, android.R.color.white))
        changeTitle(option)
    }

    fun changeTitle(option : String) {
        when(option){
            "showPress" ->
                findViewById<TextView>(R.id.trigger).text = "Show Press Trigger"
            "singleTapUp" ->
                findViewById<TextView>(R.id.trigger).text = "Single Tapup Trigger"
            "down" ->
                findViewById<TextView>(R.id.trigger).text = "Down Trigger"
            "fling" ->
                findViewById<TextView>(R.id.trigger).text = "Fling Trigger"
            "scroll" ->
                findViewById<TextView>(R.id.trigger).text = "Scroll Trigger"
            "longPress" ->
                findViewById<TextView>(R.id.trigger).text = "Long Press Trigger"
            "doubleTap" ->
                findViewById<TextView>(R.id.trigger).text = "Double Tap Trigger"
            "doubleTapEvent" ->
                findViewById<TextView>(R.id.trigger).text = "Double Tap Event Trigger"
            "singleTapConfirmed" ->
                findViewById<TextView>(R.id.trigger).text = "Single Tap Confirmed Trigger"
        }
    }

    // Run Event
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        this.mDetector?.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    // Trigger Action
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_gesture, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onShowPress(p0: MotionEvent?) {
        if(triggerCond == "showPress"){
            changeTitle(triggerCond)
            // Set Trigger Status Bar
            triggerStatus.text = getString(R.string.trueCondition)
            triggerStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
            
        }
        else if(triggerCond == "singleTapUp"){
            triggerStatus.text = getString(R.string.falseCondition)
            triggerStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light))
        }
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        Log.v("Response 3", "Tap Up")
        if(triggerCond == "singleTapUp"){
            changeTitle(triggerCond)
            triggerStatus.text = getString(R.string.trueCondition)
            triggerStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_light))

        }
        else{
            triggerStatus.text = getString(R.string.falseCondition)
            triggerStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light))
            if(triggerCond == "doubleTapEvent" || triggerCond == "singleTapConfirmed"){
                findViewById<RelativeLayout>(R.id.relativeLayout).setBackgroundColor(ContextCompat
                    .getColor(this, android.R.color.white))
                findViewById<ImageView>(R.id.akariGesture).setImageResource(R.drawable.hide_akarin_special)
            }
        }
        return true
    }

    override fun onDown(p0: MotionEvent?): Boolean {
        if(triggerCond == "down"){
            changeTitle(triggerCond)

            // Set Trigger Status Bar
            triggerStatus.text = getString(R.string.trueCondition)
            triggerStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
            
        }
        else if(triggerCond == "singleTapUp"){
            triggerStatus.text = getString(R.string.falseCondition)
            triggerStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light))
        }
        return true
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        if(triggerCond == "fling"){
            changeTitle(triggerCond)

            // Set Trigger Status Bar
            triggerStatus.text = getString(R.string.trueCondition)
            triggerStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
            xPosition.text = String.format("%.2f", p2)
            yPosition.text = String.format("%.2f", p3)
            
        }
        else if(triggerCond == "singleTapUp"){
            triggerStatus.text = getString(R.string.falseCondition)
            triggerStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light))
        }
        return false
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        if(triggerCond == "scroll"){
            changeTitle(triggerCond)

            // Set Trigger Status Bar
            triggerStatus.text = getString(R.string.trueCondition)
            triggerStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
            xPosition.text = String.format("%.2f", p2)
            yPosition.text = String.format("%.2f", p3)
            
        }
        else if(triggerCond == "singleTapUp"){
            triggerStatus.text = getString(R.string.falseCondition)
            triggerStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light))
        }
        return false
    }

    override fun onLongPress(p0: MotionEvent?) {
        if(triggerCond == "longPress"){
            changeTitle(triggerCond)

            // Set Trigger Status Bar
            triggerStatus.text = getString(R.string.trueCondition)
            triggerStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
            
        }
        else{
            findViewById<TextView>(R.id.trigger).text = "Long Press Trigger (Tap again for release from Long Press Trigger)"
            triggerStatus.text = getString(R.string.falseCondition)
            triggerStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light))
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onDoubleTap(p0: MotionEvent?): Boolean {
        if(triggerCond == "doubleTap"){
            changeTitle(triggerCond)

            // Set Trigger Status Bar
            triggerStatus.text = getString(R.string.trueCondition)
            triggerStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
            
        }
        else if(triggerCond == "singleTapUp"){
            triggerStatus.text = getString(R.string.falseCondition)
            triggerStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light))
        }
        return false
    }

    override fun onDoubleTapEvent(p0: MotionEvent?): Boolean {
        if(triggerCond == "doubleTapEvent"){
            findViewById<ImageView>(R.id.akariGesture).setImageResource(R.drawable.peace_akarin)
            findViewById<RelativeLayout>(R.id.relativeLayout).setBackgroundColor(ContextCompat
                .getColor(this, android.R.color.black))

            triggerStatus.text = getString(R.string.trueCondition)
            triggerStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
        }
        return false
    }

    override fun onSingleTapConfirmed(p0: MotionEvent?): Boolean {
        if(triggerCond == "singleTapConfirmed") {
            findViewById<ImageView>(R.id.akariGesture).setImageResource(R.drawable.peace_akarin)
            findViewById<RelativeLayout>(R.id.relativeLayout).setBackgroundColor(ContextCompat
                .getColor(this, android.R.color.black))

            triggerStatus.text = getString(R.string.trueCondition)
            triggerStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
        }
        return false
    }
}
