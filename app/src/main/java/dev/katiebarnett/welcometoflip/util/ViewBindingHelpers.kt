package dev.katiebarnett.welcometoflip.util

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter

@BindingAdapter("visibleOrGone")
fun View.setVisibleOrGone(show: Boolean?) {
    visibility = if (show == true) View.VISIBLE else View.GONE
}

@BindingAdapter("visibleOrGone")
fun View.setVisibleOrGone(text: String?) {
    visibility = if (!text.isNullOrBlank()) View.VISIBLE else View.GONE
}

@BindingAdapter("visibleOrInvisible")
fun View.setVisibleOrInvisible(text: String?) {
    visibility = if (!text.isNullOrBlank()) View.INVISIBLE else View.GONE
}

@BindingAdapter("loadImage")
fun ImageView.loadImage(@DrawableRes res: Int?) {
    if (res != null) {
        setImageResource(res)
    } else {
        setImageResource(0)
    }
}
