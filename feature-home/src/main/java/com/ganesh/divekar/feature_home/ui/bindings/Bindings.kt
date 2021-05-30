package com.ganesh.divekar.feature_home.ui.bindings

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.databinding.ObservableField
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.ganesh.divekar.feature_home.R
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


@BindingConversion
fun convertBooleanToVisibility(visible: Boolean): Int {
    return if (visible) View.VISIBLE else View.GONE
}


@BindingAdapter("imageUrl")
fun setImageUrl(view: ImageView?, field: ObservableField<String>?) {
    setImageUrl(view, field, 220, 300)
}


@BindingAdapter("bigImageUrl")
fun setBigImageUrl(view: ImageView?, field: ObservableField<String>?) {
    if (view?.context?.resources?.getBoolean(R.bool.isTablet)==true)
        setImageUrl(view, field, 1500, 1500)
    else
        setImageUrl(view, field, 1100, 1500)

}
@BindingAdapter("bigImageUrl")
fun setBigImageUrls(view: FrameLayout?, field: ObservableField<String>?) {
    if (view?.context?.resources?.getBoolean(R.bool.isTablet)==true)
        setImageUrls(view, field, 1500, 1500)
    else
        setImageUrls(view, field, 1100, 1500)

}
fun getBitmapFromURL(imageUrl: String?): Bitmap? {
    return try {
        val url = URL(imageUrl)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.setDoInput(true)
        connection.connect()
        val input: InputStream = connection.getInputStream()
        BitmapFactory.decodeStream(input)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}
fun setImageUrls(view: FrameLayout?, field: ObservableField<String>?, i: Int, i1: Int) {
    val url = field?.get()
    if (!url.isNullOrBlank() && view != null) {
        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val myImage: Bitmap = getBitmapFromURL(url)!!
        val dr: Drawable = BitmapDrawable(myImage)
        view.setBackgroundDrawable(dr)

        /*Glide.with(view.context).load(url)
            .dontAnimate()
            .placeholder(R.drawable.marvel_thumbnail)
            .signature(ObjectKey(getSignature()))
            .into(view)*/

    }
}

fun getSignature(): String {
    var signatureV = System.currentTimeMillis().toString()
    try {
        val cal = Calendar.getInstance()
        val today = cal.time
        val sdfD = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        val date = sdfD.format(today)
        val sdft = SimpleDateFormat("H", Locale.getDefault())
        var time = sdft.format(today)
        val hr = Integer.valueOf(time)
        if (hr in 1..6) {
            time = "0"
        }
        if (hr in 7..12) {
            time = "6"
        }
        if (hr in 13..18) {
            time = "12"
        }
        if (hr in 19..24) {
            time = "18"
        }
        signatureV = date + time
        Log.d("image-comp", "$date: $time")
    } catch (e: Exception) {
        Log.d("image-comp", "Exception: " + e.toString())
    }

    return signatureV
}

fun setImageUrl(view: ImageView?, field: ObservableField<String>?, width: Int, height: Int) {
    val url = field?.get()
    if (!url.isNullOrBlank() && view != null) {

            Glide.with(view.context).load(url)
                .dontAnimate()
                .placeholder(R.drawable.marvel_thumbnail)
                .signature(ObjectKey(getSignature()))
                .into(view)

/*        Glide.with(view.context)
            .load(url).apply(
                RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .dontTransform()
                    .encodeFormat(Bitmap.CompressFormat.PNG)
                    .override(width, height)
                    .format(DecodeFormat.PREFER_RGB_565)
                    .sizeMultiplier(0.5f)
                    .placeholder(R.drawable.marvel_thumbnail)
            )
            .into(view)*/
    }

}
