package id.canwar.ysala.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.canwar.ysala.R

class ProfileFragment : Fragment() {

    lateinit var view: ViewGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        view = (inflater.inflate(R.layout.fragment_profile, container, false) as ViewGroup).apply {



        }


        return view
    }

}