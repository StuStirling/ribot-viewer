package com.stustirling.ribotviewer.allribot

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.stustirling.ribotviewer.R
import com.stustirling.ribotviewer.domain.model.Ribot

/**
 * Created by Stu Stirling on 24/09/2017.
 */
class AllRibotAdapter : RecyclerView.Adapter<AllRibotAdapter.ViewHolder> () {

    var items : List<Ribot> = emptyList()
    set(value) {
        field = value
        notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.content_all_ribot,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind( items[position] )

    override fun getItemCount(): Int  = items.size

    class ViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val avatar = itemView.findViewById<ImageView>(R.id.civ_alc_avatar)
        private val name = itemView.findViewById<TextView>(R.id.tv_alc_name)

        fun bind(ribot:Ribot) {
            if ( ribot.avatar != null ) {
                Glide.with(avatar.context)
                        .load(ribot.avatar)
                        .apply(RequestOptions.fitCenterTransform()
                                .placeholder(R.drawable.small_ribot_logo))
                        .into(avatar)
            } else {
                avatar.setImageResource(R.drawable.small_ribot_logo)
            }
            name.text = avatar.context.getString(
                    R.string.full_name_format,ribot.firstName,ribot.lastName)
        }
    }
}