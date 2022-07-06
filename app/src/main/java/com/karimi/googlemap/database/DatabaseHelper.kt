package com.karimi.googlemap.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.karimi.googlemap.database.dao.CustomerDao
import com.karimi.googlemap.model.Customer

@Database(
    entities = [Customer::class],
    exportSchema = false,
    version = 1)
abstract class DatabaseHelper : RoomDatabase() {


          /*  private val DB_NAME = "db_name"
            private var instance: DatabaseHelper? = null

*/
       /* companion object {
            // Singleton prevents multiple instances of database opening at the
            // same time.
            @Volatile
            private var INSTANCE: DatabaseHelper? = null

            fun getDatabase(context: Context): DatabaseHelper {
                val tempInstance = INSTANCE
                if (tempInstance != null) {
                    return tempInstance
                }
                synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseHelper::class.java,
                        "customer_database"
                    ).build()
                    INSTANCE = instance
                    return instance
                }
            }
          }*/



//     second code
companion object {
    private const val TAG = "AppDatabase"
    private const val DATABASE_NAME = "user-database.db"

    @Volatile
    private var instance: DatabaseHelper? = null

    fun getInstance(context: Context): DatabaseHelper =
        instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }

    private fun buildDatabase(appContext: Context): DatabaseHelper {
        val builder =
            Room.databaseBuilder(
                appContext,
                DatabaseHelper::class.java,
                DATABASE_NAME
            ).allowMainThreadQueries()
        return builder.build()
    }

  /*  private fun buildDatabase(appContext: Context): DatabaseHelper {
        val builder =
            Room.databaseBuilder(
                appContext,
                DatabaseHelper::class.java,
                DATABASE_NAME
            ).openHelperFactory(AssetSQLiteOpenHelperFactory())
                .allowMainThreadQueries()
        return builder.build()
    }
*/
}
    abstract fun customerDao(): CustomerDao


}
