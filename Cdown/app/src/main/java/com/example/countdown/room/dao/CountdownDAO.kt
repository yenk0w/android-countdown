package com.example.countdown.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.countdown.room.entities.Countdown

@Dao
interface CountdownDAO {

    @Insert
    suspend fun insert(countdown: Countdown)

    @Update
    suspend fun update(countdown: Countdown)

    @Delete
    suspend fun delete(countdown: Countdown)

    @Query("DELETE FROM COUNTDOWN_TABLE")
    fun deleteAll()

    @Query("SELECT * FROM COUNTDOWN_TABLE")
    fun getAllCountdowns(): LiveData<List<Countdown>>

    @Query("SELECT * FROM COUNTDOWN_TABLE WHERE id=:id ")
    fun getCountdownsById(id: Int): LiveData<Countdown>
}