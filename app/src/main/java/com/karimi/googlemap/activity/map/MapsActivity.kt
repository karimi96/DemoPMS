package com.karimi.googlemap.activity

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.karimi.googlemap.R
import com.karimi.googlemap.database.DatabaseHelper
import com.karimi.googlemap.database.dao.CustomerDao
import com.karimi.googlemap.model.Customer
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import kotlinx.android.synthetic.main.activity_maps2.*
import kotlinx.android.synthetic.main.dialog_get_user.*


class MapsActivity : AppCompatActivity() {


//    map
    private val MAPKIT_API_KEY: String? = "your_api_key"
    private val TARGET_LOCATION: Point = Point(59.945933, 30.320045)

    private var mapView: MapView? = null
//    map

    //     lateinit var listener : Listener
    private lateinit var db: DatabaseHelper
    private lateinit var dao: CustomerDao

    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * Set the api key before calling initialize on MapKitFactory.
         * It is recommended to set api key in the Application.onCreate method,
         * but here we do it in each activity to make examples isolated.
         */
        /**
         * Set the api key before calling initialize on MapKitFactory.
         * It is recommended to set api key in the Application.onCreate method,
         * but here we do it in each activity to make examples isolated.
         */
        MapKitFactory.setApiKey(MAPKIT_API_KEY!!)
        /**
         * Initialize the library to load required native libraries.
         * It is recommended to initialize the MapKit library in the Activity.onCreate method
         * Initializing in the Application.onCreate method may lead to extra calls and increased battery use.
         */
        /**
         * Initialize the library to load required native libraries.
         * It is recommended to initialize the MapKit library in the Activity.onCreate method
         * Initializing in the Application.onCreate method may lead to extra calls and increased battery use.
         */
        MapKitFactory.initialize(this)


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        initDataBase()
        initSaveBtn()



//        map

        // Now MapView can be created.
        // Now MapView can be created.
        mapView = findViewById<View>(R.id.mapview) as MapView

        // And to show what can be done with it, we move the camera to the center of Saint Petersburg.

        // And to show what can be done with it, we move the camera to the center of Saint Petersburg.
        mapView!!.getMap().move(
            CameraPosition(TARGET_LOCATION, 14.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 5f),
            null
        )
//        map
    }


//    map
    override fun onStop() {
        // Activity onStop call must be passed to both MapView and MapKit instance.
        mapView!!.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        // Activity onStart call must be passed to both MapView and MapKit instance.
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView!!.onStart()
    }
//    map

    private fun initSaveBtn() {
        save_map.setOnClickListener {
//            listener.showDialog()
            val metrics = resources.displayMetrics
            val width = metrics.widthPixels
            val height = metrics.heightPixels
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_get_user)
            dialog.setCancelable(false)
            dialog.window?.setLayout(
                ((6.3 * width) / 7).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawableResource(R.drawable.border_dialog)
//            var close = dialog.close_dialog
//            close.setOnClickListener { dialog.dismiss() }
           dialog.close_dialog.setOnClickListener { dialog.dismiss() }
            dialog.save_dialog.setOnClickListener { checkEditText(dialog) }

            dialog.window?.attributes!!.windowAnimations = R.style.MyDialogAnimation
            dialog.show()

//            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)


        }
    }

    private fun editTextValue(et: EditText): String {
        return et.text.toString().trim()
    }

    private fun checkEditText(dialog: Dialog) {
//        if(TextUtils.isEmpty(editTextValue(name_dialog)) || TextUtils.isEmpty(editTextValue(device_dialog)) || TextUtils.isEmpty(editTextValue(phone_dialog)) ){
//            toast("فیلد های مورد نظر را پر کنید")
//        }
        if(TextUtils.isEmpty(dialog.name_dialog.text) || TextUtils.isEmpty(dialog.phone_dialog.text) || TextUtils.isEmpty(dialog.device_dialog.text) )
            toast("فیلد های مورد نظر را پر کنید")
        else if (dialog.phone_dialog.text.length != 11) toast("تلفن همراه شما باید 11 کاراکتر باشد!")
        else{
            saveData(dialog)
            dialog.dismiss()
            finish()
        }
        }

    private fun saveData(dialog: Dialog) {
        dao.insert(Customer(editTextValue(dialog.name_dialog ), editTextValue(dialog.device_dialog),
            editTextValue(dialog.phone_dialog),"15 خرداد کوچه 28 "))
    }

    private fun initDataBase() {
        db = DatabaseHelper.getInstance(this)
        dao = db.customerDao()
    }

    private fun toast(text : String){
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }

}