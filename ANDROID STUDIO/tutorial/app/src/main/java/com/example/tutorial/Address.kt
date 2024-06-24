package com.example.tutorial

import android.util.Log
import androidx.lifecycle.findViewTreeViewModelStoreOwner

class Address (address : String) {
    var address = ""
    init {
        this.address = address
    }
    fun showDetails(){
        Log.i("tag", address)
        address = "$address wew"
    }
}