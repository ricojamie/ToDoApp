package com.jamierico.todoapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        login_button.setOnClickListener {
            performLogin()
        }


    }

    private fun performLogin() {
        val email = email_edittext_login.text.toString()
        val password = password_edittext_login.text.toString()

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter a valid email and password", Toast.LENGTH_SHORT).show()
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                Toast.makeText(this, "Success! ${it.result?.user?.uid}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
