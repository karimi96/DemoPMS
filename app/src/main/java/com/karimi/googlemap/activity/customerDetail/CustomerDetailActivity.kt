package com.karimi.googlemap.activity.customerDetail

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.karimi.googlemap.R
import com.karimi.googlemap.activity.MapsActivity
import com.karimi.googlemap.sqlite.DBHelperJavaSimin
import kotlinx.android.synthetic.main.activity_customer_detaile.*
import kotlinx.android.synthetic.main.box_device_.*
import kotlinx.android.synthetic.main.box_title.*
import kotlinx.android.synthetic.main.box_user.*
import kotlinx.android.synthetic.main.dialog_no_found_item.*
import kotlinx.android.synthetic.main.toolbar_detail.*

class CustomerDetailActivity : AppCompatActivity() {
    var scannedResult: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_detaile)
        initInquiryUserInformation()
        initRecordLocation()

            scan.setOnClickListener {
            run {
                IntentIntegrator(this).initiateScan();
            }
        }
        initBack()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        var result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if(result != null){

            if(result.contents != null){
                scannedResult = result.contents
                et_TerminalNumber.setText(scannedResult)


            } else {
                et_TerminalNumber.setText("scan failed")
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    private fun initInquiryUserInformation() {
        var db = DBHelperJavaSimin(this)

        btn_InquiryUserInformation.setOnClickListener {
            var getEditText = et_TerminalNumber.text.toString()
            var name = db.getTerminal_number(getEditText)
            if (getEditText.isEmpty()) {
                toast("شماره پایانه را وارد کنید")
            } else if (name.size == 0 || name.isEmpty()) {
                showAlertDialog()
            } else {
                rl_scan.visibility = View.GONE
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                rl_information.visibility = View.VISIBLE
                userInformation(name)
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


    private fun toast(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }


    private fun initRecordLocation() {
        btn_record_location.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }


    private fun userInformation(info: ArrayList<String>) {
        storeName.text = info[0]
        terminalNumber_title.text = info[1]
        customerName.text = info[2]
        phone_sabet.text = info[3]
        phone_hamrah.text = info[4]
        support_Tecnisiyan.text = info[5]
        terminalNumber_info.text = info[6]
        deviceModel.text = info[6]
        deviceSerial.text = info[7]
        rollNumber.text = info[8]
    }

    private fun initBack(){
        img_back.setOnClickListener { finish() }
    }

}