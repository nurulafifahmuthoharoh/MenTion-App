package com.example.mention.data

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("password")
	val password: String? = null


)
