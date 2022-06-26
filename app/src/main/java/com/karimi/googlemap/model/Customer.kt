package com.karimi.googlemap.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_table")
 class Customer {

    @PrimaryKey(autoGenerate = true)
    var customerID = 0

    @ColumnInfo(name = "name")
    var name: String? = null


    @ColumnInfo(name = "device")
    var device: String? = null

    @ColumnInfo(name = "phone")
    var phone: String? = null


    @ColumnInfo(name = "address")
    var address: String? = null


    constructor(name: String, device: String, phone: String, address : String) {
        this.name = name
        this.device = device
        this.phone = phone
        this.address = address
    }

}