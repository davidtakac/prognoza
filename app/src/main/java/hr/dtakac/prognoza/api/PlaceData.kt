package hr.dtakac.prognoza.api

import com.google.gson.annotations.SerializedName

data class PlaceResponse(
    @SerializedName("place_id")
    val id: Int,
    @SerializedName("lat")
    val latitude: Float,
    @SerializedName("lon")
    val longitude: Float,
    @SerializedName("display_name")
    val displayName: String
)