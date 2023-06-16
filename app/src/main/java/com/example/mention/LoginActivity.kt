package com.example.mention

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.mention.data.ApiInterface
import com.example.mention.data.LoginResponse
import com.example.mention.data.RetrofitInstance
import com.example.mention.databinding.ActivityLoginBinding
import com.example.mention.databinding.ActivityMainBinding
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.sign


@SuppressLint("CheckResult")
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val emailStream = RxTextView.textChanges(binding.etEmail)
            .skipInitialValue()
            .map { email->
                email.isEmpty()
            }
        emailStream.subscribe{
            showTextMinimalAlert(it, "Email")
        }

        val pwdStream = RxTextView.textChanges(binding.password)
            .skipInitialValue()
            .map { pwd->
                pwd.isEmpty()
            }
        pwdStream.subscribe{
            showTextMinimalAlert(it, "Password")
        }

        val invalidFieldStream = Observable.combineLatest(
            emailStream,
            pwdStream,
            { emailInvalid: Boolean, passwordInvalid: Boolean, ->
               !emailInvalid &&  !passwordInvalid
            }
        )

        invalidFieldStream.subscribe{ isValid ->
            if (isValid){
                binding.btnLogin.isEnabled = true
                binding.btnLogin.backgroundTintList = ContextCompat.getColorStateList(this, R.color.primary)
            } else {
                binding.btnLogin.isEnabled = false
                binding.btnLogin.backgroundTintList = ContextCompat.getColorStateList(this, R.color.grey)
            }
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.password.text.toString().trim()
            signin(email, password)
        }
        binding.tvHaventAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun showTextMinimalAlert(isNotValid: Boolean, text: String) {
        if (text  == "Password") {
            binding.password.error = if (isNotValid) "$text harus lebih dari 8 huruf" else null
        }
        else if (text  == "Email"){
            binding.etEmail.error = if (isNotValid) "Masukan alamat $text yang valid!" else null
        }
    }

    private fun signin(email: String, password: String){
        val retIn = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
        val signInInfo = LoginResponse(email, password)
        retIn.signin(signInInfo).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@LoginActivity,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {
                    Intent(this@LoginActivity, ChatActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                        Toast.makeText(this@LoginActivity, "Login success!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


}