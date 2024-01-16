package com.birdshoe.projemanage.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import com.birdshoe.projemanage.databinding.ActivitySplashBinding
import com.birdshoe.projemanage.firebase.FirestoreClass

class SplashActivity : AppCompatActivity() {

    private var binding: ActivitySplashBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())

        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
            )
        }
        val typeFace: Typeface = Typeface.createFromAsset(assets, "carbon bl.otf")
        binding?.tvAppName?.typeface = typeFace

        Handler(Looper.getMainLooper()).postDelayed(
            {
                var currentUserId = FirestoreClass().getCurrentUserId()

                if(currentUserId.isNotEmpty()){
                    startActivity(Intent(this, MainActivity::class.java))
                }else{
                startActivity(Intent(this, IntroActivity::class.java))
                }
                finish()
            }, 2500
        )
    }
}