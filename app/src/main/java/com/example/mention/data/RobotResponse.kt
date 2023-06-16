package com.example.mention.data

import com.google.gson.annotations.SerializedName

data class RobotResponse(

	@field:SerializedName("response")
	val response: String? = null,

	@field:SerializedName("alternative_response")
	val alternativeResponse: String? = null
)
