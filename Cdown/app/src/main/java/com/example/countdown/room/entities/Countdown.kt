package com.example.countdown.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "COUNTDOWN_TABLE")
data class Countdown(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val desc: String,
    val dateStart: String,
    val hourStart: String,
    val dateEnd: String,
    val hourEnd: String,
    val reminder: Boolean?
)