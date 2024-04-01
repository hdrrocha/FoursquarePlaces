package com.example.foursquareplaces.utils
fun String.fixImageUrl(): String {
    return this.replace("//", "/")
}


