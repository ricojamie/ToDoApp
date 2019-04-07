package com.jamierico.todoapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        FirebaseApp.initializeApp(this)

        register_button.setOnClickListener {
           performRegister()
        }

        already_have_account_textview.setOnClickListener {
            Log.d("RegisterActivity", "Will show login activity")

            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performRegister() {
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter a valid email and password", Toast.LENGTH_SHORT).show()
        }

        Log.d("RegisterActivity", "Email is: " + email)
        Log.d("RegisterActivity", "Password is: " + password)

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d("RegisterActivity", "Successfully created user: ${it.result?.user?.uid}")
            }
            .addOnFailureListener{
                Toast.makeText(this, "Failed to register! Reason: ${it.message}", Toast.LENGTH_SHORT).show()
                Log.d("RegisterActivity", "Failed to create user with uid ${it.message}")
            }
    }
}
