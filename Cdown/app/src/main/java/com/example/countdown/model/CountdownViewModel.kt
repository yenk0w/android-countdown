package com.example.countdown.model

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countdown.repository.Repository
import com.example.countdown.room.entities.Countdown
import com.example.countdown.utils.CoroutineDispatcherProvider
import com.example.countdown.utils.DateUtils
import com.example.countdown.utils.Event
import com.example.countdown.worker.NotificationManager
import kotlinx.coroutines.launch

class CountdownViewModel(
    private val repository: Repository,
    private val dispatcherProvider: CoroutineDispatcherProvider
) : ViewModel(), Observable {

    val countdowns = repository.allCountdowns

    private val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
        get() = statusMessage

    @Bindable
    var desc = MutableLiveData<String>()

    @Bindable
    var dateEnd = MutableLiveData<String>()

    @Bindable
    var hourEnd = MutableLiveData<String>()

    @Bindable
    var addOrUpdateBtnText = MutableLiveData<String>()

    @Bindable
    var addOrUpdateTitleText = MutableLiveData<String>()

    @Bindable
    var reminder = MutableLiveData<Boolean?>()

    private lateinit var countdownToUpdate: Countdown

    var updateMode = false


    init {
        setUp()
    }

    /**
     * inserts or delete a Countdown object
     **/
    fun addOrUpdateCountdown() {
        if (desc.value!!.isNotEmpty() && dateEnd.value!!.isNotEmpty() && hourEnd.value!!.isNotEmpty()) {
            if (!updateMode) {
                val dateStart = DateUtils.getDateStart()
                val hourStart = DateUtils.getHourStart()
                val countdown = createCountdown(null, dateStart, hourStart)
                insert(countdown)
                statusMessage.value = Event("Evento Agregado.")

            } else if (updateMode) {
                val dateStart = countdownToUpdate.dateStart
                val hourStart = countdownToUpdate.hourStart
                val countdown = createCountdown(countdownToUpdate.id, dateStart, hourStart)
                update(countdown)
                clear()
                statusMessage.value = Event("Evento Actualizado.")
            }
        } else {
            statusMessage.value = Event("Error, Campos Vacios.")
        }
    }



    /**
     * @param id receives an Int
     * @param dateStart receives an String
     * @param hourStart receives an String
     * @return returns a Countdown object
     **/
    private fun createCountdown(id: Int?, dateStart: String, hourStart: String): Countdown {
        val desc = desc.value.toString()
        val dateEnd = dateEnd.value.toString()
        val hourEnd = hourEnd.value.toString()
        val reminder = reminder.value

        return Countdown(id, desc, dateStart, hourStart, dateEnd, hourEnd, reminder)
    }


    private fun setUp() {
        addOrUpdateBtnText.value = "Agregar"
        addOrUpdateTitleText.value = "Nuevo Evento"
        reminder.value = false
    }

    /**
     * clear variables
     **/
    fun clear() {
        desc.value = ""
        dateEnd.value = ""
        hourEnd.value = ""
        addOrUpdateTitleText.value = "Nuevo Evento"
        addOrUpdateBtnText.value = "Agregar"
        updateMode = false
    }

    /**
     * initializes update mode
     **/
    fun initUpdate(countdown: Countdown) {
        updateMode = true
        addOrUpdateBtnText.value = "Actualizar"
        addOrUpdateTitleText.value = "Actualizar Evento"
        countdownToUpdate = countdown
        desc.value = countdown.desc
        hourEnd.value = countdown.hourEnd
        dateEnd.value = countdown.dateEnd
        reminder.value = countdown.reminder
    }

    /**
     * calls insert function on the Repository
     * @param countdown receives a Countdown object
     **/
     fun insert(countdown: Countdown) {
        viewModelScope.launch(dispatcherProvider.default) {
            repository.insert(countdown)
            if(countdown.reminder == true){
                NotificationManager.scheduleOneTimeNotification(
                    countdown.desc,
                    countdown.hourEnd,
                    countdown.dateEnd
                )
            }

        }
    }

    /**
     * calls delete function on the Repository
     * @param countdown receives a Countdown object
     **/
    fun delete(countdown: Countdown) {
        viewModelScope.launch(dispatcherProvider.default) {
            repository.delete(countdown)
        }
    }

    /**
     * calls update function on the Repository
     * @param countdown receives a Countdown object
     **/
    private fun update(countdown: Countdown) {
        viewModelScope.launch(dispatcherProvider.default) {
            repository.update(countdown)
            NotificationManager.cancelNotification(countdown.desc)
            if(countdown.reminder == true){
                NotificationManager.scheduleOneTimeNotification(
                    countdown.desc,
                    countdown.hourEnd,
                    countdown.dateEnd
                )
            }
        }
    }

    /**
     * calls deleteAll function on the Repository
     **/
    fun deleteAll(){
        statusMessage.value = Event("Eventos Borrados.")
        repository.deleteAll()
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}


}