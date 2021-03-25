package com.hmis_tn.lims.db;

import androidx.room.*
import com.hmis_tn.lims.ui.login.model.login_response_model.UserDetails


@Dao
abstract class UserDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUserDetails(userDetailsResponseContent: UserDetails?)

    @get:Query("SELECT * FROM UserDetails")
    abstract val userDetails: UserDetails?

    @Query("DELETE FROM UserDetails")
    abstract fun deleteUserDetails()


    fun deleteAndInsert(userDetails: UserDetails?) {
        deleteUserDetails()
        insertUserDetails(userDetails)
    }
}