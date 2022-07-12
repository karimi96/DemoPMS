package com.karimi.googlemap.activity.customerDetail

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.karimi.googlemap.R
import com.karimi.googlemap.activity.map.MapsActivity
import com.karimi.googlemap.database.DatabaseHelper
import com.karimi.googlemap.database.dao.CustomerDao
import com.karimi.googlemap.model.Customer
import com.karimi.googlemap.sqlite.DBHelperJavaSimin
import kotlinx.android.synthetic.main.activity_customer_detaile.*
import kotlinx.android.synthetic.main.box_device_detail.*
import kotlinx.android.synthetic.main.box_title.*
import kotlinx.android.synthetic.main.box_user_detail.*
import kotlinx.android.synthetic.main.dialog_no_found_item.*
import kotlinx.android.synthetic.main.toolbar_detail.*
import android.R.string.no





class CustomerDetailActivity : AppCompatActivity(), View.OnTouchListener {
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
//      initSaveInfo()
        initScan()
        initBack()
    }


    private fun initScan() {
        scan.setOnClickListener {
            run {
                IntentIntegrator(this)
                    .initiateScan()
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




    private fun clickDoneAction() {
        var dbFromAssets = DBHelperJavaSimin(this)
        et_TerminalNumber.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
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
                            rl_information.isFocusableInTouchMode = true
                            rl_scan.visibility = View.GONE
                            overridePendingTransition(
                                android.R.anim.fade_in,
                                android.R.anim.fade_out
                            )
                            rl_information.visibility = View.VISIBLE
                            newUserInformation(userInfo)

                            val imm =
                                v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
        et_OwnerDevice.setText(info[2])
        et_PhoneSabet.setText(info[3])
        et_PhoneHamrah.setText(info[4])
        et_Address.setText(info[9])
        et_SupportDevice.setText(info[5])
        et_terminalNumber.setText(info[1])
        et_DeviceModel.setText(info[6])
        et_DeviceSerial.setText(info[7])
        et_RollNumber.setText(info[8])
    }


    private fun userInformationExist(c: Customer) {
        storeName.text = c?.storeName
        et_OwnerDevice.setText(c?.deviceOwner)
        et_PhoneSabet.setText(c?.phoneSabet)
        et_PhoneHamrah.setText(c?.phoneHamrah)
        et_Address.setText(c?.address)
        et_SupportDevice.setText(c?.supportName)
        et_terminalNumber.setText(c?.terminalnNumer)
        et_DeviceModel.setText(c?.deviceModel)
        et_DeviceSerial.setText(c?.deviceSerial)
        et_RollNumber.setText(c?.rollNumber)

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
        arrayOf(rl_information).forEach { it.visibility = View.VISIBLE }
        title_detail.text = "ویرایش مشتری"
        tv_record_location.text = "ثبت اطلاعات"
        userInformationExist(customer!!)
        initWebView()
    }


    private fun checkLatLong(terminal: String): Boolean {
        cm = dao!!.checkTerminal(terminal)
        return cm?.latitude != null
    }


    private fun checkEdittext(et: EditText): String {
        return et.text.toString().trim()
    }


    private fun getEditTextValue(): Boolean {
        var b = DBHelperJavaSimin(this).getTerminal_number(et_terminalNumber.text.toString())
        Log.e("444", "getEditTextValue: ${et_terminalNumber.text}")
        if (checkEdittext(et_OwnerDevice).isEmpty() || checkEdittext(et_PhoneSabet).isEmpty()
            || checkEdittext(et_PhoneHamrah).isEmpty() || checkEdittext(et_Address).isEmpty() ||
            checkEdittext(et_SupportDevice).isEmpty() || checkEdittext(et_terminalNumber).isEmpty()
            || checkEdittext(et_DeviceModel).isEmpty() || checkEdittext(et_DeviceSerial).isEmpty() ||
            checkEdittext(et_RollNumber).isEmpty()
        ) {
            toast("همه فیلد ها را پرکنید")
        } else if (et_PhoneSabet.text!!.length != 8) {
            toast("تلفن ثابت وارد شده باید 8 کاراکتر باشد")
        } else if (et_PhoneHamrah.length() != 11) {
            toast("تلفن همراه باید 11 کاراکتر باشد")
        } else if (b.size == 0) {
            toast("شماره پایانه وارد شده نامعتبر می باشد")
        } else return true
        return false
    }


    private fun getAllEditTextValue(): Customer {
        return Customer(
            storeName.text.toString(),
            et_terminalNumber.text.toString(),
            et_OwnerDevice.text.toString(),
            et_PhoneSabet.text.toString(),
            et_PhoneHamrah.text.toString(),
            et_Address.text.toString(),
            et_SupportDevice.text.toString(),
            et_DeviceModel.text.toString(),
            et_DeviceSerial.text.toString(),
            et_RollNumber.text.toString()
        )
    }


    private fun initRecordLocation() {
        btn_record_location.setOnClickListener {
            if (getEditTextValue()) {
                if (checkLatLong(et_terminalNumber.text.toString())) {

                    dao?.updateUserWithoutLatLong(getAllEditTextValue().terminalnNumer.toString() ,
                getAllEditTextValue().deviceOwner.toString() ,getAllEditTextValue().phoneSabet.toString()  ,
                getAllEditTextValue().phoneHamrah.toString() ,getAllEditTextValue().address.toString() ,
                getAllEditTextValue().supportName.toString() ,getAllEditTextValue().deviceModel.toString() ,
                getAllEditTextValue().deviceSerial.toString(),getAllEditTextValue().rollNumber.toString() ,
                cm!!.customerID)
                    finish()


//                    var intent = Intent(this, MapsActivity::class.java)
//                    intent.putExtra("latitude", cm?.latitude)
//                    intent.putExtra("longitude", cm?.longitude)//
//                    intent.putExtra("id_customer_detail", cm!!.customerID)
//                    intent.putExtra("dd", Gson().toJson(getAllEditTextValue()))
//                    Log.e("111", "initRecordLocation: ${cm!!.customerID}")
//                    Log.e("111", "initRecordLocation: $id")
//                    startActivity(intent)
                } else {
                    var i = Intent(applicationContext, MapsActivity::class.java)
                    i.putExtra("edittext_value", getEditText)
                    i.putExtra("dd", Gson().toJson(getAllEditTextValue()))
                    startActivity(i)
                }
            }
        }
    }


//    private fun initSaveInfo(){
//        tv_save_info.setOnClickListener {
////            dao?.updateUserWithoutLatLong(getAllEditTextValue().terminalnNumer.toString() ,
////                getAllEditTextValue().deviceOwner.toString() ,getAllEditTextValue().phoneSabet.toString()  ,
////                getAllEditTextValue().phoneHamrah.toString() ,getAllEditTextValue().address.toString() ,
////                getAllEditTextValue().supportName.toString() ,getAllEditTextValue().deviceModel.toString() ,
////                getAllEditTextValue().deviceSerial.toString(),getAllEditTextValue().rollNumber.toString() ,
////                id!!)
//
//            dao?.insert(Customer(storeName.text.toString(),
//                getAllEditTextValue().terminalnNumer.toString() ,
//                getAllEditTextValue().deviceOwner.toString() ,getAllEditTextValue().phoneSabet.toString()  ,
//                getAllEditTextValue().phoneHamrah.toString() ,getAllEditTextValue().address.toString() ,
//                getAllEditTextValue().supportName.toString() ,getAllEditTextValue().deviceModel.toString() ,
//                getAllEditTextValue().deviceSerial.toString(),getAllEditTextValue().rollNumber.toString() ))
//            finish()
//        }
//    }


    private fun initWebView() {
        linear_WebView.visibility = View.VISIBLE
        mWebView.webViewClient = WebViewClient()
        mWebView.loadUrl("file:///android_asset/map.html")
        mWebView.settings.javaScriptEnabled = true
        mWebView.settings.setSupportZoom(true)
        mWebView.setOnTouchListener(this)
//        var client = object : WebViewClient() {
//            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//        toast("hello")
//                return false
//            }
//        }
//        mWebView.webViewClient = client;


        
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


    private fun initBack() {
        img_back.setOnClickListener { finish() }
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        if (p0 == mWebView && p1!!.action == MotionEvent.ACTION_DOWN) {
            toast("hello")
            cm = dao!!.checkTerminal(et_terminalNumber.text.toString())
            var intent = Intent(this, MapsActivity::class.java)
                 intent.putExtra("latitude", cm?.latitude)
                 intent.putExtra("longitude", cm?.longitude)
                 intent.putExtra("id_customer_detail", cm!!.customerID)
//                    intent.putExtra("dd", Gson().toJson(getAllEditTextValue()))
                 Log.e("111", "onTouch : ${cm!!.customerID}")
                 Log.e("111", "onTouch: ${cm!!.latitude}")
                 Log.e("111", "onTouch: ${cm!!.longitude}")
                 startActivity(intent)
        }
        return false
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