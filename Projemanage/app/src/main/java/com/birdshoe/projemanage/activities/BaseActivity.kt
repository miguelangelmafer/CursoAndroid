package com.birdshoe.projemanage.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.birdshoe.projemanage.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

open class BaseActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false

    private lateinit var mProgressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(this)

        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.findViewById<TextView>(R.id.tv_progress_text).text = text

        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    fun doubleBackToExit(){
        if(doubleBackToExitPressedOnce){
            //TODO posible revisión y busqueda de alternativa
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(
            this,
            resources.getString(R.string.please_click_back_again_to_exit),
            Toast.LENGTH_LONG
        ).show()

        Handler(Looper.getMainLooper()).postDelayed(
            {
                doubleBackToExitPressedOnce = false
            }, 2500
        )
    }

    fun showErrorSnackBar(message:String){
        val snackbar = Snackbar.make(findViewById(android.R.id.content),
            message,Snackbar.LENGTH_LONG)

        val snackBarView = snackbar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(this,R.color.snackbar_error_color))

        snackbar.show()
    }

    fun getCurrentUserID(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }
}