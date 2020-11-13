package id.canwar.ysala.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import id.canwar.ysala.R
import id.canwar.ysala.helpers.*
import id.canwar.ysala.models.Homestay
import kotlinx.android.synthetic.main.activity_details_homestay.*

class DetailsHomestayActivity : AppCompatActivity() {

    private var homestay: Homestay? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_homestay)

        initUI()
    }

    private fun initUI() {

        getDataIntent(intent.extras!!)

        tv_back.setOnClickListener {
            finish()
        }

        tv_booking.setOnClickListener {
            // Go to activity Booking
        }

    }

    private fun getDataIntent(bundle: Bundle) {

        val id = bundle.getString(HOMESTAY_ID)!!
        val name = bundle.getString(HOMESTAY_NAME)!!
        val image = bundle.getString(HOMESTAY_IMAGE)!!
        val address = bundle.getString(HOMESTAY_ADDRESS)!!
        val price = bundle.getInt(HOMESTAY_PRICE)

        homestay = Homestay(id, name, image, address, price)

        try {
            if (image != "") {
                Picasso.get().load(image).into(iv_homestay)
            }
        } catch (e: Exception) {

        }

        tv_name.text = name
        tv_price.text = "Rp. $price,00 / Night"
        tv_address.text = address

    }
}