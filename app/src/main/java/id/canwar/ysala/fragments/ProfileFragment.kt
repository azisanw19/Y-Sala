package id.canwar.ysala.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.canwar.ysala.R
import id.canwar.ysala.helpers.FIREBASE_USERS
import id.canwar.ysala.models.User
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {

    private lateinit var view: ViewGroup
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var user: User? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = (inflater.inflate(R.layout.fragment_profile, container, false) as ViewGroup).apply {

        }

        return view
    }

    override fun onStart() {
        super.onStart()
        loadProfileData(view)
        //Default State:
        view.edit_name.visibility=View.GONE
        view.edit_email.visibility=View.GONE
        view.edit_phone.visibility=View.GONE
        view.confirm_info_edit.visibility=View.GONE

        //To change profile Pic
        view.profile_profilepic.setOnClickListener{
            //choose picture
            //Send to Storage
        }

        //To change user info(Only enable edit icon)
        view.profile_change_info.setOnClickListener{
            view.profile_change_info.visibility=View.GONE
            view.profile_change_text.visibility=View.GONE

            view.edit_name.visibility=View.VISIBLE
            view.edit_email.visibility=View.VISIBLE
            view.edit_phone.visibility=View.VISIBLE
            view.confirm_info_edit.visibility=View.VISIBLE
        }
        //To confirm user info change
        view.confirm_info_edit.setOnClickListener {
            //send to DB and update profile UI


            //Default State:
            view.profile_change_info.visibility=View.VISIBLE
            view.profile_change_text.visibility=View.VISIBLE
            view.edit_name.visibility=View.GONE
            view.edit_email.visibility=View.GONE
            view.edit_phone.visibility=View.GONE
            view.confirm_info_edit.visibility=View.GONE
        }

        //To sign out
        view.profile_logout.setOnClickListener{
            //Sign Out
        }
        //Addition:


    }

    private fun showImageOptionDialogue(){
        //TODO: Not yet Implemented
    }

    private fun loadProfileData(view: View) {
        val uid =  firebaseAuth.currentUser!!.uid
        val databaseReference = firebaseDatabase.getReference("$FIREBASE_USERS/$uid")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                user = snapshot.getValue(User::class.java)

                view.profile_fullname.text = "${user!!.fullName}"
                view.profile_emailaddress.text="${user!!.email}"
                view.profile_phonenumber.text = "${user!!.phone}"

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    /*
       val user=Firebase.auth.currentUser

        user?.let {
            view.profile_fullname.text = user.displayName
            view.profile_emailaddress.text=user.email
            view.profile_phonenumber.text=user.phoneNumber





            //TODO:Not Implemented Yet
            /*
            if (user.image != "") {
                Picasso.get().load(user.image).into(view.profile_profilepic)
            }
            */
    }
*/
    }
}