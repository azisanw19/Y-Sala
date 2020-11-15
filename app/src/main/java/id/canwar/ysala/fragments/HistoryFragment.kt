package id.canwar.ysala.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.canwar.ysala.R
import id.canwar.ysala.adapters.HistoryAdapter
import id.canwar.ysala.helpers.FIREBASE_BOOKING
import id.canwar.ysala.helpers.FIREBASE_HOMESTAYS
import id.canwar.ysala.helpers.HOMESTAY_EAT_PRICE
import id.canwar.ysala.models.Booking
import id.canwar.ysala.models.Eat
import id.canwar.ysala.models.Homestay
import kotlinx.android.synthetic.main.fragment_history.view.*

class HistoryFragment : Fragment() {

    lateinit var view: ViewGroup

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        view = (inflater.inflate(R.layout.fragment_history, container, false) as ViewGroup).apply {

        }

        return view
    }

    override fun onStart() {
        super.onStart()

        val uid = firebaseAuth.uid!!

        retrieveDataBooking(uid)
    }

    private fun retrieveDataBooking(uid: String) {

        val databaseReference = firebaseDatabase.reference

        val query = databaseReference.child(FIREBASE_BOOKING).orderByChild("userId").equalTo(uid)

        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val bookings = ArrayList<Booking>()
                val homestays = ArrayList<Homestay>()
                val eats = ArrayList<Eat>()

                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val booking = dataSnapshot.getValue(Booking::class.java)!!
                        bookings.add(booking)

                        val refHomestay =
                            firebaseDatabase.getReference("$FIREBASE_HOMESTAYS/${booking.homestayId}")
                        refHomestay.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                val homestay = snapshot.getValue(Homestay::class.java)!!
                                homestays.add(homestay)

                                val refEat = firebaseDatabase.getReference("$FIREBASE_HOMESTAYS/${homestay.id}/$HOMESTAY_EAT_PRICE")
                                refEat.addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val eat = snapshot.getValue(Eat::class.java)!!
                                        eats.add(eat)

                                        if (bookings.size != 0 && bookings.size == homestays.size && bookings.size == eats.size && homestays.size == eats.size){
                                            val historyAdapter = HistoryAdapter(view.context, bookings, homestays, eats)
                                            view.rv_history.apply {
                                                layoutManager = LinearLayoutManager(context)
                                                adapter = historyAdapter
                                            }
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }

                                })
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

}