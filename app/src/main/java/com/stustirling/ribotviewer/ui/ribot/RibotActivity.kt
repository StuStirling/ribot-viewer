package com.stustirling.ribotviewer.ui.ribot

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.view.ViewCompat
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.stustirling.ribotviewer.BaseActivity
import com.stustirling.ribotviewer.R
import com.stustirling.ribotviewer.model.RibotModel
import com.stustirling.ribotviewer.shared.di.AppComponent
import com.stustirling.ribotviewer.shared.di.scopes.ActivityScope
import kotlinx.android.synthetic.main.activity_ribot.*
import kotlinx.android.synthetic.main.content_ribot.*
import java.text.SimpleDateFormat
import java.util.*

class RibotActivity : BaseActivity() {

    @SuppressLint("SimpleDateFormat")
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy").apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    override fun layoutRes(): Int = R.layout.activity_ribot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportPostponeEnterTransition()

        setSupportActionBar(tb_rb_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val ribot = intent.getParcelableExtra<RibotModel>("ribot")
        setupWithRibot(ribot)
    }

    private fun setupWithRibot(ribotModel: RibotModel) {
        tv_ra_title.text =
                getString(R.string.full_name_format,ribotModel.firstName,ribotModel.lastName)

        val color = Color.parseColor(ribotModel.hexColor)
        tb_rb_toolbar.setBackgroundColor(color)
        window.statusBarColor = color

        ribotModel.bio?.let { tv_ra_bio.text = it }
        tv_ra_date.text = dateFormatter.format(ribotModel.dataOfBirth)

        ViewCompat.setTransitionName(iv_ra_avatar,ribotModel.id)
        supportStartPostponedEnterTransition()

        val placeholder = getDrawable(R.drawable.small_ribot_logo).apply {
            mutate()
            DrawableCompat.setTint(this,color)
        }

        Glide.with(this)
                .load(ribotModel.avatar)
                .apply(RequestOptions.fitCenterTransform()
                        .placeholder(placeholder))
                .into(iv_ra_avatar)


        fab_ra_email.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
                    .apply { putExtra(Intent.EXTRA_EMAIL,ribotModel.email) }
            if ( emailIntent.resolveActivity(packageManager) != null ) {
                startActivity(emailIntent)
            } else {
                Toast.makeText(this,R.string.unable_to_email,Toast.LENGTH_SHORT)
                        .show()
            }

        }
    }

    override fun inject(appComponent: AppComponent) {
        DaggerRibotActivity_Component.builder()
                .appComponent(appComponent)
                .build().inject(this)
    }

    @dagger.Component(dependencies = arrayOf(AppComponent::class))
    @ActivityScope
    interface Component {
        fun inject(ribotActivity: RibotActivity)
    }
}
