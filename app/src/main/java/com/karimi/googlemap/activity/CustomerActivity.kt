package com.karimi.googlemap.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karimi.googlemap.R
import com.karimi.googlemap.adapter.CustomerAdapter
import com.karimi.googlemap.database.DatabaseHelper
import com.karimi.googlemap.database.dao.CustomerDao
import com.karimi.googlemap.model.Customer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_show_user.*

class MainActivity : AppCompatActivity() , CustomerAdapter.Listener{
    private lateinit var listRequest: List<Customer>
    private lateinit var adapter: CustomerAdapter

    private lateinit var dao: CustomerDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDB()
        initFab()
        setReverseRecycler()
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
        adapter = CustomerAdapter(listRequest, applicationContext,this)
        recycler.adapter = adapter
    }


    private fun initListAdapter() {
        listRequest = dao.getAllCustomer()
    }


    private fun initFab() {
        add.setOnClickListener {
            startActivity(Intent(this, MapsActivity2::class.java))
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
            noItem.visibility = View.GONE
        }else {
            noItem.visibility = View.VISIBLE

        }
    }

    override fun showDialogDetail(list: List<Customer> , pos: Int) {
                val metrics = resources.displayMetrics
                val width = metrics.widthPixels
                val height = metrics.heightPixels
                val dialog = Dialog(this)
                dialog.setContentView(R.layout.dialog_show_user)
                dialog.setCancelable(false)
                dialog.window?.setLayout(((6.3 * width) / 7).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
                dialog.window?.setBackgroundDrawableResource(R.drawable.border_dialog)
                dialog.close_c.setOnClickListener { dialog.dismiss() }
                dialog.name_c.text = list[pos].name
                dialog.device_c.text = list[pos].device
                dialog.phone_c.text = list[pos].phone
                dialog.address_c.text = list[pos].address
                dialog.show()
    }

    private fun setReverseRecycler() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        linearLayoutManager.reverseLayout = true
        recycler.layoutManager = linearLayoutManager
    }



}