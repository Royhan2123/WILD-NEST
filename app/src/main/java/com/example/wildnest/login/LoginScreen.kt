package com.example.wildnest.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.wildnest.MainActivity
import com.example.wildnest.R
import com.example.wildnest.register.RegisterScreen
import com.example.wildnest.databinding.ActivityLoginScreenBinding
import com.example.wildnest.model.UsersModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Suppress("DEPRECATION")
class LoginScreen : AppCompatActivity() {
    private lateinit var binding:ActivityLoginScreenBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")
        binding.txtSignUp.setOnClickListener {
            startActivity(Intent(this@LoginScreen, RegisterScreen::class.java))
        }

        binding.btnLogin.setOnClickListener{
            val signUpEmail = binding.edtEmail.text.toString()
            val signUppPassword = binding.edtPassword.text.toString()

            if (signUpEmail.isNotEmpty() && signUppPassword.isNotEmpty()){
                loginUser(signUpEmail,signUppPassword)
            }else {
                Toast.makeText(this@LoginScreen, "All Fields are mandantory", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email:String,password:String){
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val userData = userSnapshot.getValue(UsersModel::class.java)

                        if (userData != null && userData.password == password ){
                            val intent = Intent(this@LoginScreen, MainActivity::class.java)
                            intent.putExtra("USERNAME", userData.username)
                            startActivity(intent)
                            finish()
                            return
                        }
                    }
                }
                Toast.makeText(this@LoginScreen,"Login Failed",Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginScreen,"Database Error: $error",Toast.LENGTH_SHORT).show()

            }
        })

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()

        this@LoginScreen.overridePendingTransition(
            R.anim.fade_in,
            R.anim.fade_out,
        )
    }
}