package id.canwar.ysala.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.canwar.ysala.R
import id.canwar.ysala.activities.DetailsPaymentActivity
import id.canwar.ysala.helpers.*
import id.canwar.ysala.models.Booking
import id.canwar.ysala.models.Eat
import id.canwar.ysala.models.Homestay
import kotlinx.android.synthetic.main.fragment_history_item_holder.view.*

class HistoryAdapter(val context: Context, val bookings: ArrayList<Booking>, val homestays: ArrayList<Homestay>, val eats: ArrayList<Eat>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    open inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(booking: Booking, homestay: Homestay, eat: Eat) {

            val chekin = Formatter.getDateFromDate(booking.timeCheckIn)
            val checkout = Formatter.getDateFromDate(booking.timeCheckOut)
            val timePick = Formatter.getTimeFromTime(booking.timeCheckIn)

            view.history_placeholder.setOnClickListener{

                val intent = Intent(it.context, DetailsPaymentActivity::class.java).apply {
                    val bundle = Bundle().apply {
                        putExtra(HOMESTAY_ID, homestay.id)
                        putExtra(HOMESTAY_NAME, homestay.name)
                        putExtra(HOMESTAY_IMAGE, homestay.image)
                        putExtra(HOMESTAY_ADDRESS, homestay.address)
                        putExtra(HOMESTAY_PRICE, homestay.price)
                        putExtra(HOMESTAY_TELEPHONE, homestay.telephone)

                        putExtra(EAT_BREAKFAST, eat.breakfast)
                        putExtra(EAT_LUNCH, eat.lunch)
                        putExtra(EAT_DINNER, eat.dinner)

                        putExtra(BOOKING_ID, booking.id)
                        putExtra(BOOKING_TIME, booking.timeOrder)
                        putExtra(BOOKING_CHEKIN, chekin)
                        putExtra(BOOKING_CHECKOUT, checkout)
                        putExtra(BOOKING_TIME_PICK, timePick)
                        putExtra(BOOKING_EAT, booking.eat)
                        putExtra(BOOKING_PEOPLE, booking.numberOfPeople)
                        putExtra(BOOKING_LOCATION, booking.locationPickUp)
                        putExtra(BOOKING_PAYMENT_METHOD, booking.paymentMethod)
                    }

                    putExtras(bundle)
                }

                view.context.startActivity(intent)
            }

            try {
                if (homestay.image != "") {
                    Picasso.get().load(homestay.image).into(view.iv_homestay)
                }
            } catch (e: Exception) {

            }

            view.tv_name.text = homestay.name
            view.tv_booking_date.text = "$chekin - $checkout"

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.fragment_history_item_holder, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(bookings[position], homestays[position], eats[position])
    }

    override fun getItemCount(): Int = bookings.size

}