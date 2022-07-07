package com.karimi.googlemap.activity.map

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.karimi.googlemap.R
import com.karimi.googlemap.activity.customerDetail.CustomerDetailActivity
import com.karimi.googlemap.database.DatabaseHelper
import com.karimi.googlemap.database.dao.CustomerDao
import com.karimi.googlemap.model.Customer
import com.karimi.googlemap.sqlite.DBHelperJavaSimin
import kotlinx.android.synthetic.main.activity_maps.*
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker


class MapsActivity : AppCompatActivity() {
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private lateinit var m : CustomerDetailActivity

    private lateinit var marker : Marker

    private lateinit var db: DatabaseHelper
    private lateinit var dao: CustomerDao

    private var et : String? = null
    private var userInfo : ArrayList<String> = ArrayList()

    private var geoPoint : GeoPoint = GeoPoint(0.0,0.0)
    private var id : Int? = null

    private var longitude : Double = 0.0
    private var latitude : Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkConnection()
        setContentView(R.layout.activity_maps)

        initMap()
        initDataBase()
        checkIntentTEST()
            marker = Marker(map)

        if (id != null){
            Log.e("111", " show lot lang : "  + " id is not null")
            Log.e("111", " show lot lang : "  + " id is not null  $id")
            var customer : Customer = dao.getOneCustomer(id!!)
            Log.e("111", " show lot lang : $latitude    $longitude")
            marker.position = GeoPoint(customer.latitude!!, customer.longitude!!)
            marker.icon  = getDrawable(R.drawable.locationblue)
            map.overlays.add(marker)
            map.invalidate()
        }

//        addLastLocationMarker()
        addMarker()
        initArrayUser()
        initSaveLocationBtn()

    }



    private fun initMap(){
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.isTilesScaledToDpi = true
        map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        map.setBuiltInZoomControls(true)
        map.setMultiTouchControls(true)
        val mapController = map.controller
        mapController.setZoom(9.0)
        val startPoint = GeoPoint(34.640557,50.881634) //55.751442, 37.615569
        mapController.setCenter(startPoint) }


    private fun initDataBase() {
        db = DatabaseHelper.getInstance(this)
        dao = db.customerDao() }


    private fun checkIntentTEST(){
        if (intent.extras != null){
            if (intent.getStringExtra("edittext_value") != null){
                et = intent.getStringExtra("edittext_value").toString()
                Log.e("111", "onCreate get et from new user: $et")

            }else if (intent.getIntExtra("id_customer_detail" ,0) != null){
                longitude = intent.getDoubleExtra("latitude",0.0)
                latitude = intent.getDoubleExtra("longitude",0.0)
                id = intent.getIntExtra("id_customer_detail" , 0)
                Log.e("111", "onCreate  get from edit: $id")
            }
        }
    }


    private fun addLastLocationMarker(){
        if (id != null){
            var markers = Marker(map)
            var customer : Customer = dao.getOneCustomer(id!!)
            markers.position = GeoPoint(latitude!!,longitude!!)
//            marker.position = GeoPoint(customer.latitude!!, customer.longitude!!)
            Log.e("111", " show lot lang : $latitude")
            markers.icon  = getDrawable(R.drawable.locationblue)
            map.overlays.add(markers)
            map.invalidate()
        }
    }


    private fun addMarker(){
//        marker = Marker(map)
        val mReceive: MapEventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                Log.e("111", " add marker : "  + " id is null and add new marker")
                marker.position = p
//                marker.title  = "company"
//                marker.subDescription = "simin company"
                marker.icon  = getDrawable(R.drawable.locationblue)
                geoPoint = p
                return false
            }
            override fun longPressHelper(p: GeoPoint): Boolean {
                return false
            }
        }
        val overlayEvents = MapEventsOverlay(baseContext, mReceive)
        map.overlays.add(overlayEvents)
        map.overlays.add(marker)
        map.invalidate()
    }


    private fun initArrayUser(){
        var dbFromAssets = DBHelperJavaSimin(this)
        userInfo = dbFromAssets.getTerminal_number(et)
//        userInfo = dbFromAssets.getTerminal_number(et)
    }


    private fun initSaveLocationBtn(){
        save_map.setOnClickListener {
            if (latitude != 0.0) {
                if (geoPoint.longitude == 0.0){
                    toast("موقعیت مورد نظر را انتخاب کنید")
                }else{
                    dao.updateLatLong(geoPoint.latitude,geoPoint.longitude, id!!)
                    finish()
                }
            }else {
                Log.e("111", "initSaveLocationBtn:  new user with "  )
                if (geoPoint.longitude == 0.0) {
                    toast("موقعیت مورد نظر را انتخاب کنید")
                }else{
                    Log.e("111", "initSaveLocationBtn:  new user with "  +  geoPoint.longitude)

                    dao.insert(Customer(userInfo[0],userInfo[1],userInfo[2],userInfo[3],userInfo[4],userInfo[9],userInfo[5],
                        userInfo[6],userInfo[7],userInfo[8],geoPoint.longitude , geoPoint.latitude))
                    finish()
                }
            }
        }
    }


    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val permissionsToRequest = ArrayList<String>()
        var i = 0
        while (i < grantResults.size) {
            permissionsToRequest.add(permissions[i]);
            i++
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE)
        }
    }


    private fun toast(text : String){
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause();
        map.onPause()
    }


    private fun checkConnection() {
        val connectivityManager =
            this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        if (wifi!!.isConnected) {
            Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        } else if (mobile!!.isConnected) {
            Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        } else {
         toast("اینترنت خود را بررسی کیند")
        }
    }



//    private fun checkIntent(){
//        if (intent.extras != null){
//            if (intent.getStringExtra("edittext_value") != null){
//                et = intent.getStringExtra("edittext_value").toString()
//                Log.e("111", "onCreate get et from new user: $et")
//
//            }else if (intent.getIntExtra("id_customer_detail" ,0) != null){
//                id = intent.getIntExtra("id_customer_detail" , 0)
//                Log.e("111", "onCreate  get from edit: $id")
//            }
//        }
//    }








//    private fun initSaveLocationBtn(){
//        save_map.setOnClickListener {
//            if (id != null){
//                if (geoPoint.longitude == 0.0){
//                    toast("موقعیت مورد نظر را انتخاب کنید")
//                }else{
//                    dao.updateLatLong(geoPoint.latitude,geoPoint.longitude, id!!)
//                    finish()
//                }
//
//            }else{
//                if (geoPoint.longitude == 0.0){
//                    toast("موقعیت مورد نظر را انتخاب کنید")
//                }else if (dao.checkTerminal(userInfo[1]) != null){
//                    toast(" کاربر مورد نظر با شماره پایانه " + " ' " +userInfo[1] +" ' "+ "  قبلا ثبت شده است")
//                }else{
//                    dao.insert(Customer(userInfo[0],userInfo[1],userInfo[2],userInfo[3],userInfo[4],userInfo[5],
//                        userInfo[6],userInfo[7],userInfo[8],geoPoint.longitude , geoPoint.latitude))
//                    finish()
////                m.rl_information.visibility = View.GONE
//                }
//            }
//        }
//    }
}