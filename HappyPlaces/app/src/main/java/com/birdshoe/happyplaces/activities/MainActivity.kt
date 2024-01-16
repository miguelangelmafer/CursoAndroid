package com.birdshoe.happyplaces.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.birdshoe.happyplaces.R
import com.birdshoe.happyplaces.adapters.HappyPlacesAdapter
import com.birdshoe.happyplaces.database.DatabaseHandler
import com.birdshoe.happyplaces.models.HappyPlaceModel
import com.birdshoe.happyplaces.utils.SwipeToDeleteCallback
import com.birdshoe.happyplaces.utils.SwipeToEditCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<FloatingActionButton>(R.id.fabAddHappyPlace).setOnClickListener {
            val intent = Intent(this, AddHappyPlaceActivity::class.java)
            startActivity(intent)
        }
        getHappyPlacesListFromLocalDB()
    }

    override fun onResume() {
        super.onResume()
        getHappyPlacesListFromLocalDB()
    }

    private fun setupHappyPlacesRecyclerView(happyPlacesList: ArrayList<HappyPlaceModel>) {
        findViewById<RecyclerView>(R.id.rv_happy_places_list).layoutManager =
            LinearLayoutManager(this)

        findViewById<RecyclerView>(R.id.rv_happy_places_list).setHasFixedSize(true)

        val placesAdapter = HappyPlacesAdapter(this, happyPlacesList)

        findViewById<RecyclerView>(R.id.rv_happy_places_list).adapter = placesAdapter

        placesAdapter.setOnClickListener(object : HappyPlacesAdapter.OnClickListener {
            override fun onClick(position: Int, model: HappyPlaceModel) {
                val intent = Intent(this@MainActivity, HappyPlaceDetailActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS,model)
                startActivity(intent)
            }
        })

        val editSwipeHandler = object  :SwipeToEditCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = findViewById<RecyclerView>(R.id.rv_happy_places_list).adapter as HappyPlacesAdapter
                adapter.notifyEditItem(this@MainActivity,viewHolder.adapterPosition,ADD_PLACE_ACTIVITY_REQUEST_CODE)
            }
        }

        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(findViewById(R.id.rv_happy_places_list))

        val deleteSwipeHandler = object  :SwipeToDeleteCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = findViewById<RecyclerView>(R.id.rv_happy_places_list).adapter as HappyPlacesAdapter
                adapter.removeAt(viewHolder.adapterPosition)

                getHappyPlacesListFromLocalDB()
            }
        }

        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(findViewById(R.id.rv_happy_places_list))
    }



    private fun getHappyPlacesListFromLocalDB() {
        val dbHandler = DatabaseHandler(this)
        val getHappyPlaceList: ArrayList<HappyPlaceModel> = dbHandler.getHappyPlacesList()

        if (getHappyPlaceList.size > 0) {
            for (i in getHappyPlaceList) {
                findViewById<RecyclerView>(R.id.rv_happy_places_list).visibility = View.VISIBLE
                findViewById<TextView>(R.id.tv_no_records_available).visibility = View.GONE
                setupHappyPlacesRecyclerView(getHappyPlaceList)
            }
        } else {
            findViewById<RecyclerView>(R.id.rv_happy_places_list).visibility = View.GONE
            findViewById<TextView>(R.id.tv_no_records_available).visibility = View.VISIBLE
        }

    }

    companion object{
        var ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
        var EXTRA_PLACE_DETAILS = "extra_place_details"
    }
}