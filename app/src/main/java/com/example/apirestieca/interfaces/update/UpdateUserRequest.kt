package com.example.apirestieca.interfaces.update

import com.google.gson.annotations.SerializedName

data class UpdateUserRequest(
  @SerializedName("name") val name: String,
  @SerializedName("email") val email: String,
  @SerializedName("token") val token: String,
)

data class UpdatePasswordRequest(
  @SerializedName("password") val password: String,
  @SerializedName("token") val token: String,
)
