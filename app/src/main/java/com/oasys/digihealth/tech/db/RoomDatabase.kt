package com.oasys.digihealth.tech.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.oasys.digihealth.tech.ui.login.model.login_response_model.UserDetails

@Database(entities = [UserDetails::class], version = 4, exportSchema = false)
abstract class RoomDatabase : androidx.room.RoomDatabase() {
    abstract fun userDetailsDao(): UserDetailsDao?

    companion object {
        private var INSTANCE: com.oasys.digihealth.tech.db.RoomDatabase? = null

        /**
         * from developers android, made my own singleton
         *
         * @param context
         * @return
         */
        fun getInstance(context: Context): com.oasys.digihealth.tech.db.RoomDatabase? {
            if (INSTANCE == null) {
                synchronized(RoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE =
                            Room.databaseBuilder(
                                context.applicationContext,
                                com.oasys.digihealth.tech.db.RoomDatabase::class.java, "hmis_doctor_db"
                            )
                                .addCallback(sRoomDatabaseCallback)
                                .fallbackToDestructiveMigration()
                                .build()
                    }
                }
            }
            return INSTANCE
        }

        private val sRoomDatabaseCallback: Callback =
            object : Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                }
            }
    }
}