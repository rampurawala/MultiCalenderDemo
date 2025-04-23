package com.app.multicalenderdemo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.app.multicalenderdemo.databinding.FragmentSingleSelectorBinding
import com.app.multicalenderdemo.horizontal_calender.DD_MM_YYYY
import com.app.multicalenderdemo.horizontal_calender.formatCalendar
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.CalendarWeekDay
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import com.applandeo.materialcalendarview.utils.midnightCalendar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.HashMap

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SingleDateSelectorFragment : Fragment() {

    private var _binding: FragmentSingleSelectorBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSingleSelectorBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.calendarView.setForwardButtonImage(
            ContextCompat.getDrawable(
                context,
                R.drawable.ic_date_next
            )!!
        )
        binding.calendarView.setPreviousButtonImage(
            ContextCompat.getDrawable(
                context,
                R.drawable.ic_date_previous
            )!!
        )
        // binding.calendarView.selectedDates= listOf(Calendar.getInstance())
        val calendar = Calendar.getInstance()
        val minDate = Calendar.getInstance()
        binding.calendarView.setMinimumDate(midnightCalendar)
        Log.e("TAG", "initViews:mindate ${formatCalendar(minDate, DD_MM_YYYY)}")
        val maxDate = Calendar.getInstance()

        maxDate.set(Calendar.YEAR, calendar.get(java.util.Calendar.YEAR) + 1)
        maxDate.set(Calendar.MONTH, calendar.get(java.util.Calendar.MONTH))
        maxDate.set(Calendar.DAY_OF_MONTH, calendar.get(java.util.Calendar.DAY_OF_MONTH))
        Log.e(
            "TAG",
            "initViews:mindate maxdate ${
         formatCalendar(
                    maxDate,
                    DD_MM_YYYY
                )
            } ${binding.calendarView.currentPageDate}",
        )


        binding.calendarView.setMaximumDate(maxDate)

        binding.calendarView.setFirstDayOfWeek(CalendarWeekDay.SUNDAY)
        binding.calendarView.setCalendarDayLayout(R.layout.custom_calendar_day_row)
        binding.calendarView.setOnCalendarDayClickListener(object : OnCalendarDayClickListener {
            override fun onClick(calendarDay: CalendarDay) {

            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}