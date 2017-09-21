package com.stustirling.ribotviewer.data.api.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Stu Stirling on 21/09/2017.
 */
data class ApiRibot(@SerializedName("profile")val profile : ApiRibotProfile) {
    data class ApiRibotProfile(
            @SerializedName("name") val name: ApiRibotName,
            @SerializedName("email") val email: String,
            @SerializedName("hexColor") val color: String,
            @SerializedName("avatar") val avatar: String,
            @SerializedName("dateOfBirth") val dob: Date,
            @SerializedName("bio") val bio: String,
            @SerializedName("isActive") val isActive: Boolean) {

       data class ApiRibotName(
                @SerializedName("first") val first: String,
                @SerializedName("last") val last: String)
        }
}