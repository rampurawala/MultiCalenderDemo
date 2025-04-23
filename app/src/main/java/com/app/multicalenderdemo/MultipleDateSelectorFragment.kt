package com.app.multicalenderdemo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.app.multicalenderdemo.databinding.FragmentMultipleSelectorBinding
import com.app.multicalenderdemo.horizontal_calender.SERVER_DATE_FORMAT
import com.app.multicalenderdemo.horizontal_calender.YYYY_MM_DD
import com.app.multicalenderdemo.horizontal_calender.formatCalendar
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener
import java.util.Calendar

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class MultipleDateSelectorFragment : Fragment() {

    private var _binding: FragmentMultipleSelectorBinding? = null
    val enabledDates: ArrayList<String> = arrayListOf()
    val calendarsSelected: MutableList<Calendar> = java.util.ArrayList()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMultipleSelectorBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val days = 28 /*+dates.size*/

        //   binding.txtMonthName.text = getMonth(Calendar.getInstance())

        binding.calendarView.setForwardButtonImage(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_date_next
            )!!
        )
        binding.calendarView.setPreviousButtonImage(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_date_previous
            )!!
        )
        //     Log.e("TAG", "initViews:calendarsSelected ${Gson.toJson(calendarsSelected)}", )
        //   binding.calendarView.selectedDates=calendarsSelected
        binding.calendarView.setOnForwardPageChangeListener(object : OnCalendarPageChangeListener {
            override fun onChange() {
                //   val disabledDates:List<Calendar> = arrayListOf()
                val disabledDates: MutableList<Calendar> = java.util.ArrayList()
                val cal = binding.calendarView.currentPageDate
                //  val cal= calendar
                // getDaysOfMonthCalendar(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH))
                getDisabledDays1()
                Log.e(
                    "TAG",
                    "onChange:setOnForwardPageChangeListener ${
                        formatCalendar(
                            cal,
                            YYYY_MM_DD
                        )
                    } ${cal.get(Calendar.MONTH)}"
                )
                val cal1 = binding.calendarView.currentPageDate

                  /*  cal.set(Calendar.DAY_OF_MONTH,14)
                disabledDates.plus(cal)*/
                //  Log.e(TAG, "onChange:setOnForwardPageChangeListener ${formatCalendar(cal, YYYY_MM_DD)}", )
                //    binding.calendarView.setDisabledDays(/*listOf(cal)*/disabledDates )
                invalidateRecursive(binding.calendarView)
                //  binding.calendarView.setDate(cal)

            }

        })

        binding.calendarView.setOnPreviousPageChangeListener(object : OnCalendarPageChangeListener {
            override fun onChange() {
                val cal = binding.calendarView.currentPageDate
                // getDaysOfMonthCalendar(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH))
                getDisabledDays1()
            }

        })
        val totalDays = ((days) - 1)
        val calendarLoop = Calendar.getInstance()
        for (i in 0..totalDays) {
            //  Log.e(TAG, "initViews: disable dates outer${formatCalendar(calendarLoop, YYYY_MM_DD)}", )
            val calFormat = formatCalendar(calendarLoop, YYYY_MM_DD)
            val format =
                formatCalendar(calendarLoop.timeInMillis, SERVER_DATE_FORMAT)
            var contains = false

            if (!contains) {
                // calendarLoop.setMidnight()
                enabledDates.add(format)
            }
            calendarLoop.add(Calendar.DAY_OF_MONTH, 1)
        }
        getDisabledDays1()
    }

    private fun invalidateRecursive(layout: ViewGroup) {
        val count = layout.childCount
        var child: View?
        for (i in 0 until count) {
            child = layout.getChildAt(i)
            if (child is ViewPager) {
                child.adapter!!.notifyDataSetChanged()
            }
            if (child is ViewGroup) {
                invalidateRecursive(child)
            }
        }
    }

    private fun getDisabledDays1()/*: MutableList<Calendar>*/ {
        val calendars: MutableList<Calendar> = java.util.ArrayList()

        var calendar = binding.calendarView.currentPageDate

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val days = mutableListOf<Calendar>()
        for (i in 1..daysInMonth) {
            calendar = binding.calendarView.currentPageDate
            calendar.set(Calendar.DAY_OF_MONTH, i)
            val format =
                formatCalendar(calendar.timeInMillis, SERVER_DATE_FORMAT)
            var contains = false

            enabledDates.forEach {
                if (format.equals(it)) {
                    contains = true
                    return@forEach
                } else {
                    // Log.e(TAG, "initViews: disable dates${formatCalendar(min, YYYY_MM_DD)}", )

                    //  calendarDays.add(CalendarDay(calendar = calendarLoop))
                }
            }
            if (!contains) {
                Log.e("TAG", "initViews: disable  filter${formatCalendar(calendar, YYYY_MM_DD)}")
                // min.setMidnight()
                //   binding.calendarView.setDisabledDays(listOf(calendar))
                calendars.add(calendar)
            }
            //days.add(calendar)
        }


        /*val firstDisabled = binding.calendarView.currentPageDate
        firstDisabled.set(Calendar.DAY_OF_MONTH, 2)
        calendars.add(firstDisabled)
       val secondDisabled = binding.calendarView.currentPageDate
        secondDisabled.set(Calendar.DAY_OF_MONTH, 10)
        calendars.add(secondDisabled)
        val thirdDisabled = binding.calendarView.currentPageDate
        thirdDisabled.set(Calendar.DAY_OF_MONTH, 18)
        calendars.add(thirdDisabled)*/
        /* calendars.add(firstDisabled)
         calendars.add(secondDisabled)
         calendars.add(thirdDisabled)*/
        // binding.calendarView.selectedDates=calendarsSelected
        binding.calendarView.setDisabledDays(calendars)
        //  return calendars

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}