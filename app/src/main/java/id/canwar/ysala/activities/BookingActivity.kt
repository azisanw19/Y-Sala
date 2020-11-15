package id.canwar.ysala.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import id.canwar.ysala.R
import id.canwar.ysala.helpers.*
import id.canwar.ysala.models.Booking
import id.canwar.ysala.models.Eat
import id.canwar.ysala.models.Homestay
import kotlinx.android.synthetic.main.activity_booking.*
import kotlinx.android.synthetic.main.activity_booking.iv_homestay
import kotlinx.android.synthetic.main.activity_booking.tv_address
import kotlinx.android.synthetic.main.activity_booking.tv_name
import kotlinx.android.synthetic.main.activity_booking.tv_price
import java.text.SimpleDateFormat
import java.util.*

class BookingActivity : AppCompatActivity() {

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()

    private var homestay: Homestay? = null
    private var eatPrice: Eat? = null
    private var total = 0
    private var dp = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        initUI()
    }

    private fun initUI() {

        getDataIntent(intent.extras!!)
        getEatPrice()

        tv_back.setOnClickListener {
            finish()
        }

        val calendar = Calendar.getInstance()
        val date = SimpleDateFormat("dd MMMM yyyy", Locale.US).format(calendar.time)

        tv_chekin.text = date

        tv_total_payment.text = "Rp. 0,00"
        tv_dp_payment.text = "Rp. 0,00"

        tv_chekin.setOnClickListener {
            setupDatePicker(tv_chekin)
        }

        tv_checkout.text = date

        tv_checkout.setOnClickListener {
            setupDatePicker(tv_checkout)
        }

        tv_time_text.visibility = View.GONE
        tv_time.visibility = View.GONE

        et_pick_up_location.addTextChangedListener {
            if (et_pick_up_location.text.toString() == "") {
                tv_time_text.visibility = View.GONE
                tv_time.visibility = View.GONE
            } else {
                tv_time_text.visibility = View.VISIBLE
                tv_time.visibility = View.VISIBLE
            }
        }

        val time = SimpleDateFormat("HH:mm", Locale.US).format(calendar.time)
        tv_time.text = time

        tv_time.setOnClickListener {
            setupTimePicker(tv_time)
        }

        tv_payment.setOnClickListener {
            val payments = resources.getStringArray(R.array.payment_method)
            setupDialogSingleChoice(payments, tv_payment)
        }

        tv_eat.setOnClickListener {
            val eats = arrayOf("Breakfast Rp. ${eatPrice?.breakfast}", "Lunch Rp. ${eatPrice?.lunch}", "Dinner Rp. ${eatPrice?.dinner}")
            setupDialogMultipleChoice(eats, tv_eat)
        }

        btn_booking.setOnClickListener{
            val booking = pushDataBooking()
            // intent ke Details pembayaran ada pilihan bayar DP atau bayar total
            val intent = Intent(this, DetailsPaymentActivity::class.java).apply {
                val bundle = Bundle().apply {

                    putExtra(HOMESTAY_ID, homestay!!.id)
                    putExtra(HOMESTAY_NAME, homestay!!.name)
                    putExtra(HOMESTAY_IMAGE, homestay!!.image)
                    putExtra(HOMESTAY_ADDRESS, homestay!!.address)
                    putExtra(HOMESTAY_PRICE, homestay!!.price)
                    putExtra(HOMESTAY_TELEPHONE, homestay!!.telephone)

                    putExtra(EAT_BREAKFAST, eatPrice!!.breakfast)
                    putExtra(EAT_LUNCH, eatPrice!!.lunch)
                    putExtra(EAT_DINNER, eatPrice!!.dinner)

                    putExtra(BOOKING_ID, booking.id)
                    putExtra(BOOKING_TIME, booking.timeOrder)
                    putExtra(BOOKING_CHEKIN, tv_chekin.text.toString())
                    putExtra(BOOKING_CHECKOUT, tv_checkout.text.toString())
                    putExtra(BOOKING_TIME_PICK, tv_time.text.toString())
                    putExtra(BOOKING_EAT, booking.eat)
                    putExtra(BOOKING_PEOPLE, booking.numberOfPeople)
                    putExtra(BOOKING_LOCATION, booking.locationPickUp)
                    putExtra(BOOKING_PAYMENT_METHOD, booking.paymentMethod)
                }

                putExtras(bundle)
            }
            startActivity(intent)
            finish()

        }

    }

    private fun pushDataBooking(): Booking {

        val databaseReferenceBooking = firebaseDatabase.getReference(FIREBASE_BOOKING)
        val calendarOrder = Calendar.getInstance()

        val chekIn = tv_chekin.text.toString().split(" ")
        val timeIn = tv_time.text.toString().split(":")
        val dayIn = chekIn[0].toInt()
        val monthIn = getMonthInt(chekIn[1])
        val yearIn = chekIn[2].toInt()
        val hourIn = timeIn[0].toInt()
        val minuteIn = timeIn[1].toInt()

        val calendar = Calendar.getInstance().apply {
            set(yearIn, monthIn, dayIn, hourIn, minuteIn)
        }

        val checkout = tv_checkout.text.toString().split(" ")
        val dayOut = checkout[0].toInt()
        val monthOut = getMonthInt(checkout[1])
        val yearOut = checkout[2].toInt()
        calendar.set(yearOut, monthOut, dayOut)

        val uid = databaseReferenceBooking.key!!
        val homestayID = homestay!!.id
        val userId = firebaseAuth.currentUser!!.uid
        val timerOrder = calendarOrder.time
        // Waktu chekin adalah waktu penjemputan jika ada lokasi penjemputan
        val timeChekin = calendar.time
        val timeCheckout = calendar.time
        var eat: String? = tv_eat.toString()
        if (eat == resources.getString(R.string.select_package))
            eat = null
        var peopleString = et_people.text.toString()
        val people = if (peopleString == "") 0 else peopleString.toInt()
        var locationPickUp: String? = et_pick_up_location.text.toString()
        if (locationPickUp == "")
            locationPickUp = null
        val paymentMethod = tv_payment.text.toString()


        val booking = Booking(uid, homestayID, userId, timerOrder, timeChekin, timeCheckout, eat, people, locationPickUp, paymentMethod)

        databaseReferenceBooking.child(uid).setValue(booking)

        return booking

    }

    private fun calculatePayment(durationBooking: Int, eatTotalOneDay: Int){

        val numberOfPerson = if (et_people.text.toString() == "") 0 else et_people.text.toString().toInt()

        val homestayPrice = durationBooking * homestay!!.price
        val eatTotalPrice = eatTotalOneDay * durationBooking * numberOfPerson

        total = homestayPrice + eatTotalPrice
        dp = total / DP_PERCENT

        tv_dp_payment.text = "DP: Rp. $dp,00"
        tv_total_payment.text = "Rp. $total,00"
    }

    private fun calculateEatPrice(): Int {

        val eat = tv_eat.text.toString().split(" & ")

        var total = 0

        for (i in eat) {
            total += when (i) {
                "Breakfast" -> eatPrice?.breakfast ?: 0
                "Lunch" -> eatPrice?.lunch ?: 0
                "Dinner" -> eatPrice?.dinner ?: 0
                else -> 0
            }
        }

        return total

    }

    private fun getEatPrice() {

        val databaseReference = firebaseDatabase.getReference("$FIREBASE_HOMESTAYS/${homestay!!.id}/$HOMESTAY_EAT_PRICE")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                eatPrice = snapshot.getValue(Eat::class.java)

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
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

    private fun getBookingDuration(chekin: String, checkout: String): Int {

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

    private fun setupDialogSingleChoice(array: Array<String>, textView: TextView) {

        val choice = array.indexOf(textView.text)

        AlertDialog.Builder(this, R.style.DialogTheme)
                .setSingleChoiceItems(array, choice) { dialog, which ->
                    textView.text = array[which]
                    dialog.dismiss()
                }
                .create()
                .show()

    }

    private fun setupDialogMultipleChoice(array: Array<String>, textView: TextView) {

        val checkedArray = getCheckedBoolean(textView.text.toString())

        AlertDialog.Builder(this, R.style.DialogTheme)
                .setMultiChoiceItems(array, checkedArray) {dialog, which, isChecked ->
                    checkedArray[which] = isChecked
                }
                .setPositiveButton("Ok") { dialog, which ->
                    var eat = ""
                    for (i in array.indices) {

                        if (checkedArray[i]) {
                            try {
                                val temp = array[i].split(" Rp. ")[0]
                                eat += if (eat == "")
                                    temp
                                else
                                    " & $temp"
                            }
                            catch (e: Exception) {
                                Log.e("Error", e.toString())
                            }
                        }

                    }
                    textView.text = eat
                    calculatePayment(getBookingDuration(tv_chekin.text.toString(), tv_checkout.text.toString()), calculateEatPrice())
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .create()
                .show()
    }

    private fun getCheckedBoolean(eat: String): BooleanArray {

        val array = arrayOf("Breakfast, Lunch, Dinner")

        val eats = eat.split(" & ")
        val checkedArray = booleanArrayOf(false, false, false)
        for (i in eats) {
            for (j in array.indices) {
                if (i == array[j]) {
                    checkedArray[j] = true
                }
            }
        }

        return checkedArray

    }

    private fun setupTimePicker(textView: TextView) {

        val calendar = Calendar.getInstance()
        calendar.time = SimpleDateFormat("HH:mm", Locale.US).parse(textView.text.toString())
        val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calendar.apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
            }
            textView.text = SimpleDateFormat("HH:mm", Locale.US).format(calendar.time)
        }

        TimePickerDialog(this, R.style.DialogTheme, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
    }

    private fun setupDatePicker(textView: TextView) {

        val calendar = Calendar.getInstance()
        calendar.time = SimpleDateFormat("dd MMMM yyyy", Locale.US).parse(textView.text.toString())
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.apply {
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
                set(Calendar.MONTH, month)
                set(Calendar.YEAR, year)
            }
            textView.text = SimpleDateFormat("dd MMMM yyyy", Locale.US).format(calendar.time)
            calculatePayment(getBookingDuration(tv_chekin.text.toString(), tv_checkout.text.toString()), calculateEatPrice())
        }

        DatePickerDialog(this, R.style.DialogTheme, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()

    }

    private fun getDataIntent(bundle: Bundle) {

        val id = bundle.getString(HOMESTAY_ID)!!
        val name = bundle.getString(HOMESTAY_NAME)!!
        val image = bundle.getString(HOMESTAY_IMAGE)!!
        val address = bundle.getString(HOMESTAY_ADDRESS)!!
        val price = bundle.getInt(HOMESTAY_PRICE)
        val telephone = bundle.getString(HOMESTAY_TELEPHONE)!!

        homestay = Homestay(id, name, image, address, price, telephone)

        try {
            if (image != "") {
                Picasso.get().load(image).into(iv_homestay)
            }
        } catch (e: Exception) {

        }

        tv_name.text = name
        tv_address.text = address
        tv_telephone.text = telephone
        tv_price.text = "Rp. $price,00 / Night"

    }
}