package com.karimi.googlemap.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "customer_table")
 class Customer {

    @PrimaryKey(autoGenerate = true)
    var customerID = 0

    @ColumnInfo(name = "store_name")
    var storeName: String? = null

    @ColumnInfo(name = "terminal_num")
    var terminalnNumer: String? = null

    @ColumnInfo(name = "device_owner")
    var deviceOwner: String? = null

    @ColumnInfo(name = "phone_sabet")
    var phoneSabet: String? = null

    @ColumnInfo(name = "phone_hamrah")
    var phoneHamrah: String? = null

    @ColumnInfo(name = "address")
    var address: String? = null

    @ColumnInfo(name = "support_name")
    var supportName: String? = null

    @ColumnInfo(name = "device_model")
    var deviceModel: String? = null

    @ColumnInfo(name = "device_Serial")
    var deviceSerial: String? = null

    @ColumnInfo(name = "roll_num")
    var rollNumber: String? = null

    @ColumnInfo(name = "longitude")
    var longitude: Double? = null

    @ColumnInfo(name = "latitude")
    var latitude: Double? = null


    constructor(
        customerID: Int,
        storeName: String?,
        terminalnNumer: String?,
        deviceOwner: String?,
        phoneSabet: String?,
        phoneHamrah: String?,
        address: String?,
        supportName: String?,
        deviceModel: String?,
        deviceSerial: String?,
        rollNumber: String?,
        longitude: Double?,
        latitude: Double?
    ) {
        this.customerID = customerID
        this.storeName = storeName
        this.terminalnNumer = terminalnNumer
        this.deviceOwner = deviceOwner
        this.phoneSabet = phoneSabet
        this.phoneHamrah = phoneHamrah
        this.address = address
        this.supportName = supportName
        this.deviceModel = deviceModel
        this.deviceSerial = deviceSerial
        this.rollNumber = rollNumber
        this.longitude = longitude
        this.latitude = latitude
    }


    @Ignore
    constructor(
        storeName: String?,
        terminalnNumer: String?,
        deviceOwner: String?,
        phoneSabet: String?,
        phoneHamrah: String?,
        address: String?,
        supportName: String?,
        deviceModel: String?,
        deviceSerial: String?,
        rollNumber: String?,
        longitude: Double?,
        latitude: Double?
    ) {
        this.storeName = storeName
        this.terminalnNumer = terminalnNumer
        this.deviceOwner = deviceOwner
        this.phoneSabet = phoneSabet
        this.phoneHamrah = phoneHamrah
        this.address = address
        this.supportName = supportName
        this.deviceModel = deviceModel
        this.deviceSerial = deviceSerial
        this.rollNumber = rollNumber
        this.longitude = longitude
        this.latitude = latitude
    }


 @Ignore
    constructor(
        storeName: String?,
        terminalnNumer: String?,
        deviceOwner: String?,
        phoneSabet: String?,
        phoneHamrah: String?,
        address: String?,
        supportName: String?,
        deviceModel: String?,
        deviceSerial: String?,
        rollNumber: String?,
    ) {
        this.storeName = storeName
        this.terminalnNumer = terminalnNumer
        this.deviceOwner = deviceOwner
        this.phoneSabet = phoneSabet
        this.phoneHamrah = phoneHamrah
        this.address = address
        this.supportName = supportName
        this.deviceModel = deviceModel
        this.deviceSerial = deviceSerial
        this.rollNumber = rollNumber
    }


}