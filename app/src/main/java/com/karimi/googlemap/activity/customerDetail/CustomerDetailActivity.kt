package com.karimi.googlemap.activity.customerDetail

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.karimi.googlemap.R
import com.karimi.googlemap.activity.map.MapsActivity
import com.karimi.googlemap.database.DatabaseHelper
import com.karimi.googlemap.database.dao.CustomerDao
import com.karimi.googlemap.model.Customer
import com.karimi.googlemap.sqlite.DBHelperJavaSimin
import kotlinx.android.synthetic.main.activity_customer_detaile.*
import kotlinx.android.synthetic.main.box_device_.*
import kotlinx.android.synthetic.main.box_title.*
import kotlinx.android.synthetic.main.box_user.*
import kotlinx.android.synthetic.main.dialog_no_found_item.*
import kotlinx.android.synthetic.main.toolbar_detail.*


class CustomerDetailActivity : AppCompatActivity() {
    private var scannedResult: String = ""
    private var userInfo: ArrayList<String> = ArrayList()
    private var getEditText: String? = null
    private var id: Int? = null

    private var dao: CustomerDao? = null
    private var cm: Customer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_detaile)

        initDataBase()
        clickDoneAction()
        initInquiryUserInformation()
        checkIntent()
        initRecordLocation()
        initScan()
        initBack()

    }


    private fun initScan() {
        scan.setOnClickListener {
            run {
                IntentIntegrator(this).initiateScan();
            }
        }
    }


    private fun initDataBase() {
        var db: DatabaseHelper = DatabaseHelper.getInstance(this)
        dao = db.customerDao()
    }


    private fun initInquiryUserInformation() {
        var dbFromAssets = DBHelperJavaSimin(this)
        btn_InquiryUserInformation.setOnClickListener {
            getEditText = et_TerminalNumber.text.toString()
            if (getEditText!!.isEmpty()) {
                toast("شماره پایانه را وارد کنید")
            } else {
                var c = dao?.checkTerminal(getEditText!!)
                if (c != null) {
                    changeSomeText(c)
                } else {
                    userInfo = dbFromAssets.getTerminal_number(getEditText)
                    if (userInfo.size == 0 || userInfo.isEmpty()) {
                        showAlertDialog()
                    } else {
                        rl_scan.visibility = View.GONE
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        rl_information.visibility = View.VISIBLE
                        newUserInformation(userInfo)
                    }
                }
            }
        }
    }



    private fun clickDoneAction(){
        var dbFromAssets = DBHelperJavaSimin(this)
        et_TerminalNumber.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                getEditText = et_TerminalNumber.text.toString()
                if (getEditText!!.isEmpty()) {
                    toast("شماره پایانه را وارد کنید")
                } else {
                    var c = dao?.checkTerminal(getEditText!!)
                    if (c != null) {
                        changeSomeText(c)
                    } else {
                        userInfo = dbFromAssets.getTerminal_number(getEditText)
                        if (userInfo.size == 0 || userInfo.isEmpty()) {
                            showAlertDialog()
                        } else {
                            rl_scan.visibility = View.GONE
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                            rl_information.visibility = View.VISIBLE
                            newUserInformation(userInfo)

                            val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(v.windowToken, 0)
                        }
                    }
                }
                true
            } else {
                false
            }
        }
    }





// call from inside an Activity...
//    hideKeyboard(this, view);
//    hideKeyboard(this, getCurrentFocus());
//    hideKeyboard(this, getWindow().getDecorView());
//    hideKeyboard(this, findViewById(android.R.id.content));




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


    private fun newUserInformation(info: ArrayList<String>) {
        storeName.text = info[0]
        terminalNumber_title.text = info[1]
        customerName.text = info[2]
        phone_sabet.text = info[3]
        phone_hamrah.text = info[4]
        support_Tecnisiyan.text = info[5]
        terminalNumber_info.text = info[1]
        deviceModel.text = info[6]
        deviceSerial.text = info[7]
        rollNumber.text = info[8]
    }


    private fun checkIntent() {
        if (intent.extras != null) id = intent.getIntExtra("id_home", 0)

        if (id != null) {
            var customer = dao!!.getOneCustomer(id!!)
            changeSomeText(customer)
            Log.e("111", "checkIntent: $id")
        }
    }


    private fun changeSomeText(customer: Customer) {
        rl_scan.visibility = View.GONE
        arrayOf(rl_information ,map_pic ).forEach { it.visibility = View.VISIBLE }
        title_detail.text = "ویرایش مشتری"
        tv_record_location.text = "ویرایش موقعیت"
        userInformationExist(customer!!)
        initWebView()
    }


    private fun checkLatLong(terminal: String): Boolean {
        cm = dao!!.checkTerminal(terminal)
        return cm?.latitude != null
    }


    private fun initRecordLocation() {
        btn_record_location.setOnClickListener {
            if (checkLatLong(terminalNumber_title.text.toString())) {
                var intent = Intent(this, MapsActivity::class.java)
                intent.putExtra("latitude", cm?.latitude)
                intent.putExtra("longitude", cm?.longitude)//
                intent.putExtra("id_customer_detail", cm!!.customerID)
//                intent.putExtra("id_customer_detail", id)
                startActivity(intent)
            } else {
                var i = Intent(this, MapsActivity::class.java)
                i.putExtra("edittext_value", getEditText)
                startActivity(i)
            }
        }
    }


    private fun initWebView(){
        mWebView.visibility = View.VISIBLE
        mWebView.webViewClient = WebViewClient()
        mWebView.loadUrl("file:///android_asset/map.html");
//        mWebView.loadUrl("https://www.geeksforgeeks.org/")
        mWebView.settings.javaScriptEnabled = true
        mWebView.settings.setSupportZoom(true)
    }


    override fun onBackPressed() {
        if (mWebView.canGoBack())
            mWebView.goBack()
        else
            super.onBackPressed()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var result: IntentResult? =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (result.contents != null) {
                scannedResult = result.contents
                et_TerminalNumber.setText(scannedResult)
            } else {
                et_TerminalNumber.setText("scan failed")
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    private fun toast(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }


//    private fun initRecordLocation() {
//        btn_record_location.setOnClickListener {
//            if (id != null) {
//                Log.e("111", "checkIntent: " + "click")
//                var intent = Intent(this, MapsActivity::class.java)
////                intent.putExtra("latitude", customer?.latitude)
////                intent.putExtra("longitude", customer?.longitude)
//                intent.putExtra("id_customer_detail", id)
//                Log.e("111", "checkIntent: $id" )
//
//                startActivity(intent)
//            }else{
//                var intent = Intent(this, MapsActivity::class.java)
//                intent.putExtra("edittext_value", getEditText)
//                Log.e("111", "checkIntent: $getEditText" )
//                startActivity(intent)
//            }
//
//        }
//    }


    private fun userInformationExist(c: Customer) {
        storeName.text = c?.storeName
        terminalNumber_title.text = c?.terminalnNumer
        customerName.text = c?.deviceOwner
        phone_sabet.text = c?.phoneSabet
        phone_hamrah.text = c?.phoneHamrah
        support_Tecnisiyan.text = c?.supportName
        terminalNumber_info.text = c?.terminalnNumer
        deviceModel.text = c?.deviceModel
        deviceSerial.text = c?.deviceSerial
        rollNumber.text = c?.rollNumber
    }


    private fun initBack() {
        img_back.setOnClickListener { finish() }
    }


}

//internal class DoneOnEditorActionListener : OnEditorActionListener {
//    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
//        if (actionId == EditorInfo.IME_ACTION_DONE) {
//            val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(v.windowToken, 0)
//            return true
//        }
//        return false
//    }
//}