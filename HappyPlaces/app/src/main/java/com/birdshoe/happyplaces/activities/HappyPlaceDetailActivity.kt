package com.birdshoe.happyplaces.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import com.birdshoe.happyplaces.R
import com.birdshoe.happyplaces.models.HappyPlaceModel

class HappyPlaceDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_happy_place_detail)

        var happyPlaceDetailModel : HappyPlaceModel? = null

        if(intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)){
            happyPlaceDetailModel = intent.getParcelableExtra(MainActivity.EXTRA_PLACE_DETAILS) as HappyPlaceModel?
        }

        if(happyPlaceDetailModel != null){
            setSupportActionBar(findViewById(R.id.toolbar_happy_place_detail))
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = happyPlaceDetailModel.title

            findViewById<Toolbar>(R.id.toolbar_happy_place_detail).setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            findViewById<AppCompatImageView>(R.id.iv_place_image).setImageURI(Uri.parse(happyPlaceDetailModel.image))
            findViewById<TextView>(R.id.tv_description).text = happyPlaceDetailModel.description
            findViewById<TextView>(R.id.tv_location).text =happyPlaceDetailModel.location

            findViewById<Button>(R.id.btn_view_on_map).setOnClickListener {
                val intent = Intent(this,MapActivity::class.java )
                intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, happyPlaceDetailModel)
                startActivity(intent)
            }
        }
    }
}