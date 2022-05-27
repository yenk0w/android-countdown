package com.example.countdown.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.countdown.room.dao.CountdownDAO
import com.example.countdown.room.entities.Countdown

@Database(entities = [Countdown::class], version = 4)
abstract class CountdownDB : RoomDatabase() {

    abstract val countdownDao: CountdownDAO

    companion object {
        @Volatile
        private var INSTANCE: CountdownDB? = null

        fun getInstance(context: Context): CountdownDB {
            synchronized(this) {
                var instance = Room.databaseBuilder(
                    context.applicationContext,
                    CountdownDB::class.java,
                    "COUNTDOWN_DATABASE"
                ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}