package com.treatout.travel.treatoutmobile.Adapters

import android.content.Context
import android.opengl.Visibility
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.treatout.travel.treatoutmobile.Classes.Review
import com.treatout.travel.treatoutmobile.R
import kotlinx.android.synthetic.main.review_row.view.*

class ReviewAdapter ( val context: Context,val reviewList: ArrayList<Review>): RecyclerView.Adapter<CustomViewHolder>() {

    private val mContext: Context? = null
    override fun onBindViewHolder(p0: CustomViewHolder, p1: Int) {

        val review = reviewList[ p1 ]
        p0.itemView.user.text = review.name
        p0.itemView.comment.text = review.comment

        p0.itemView.reviewStar.rating = review.rating.toFloat()

        if(review.rating.toFloat() < 1.0){
            p0.itemView.reviewStar.visibility = View.GONE
        }



    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(p0?.context)
        val cellForRow = layoutInflater.inflate(R.layout.review_row, p0, false)
        return CustomViewHolder(cellForRow)
    }

}
