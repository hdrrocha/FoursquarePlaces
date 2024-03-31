package com.example.foursquareplaces.data.api

import com.example.foursquareplaces.BuildConfig
import com.example.foursquareplaces.data.model.Place
import com.example.foursquareplaces.data.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface FoursquareApi {
    @Headers(
        "Accept: application/json",
        "Authorization: ${BuildConfig.AUTH_TOKEN}"
    )
    @GET("places/search")
    suspend fun searchPlaces(
        @Query("ll") latLong: String,
        @Query("client_id") clientId: String = BuildConfig.CLIENT_ID,
        @Query("client_secret") clientSecret: String =  BuildConfig.CLIENT_SECRET,
        @Query("v") version: String = BuildConfig.VERSION_API,
        @Query("query") query: String = BuildConfig.FLAVOR_SELECTED,
        @Query("radius") radius: String = BuildConfig.RADIUS,
        @Query("fields") fields: String =BuildConfig.SEARCH_FIELDS
    ): Response<SearchResponse>
    @Headers(
        "Accept: application/json",
        "Authorization: ${BuildConfig.AUTH_TOKEN}"
    )
    @GET("places/search")
    suspend fun filterPlaces(
        @Query("min_price") minPrice: Int,
        @Query("open_now") openNow: Boolean,
        @Query("ll") latLong: String,
        @Query("client_id") clientId: String = BuildConfig.CLIENT_ID,
        @Query("client_secret") clientSecret: String =  BuildConfig.CLIENT_SECRET,
        @Query("v") version: String = BuildConfig.VERSION_API,
        @Query("query") query: String = BuildConfig.FLAVOR_SELECTED,
        @Query("radius") radius: String = BuildConfig.RADIUS,
        @Query("fields") fields: String =BuildConfig.SEARCH_FIELDS
    ): Response<SearchResponse>
    @Headers(
        "Accept: application/json",
        "Authorization: ${BuildConfig.AUTH_TOKEN}"
    )
    @GET("places/search")
    suspend fun placeDetails(
        @Query("fsq_id") fsqId: String,
        @Query("client_id") clientId: String = BuildConfig.CLIENT_ID,
        @Query("client_secret") clientSecret: String =  BuildConfig.CLIENT_SECRET,
        @Query("v") version: String = BuildConfig.VERSION_API,
        @Query("query") query: String = BuildConfig.FLAVOR_SELECTED,
        @Query("radius") radius: String = BuildConfig.RADIUS,
        @Query("fields") fields: String =BuildConfig.DETAILS_FIELDS
    ): Response<Place>
}