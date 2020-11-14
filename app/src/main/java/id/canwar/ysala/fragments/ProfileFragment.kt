package id.canwar.ysala.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.canwar.ysala.R
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {

    private lateinit var view: ViewGroup
    lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = (inflater.inflate(R.layout.fragment_profile, container, false) as ViewGroup).apply {

        }

        return view
    }

    override fun onStart() {
        super.onStart()
        loadProfileData(view)
    }

    private fun loadProfileData(view: View) {
        //val us=FirebaseDatabase.getInstance().get
        //val user=auth.currentUser
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

    }
}