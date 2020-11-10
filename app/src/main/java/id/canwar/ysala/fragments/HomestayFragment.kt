package id.canwar.ysala.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.canwar.ysala.R

class HomestayFragment : Fragment() {

    lateinit var view: ViewGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        view = (inflater.inflate(R.layout.fragment_homestay, container, false) as ViewGroup).apply {



        }

        return view

    }

}