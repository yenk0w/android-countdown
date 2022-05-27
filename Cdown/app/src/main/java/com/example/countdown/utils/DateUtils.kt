package com.example.countdown.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.res.Resources
import com.example.countdown.R
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class DateUtils {


    companion object {
        private val calendar: Calendar = Calendar.getInstance()
        private val year = calendar.get(Calendar.YEAR)
        private val month = calendar.get(Calendar.MONTH)
        private val today = calendar.get(Calendar.DAY_OF_MONTH)
        private val todayHour = calendar.get(Calendar.HOUR_OF_DAY)
        private val todayMinutes = calendar.get(Calendar.MINUTE)


        /**
         * Parses a string to a date
         * @param dateEnd receives a String
         * @return returns the difference between them in milliseconds
         */
        fun calculateMilliseconds(dateEnd: String): Long {
            val dateEndParsed = SimpleDateFormat("dd/MM/yyyy").parse(dateEnd)

            val todayMils = calendar.timeInMillis
            val endMils = dateEndParsed!!.time

            return endMils - todayMils
        }

        /**
         * @return returns Hour and Minutes as a String
         **/
        fun getHourStart(): String {
            return ("$todayHour:$todayMinutes")
        }

        /**
         *  @return returns Day, Month and Year as a String
         */
        fun getDateStart(): String {
            month + 1
            return ("$today, $month , $year")
        }

        /**
         * Parses a string to a date
         * Translate to spanish
         * @param dateEnd receives a String
         * @return returns the name of the day in spanish as a string
         */
        fun getDayName(dateEnd: String): String {
            val inFormat = SimpleDateFormat("dd/MM/yyyy")
            val date: Date? = inFormat.parse(dateEnd)
            val dayFormat = SimpleDateFormat("EEEE")

            val dayName = when (dayFormat.format(date!!)) {
                "Sunday" -> "Domingo"
                "Monday" -> "Lunes"
                "Tuesday" -> "Martes"
                "Wednesday" -> "Miércoles"
                "Thursday" -> "Jueves"
                "Friday" -> "Viernes"
                "Saturday" -> "Sábado"

                else -> throw IllegalArgumentException("Error")
            }

            return dayName
        }

        /**
         * Parses a string to a date
         * Translate to spanish
         * @param dateEnd receives a String
         * @return returns month in spanish and year as a string
         */
        fun getMonthAndYear(dateEnd: String): String{
            val inFormat = SimpleDateFormat("dd/MM/yyyy")
            val date: Date? = inFormat.parse(dateEnd)
            val monthFormat = SimpleDateFormat("MMMM")
            val monthEnglish = monthFormat.format(date!!)
            val yearFormat = SimpleDateFormat("yyyy")
            val year = yearFormat.format(date)

            val month = when(monthEnglish){
                "January" -> "Enero"
                "February" -> "Febrero"
                "March" -> "Marzo"
                "April" -> "Abril"
                "May" -> "Mayo"
                "June" -> "Junio"
                "July" -> "Julio"
                "August" -> "Agosto"
                "September" -> "Septiembre"
                "October" -> "Octubre"
                "November" -> "Noviembre"
                "December" -> "Diciembre"

                else -> throw IllegalArgumentException("Error")
            }

            return ("$month de $year")
        }

        /**
         * Parses a string to a date
         * Translate to spanish
         * @param dateEnd receives a String
         * @return returns day date as String
         */
        fun getDayDate(dateEnd: String): String{
            val inFormat = SimpleDateFormat("dd/MM/yyyy")
            val date: Date? = inFormat.parse(dateEnd)
            val dayFormat = SimpleDateFormat("dd")

            return dayFormat.format(date!!)
        }
    }


}