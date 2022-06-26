package com.karimi.googlemap.activity

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.karimi.googlemap.R
import com.karimi.googlemap.database.DatabaseHelper
import com.karimi.googlemap.database.dao.CustomerDao
import com.karimi.googlemap.model.Customer
import kotlinx.android.synthetic.main.activity_maps2.*
import kotlinx.android.synthetic.main.dialog_get_user.*


class MapsActivity2 : AppCompatActivity() {

    //     lateinit var listener : Listener
    private lateinit var db: DatabaseHelper
    private lateinit var dao: CustomerDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps2)
        initDataBase()
        initSaveBtn()
    }


//    interface Listener{
//        fun showDialog();
//    }
//
//
//     fun init(listener : Listener) {
//        this.listener = listener
//    }


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