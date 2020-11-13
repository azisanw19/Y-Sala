package id.canwar.ysala.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import id.canwar.ysala.R
import id.canwar.ysala.adapters.FacilityAdapter
import id.canwar.ysala.helpers.*
import id.canwar.ysala.models.Facility
import id.canwar.ysala.models.Homestay
import kotlinx.android.synthetic.main.activity_details_homestay.*

class DetailsHomestayActivity : AppCompatActivity() {

    private var homestay: Homestay? = null
    private val firebaseDatabase = FirebaseDatabase.getInstance()

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

        getDataFacilities()

    }

    private fun getDataFacilities() {

        val databaseReference = firebaseDatabase.getReference("$FIREBASE_FACILITIES/${homestay!!.id}")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val facilities = ArrayList<Facility>()

                for (dataSnapshot in snapshot.children) {
                    facilities.add(dataSnapshot.getValue(Facility::class.java)!!)
                }

                val facilityAdapter = FacilityAdapter(this@DetailsHomestayActivity, facilities)
                rv_facilities.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = facilityAdapter

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

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