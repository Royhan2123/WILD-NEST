package com.example.wildnest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.wildnest.databinding.ActivityLoginScreenBinding
import com.example.wildnest.model.UsersModel
import com.google.firebase.auth.FirebaseAuth
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
                            Toast.makeText(this@LoginScreen,"Login Succesful",Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginScreen,MainActivity::class.java))
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