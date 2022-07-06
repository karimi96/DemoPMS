package com.karimi.googlemap.activity.home

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karimi.googlemap.R
import com.karimi.googlemap.activity.customerDetail.CustomerDetailActivity
import com.karimi.googlemap.adapter.CustomerAdapter
import com.karimi.googlemap.database.DatabaseHelper
import com.karimi.googlemap.database.dao.CustomerDao
import com.karimi.googlemap.model.Customer
import com.karimi.googlemap.sqlite.DBHelperJavaSimin
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.box_title.*
import kotlinx.android.synthetic.main.box_user.*
import kotlinx.android.synthetic.main.dialog_show_user.*
import kotlinx.android.synthetic.main.toolbar_home.*

class HomeActivity : AppCompatActivity() {
    private lateinit var listRequest: List<Customer>
    private lateinit var adapter: CustomerAdapter

    private lateinit var dao: CustomerDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()
        initDB()
        initFab()
        setReverseRecycler()
    }


    private fun initToolbar() {
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setSupportActionBar(findViewById(R.id.toolbar))
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun initDB() {
        var db = DatabaseHelper.getInstance(this)
        if (db != null) {
            dao = db.customerDao()
        }
    }

    private fun initRecycler() {
        initListAdapter()
        adapter = CustomerAdapter(listRequest, applicationContext)
        recycler.adapter = adapter
    }


    private fun initListAdapter() {
        listRequest = dao.getAllCustomer()
    }


    private fun initFab() {
        add.setOnClickListener {
            startActivity(Intent(this, CustomerDetailActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }


    fun layoutAnimationRecycler(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val layoutAnimationController =
            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_slide_right)
        recyclerView.layoutAnimation = layoutAnimationController
        recyclerView.adapter!!.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }


    override fun onResume() {
        super.onResume()
        if (dao.getAllCustomer().isNotEmpty()) {
            initRecycler()
            lottie.visibility = View.GONE
        }else {
            lottie.visibility = View.VISIBLE

        }
    }

//    override fun showDialogDetail(list: List<Customer>, pos: Int) {
//        val metrics = resources.displayMetrics
//        val width = metrics.widthPixels
//        val height = metrics.heightPixels
//        val dialog = Dialog(this)
//        dialog.setContentView(R.layout.dialog_show_user)
//        dialog.setCancelable(true)
////        dialog.window?.setLayout(((6.3 * width) / 7).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
//        dialog.window?.setLayout(((6.3 * width) / 7).toInt(), ((6 * height) / 7).toInt())
//        dialog.window?.setBackgroundDrawableResource(R.drawable.border_dialog)
////        dialog.close_c.setOnClickListener { dialog.dismiss() }
//        dialog.storeName.text = list[pos].storeName
//        dialog.terminalNumber_title.text = list[pos].terminalnNumer
//        dialog.customerName.text = list[pos].deviceOwner
//        dialog.phone_sabet.text = list[pos].phoneSabet
//        dialog.show()
//    }

    private fun setReverseRecycler() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        linearLayoutManager.reverseLayout = true
        recycler.layoutManager = linearLayoutManager
    }

}