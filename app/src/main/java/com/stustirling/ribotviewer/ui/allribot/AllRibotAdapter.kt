package com.stustirling.ribotviewer.ui.allribot

import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.stustirling.ribotviewer.R
import com.stustirling.ribotviewer.model.RibotModel

/**
 * Created by Stu Stirling on 24/09/2017.
 */
class AllRibotAdapter(val callback: (ribot: RibotModel, sharedView:View) -> Unit) : RecyclerView.Adapter<AllRibotAdapter.ViewHolder> () {

    var items : List<RibotModel> = emptyList()
    set(value) {
        field = value
        notifyDataSetChanged() }

    override fun getItemId(position: Int): Long {
        return items[position].id.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.content_all_ribot,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind( items[position] )

    override fun getItemCount(): Int  = items.size

    inner class ViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val container = itemView.findViewById<ConstraintLayout>(R.id.cl_alc_container)
        private val avatar = itemView.findViewById<ImageView>(R.id.civ_alc_avatar)
        private val name = itemView.findViewById<TextView>(R.id.tv_alc_name)

        fun bind(ribot:RibotModel) {
            container.setOnClickListener { callback(items[adapterPosition],avatar) }

            ViewCompat.setTransitionName(avatar,ribot.id)

            val placeholder = container.context.getDrawable(R.drawable.small_ribot_logo).apply {
                mutate()
                DrawableCompat.setTint(this, Color.parseColor(ribot.hexColor))
            }

            Glide.with(avatar.context)
                    .load(ribot.avatar)
                    .apply(RequestOptions.fitCenterTransform()
                            .placeholder(placeholder))
                    .into(avatar)
            name.text = avatar.context.getString(
                    R.string.full_name_format,ribot.firstName,ribot.lastName)
        }
    }
}