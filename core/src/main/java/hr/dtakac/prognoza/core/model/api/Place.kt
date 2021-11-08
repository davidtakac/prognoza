package hr.dtakac.prognoza.core.model.api

import com.google.gson.annotations.SerializedName

data class PlaceResponse(
    @SerializedName("place_id")
    val id: String,
    @SerializedName("lat")
    val latitude: Double,
    @SerializedName("lon")
    val longitude: Double,
    @SerializedName("display_name")
    val displayName: String
)