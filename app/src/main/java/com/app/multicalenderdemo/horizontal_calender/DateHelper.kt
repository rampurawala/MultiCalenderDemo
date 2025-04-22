package com.app.multicalenderdemo.horizontal_calender

import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Locale

const val LOCAL_DATE_TIME_FORMAT = "dd MMM yyyy, hh:mm aa"

fun getCurrentDateMillis(): Long {
    val cal = Calendar.getInstance()
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.HOUR_OF_DAY, 0)
    return cal.timeInMillis
}

fun getMonth(calendar: Calendar): String? {
    return DateFormatSymbols().months[calendar.get(Calendar.MONTH)]
}
fun isToday(calendar: Calendar): Boolean {
    val today = Calendar.getInstance()
    return calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
            calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)
}
fun removePastDates(dates: ArrayList<CalendarDateModel>): ArrayList<CalendarDateModel> {
    val today = Calendar.getInstance()
    today.set(Calendar.HOUR_OF_DAY, 0)
    today.set(Calendar.MINUTE, 0)
    today.set(Calendar.SECOND, 0)
    today.set(Calendar.MILLISECOND, 0)

    return dates.filter { !it.data.before(today.time) }.toCollection(arrayListOf())
}

fun formatCalendar(milisecond: Long=Calendar.getInstance().timeInMillis, dateTimeFormat: String?): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = milisecond
    val simpleDateFormat =
        SimpleDateFormat(dateTimeFormat, Locale.getDefault())
    return simpleDateFormat.format(calendar.time)
}
fun formatToLocalDateTime(date: Long) =
    formatCalendar(date, LOCAL_DATE_TIME_FORMAT)