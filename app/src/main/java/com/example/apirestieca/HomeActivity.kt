package com.example.apirestieca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class HomeActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)

    val profileName = findViewById<TextView>(R.id.profileName)
    val profileEmail = findViewById<TextView>(R.id.profileEmail)
    val profilePassword = findViewById<TextView>(R.id.profilePassword)
    val btnLogout = findViewById<Button>(R.id.btnLogout)
    val btnEdit = findViewById<Button>(R.id.btnEdit)

    profileName.text = getSharedPreferences("userData", MODE_PRIVATE).getString("name", "")
    profileEmail.text = getSharedPreferences("userData", MODE_PRIVATE).getString("email", "")
    profilePassword.text = getSharedPreferences("userData", MODE_PRIVATE).getString("password", "")

    btnLogout.setOnClickListener {
      getSharedPreferences("token", MODE_PRIVATE).edit().clear().apply()
      startActivity(Intent(this@HomeActivity, MainActivity::class.java))
    }

    btnEdit.setOnClickListener {
      startActivity(Intent(this@HomeActivity, EditProfile::class.java))
    }
  }
}