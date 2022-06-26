package com.karimi.googlemap.database.dao

import androidx.room.*
import com.karimi.googlemap.model.Customer

@Dao
interface CustomerDao {

    @Query("SELECT * from customer_table")
    fun getAllCustomer() : List<Customer>

//    @Query("select * from product where branch=:branch")
//    fun selectProduct(branch: Int): List<Product>

    @Insert
    fun insert(customer: Customer)

    @Delete
    fun delete(customer: Customer)

    @Update
    fun update(customer: Customer)

}