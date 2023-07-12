package com.example.apirestieca

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.apirestieca.interfaces.ApiClient
import com.example.apirestieca.interfaces.post.LoginRequest
import com.example.apirestieca.interfaces.post.LoginResponse
import retrofit2.Call
import retrofit2.Response

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val btnLogin = findViewById<Button>(R.id.login)
    val editEmail = findViewById<EditText>(R.id.email)
    val editPassword = findViewById<EditText>(R.id.password)

    if(getSharedPreferences("token", MODE_PRIVATE).getString("token", "") != ""){
      startActivity(Intent(this@MainActivity, HomeActivity::class.java))
    }else{
      Toast.makeText(this@MainActivity, "No hay token", Toast.LENGTH_SHORT).show()
    }

    btnLogin.setOnClickListener {
      val apiService = ApiClient.getClient()
      val body: LoginRequest = LoginRequest(editEmail.text.toString(), editPassword.text.toString())
      val call = apiService.login(body)

      call.enqueue(object : retrofit2.Callback<LoginResponse> {
        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
          if (response.isSuccessful) {
            val loginResponse = response.body()
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            saveTokenCache(loginResponse?.token.toString())
            saveUserDataCache(loginResponse?.name.toString(), email, password)

            intent.putExtra("email", email)
            intent.putExtra("password", password)

            startActivity(intent)
            Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
          } else {
            Toast.makeText(this@MainActivity, "Error of network", Toast.LENGTH_SHORT).show()
          }
        }
        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
          println("t.message: ${t.message}")
        }
      })
    }
  }

  fun saveTokenCache(token: String){
    val sharedPreferences = getSharedPreferences("token", MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("token", token)
    editor.apply()
  }

  fun saveUserDataCache(name: String, email: String, password: String){
    val sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("name", name)
    editor.putString("email", email)
    editor.putString("password", password)
    editor.apply()
  }
}
