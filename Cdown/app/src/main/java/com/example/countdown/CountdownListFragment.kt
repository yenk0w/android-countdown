package com.example.countdown

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.countdown.adapter.CountDownListRecyclerViewAdapter
import com.example.countdown.databinding.FragmentCountdownlistBinding
import com.example.countdown.model.CountdownViewModel
import com.example.countdown.model.CountdownViewModelFactory
import com.example.countdown.repository.Repository
import com.example.countdown.room.CountdownDB
import com.example.countdown.room.entities.Countdown
import com.example.countdown.utils.CoroutineDispatcherProvider

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CountdownListFragment : Fragment() {
    private var binding: FragmentCountdownlistBinding? = null
    private lateinit var sharedViewModel: CountdownViewModel
    private lateinit var adapter: CountDownListRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentCountdownlistBinding =
            FragmentCountdownlistBinding.inflate(inflater, container, false)
        binding = fragmentCountdownlistBinding

        return fragmentCountdownlistBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val countdownDAO =
            CountdownDB.getInstance(requireActivity().applicationContext).countdownDao
        val repository = Repository(countdownDAO)
        val dispatcherProvider = CoroutineDispatcherProvider()
        val factory = CountdownViewModelFactory(repository, dispatcherProvider)
        sharedViewModel = ViewModelProvider(activity as MainActivity,factory)[CountdownViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            countdownListFragment = this@CountdownListFragment
        }
        initRecyclerView()
    }

    /**
     * Initialize the RecyclerView
     **/
    private fun initRecyclerView() {
        binding!!.rvCountdowns.layoutManager = LinearLayoutManager(requireActivity().application)
        adapter = CountDownListRecyclerViewAdapter { selectedItem: Countdown ->
            listItemClicked(selectedItem)
        }
        binding!!.rvCountdowns.adapter = adapter
        displayCountdowns()
        swipe()
    }

    /**
     * Calls the delete function on the ViewModel
     **/
    private fun swipe() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                @NonNull recyclerView: RecyclerView,
                @NonNull viewHolder: RecyclerView.ViewHolder,
                @NonNull viewHolder1: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            override fun onSwiped(@NonNull viewHolder: RecyclerView.ViewHolder, i: Int) {
                sharedViewModel.delete(
                    adapter.getCountdown(
                        viewHolder.bindingAdapterPosition
                    )
                )
            }
        }).attachToRecyclerView(binding!!.rvCountdowns)
    }

    /**
     * Calls the clear function on the ViewModel
     * Navigates to the DetailFragment
     **/
    fun newCountdown(){
        sharedViewModel.clear()
        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
    }

    /**
     * Set the list of countdowns on the RecyclerView
     **/
    @SuppressLint("NotifyDataSetChanged")
    private fun displayCountdowns() {
        sharedViewModel.countdowns.observe(viewLifecycleOwner, Observer {
           // Log.i("MY TAG", it.toString())
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    /**
     * Calls the initUpdate function on the ViewModel
     * Navigates to the DetailFragment
     **/
    private fun listItemClicked(countdown: Countdown) {
        sharedViewModel.initUpdate(countdown)
        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}