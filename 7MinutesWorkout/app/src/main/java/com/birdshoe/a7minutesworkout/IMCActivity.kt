package com.birdshoe.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import com.birdshoe.a7minutesworkout.databinding.ActivityImcBinding
import java.math.BigDecimal
import java.math.RoundingMode

class IMCActivity : AppCompatActivity() {

    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
        private const val US_UNITS_VIEW = "US_UNIT_VIEW"
    }

    private var binding: ActivityImcBinding? = null
    private var currentVisibleView: String =
        METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImcBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarImcActivity)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULA TU IMC"
        }

        binding?.toolbarImcActivity?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        makeVisibleMetricUnitsView()

        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId: Int ->
            if (checkedId == R.id.rbMetricUnits) {
                makeVisibleMetricUnitsView()
            } else {
                makeVisibleUsUnitsView()
            }
        }

        binding?.btnCalculateUnits?.setOnClickListener {
            calculateUnits()
        }
    }

    private fun makeVisibleMetricUnitsView() {
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.tilMetricsUnitHeight?.visibility = View.VISIBLE
        binding?.tilMetricsUnitWeight?.visibility = View.VISIBLE

        binding?.tilMetricsUsUnitWeight?.visibility = View.INVISIBLE
        binding?.tilMetricsUsUnitHeightFeet?.visibility = View.INVISIBLE
        binding?.tilMetricsUsUnitHeightInch?.visibility = View.INVISIBLE

        binding?.etMetricUnitHeight?.text!!.clear()
        binding?.etMetricUnitWeight?.text!!.clear()

        binding?.llDisplayIMCResult?.visibility = View.INVISIBLE

    }

    private fun makeVisibleUsUnitsView() {
        currentVisibleView = US_UNITS_VIEW
        binding?.tilMetricsUnitHeight?.visibility = View.INVISIBLE
        binding?.tilMetricsUnitWeight?.visibility = View.INVISIBLE

        binding?.tilMetricsUsUnitWeight?.visibility = View.VISIBLE
        binding?.tilMetricsUsUnitHeightFeet?.visibility = View.VISIBLE
        binding?.tilMetricsUsUnitHeightInch?.visibility = View.VISIBLE

        binding?.etUsMetricUnitWeight?.text!!.clear()
        binding?.etUsMetricUnitHeightFeet?.text!!.clear()
        binding?.etUsMetricUnitHeightInch?.text!!.clear()

        binding?.llDisplayIMCResult?.visibility = View.INVISIBLE

    }

    private fun displayIMCResults(imc: Float) {

        val imcLabel: String
        val imcDescription: String

        if (imc.compareTo(15f) <= 0) {
            imcLabel = "Very severely underweight"
            imcDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (imc.compareTo(15f) > 0 && imc.compareTo(16f) <= 0
        ) {
            imcLabel = "Severely underweight"
            imcDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (imc.compareTo(16f) > 0 && imc.compareTo(18.5f) <= 0
        ) {
            imcLabel = "Underweight"
            imcDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (imc.compareTo(18.5f) > 0 && imc.compareTo(25f) <= 0
        ) {
            imcLabel = "Normal"
            imcDescription = "Congratulations! You are in a good shape!"
        } else if (java.lang.Float.compare(imc, 25f) > 0 && java.lang.Float.compare(
                imc,
                30f
            ) <= 0
        ) {
            imcLabel = "Overweight"
            imcDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (imc.compareTo(30f) > 0 && imc.compareTo(35f) <= 0
        ) {
            imcLabel = "Obese Class | (Moderately obese)"
            imcDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (imc.compareTo(35f) > 0 && imc.compareTo(40f) <= 0
        ) {
            imcLabel = "Obese Class || (Severely obese)"
            imcDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            imcLabel = "Obese Class ||| (Very Severely obese)"
            imcDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        val imcValue = BigDecimal(imc.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        binding?.llDisplayIMCResult?.visibility = View.VISIBLE
        binding?.tvIMCValue?.text = imcValue
        binding?.tvIMCType?.text = imcLabel
        binding?.tvIMCDescription?.text = imcDescription
    }

    private fun validateMetricsUnits(): Boolean {
        var isValid = true

        if (binding?.etMetricUnitWeight?.text.toString().isEmpty()) {
            isValid = false
        } else if (binding?.etMetricUnitHeight?.text.toString().isEmpty()) {
            isValid = false
        }
        return isValid
    }

    private fun calculateUnits(){
        if(currentVisibleView == METRIC_UNITS_VIEW){
            if (validateMetricsUnits()) {
                val heightValue: Float =
                    binding?.etMetricUnitHeight?.text.toString().toFloat() / 100
                val weightValue: Float = binding?.etMetricUnitWeight?.text.toString().toFloat()

                val imc = weightValue / (heightValue * heightValue)

                displayIMCResults(imc)

            } else {
                Toast.makeText(
                    this@IMCActivity,
                    "Por favor, introduce valores válidos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }else{
            if(validateUsUnits()){
                val usUnitHeightValueFeet: String =
                    binding?.etUsMetricUnitHeightFeet?.text.toString()

                val usUnitHeightValueInch: String =
                    binding?.etUsMetricUnitHeightInch?.text.toString()

                val usUnitWeightValue: Float =
                    binding?.etUsMetricUnitWeight?.text.toString().toFloat()

                val heightValue =
                    usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12

                val imc = 703 * (usUnitWeightValue/(heightValue*heightValue))

                displayIMCResults(imc)
            }else {
                Toast.makeText(
                    this@IMCActivity,
                    "Por favor, introduce valores válidos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun validateUsUnits(): Boolean {
        var isValid = true

        when {
            binding?.etUsMetricUnitHeightFeet?.text.toString().isEmpty() -> {
                isValid = false
            }

            binding?.etUsMetricUnitHeightInch?.text.toString().isEmpty() -> {
                isValid = false
            }

            binding?.etUsMetricUnitWeight?.text.toString().isEmpty() -> {
                isValid = false
            }
        }

        return isValid
    }
}