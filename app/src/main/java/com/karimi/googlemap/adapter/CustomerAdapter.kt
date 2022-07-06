package com.karimi.googlemap.adapter

import android.content.Context
import android.content.Intent
import android.location.GnssAntennaInfo
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.karimi.googlemap.R
import com.karimi.googlemap.activity.customerDetail.CustomerDetailActivity
import com.karimi.googlemap.model.Customer
import kotlinx.android.synthetic.main.list_item_customer.view.*

class CustomerAdapter(list: List<Customer>, context: Context ) : RecyclerView.Adapter<CustomerAdapter.MyViewHolder>() {
    var context :Context = context
    private var list: List<Customer> = list
//    var listener : Listener = listener

    interface Listener{
        fun showDialogDetail(list: List<Customer> , position: Int)
    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.name
        var device: TextView = view.device
        var phone: TextView = view.phone

        init {
            itemView.setOnClickListener{
                var intent =  Intent(context, CustomerDetailActivity:: class.java)
                intent.putExtra("id_home",list[position].customerID)
                context.startActivity(intent)

//                intent.putExtra("terminal",list[position].terminalnNumer)
            }
//            itemView.setOnClickListener{listener.showDialogDetail(list,position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyViewHolder(layoutInflater.inflate(R.layout.list_item_customer, parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var customer = list[position]

        holder.name.text = customer.deviceOwner
        holder.device.text = customer.deviceModel
//        holder.phone.text = customer.latitude.toString() + customer.longitude
        holder.phone.text = customer.phoneHamrah
    }

    override fun getItemCount(): Int {
        return list.size
    }


}