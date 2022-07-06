package com.karimi.googlemap.database.dao

import androidx.room.*
import com.karimi.googlemap.model.Customer

@Dao
interface CustomerDao {

    @Query("SELECT * from customer_table")
    fun getAllCustomer() : List<Customer>

    @Query("SELECT * from customer_table where terminal_num = :terminal limit 1")
    fun checkTerminal(terminal : String) : Customer

    @Query("SELECT * from customer_table where customerID = :id limit 1")
    fun getOneCustomer(id: Int) :Customer

//    @Query("SELECT latitude,longitude from customer_table where customerID = :id")
//    fun getLatLong(id: Int) :List<String>

    @Query("UPDATE customer_table SET latitude = :lat ,longitude = :longitude where customerID = :id")
    fun updateLatLong(lat : Double? , longitude: Double? , id: Int)


    @Insert
    fun insert(customer: Customer)

    @Delete
    fun delete(customer: Customer)

    @Update
    fun update(customer: Customer)

}