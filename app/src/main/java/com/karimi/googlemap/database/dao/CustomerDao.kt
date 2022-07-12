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





//    @Query("UPDATE customer_table SET " +
//            "terminal_num = :terminal , " +
//            "device_owner = :deviceOwner , " +
//            "phone_sabet = :pSabet ," +
//            "phone_hamrah = :pHamrah ," +
//            "address = :address ," +
//            "support_name = :supportName ," +
//            "device_model = :dModel ," +
//            "device_Serial = :dSerial ," +
//            "roll_num = :roll , " +
//            "latitude = :lat ," +
//            "longitude = :longitude where " +
//            "customerID = :id")
//    fun updateLatLong(terminal: String ,deviceOwner: String ,pSabet: String ,
//                      pHamrah: String ,address: String ,supportName: String ,
//                      dModel: String ,dSerial: String ,roll: String ,
//                      lat : Double? , longitude: Double? , id: Int)



    @Query("UPDATE customer_table SET " +
            "terminal_num = :terminal , " +
            "device_owner = :deviceOwner , " +
            "phone_sabet = :pSabet ," +
            "phone_hamrah = :pHamrah ," +
            "address = :address ," +
            "support_name = :supportName ," +
            "device_model = :dModel ," +
            "device_Serial = :dSerial ," +
            "roll_num = :roll , " +
            "customerID = :id")
    fun updateUserWithoutLatLong(terminal: String ,deviceOwner: String ,pSabet: String ,
                      pHamrah: String ,address: String ,supportName: String ,
                      dModel: String ,dSerial: String ,roll: String ,id: Int)


//    @Query("INSERT INTO customer_table VALUES(null,:a,:b)")
//    fun getall(a: String?, b: String?, columna: String?, columnb: String?)
//
//

    @Insert
    fun insert(customer: Customer)

    @Delete
    fun delete(customer: Customer)

    @Update
    fun update(customer: Customer)

}