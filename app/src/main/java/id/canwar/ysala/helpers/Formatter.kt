package id.canwar.ysala.helpers

import id.canwar.ysala.models.Eat
import java.text.SimpleDateFormat
import java.util.*

object Formatter {

    fun getTimeFromTime(date: Date): String {

        return SimpleDateFormat("HH:mm", Locale.US).format(date).toString()

    }

    fun getDateFromDate(date: Date): String {

        return SimpleDateFormat("dd MMMM yyyy", Locale.US).format(date).toString()

    }

    fun getCostEat(price: Eat, food: String, durationDay: Int): Int {

        val foods = food.split(" & ")

        var total = 0

        for (i in foods) {
            if (i == "Breakfast")
                total += price.breakfast
            else if (i == "Lunch")
                total += price.lunch
            else if (i == "Dinner")
                total += price.dinner
        }

        return total * durationDay

    }

    fun getCostHomestay(price: Int, durationDay: Int): Int {

        return price * durationDay

    }

    fun getBookingDuration(chekin: String, checkout: String): Int {

        val calChekin = chekin.split(" ")
        val dayChekin = calChekin[0].toInt()
        val stringMonthChekin = calChekin[1]
        val yearChekin = calChekin[2].toInt()

        val monthChekin = getMonthInt(stringMonthChekin)

        val calendarChekin = Calendar.getInstance().apply {
            set(yearChekin, monthChekin, dayChekin)
        }

        val calCheckout = checkout.split(" ")
        val dayCheckout = calCheckout[0].toInt()
        val stringMonthCheckout = calCheckout[1]
        val yearCheckout = calCheckout[2].toInt()

        val monthCheckout = getMonthInt(stringMonthCheckout)

        val calendarCheckout = Calendar.getInstance().apply {
            set(yearCheckout, monthCheckout, dayCheckout)
        }

        val diff = calendarCheckout.timeInMillis - calendarChekin.timeInMillis
        val diffDays = diff / (24 * 60 * 60 * 1000)


        return diffDays.toInt()

    }

    private fun getMonthInt(stringMonth: String): Int {
        return when (stringMonth) {
            "January" -> Calendar.JANUARY
            "February" -> Calendar.FEBRUARY
            "March" -> Calendar.MARCH
            "April" -> Calendar.APRIL
            "May" -> Calendar.MAY
            "June" -> Calendar.JUNE
            "July" -> Calendar.JULY
            "August" -> Calendar.AUGUST
            "September" -> Calendar.SEPTEMBER
            "November" -> Calendar.NOVEMBER
            else -> Calendar.DECEMBER
        }
    }

}