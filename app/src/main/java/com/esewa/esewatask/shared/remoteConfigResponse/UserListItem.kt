package com.esewa.esewatask.shared.remoteConfigResponse


import com.google.gson.annotations.SerializedName

data class UserListItem(
    @SerializedName("age")
    val age: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("sex")
    val sex: String
)