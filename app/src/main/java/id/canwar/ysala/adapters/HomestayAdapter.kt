package id.canwar.ysala.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.canwar.ysala.R
import id.canwar.ysala.activities.DetailsHomestayActivity
import id.canwar.ysala.helpers.*
import id.canwar.ysala.models.Homestay
import kotlinx.android.synthetic.main.fragment_homestay_item_holder.view.*

class HomestayAdapter(val context: Context, val homestays: ArrayList<Homestay>) : RecyclerView.Adapter<HomestayAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.fragment_homestay_item_holder, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(homestays[position])
    }

    override fun getItemCount(): Int = homestays.size

    open inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(homestay: Homestay) {
            view.tv_name.text = homestay.name
            view.tv_price.text = "$RUPIAH${homestay.price}$END_RUPIAH"
            view.tv_address.text = homestay.address

            try {
                if (homestay.image != "") {
                    Picasso.get().load(homestay.image).into(view.iv_homestay)
                }
            } catch (e: Exception) {

            }


            view.homestay_placeholder.setOnClickListener {

                Intent(it.context, DetailsHomestayActivity::class.java).apply {
                    val bundle = Bundle().apply {
                        putExtra(HOMESTAY_ID, homestay.id)
                        putExtra(HOMESTAY_NAME, homestay.name)
                        putExtra(HOMESTAY_IMAGE, homestay.image)
                        putExtra(HOMESTAY_ADDRESS, homestay.address)
                        putExtra(HOMESTAY_PRICE, homestay.price)
                        putExtra(HOMESTAY_TELEPHONE, homestay.telephone)
                    }

                    putExtras(bundle)

                    view.context.startActivity(this)
                }
            }
        }

    }

}