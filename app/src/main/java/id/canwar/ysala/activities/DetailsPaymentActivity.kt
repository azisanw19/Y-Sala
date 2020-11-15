package id.canwar.ysala.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import id.canwar.ysala.R
import id.canwar.ysala.helpers.*
import id.canwar.ysala.helpers.Formatter
import id.canwar.ysala.models.Eat
import id.canwar.ysala.models.Payment
import id.canwar.ysala.models.User
import kotlinx.android.synthetic.main.activity_details_payment.*
import kotlinx.android.synthetic.main.fragment_homestay_item_holder.view.*
import java.util.*

class DetailsPaymentActivity : AppCompatActivity() {

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_payment)

        initUI()

    }

    override fun onStart() {
        super.onStart()

        getDataUser()
    }

    private fun getDataUser() {

        val uid =  firebaseAuth.currentUser!!.uid

        val databaseReference = firebaseDatabase.getReference("$FIREBASE_USERS/$uid")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                user = snapshot.getValue(User::class.java)

                tv_name.text = ": ${user!!.fullName}"
                tv_phone.text = ": ${user!!.phone}"

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    private fun initUI() {

        val bundle = intent.extras!!

        tv_back.setOnClickListener {
            finish()
        }

        val bookingId = bundle.getString(BOOKING_ID)
        tv_booking_id.text = "ID: ${bookingId}"

        try {
            val image = bundle.getString(HOMESTAY_IMAGE)
            if (image != "") {
                Picasso.get().load(image).into(iv_homestay)
            }
        } catch (e: Exception) {

        }

        tv_homestay_name.text = bundle.getString(HOMESTAY_NAME)
        tv_homestay_address.text = bundle.getString(HOMESTAY_ADDRESS)
        tv_homestay_telephone.text = bundle.getString(HOMESTAY_TELEPHONE)

        tv_booking_checkin.text = bundle.getString(BOOKING_CHEKIN)
        tv_booking_checkout.text = bundle.getString(BOOKING_CHECKOUT)

        val duration = Formatter.getBookingDuration(bundle.getString(BOOKING_CHEKIN)!!, bundle.getString(BOOKING_CHECKOUT)!!)
        val costHomestay = Formatter.getCostHomestay(bundle.getInt(HOMESTAY_PRICE), duration)
        tv_duration.text = "${duration} nights"
        tv_cost_homestay.text = "Rp. ${costHomestay},00"

        val location = bundle.getString(BOOKING_LOCATION)
        if (location == "" || location == null) {
            ll_pick_service.visibility = View.GONE
            ll_time_pick.visibility = View.GONE
            ll_pick_separator.visibility = View.GONE
            ll_pick_service_total.visibility = View.GONE
        } else {
            ll_pick_service.visibility = View.VISIBLE
            ll_time_pick.visibility = View.VISIBLE
            ll_pick_separator.visibility = View.VISIBLE
            ll_pick_service_total.visibility = View.VISIBLE
            tv_booking_pick_location.text = location
            tv_booking_time_pick.text = "${bundle.getString(BOOKING_CHEKIN)} ${bundle.getString(BOOKING_TIME)} WIB"
        }

        val eatOrder = bundle.getString(BOOKING_EAT)
        val person = bundle.getInt(BOOKING_PEOPLE)
        val eat = Eat(bundle.getInt(EAT_BREAKFAST), bundle.getInt(EAT_LUNCH), bundle.getInt(EAT_DINNER))
        var costEat = 0
        if (eatOrder == null) {
            ll_booking_eat_order.visibility = View.GONE
            ll_booking_eat_separator.visibility = View.GONE
            ll_booking_eat_total.visibility = View.GONE
        } else {
            ll_booking_eat_order.visibility = View.VISIBLE
            ll_booking_eat_separator.visibility = View.VISIBLE
            ll_booking_eat_total.visibility = View.VISIBLE
            tv_eat_order.text = "$eatOrder for ${person} persons"
            costEat = Formatter.getCostEat(eat, eatOrder, duration)
            tv_booking_eat_total.text = "Rp. ${eatOrder},00"
        }

        tv_payment_method.text = bundle.getString(BOOKING_PAYMENT_METHOD)

        val total = costHomestay + costEat
        tv_payment_total.text = "Rp. $total,00"

        val dp = total/ DP_PERCENT
        tv_dp_payment.text = "Rp. $dp,00"

        btn_pay_dp.setOnClickListener{
            pushDataPayment(bookingId!!, total, dp)
            finish()
        }

        btn_pay_total.setOnClickListener{
            pushDataPayment(bookingId!!, total, total)
        }
    }

    private fun pushDataPayment(id: String, total: Int, beenPay: Int) {

        val calendar = Calendar.getInstance()

        val payment = Payment(id, calendar.time, total, beenPay)

        val databaseReference = firebaseDatabase.getReference(FIREBASE_PAYMENT)

        databaseReference.child(id).setValue(payment)

    }
}