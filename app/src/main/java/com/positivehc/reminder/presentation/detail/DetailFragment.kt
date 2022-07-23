package com.positivehc.reminder.presentation.detail

import com.positivehc.reminder.R
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.positivehc.reminder.databinding.FragmentDetailBinding
import com.positivehc.reminder.domain.Event
import com.positivehc.reminder.presentation.receiver.AlarmReceiver
import com.positivehc.reminder.presentation.utils.AlarmUtils
import com.positivehc.reminder.presentation.utils.BundleUtils.Companion.KEY_DESCRIPTION
import com.positivehc.reminder.presentation.utils.FragmentViewMode
import com.positivehc.reminder.presentation.utils.toLocalDate
import com.positivehc.reminder.presentation.utils.toLocalTime
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.util.*

class DetailFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this)[DetailViewModel::class.java]
    }

    private var _binding: FragmentDetailBinding? = null
    private val binding
        get() = _binding!!

    private var eventDateTime = DATETIME_UNDEFINED
    private var eventId = Event.UNDEFINED_ID
    private lateinit var viewMode: FragmentViewMode

    private val args by navArgs<DetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentViewMode()
        setupBinding()
        setupBackPressedCallback()
        setListeners()
        setObservers()
        setVisibility()
        launch()
    }

    private fun setupBackPressedCallback() {
        val callback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.isEventModified()) {
                    showChangedEventCancelDialog { _, _ -> closeFragment() }
                }
                else {
                    closeFragment()
                }
            }

            private fun closeFragment() {
                isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun setFragmentViewMode() {
        if (args.eventId != -1) {
            viewMode = FragmentViewMode.EDIT
        } else if (args.eventDateTime != null) {
            viewMode = FragmentViewMode.ADD
        } else {
            throw IllegalArgumentException("Unknown arguments $args. Should set either eventId or eventDateTime")
        }
    }

    private fun setVisibility() {
        if (viewMode == FragmentViewMode.ADD) {
            binding.ivDelete.visibility = View.GONE
        }
    }

    private fun setListeners() {
        binding.ivCancel.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.ivDelete.setOnClickListener {
            showDeleteDialog { _, _ ->
                deleteEvent()
                requireActivity().onBackPressed()
            }
        }
        binding.tvEventDate.setOnClickListener {
            pickDate()
        }
        binding.tvEventTime.setOnClickListener {
            pickTime()
        }
        binding.etDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.etDescription.error = null
                viewModel.clearIsDescriptionBlankError()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun deleteEvent() {
        cancelAlarm()
        viewModel.deleteEvent()
    }

    private fun setObservers() {
        viewModel.isSetAlarm.observe(viewLifecycleOwner) {
            if (it) {
                setAlarm()
                viewModel.clearIsSetAlarm()
                findNavController().navigateUp()
            }
        }
        viewModel.isDescriptionBlankError.observe(viewLifecycleOwner) {
            if (it) {
                binding.etDescription.error = getString(R.string.blank_description_error)
            }
        }
    }

    private fun setupBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun launch() {
        when (viewMode) {
            FragmentViewMode.ADD -> {
                eventDateTime = args.eventDateTime!!
                launchAdd()
            }
            FragmentViewMode.EDIT -> {
                eventId = args.eventId
                launchEdit()
            }
        }
    }

    private fun launchAdd() {
        viewModel.initEvent(eventDateTime)
        binding.ivOk.setOnClickListener {
            viewModel.addEvent(
                binding.tvEventDate.text.toString(),
                binding.tvEventTime.text.toString(),
                binding.etDescription.text.toString()
            )
        }
    }

    private fun launchEdit() {
        viewModel.getEvent(eventId)
        binding.ivOk.setOnClickListener {
            viewModel.editEvent(
                binding.tvEventDate.text.toString(),
                binding.tvEventTime.text.toString(),
                binding.etDescription.text.toString()
            )
        }
    }

    private fun pickDate() {
        val currentDateTime = binding.tvEventDate.text.toString().toLocalDate()
        val startYear = currentDateTime.year
        val startMonth = currentDateTime.monthValue - 1
        val startDay = currentDateTime.dayOfMonth

        DatePickerDialog(requireContext(), { _, year, month, day ->
            viewModel.updateEventDate(LocalDate.of(year, month + 1, day))
        }, startYear, startMonth, startDay).show()
    }

    private fun pickTime() {
        val currentDateTime = binding.tvEventTime.text.toString().toLocalTime()
        val startHour = currentDateTime.hour
        val startMinute = currentDateTime.minute

        TimePickerDialog(
            requireContext(), { _, hour, minute ->
                viewModel.updateEventTime(LocalTime.of(hour, minute))
            },
            startHour,
            startMinute,
            false
        ).show()
    }

    private fun setAlarm() {
        val event = viewModel.event.value ?: return
        val timeInMillis = event.eventDateTime.toInstant().toEpochMilli()
        if (timeInMillis < Calendar.getInstance().timeInMillis) {
            return
        }
        val requestCode = event.id
        val description = event.description

        val pendingIntent = getAlarmReceiverPendingIntent(requestCode, description)
        AlarmUtils.setExact(requireContext(), timeInMillis, pendingIntent)
    }

    private fun cancelAlarm() {
        val event = viewModel.event.value ?: return
        val requestCode = event.id
        val pendingIntent = getAlarmReceiverPendingIntent(requestCode)
        AlarmUtils.cancel(requireContext(), pendingIntent)
    }

    private fun showChangedEventCancelDialog(positiveButtonClickListener: android.content.DialogInterface.OnClickListener) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.changed_event_cancel_title)
            .setMessage(R.string.changed_event_cancel_message)
            .setPositiveButton(R.string.yes, positiveButtonClickListener)
            .setNegativeButton(R.string.no, null)
            .show()
    }

    private fun showDeleteDialog(positiveButtonClickListener: android.content.DialogInterface.OnClickListener) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.delete_event_title)
            .setMessage(R.string.delete_event_message)
            .setPositiveButton(R.string.yes, positiveButtonClickListener)
            .setNegativeButton(R.string.no, null)
            .show()
    }

    private fun getAlarmReceiverPendingIntent(
        requestCode: Int,
        description: String? = null
    ): PendingIntent {
        val intent = AlarmReceiver.newIntent(requireContext())
        description?.let {
            intent.putExtra(KEY_DESCRIPTION, it)
        }

        return PendingIntent.getBroadcast(
            requireContext(), requestCode, intent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        val DATETIME_UNDEFINED: OffsetDateTime = OffsetDateTime.MIN
    }
}