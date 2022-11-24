package com.example.bikekollective

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.RatingBar.OnRatingBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.bikekollective.databinding.ActivityReturnBikeBinding
import com.example.bikekollective.databinding.FragmentHomeBinding

class ReturnBikeActivity : AppCompatActivity() {
    lateinit var ratingBar: RatingBar
    lateinit var button: Button
    lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_return_bike)
        ratingBar = findViewById(R.id.ratingBar)
        button = findViewById(R.id.button_rate)
        textView = findViewById<TextView>(R.id.rating)
        ratingBar.numStars = 5
        ratingBar.onRatingBarChangeListener =
            OnRatingBarChangeListener {
                    _, rating, _ ->
                textView.text = "" + rating.toInt()+ "/5"

            }
        button.setOnClickListener {
            Toast.makeText(
                this@ReturnBikeActivity, "Stars: " +
                        ratingBar.rating.toInt(), Toast.LENGTH_SHORT
            ).show()
        }

    }
}