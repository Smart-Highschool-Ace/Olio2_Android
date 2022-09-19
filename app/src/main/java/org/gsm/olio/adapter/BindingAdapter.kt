package org.gsm.olio.adapter

import android.view.View
import androidx.databinding.BindingAdapter

object BindingAdapter {

    @BindingAdapter("loading")
    @JvmStatic
    fun loading(view : View, type : Boolean){
        if(type == false){
            view.visibility = View.GONE
        }else{
            view.visibility = View.VISIBLE
        }
    }

}