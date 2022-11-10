package com.example.bikekollective
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bikekollective.databinding.ActivityEditBikeBinding
import android.graphics.Color
import android.content.Intent
import com.example.bikekollective.databinding.ChipBinding
import com.google.android.material.chip.Chip

class EditBikeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBikeBinding
    companion object {
        private const val TAG = "EditBikeActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditBikeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // hide action bar
        supportActionBar?.hide()
        if ((applicationContext as ApplicationContext).bikeTagList.isNullOrEmpty()){
            (applicationContext as ApplicationContext).queryUserBikes()
        }else{
            (applicationContext as ApplicationContext).bikeTagList?.forEach{ tag ->
                binding.editChipGroup.addView(createTagChip(baseContext, tag?.name.toString()))
            }

        }

        binding.editSubmitFormButton.setOnClickListener {
            //disable button to prevent multiple clicks
            binding.editSubmitFormButton.isEnabled = false

            val descriptionInput = binding.editBikeDescription.text.toString()
            val combinationInput = binding.editBikeLockCombination.text.toString()

            //check if there's a string in description
            if (descriptionInput.isEmpty()){
                binding.editBikeDescription.hint = "Add Description"
                binding.editBikeDescription.setHintTextColor(Color.RED)
            }

            // check if there's a string in combination
            if (combinationInput.isEmpty()){
                binding.editBikeLockCombination.hint = "Add Combination"
                binding.editBikeLockCombination.setHintTextColor(Color.RED)
            }

            Log.i(TAG, "$descriptionInput $combinationInput")
            binding.editSubmitFormButton.isEnabled = true
        }

        binding.editIvExitCamera.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun createTagChip(baseContext: Context?, tagString: String): Chip {
        val chip = ChipBinding.inflate(layoutInflater).root
        chip.text = tagString
        return chip

    }
}