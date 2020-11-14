package id.canwar.ysala.activities

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
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