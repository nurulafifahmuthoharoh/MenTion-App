package com.example.mention.data

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("fullname")
	val fullname: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("phonenumber")
	val phonenumber: String? = null,

	@field:SerializedName("password")
	val password: String? = null






)
