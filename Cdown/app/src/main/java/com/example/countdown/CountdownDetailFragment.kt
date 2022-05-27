package com.example.countdown

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.work.*
import com.example.countdown.databinding.FragmentCountdowndetailBinding
import com.example.countdown.model.CountdownViewModel
import com.example.countdown.model.CountdownViewModelFactory
import com.example.countdown.repository.Repository
import com.example.countdown.room.CountdownDB
import com.example.countdown.utils.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import java.util.*


class CountdownDetailFragment : Fragment() {
    private var binding: FragmentCountdowndetailBinding? = null
    private lateinit var sharedViewModel: CountdownViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentCountdowndetailBinding =
            FragmentCountdowndetailBinding.inflate(inflater, container, false)
        binding = fragmentCountdowndetailBinding

       sharedViewModel.message.observe(viewLifecycleOwner) { it ->
           it.getContentIfNotHandled()?.let {
               Toast.makeText(context, it, Toast.LENGTH_LONG).show()
           }
       }
        return fragmentCountdowndetailBinding.root
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val countdownDAO =
            CountdownDB.getInstance(requireActivity().applicationContext).countdownDao
        val repository = Repository(countdownDAO)
        val dispatcherProvider = CoroutineDispatcherProvider()
        val factory = CountdownViewModelFactory(repository, dispatcherProvider)
        sharedViewModel =
            ViewModelProvider(activity as MainActivity, factory)[CountdownViewModel::class.java]

    }

    /**
     * Calls the addOrUpdate function on the ViewModel
     * Navigates to the CountdownListFragment
     **/
    fun addOrUpdateCountdown() {
        sharedViewModel.addOrUpdateCountdown()
        findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
    }

    /**
     * Initialize and show the DatePickerDialog
     **/
    fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { onDateSelected(it) }
        datePicker.show(parentFragmentManager, "date")
    }

    /**
     * Initialize and show the TimePickerFragment
     **/
    fun showTimePickerDialog() {
        val timePicker = TimePickerFragment { onTimeSelected(it) }
        timePicker.show(parentFragmentManager, "time")
    }

    /**
     * Sets the dateEnd value on the ViewModel
     * @param date receives an String
     * @return returns a Countdown object
     **/
    private fun onDateSelected(date: String) {
        sharedViewModel.dateEnd.value = date
    }

    /**
     * Sets the hourEnd value on the ViewModel
     * @param time receives an String
     * @return returns a Countdown object
     **/
    private fun onTimeSelected(time: String) {
        sharedViewModel.hourEnd.value = time
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            countdownDetailFragment = this@CountdownDetailFragment

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}