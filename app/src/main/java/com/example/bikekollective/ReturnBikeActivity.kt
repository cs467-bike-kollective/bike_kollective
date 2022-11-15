package com.example.bikekollective

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

class ReturnBikeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_return_bike)

        val rBar = RatingBar(this)
        val layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        rBar.rating = 4.5F
        rBar.stepSize = 0.5.toFloat()
        rBar.numStars = 5
        rBar.layoutParams = layoutParams

        val button = findViewById<Button>(R.id.button_rate)
        ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)

        val linearLayout = findViewById<ConstraintLayout>(R.id.container)
        linearLayout?.addView(rBar)
        linearLayout?.addView(button)

        button?.setOnClickListener {
            val msg = rBar.rating.toString()
            Toast.makeText(this@ReturnBikeActivity, "Given Rating: $msg",
                Toast.LENGTH_SHORT).show()
        }
    }
}