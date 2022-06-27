package com.karimi.googlemap.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.karimi.googlemap.R
import kotlinx.android.synthetic.main.activity_customer_detaile.*
import kotlinx.android.synthetic.main.box_device_.*
import kotlinx.android.synthetic.main.dialog_no_found_item.*

class CustomerDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_detaile)
        initInquiryUserInformation()
        initRecordLocation()
    }

    private fun initInquiryUserInformation() {
        btn_InquiryUserInformation.setOnClickListener {
            var getEditText = et_TerminalNumber.text.toString()
            if (getEditText.isEmpty()) {
                toast("شماره پایانه را وارد کنید")
            } else if (getEditText != "555") {
                showAlertDialog()
            }else {
                rl_scan.visibility = View.GONE
                rl_information.visibility = View.VISIBLE
            }
        }
    }


    private fun showAlertDialog() {
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        var d = Dialog(this)
        d.setContentView(R.layout.dialog_no_found_item)
        d.setCancelable(false)
        d.window?.setLayout(((6.3 * width) / 7).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        d.window?.setBackgroundDrawableResource(R.drawable.border_dialog)
        d.tv_confirm.setOnClickListener { d.dismiss() }
        d.show()
    }

    private fun toast(text : String){
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }

   private fun initRecordLocation(){
        btn_record_location.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }

}