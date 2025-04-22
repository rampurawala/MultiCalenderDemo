package com.app.multicalenderdemo.horizontal_calender

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.app.multicalenderdemo.R

class EventCalendarAdapter(
    private val context: Context,
    private val list: ArrayList<CalendarDateModel>,
    private val listener: (calendarDateModel: CalendarDateModel, position: Int) -> Unit
) :
    RecyclerView.Adapter<EventCalendarAdapter.MyViewHolder>() {
    // private val list = ArrayList<CalendarDateModel>()
    private val itemWidth: Int
        get() {
            val displayMetrics = context.resources.displayMetrics
            return displayMetrics.widthPixels / 7
        }
    var selectedDatePosition = -1

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(calendarDateModel: CalendarDateModel) {
            // val calendarDay = itemView.findViewById<TextView>(R.id.tv_calendar_day)
            val calendarDate = itemView.findViewById<TextView>(R.id.tv_calendar_date)
            val rootBg = itemView.findViewById<ConstraintLayout>(R.id.rootBg)
            val eventAvailableBg = itemView.findViewById<View>(R.id.eventAvailable)
            // val cardView = itemView.findViewById<CardView>(R.id.card_calendar)
            eventAvailableBg.isVisible = calendarDateModel.hasEvent == true
            if (calendarDateModel.isSelected) {
                /*  calendarDay.setTextColor(
                      ContextCompat.getColor(
                          itemView.context,
                          R.color.white
                      )
                  )*/
                calendarDate.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.white
                    )
                )
                eventAvailableBg.background =
                    (ContextCompat.getDrawable(itemView.context, R.drawable.bg_rounded_5_white))
                calendarDate.background =
                    (ContextCompat.getDrawable(itemView.context, R.drawable.bg_circle_theme))
                // rootBg.setBackgroundResource(itemView.context.resources.getDrawable(R.drawable.bg_circle_theme))
                /* cardView.setCardBackgroundColor(
                     ContextCompat.getColor(
                         itemView.context,
                         R.color.colorPrimary
                     )
                 )*/
                setSelectedPosition(position = layoutPosition)
            } else {
                /* calendarDay.setTextColor(
                     ContextCompat.getColor(
                         itemView.context,
                         R.color.black
                     )
                 )*/
                calendarDate.background =
                    (ContextCompat.getDrawable(itemView.context, R.drawable.bg_circle_transparent))

                calendarDate.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.black
                    )
                )
                eventAvailableBg.background =
                    (ContextCompat.getDrawable(itemView.context, R.drawable.bg_circle_theme))

                /* cardView.setCardBackgroundColor(
                     ContextCompat.getColor(
                         itemView.context,
                         R.color.white
                     )
                 )*/
            }

            // calendarDay.text = calendarDateModel.calendarDay
            calendarDate.text = calendarDateModel.calendarDate
            rootBg.setOnClickListener {
                if (calendarDateModel.data.time < getCurrentDateMillis()) {
                } else {
                    listener.invoke(calendarDateModel, layoutPosition)
                }
            }
        }

    }

    fun setSelectedPosition(position: Int) {
        selectedDatePosition = position
    }

    fun getDateSelectedPosition(): Int {
        return selectedDatePosition
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_calendar_date, parent, false)
        view.layoutParams = ViewGroup.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(calendarList: ArrayList<CalendarDateModel>) {
        list.clear()
        list.addAll(calendarList)
        notifyDataSetChanged()
    }
}
