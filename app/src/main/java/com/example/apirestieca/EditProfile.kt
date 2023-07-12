package com.example.apirestieca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.apirestieca.interfaces.ApiClient
import com.example.apirestieca.interfaces.update.UpdatePasswordRequest
import com.example.apirestieca.interfaces.update.UpdateUserRequest
import com.example.apirestieca.interfaces.update.UpdateUserResponse
import retrofit2.Call
import retrofit2.Response

class EditProfile : AppCompatActivity() {
  private lateinit var editTextName: EditText
  private lateinit var editTextEmail: EditText
  private lateinit var editTextPassword: EditText
  private lateinit var buttonSaveChanges: Button
  private lateinit var buttonBack: Button
  private var apiService = ApiClient.getClient()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_edit_profile)

    editTextName = findViewById<EditText>(R.id.editName)
    editTextEmail = findViewById<EditText>(R.id.editEmail)
    editTextPassword = findViewById<EditText>(R.id.editPassword)
    buttonSaveChanges = findViewById<Button>(R.id.btnEditProfile)
    buttonBack = findViewById<Button>(R.id.btnBack)

    buttonSaveChanges.setOnClickListener{
      saveChanges()
    }

    buttonBack.setOnClickListener{
      startActivity(Intent(this@EditProfile, HomeActivity::class.java))
    }
  }

  private fun saveChanges(){
    val token = getSharedPreferences("token", MODE_PRIVATE).getString("token", "")
    val name = editTextName.text.toString()
    val email = editTextEmail.text.toString()
    val password = editTextPassword.text.toString()

    if (name.isNotEmpty() || email.isNotEmpty()) {
      val userUpdateRequest = UpdateUserRequest(name, email, token.toString())
      updateUser(userUpdateRequest)
    }

    if (password.isNotEmpty()) {
      val userUpdateRequest = UpdatePasswordRequest(password, token.toString())
      changePassword(userUpdateRequest)
    }
  }

  private fun updateUser(request: UpdateUserRequest) {
    val body: UpdateUserRequest = UpdateUserRequest(editTextName.text.toString(), editTextEmail.text.toString(), getSharedPreferences("token", MODE_PRIVATE).getString("token", "").toString())
    val call = apiService.updateUser(body)

    call.enqueue(object : retrofit2.Callback<UpdateUserResponse> {
      override fun onResponse(call: Call<UpdateUserResponse>, response: Response<UpdateUserResponse>) {
        if (response.isSuccessful) {
          val updateUserResponse = response.body()
          val email = editTextEmail.text.toString()

          saveUserDataCache(updateUserResponse?.name.toString(), email, null)
          Toast.makeText(this@EditProfile, "User updated", Toast.LENGTH_SHORT).show()
          startActivity(Intent(this@EditProfile, HomeActivity::class.java))
        } else {
          Toast.makeText(this@EditProfile, "Error of network", Toast.LENGTH_SHORT).show()
        }
      }
      override fun onFailure(call: Call<UpdateUserResponse>, t: Throwable) {
        Toast.makeText(this@EditProfile, "Error of network", Toast.LENGTH_SHORT).show()
      }
    })
  }

  private fun changePassword(request: UpdatePasswordRequest) {
    val body: UpdatePasswordRequest = UpdatePasswordRequest(editTextPassword.text.toString(), getSharedPreferences("token", MODE_PRIVATE).getString("token", "").toString())
    val call = apiService.changePassword(body)

    call.enqueue(object : retrofit2.Callback<UpdateUserResponse> {
      override fun onResponse(call: Call<UpdateUserResponse>, response: Response<UpdateUserResponse>) {
        if (response.isSuccessful) {
          val password = editTextEmail.text.toString()

          saveUserDataCache(null, null, password)
          Toast.makeText(this@EditProfile, "Password updated", Toast.LENGTH_SHORT).show()
          startActivity(Intent(this@EditProfile, HomeActivity::class.java))
        } else {
          Toast.makeText(this@EditProfile, "Error of network", Toast.LENGTH_SHORT).show()
        }
      }
      override fun onFailure(call: Call<UpdateUserResponse>, t: Throwable) {
        Toast.makeText(this@EditProfile, "Error of network", Toast.LENGTH_SHORT).show()
      }
    })
  }

  private fun saveUserDataCache(name: String?, email: String?, password: String?){
    val sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    if(name != null) editor.putString("name", name)
    if(email != null) editor.putString("email", email)
    if(password != null) editor.putString("password", password)

    editor.apply()
  }
}