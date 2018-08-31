package com.treatout.travel.treatoutmobile.Adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.treatout.travel.treatoutmobile.Classes.Place
import com.treatout.travel.treatoutmobile.PlaceActivity
import com.treatout.travel.treatoutmobile.R
import kotlinx.android.synthetic.main.list_row.view.*

class PlacesRecAdapter ( val context: Context,val placeList: ArrayList<Place>): RecyclerView.Adapter<CustomViewHolder>() {

    private val mContext: Context? = null
    override fun onBindViewHolder(p0: CustomViewHolder, p1: Int) {

        val place =  placeList[ p1 ]
        p0.itemView.title.text = place.name
        p0.itemView.address.text = place.address

        if ( place.rating == 5.0){
            p0.itemView.star.text = "★★★★★"
        }else if ( place.rating == 4.0){
            p0.itemView.star.text = "★★★★✰"
        }else if ( place.rating == 3.0){
            p0.itemView.star.text = "★★★✰✰"
        }else if ( place.rating == 2.0){
            p0.itemView.star.text = "★★✰✰✰"
        }else if ( place.rating == 1.0){
            p0.itemView.star.text = "★✰✰✰✰"
        }else {
            p0.itemView.star.text = "✰✰✰✰✰"
        }

//        for( image in place.image){
//
//            println( image )
//
//            val newImage = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=$image"
////            val view = p0.itemView.imageView
//            Picasso.get().load(newImage).into(p0.itemView.imageView)
//            break
//
//        }

        p0.itemView.setOnClickListener{
            val intent = Intent(context, PlaceActivity::class.java)
            intent.putExtra("name",  place.name)
            intent.putExtra("address",  place.address)
            intent.putExtra("rating",  place.rating.toString())
            intent.putExtra("id",  place.id)
            context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(p0?.context)
        val cellForRow = layoutInflater.inflate(R.layout.list_row, p0, false)
        return CustomViewHolder(cellForRow)
    }

}

class CustomViewHolder(v: View): RecyclerView.ViewHolder(v) {

}

