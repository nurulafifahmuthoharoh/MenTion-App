package com.example.mention

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.mention.data.ApiInterface
import com.example.mention.data.RegisterResponse
import com.example.mention.data.RetrofitInstance
import com.example.mention.databinding.ActivityRegisterBinding
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@SuppressLint("CheckResult")
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nameStream = RxTextView.textChanges(binding.etNama)
            .skipInitialValue()
            .map { name ->
                name.isEmpty()
            }
        nameStream.subscribe{
            showNameExistAlert(it)
        }

        val emailStream = RxTextView.textChanges(binding.tillEmail)
            .skipInitialValue()
            .map { email->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe{
            showEmailValidAlert(it)
        }


        val noStream = RxTextView.textChanges(binding.tillNo)
            .skipInitialValue()
            .map { no->
                no.length < 11
            }
        noStream.subscribe{
            showTextMinimalAlert(it, "No")
        }

        val pwdStream = RxTextView.textChanges(binding.tillPwd)
            .skipInitialValue()
            .map { pwd->
                pwd.length < 8
            }
        pwdStream.subscribe{
            showTextMinimalAlert(it, "Password")
        }

        val pwdkStream = Observable.merge(
            RxTextView.textChanges(binding.tillPwd)
                .skipInitialValue()
                .map { password ->
                    password.toString() != binding.tillPwdk.text.toString()
                },
            RxTextView.textChanges(binding.tillPwdk)
                .skipInitialValue()
                .map { confirmPassword ->
                    confirmPassword.toString() !=  binding.tillPwd.text.toString()
                }
        )
        pwdkStream.subscribe{
            showPasswordConfirmAlert(it)
        }

        val invalidFieldStream = Observable.combineLatest(
            nameStream,
            emailStream,
            noStream,
            pwdStream,
            pwdkStream,
            { nameInvalid: Boolean, emailInvalid: Boolean, noInvalid: Boolean, passwordInvalid: Boolean, passwordConfirmInvalid: Boolean ->
                !nameInvalid && !emailInvalid &&  !noInvalid && !passwordInvalid && !passwordConfirmInvalid
            }
        )

        invalidFieldStream.subscribe{ isValid ->
            if (isValid){
                binding.btnRegister.isEnabled = true
                binding.btnRegister.backgroundTintList = ContextCompat.getColorStateList(this, R.color.primary)
            } else {
                binding.btnRegister.isEnabled = false
                binding.btnRegister.backgroundTintList = ContextCompat.getColorStateList(this, R.color.grey)
            }
        }

        binding.btnRegister.setOnClickListener {
            val fullname = binding.etNama.text.toString().trim()
            val email = binding.tillEmail.text.toString().trim()
            val phonenumber = binding.tillNo.text.toString().trim()
            val password = binding.tillPwd.text.toString().trim()

            signup( fullname, email, phonenumber,  password)
        }
        binding.tvHavenAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun showNameExistAlert(isNotValid: Boolean){
        binding.etNama.error = if (isNotValid)"Nama tidak boleh kosong" else null

    }
    private fun showTextMinimalAlert(isNotValid: Boolean, text: String) {
        if (text  == "Password") {
            binding.tillPwd.error = if (isNotValid) "$text harus lebih dari 8 huruf" else null
        }
        else if (text  == "No"){
            binding.tillNo.error = if (isNotValid) "$text harus lebih dari 11" else null
        }
    }

    private fun showEmailValidAlert(isNotValid: Boolean){
        binding.tillEmail.error = if (isNotValid)"Email tidak valid!" else null
    }

    private fun showPasswordConfirmAlert(isNotValid: Boolean){
        binding.tillPwdk.error = if (isNotValid) "Password tidak sama!" else null
    }

    private fun signup( fullname: String, email: String, phonenumber: String, password: String ){
        val retIn = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
        val registerInfo = RegisterResponse(fullname, email, phonenumber, password )

        retIn.registerUser(registerInfo).enqueue(object :
            Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@RegisterActivity,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    Toast.makeText(this@RegisterActivity, "Registration success!", Toast.LENGTH_SHORT)
                        .show()

                }
                else{
                    Toast.makeText(this@RegisterActivity, "Registration failed!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }
}