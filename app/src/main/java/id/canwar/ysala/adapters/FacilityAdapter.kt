package id.canwar.ysala.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.canwar.ysala.R

class FacilityAdapter(val context: Context, val facilities: ArrayList<String>) : RecyclerView.Adapter<FacilityAdapter.ViewHolder>() {


    open inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(facility: String) {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.facility_item_holder, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(facilities[position])
    }

    override fun getItemCount(): Int = facilities.size

}