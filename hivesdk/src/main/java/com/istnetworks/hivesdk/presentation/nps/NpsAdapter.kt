package com.istnetworks.hivesdk.presentation.nps

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.istnetworks.hivesdk.R


var row_index: Int = -1

class NpsAdapter(
    private val npsItems: List<NpsModel>,
    private val onClick :(selectedValue:Int?)-> Unit
    ) : RecyclerView.Adapter<NpsViewHolder>() {


    override fun onCreateViewHolder(parentView: ViewGroup, p1: Int): NpsViewHolder {
        return LayoutInflater
            .from(parentView.context)
            .inflate(R.layout.nps_item, parentView, false)
            .let { NpsViewHolder(it) }
    }

    override fun onBindViewHolder(viewHolder: NpsViewHolder, position: Int) {
        viewHolder.bind(npsItems[position])
        viewHolder.itemView.setOnClickListener {
            row_index = position
            notifyDataSetChanged()
            onClick(npsItems[position].npsText?.toInt())
        }
    }

    override fun getItemCount() = npsItems.size

}

class NpsViewHolder(private val view: View) :
    RecyclerView.ViewHolder(view) {

    private val tvTitle by lazy { view.findViewById<TextView>(R.id.tvNps) }

    fun bind(npsItem: NpsModel) {
        tvTitle.text = npsItem.npsText ?: ""
        val lp: ConstraintLayout.LayoutParams =
            tvTitle.layoutParams as ConstraintLayout.LayoutParams
        val ratio = view.resources.displayMetrics.density


        lp.width = (npsItem.npsUnselectedWidth!! * ratio).toInt()
        lp.height = (npsItem.npsUnselectedHeight!! * ratio).toInt()
        tvTitle.layoutParams = lp
        tvTitle.background
            .setColorFilter(Color.parseColor(npsItem.npsBackgroundColor), PorterDuff.Mode.SRC_ATOP)

        if (row_index == adapterPosition) {

            lp.width = (npsItem.npsSelectedWidth!! * ratio).toInt()
            lp.height = (npsItem.npsSelectedHeight!! * ratio).toInt()

            tvTitle.layoutParams = lp
        } else {
            lp.width = (npsItem.npsUnselectedWidth!! * ratio).toInt()
            lp.height = (npsItem.npsUnselectedHeight!! * ratio).toInt()
            tvTitle.layoutParams = lp
        }

    }


}


