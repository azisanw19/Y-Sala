package id.canwar.ysala.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.squareup.picasso.Picasso
import id.canwar.ysala.R
import id.canwar.ysala.helpers.*
import kotlinx.android.synthetic.main.activity_booking.*
import java.text.SimpleDateFormat
import java.util.*

class BookingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        initUI()
    }

    private fun initUI() {

        getDataIntent(intent.extras!!)

        val calendar = Calendar.getInstance()
        val date = SimpleDateFormat("dd MMMM yyyy", Locale.US).format(calendar.time)

        tv_chekin.text = date

        tv_chekin.setOnClickListener {
            setupDatePicker(tv_chekin)
        }

        tv_checkout.text = date

        tv_checkout.setOnClickListener {
            setupDatePicker(tv_checkout)
        }

        if (et_pick_up_location.text.toString() != "") {
            tv_time_text.visibility = View.GONE
            tv_time.visibility = View.GONE
        } else {
            tv_time_text.visibility = View.VISIBLE
            tv_time.visibility = View.VISIBLE
        }

        val time = SimpleDateFormat("HH:mm", Locale.US).format(calendar.time)
        tv_time.text = time

        tv_time.setOnClickListener {
            setupTimePicker(tv_time)
        }

        tv_payment.setOnClickListener {
            val payments = resources.getStringArray(R.array.payment_method)
            setupDialog(payments, tv_payment)
        }

        // Add logika hitung harga
    }

    private fun setupDialog(array: Array<String>, textView: TextView) {

        val choice = array.indexOf(textView.text)

        AlertDialog.Builder(this, R.style.DialogTheme)
                .setSingleChoiceItems(array, choice) { dialog, which ->
                    textView.text = array[which]
                    dialog.dismiss()
                }
                .create()
                .show()

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