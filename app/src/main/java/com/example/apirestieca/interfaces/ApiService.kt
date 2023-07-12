package com.example.apirestieca.interfaces

import com.example.apirestieca.interfaces.post.LoginRequest
import com.example.apirestieca.interfaces.post.LoginResponse
import com.example.apirestieca.interfaces.update.UpdatePasswordRequest
import com.example.apirestieca.interfaces.update.UpdateUserRequest
import com.example.apirestieca.interfaces.update.UpdateUserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {

  @POST("login")
  fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

  @POST("update_user")
  fun updateUser(@Body updateUserRequest: UpdateUserRequest): Call<UpdateUserResponse>

  @POST("change_password")
  fun changePassword(@Body changePasswordRequest: UpdatePasswordRequest): Call<UpdateUserResponse>
}