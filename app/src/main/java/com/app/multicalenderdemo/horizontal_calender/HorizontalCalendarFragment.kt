package com.app.multicalenderdemo.horizontal_calender

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.multicalenderdemo.databinding.FragmentHorizontalBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class HorizontalCalendarFragment : Fragment() {

    private var _binding: FragmentHorizontalBinding? = null
    var selectedDate = Calendar.getInstance().timeInMillis
    private lateinit var adapter: EventCalendarAdapter
    var currentSnagPosition = 0
    private val calendarDateList = ArrayList<CalendarDateModel>()
    private val listEventDates = ArrayList<Calendar>()
    private val cal = Calendar.getInstance(Locale.ENGLISH)
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHorizontalBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txtMonthName.text = getMonth(Calendar.getInstance())
        setupListner()
        setUpCalendarAdapter()
        setUpCalendar()
        listEventDates.clear()
        //   val arrSchema = HashMap<String, com.haibin.calendarview.Calendar>()
    }

    private fun setUpCalendarAdapter() {

        // For LinearLayoutManager horizontal orientation
        binding.calendarLayout.setLayoutManager(
            LinearLayoutManager(
                binding.root.context,
                RecyclerView.HORIZONTAL,
                false
            )
        )

        val snapToBlock = SnapToBlock(1)
        snapToBlock.attachToRecyclerView(binding.calendarLayout)
        adapter =
            EventCalendarAdapter(
                this@HorizontalCalendarFragment.requireContext(),
                calendarDateList
            ) { calendarDateModel: CalendarDateModel, position: Int ->
                //selectedPosition = position
                calendarDateList.forEachIndexed { index, calendarModel ->
                    calendarDateList.get(index).isSelected = index == position
                }

                selectedDate = toCalendar(calendarDateModel.data).timeInMillis
                if (selectedDate < getCurrentDateMillis()) {

                } else {
                    Log.e(
                        "TAG",
                        "onCalendarSelect: dateSchemaHashmap  selectedDate   ${selectedDate}"
                    )
                    binding.selectedDate.text="Selected Date : ${formatToLocalDateTime(selectedDate)}"+

                            Log.e("TAG", "updateAdapter:selectedDate ${formatToLocalDateTime(selectedDate)}", )
                 //   viewModel.loadWorkout(getUserId(), selectedDate = selectedDate)
                }
                adapter.notifyDataSetChanged()
            }

        snapToBlock.setSnapBlockCallback(object : SnapToBlock.SnapBlockCallback {
            override fun onBlockSnap(snapPosition: Int) {
                Log.e("TAG", "onBlockSnap: ${snapPosition}")
                currentSnagPosition = snapPosition
                calendarDateList.forEachIndexed { index, calendarModel ->
                    if (adapter.getDateSelectedPosition() > -1 && index == (snapPosition + (adapter.getDateSelectedPosition() % 7))) {
                        calendarModel.isSelected = true
                        selectedDate = toCalendar(calendarModel.data).timeInMillis
                        //  selectedPosition=index
                        // getEventList()
                        binding.selectedDate.text="Selected Date : ${formatToLocalDateTime(selectedDate)}"+

                                Log.e("TAG", "updateAdapter:selectedDate ${formatToLocalDateTime(selectedDate)}", )
                    } else
                        calendarModel.isSelected = false


                }
                adapter.notifyDataSetChanged()/*setData(calendarList2)*/
            }

            override fun onBlockSnapped(snapPosition: Int) {
                Log.e("TAG", "onBlockSnap onBlockSnapped: ${snapPosition}")

            }

        })
        binding.calendarLayout.adapter = adapter
    }

    private fun setUpCalendar(
        selectedCalendar: Calendar? = null,
        isFromMonthChange: Boolean? = false
    ) {
        val calendarList = ArrayList<CalendarDateModel>()
        binding.txtMonthName.text = getMonth(cal)
        lifecycleScope.launch(Dispatchers.IO) {
            val monthCalendar = cal.clone() as Calendar
            monthCalendar.set(Calendar.DAY_OF_MONTH, 1)
            val firstDayOfWeek = monthCalendar.get(Calendar.DAY_OF_WEEK)

            // Adjust the calendar to the previous Sunday if the month doesn't start on a Sunday
            if (firstDayOfWeek != Calendar.SUNDAY) {
                val daysToSubtract = firstDayOfWeek - Calendar.SUNDAY
                monthCalendar.add(Calendar.DAY_OF_MONTH, -daysToSubtract)
            }
            /* if (firstDayOfWeek != Calendar.SUNDAY) {
                 calendar.add(Calendar.DAY_OF_MONTH, -((firstDayOfWeek - Calendar.SUNDAY + 7) % 7))
             }*/
            val dates = mutableListOf<Date>()
            var selectedIndex = -1;
            var isSelectedDate = false
            var index = -1

            // Start from the adjusted start date and add dates to the list
            while ((monthCalendar.get(Calendar.MONTH) <= cal.get(Calendar.MONTH) && monthCalendar.get(
                    Calendar.YEAR
                ) <= cal.get(Calendar.YEAR)) || (cal.get(Calendar.MONTH) == 0 && monthCalendar.get(
                    Calendar.MONTH
                ) >= cal.get(Calendar.MONTH) && monthCalendar.get(Calendar.YEAR) <= cal.get(Calendar.YEAR)) || monthCalendar.get(
                    Calendar.DAY_OF_WEEK
                ) != Calendar.SUNDAY
            ) {
                index++
                //  dates.add(monthCalendar.time)
                if (!isSelectedDate && isFromMonthChange == false && selectedCalendar != null && (selectedCalendar.get(
                        Calendar.DAY_OF_MONTH
                    ) == monthCalendar.get(
                        Calendar.DAY_OF_MONTH
                    ))
                ) {
                    calendarList.add(CalendarDateModel(monthCalendar.time, isSelected = true))
                    //  selectedPosition = calendarList.size - 1
                    //  selectedIndex=(currentSnagPosition + (selectedPosition % 7))
                    selectedIndex = 1
                    isSelectedDate = true
                } else if (!isSelectedDate && (adapter.getDateSelectedPosition() == -1 || (isFromMonthChange == true && monthCalendar.get(
                        Calendar.MONTH
                    ) == cal.get(Calendar.MONTH))) && isToday(monthCalendar)
                ) {
                    calendarList.add(CalendarDateModel(monthCalendar.time, isSelected = true))
                    //  selectedPosition = calendarList.size - 1
                    selectedDate = toCalendar(monthCalendar.time).timeInMillis
                    selectedIndex = 1
                    isSelectedDate = true
                } else {
                    if (/*selectedPosition<=7 && */!isSelectedDate && isFromMonthChange == true && index == adapter.getDateSelectedPosition()) {
                        calendarList.add(CalendarDateModel(monthCalendar.time, isSelected = true))
                        // selectedPosition=index
                        selectedDate = toCalendar(monthCalendar.time).timeInMillis
                        isSelectedDate = true
                    }
                    /*else if(isFromMonthChange==true  &&  (index-7 == currentSnagPosition) ){
                        calendarList.add(CalendarDateModel(monthCalendar.time, isSelected = true))
                        selectedPosition=index
                        selectedDate = toCalendar(monthCalendar.time).timeInMillis
                    }*/ else {
                        calendarList.add(CalendarDateModel(monthCalendar.time))
                    }
                    /* if ((cal?.get(Calendar.MONTH) == monthCalendar.get(
                             Calendar.MONTH
                         )) && (cal?.get(Calendar.YEAR) == monthCalendar.get(
                             Calendar.YEAR
                         )) && (cal?.get(Calendar.DAY_OF_MONTH) == 1)
                     ) {
                         calendarList.add(CalendarDateModel(monthCalendar.time, isSelected = true))
                         selectedPosition = calendarList.size - 1
                     } else {
                         calendarList.add(CalendarDateModel(monthCalendar.time))
                     }*/
                }
                monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
            }

                    Log.e("TAG", "setUpCalendar:selectedPosition ${adapter.getDateSelectedPosition()} ")
            calendarDateList.clear()
            if (cal.get(Calendar.MONTH) <= Calendar.getInstance().get(Calendar.MONTH) && cal.get(
                    Calendar.YEAR
                ) <= Calendar.getInstance().get(Calendar.YEAR)
            ) {
                val filteredDates = removePastDates(calendarList)
                val calFirst: Calendar = Calendar.getInstance()
                calFirst.time = filteredDates.get(0).data
                if (calFirst.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    val daysToSubtract = calFirst.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY
                    calFirst.add(Calendar.DAY_OF_MONTH, -daysToSubtract)
                    val calendarListFiltered = ArrayList<CalendarDateModel>()
                    while (calFirst.time.before(filteredDates[0].data)) {
                        calendarListFiltered.add(CalendarDateModel(calFirst.time))
                        calFirst.add(Calendar.DAY_OF_MONTH, 1)
                    }
                    calendarListFiltered.addAll(filteredDates)
                    updateAdapter(
                        setIsEventAvailable(calendarListFiltered),
                        selectedIndex,
                        isFromMonthChange
                    )

                } else {
                    updateAdapter(
                        setIsEventAvailable(filteredDates),
                        selectedIndex,
                        isFromMonthChange
                    )
                }
            } else {
                updateAdapter(setIsEventAvailable(calendarList), selectedIndex, isFromMonthChange)
            }
        }
        binding.selectedDate.text="Selected Date : ${formatToLocalDateTime(selectedDate)}"

    }

    fun setIsEventAvailable(calendarList: ArrayList<CalendarDateModel>): ArrayList<CalendarDateModel> {
        val monthEventList =
            listEventDates.filter { it.get(Calendar.MONTH) == cal.get(Calendar.MONTH) }
        if (monthEventList.size > 0) {
            monthEventList.forEach { eventDate ->
                calendarList.forEachIndexed { index, date ->
                    val dateCalendar: Calendar = toCalendar(date.data)
                    // if(eventDate.get(Calendar.MONTH)==dateCalendar.get(Calendar.MONTH))
                    if (eventDate.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR) &&
                        eventDate.get(Calendar.MONTH) == dateCalendar.get(Calendar.MONTH) &&
                        eventDate.get(Calendar.DAY_OF_MONTH) == dateCalendar.get(Calendar.DAY_OF_MONTH)
                    ) {
                        calendarList.get(index).hasEvent = true
                    }
                }
            }
        }
        return calendarList
    }

    fun toCalendar(date: Date?): Calendar {
        val cal = Calendar.getInstance()
        cal.timeInMillis = 0
        cal.time = date
        return cal
    }

    fun updateAdapter(
        list: ArrayList<CalendarDateModel>,
        selectedPos: Int? = -1,
        fromMonth: Boolean? = false
    ) {
        calendarDateList.addAll(list)

        lifecycleScope.launch(Dispatchers.Main) {
            binding.calendarLayout.adapter?.notifyDataSetChanged()
            Log.e("TAG", "updateAdapter:selectedPos ${selectedPos} ")
            if (selectedPos != null && selectedPos != -1) {
                val index = list.indexOfFirst { it.isSelected == true }
                Log.e("TAG", "updateAdapter: ${index}")
                if (index > 7) {
                    val indexDiv7 = getRangeEnd(index)
                    binding.calendarLayout.scrollToPosition(indexDiv7)
                } else {
                    val indexDiv7 = getRangeStart(index)
                    binding.calendarLayout.scrollToPosition(indexDiv7)
                }

            }
        }
        if (fromMonth == true) {
            binding.selectedDate.text="Selected Date : ${formatToLocalDateTime(selectedDate)}"+
            Log.e("TAG", "updateAdapter:selectedDate  ${formatToLocalDateTime(selectedDate)}", )
         //   viewModel.loadWorkout(getUserId(), selectedDate = selectedDate)
        }
    }


    private fun setupListner() {
        binding.btnPrevMonth.setOnClickListener {
            //   binding.calendarView.scrollToPre(true)
            cal.add(Calendar.MONTH, -1)
            if ((cal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR) && cal.get(
                    Calendar.MONTH
                ) >= Calendar.getInstance()
                    .get(Calendar.MONTH)) || cal.get(Calendar.YEAR) > Calendar.getInstance()
                    .get(Calendar.YEAR)
            ) {
                setUpCalendar(isFromMonthChange = true)
            } else {
                cal.add(Calendar.MONTH, 1)
            }
        }

        binding.btnNextMonth.setOnClickListener {
            cal.add(Calendar.MONTH, 1)
            setUpCalendar(isFromMonthChange = true)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun getRangeStart(number: Int): Int {
        val rangeSize = 7
        return (number / rangeSize) * rangeSize
    }

    fun getRangeEnd(number: Int): Int {
        val rangeSize = 7
        return ((number / rangeSize) + 1) * rangeSize - 1
    }
}