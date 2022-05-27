package com.example.countdown.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.countdown.R
import com.example.countdown.databinding.CountdownItemBinding
import com.example.countdown.room.entities.Countdown
import com.example.countdown.utils.DateUtils
import kotlin.collections.ArrayList

class CountDownListRecyclerViewAdapter(private val clickListener: (Countdown) -> Unit) :
    RecyclerView.Adapter<CountDownListViewHolder>() {

    private val countDownList = ArrayList<Countdown>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountDownListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: CountdownItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.countdown_item, parent, false)

        return CountDownListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountDownListViewHolder, position: Int) {
        holder.bind(countDownList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return countDownList.size
    }

    fun setList(countdowns :List<Countdown>) {
        countDownList.clear()
        countDownList.addAll(countdowns)
    }

    fun getCountdown(position: Int): Countdown {
        return countDownList[position]
    }
}

class CountDownListViewHolder(private val binding: CountdownItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(countdown: Countdown, clickListener: (Countdown) -> Unit) {

        val dayName = DateUtils.getDayName(countdown.dateEnd)
        val monthAndYear = DateUtils.getMonthAndYear(countdown.dateEnd)
        val dayDate = DateUtils.getDayDate(countdown.dateEnd)

        binding.tvDesc.text = countdown.desc
        binding.tvCompoundHour.text = itemView.context.getString(R.string.end_at, dayName, countdown.hourEnd)
        binding.tvCompoundDate.text = monthAndYear
        binding.tvDay.text = dayDate
        binding.countdownItemLayout.setOnClickListener {
            clickListener(countdown)
        }
    }
}