package id.canwar.ysala.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.canwar.ysala.R
import id.canwar.ysala.adapters.HomestayAdapter
import id.canwar.ysala.helpers.FIREBASE_HOMESTAYS
import id.canwar.ysala.models.Homestay
import kotlinx.android.synthetic.main.fragment_homestay.view.*
import java.time.LocalDate

class HomestayFragment : Fragment() {

    lateinit var view: ViewGroup
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        view = (inflater.inflate(R.layout.fragment_homestay, container, false) as ViewGroup).apply {

            initRecyclerView(this)

        }

        return view

    }

    private fun initRecyclerView(view: View) {

        val homestays = ArrayList<Homestay>()

        val databaseReference = firebaseDatabase.getReference(FIREBASE_HOMESTAYS)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e("homestay snapshot", "${snapshot.children.count()}")
                for (dataSnapshot in snapshot.children) {
                    val homestay = dataSnapshot.getValue(Homestay::class.java)!!

                    homestays.add(homestay)
                    val homestayAdapter = HomestayAdapter(view.context, homestays)
                    view.rv_homestay.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = homestayAdapter
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}