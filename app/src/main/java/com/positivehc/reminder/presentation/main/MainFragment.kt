package com.positivehc.reminder.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.positivehc.reminder.databinding.FragmentMainBinding
import com.positivehc.reminder.presentation.utils.DateUtils.Companion.nowAsOffsetTime
import com.positivehc.reminder.presentation.utils.EventDayDecorator
import com.positivehc.reminder.presentation.utils.toCalendarDay
import com.positivehc.reminder.presentation.utils.toLocalDate
import java.time.LocalDate

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private val recyclerViewAdapter by lazy {
        EventAdapter().apply {
            onClickListener = {
                val action = MainFragmentDirections.actionMainFragmentToDetailFragment(
                    eventId = it.id
                )
                findNavController().navigate(action)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupCalendarView()
        setListeners()
        setObservers()
    }

    private fun setupRecyclerView() {
        binding.rvEvents.apply {
            adapter = recyclerViewAdapter
        }
    }

    private fun setupCalendarView() {
        binding.calendarView.apply {
            selectedDate = LocalDate.now().toCalendarDay()
            state().edit().setFirstDayOfWeek(org.threeten.bp.DayOfWeek.MONDAY).commit()
            setOnDateChangedListener { _, date, selected ->
                if (selected) {
                    viewModel.updateDateEventList(date.toLocalDate())
                }
            }
        }
    }

    private fun setObservers() {
        viewModel.eventList.observe(viewLifecycleOwner) {
            it?.let {
                val dates = it.map { it.eventDateTime.toLocalDate().toCalendarDay() }.distinct()
                binding.calendarView.apply {
                    removeDecorators()
                    addDecorator(EventDayDecorator(dates))
                }
            }
        }

        viewModel.dateEventList.observe(viewLifecycleOwner) {
            it?.let {
                recyclerViewAdapter.submitList(it)
            }
        }
    }

    private fun setListeners() {
        binding.fab.setOnClickListener {
            binding.calendarView.selectedDate?.let {
                val action = MainFragmentDirections.actionMainFragmentToDetailFragment(
                    eventDateTime = it.toLocalDate().atTime(nowAsOffsetTime())
                )
                findNavController().navigate(action)
            }
        }
    }


    override fun onStart() {
        super.onStart()
        // updating events for today after coming back from detail fragment
        binding.calendarView.selectedDate?.let {
            viewModel.updateDateEventList(it.toLocalDate())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}