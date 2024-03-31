package com.example.foursquareplaces.utils

import android.content.Context
import android.widget.Toast


fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun String.fixImageUrl(): String {
    return this.replace("//", "/")
}


