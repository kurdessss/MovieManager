package com.example.ndroidapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ndroidapp.databinding.ActivityRegisterBinding
import com.google.android.gms.safetynet.SafetyNet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()
            val username = binding.usernameEt.text.toString()

            // Проверка на пустые поля
            if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
                Toast.makeText(applicationContext, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                verifyCaptcha(
                    onSuccess = {
                        registerUser(email, password, username)
                    },
                    onFailure = {
                    }
                )
            }
        }
    }

    private fun verifyCaptcha(onSuccess: () -> Unit, onFailure: () -> Unit) {
        SafetyNet.getClient(this).verifyWithRecaptcha(" 6LciHZEqAAAAAG7F5mpUJ2kreM2mmFK2oUNLmvQn")
            .addOnSuccessListener { response ->
                if (response.tokenResult?.isNotEmpty() == true) {
                    onSuccess()
                } else {
                    Toast.makeText(this, "Failed verification. Try again.", Toast.LENGTH_SHORT)
                        .show()
                    onFailure()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "reCAPTCHA failed: ${e.message}", Toast.LENGTH_SHORT).show()
                onFailure()
            }
    }

    // Метод для регистрации пользователя с проверкой email
    private fun registerUser(email: String, password: String, username: String) {
        // Проверка, не существует ли пользователь с таким email
        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result
                    if (result.signInMethods?.isNotEmpty() == true) {
                        // Если метод входа существует, значит email уже зарегистрирован
                        Toast.makeText(this@RegisterActivity, "Email is already registered.", Toast.LENGTH_SHORT).show()
                    } else {
                        // Если email не зарегистрирован, продолжаем регистрацию
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val userId = FirebaseAuth.getInstance().currentUser!!.uid

                                    // Сохраняем данные пользователя в Firebase Realtime Database
                                    val userInfo = HashMap<String, String>()
                                    userInfo["email"] = email
                                    userInfo["username"] = username
                                    userInfo["profileImage"] = ""

                                    // Добавляем информацию о пользователе в узел "Users"
                                    FirebaseDatabase.getInstance().getReference("Users")
                                        .child(userId)
                                        .setValue(userInfo)
                                        .addOnCompleteListener { task1 ->
                                            if (task1.isSuccessful) {
                                                // Переход на главный экран после успешной регистрации
                                                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                                                finish() // Закрытие активности регистрации
                                            } else {
                                                Toast.makeText(this@RegisterActivity, "Error saving user data.", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                } else {
                                    Toast.makeText(this@RegisterActivity, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                } else {
                    Toast.makeText(this@RegisterActivity, "Error checking email: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
