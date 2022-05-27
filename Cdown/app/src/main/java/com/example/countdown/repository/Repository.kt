package com.example.countdown.repository

import com.example.countdown.room.dao.CountdownDAO
import com.example.countdown.room.entities.Countdown

class Repository(private val countdownDAO: CountdownDAO) {
    val allCountdowns = countdownDAO.getAllCountdowns()

    /**
     * calls insert function on the DAO
     * @param countdown receives a Countdown object
     **/
    suspend fun insert(countdown: Countdown){
        countdownDAO.insert(countdown)
    }

    /**
     * calls update function on the DAO
     * @param countdown receives a Countdown object
     **/
    suspend fun update(countdown: Countdown){
        countdownDAO.update(countdown)
    }

    /**
     * calls delete function on the DAO
     * @param countdown receives a Countdown object
     **/
    suspend fun delete(countdown: Countdown){
        countdownDAO.delete(countdown)
    }

    fun deleteAll(){
        countdownDAO.deleteAll()
    }
}