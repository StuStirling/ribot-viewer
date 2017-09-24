package com.stustirling.ribotviewer.shared.extensions

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import kotlin.reflect.KClass

/**
 * Created by Stu Stirling on 24/09/2017.
 */

fun Context.startActivity(f: Intent.() -> Unit): Unit =
        Intent().apply(f).run(this::startActivity)

inline fun <reified T: Activity> Context.start(
        noinline f: Intent.() -> Unit = {}) = startActivity {
    component = componentFor(T::class.java)
    f(this)
}

fun Context.componentFor(targetType: KClass<*>) =
        componentFor(targetType.java)
fun Context.componentFor(targetType: Class<*>) =
        ComponentName(this, targetType)