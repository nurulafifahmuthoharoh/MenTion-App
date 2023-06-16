package com.example.mention.utils

import android.annotation.SuppressLint
import com.example.mention.utils.Constant.CONSUL
import com.example.mention.utils.Constant.CONSUL_ID
import com.example.mention.utils.Constant.OPEN_GOOGLE
import com.example.mention.utils.Constant.OPEN_SEARCH
import java.util.*

object BotResponse {
    @SuppressLint("DefaultLocale")
    fun basicResponses(_message: String): String {
        val random= (0..1).random()
        val message = _message.lowercase(Locale.getDefault())

        return when {

            message.contains("hai") or message.contains("hello") or message.contains("morning")-> {
                when (random) {
                    0 -> "How are you?"
                    1 -> "what are you feeling?"
                    else -> "error"
                }
            }

            message.contains("i feel happy") or message.contains("happy") -> {
                when (random) {
                    0 -> "Joy is an incredible gift. Fully embrace and enjoy the moment, and share your happiness with your loved ones."
                    1 -> "Joy is an incredible gift. Fully embrace and enjoy the moment, and share your happiness with your loved ones."
                    else -> "error"
                }
            }

            message.contains("im feeling sad and sorry for myself") or message.contains("sad") -> {
                when (random) {
                    0 -> "Sadness is a natural part of life. Allow yourself to feel it, but remember to seek support and maintain emotional balance."
                    1 -> "In moments of sadness, it's important to take care of your mental and physical health. Find ways to indulge yourself and interact with loved ones."
                   else -> "error"
                }
            }

            message.contains("i am just feeling cranky and blue") or message.contains("anggry") -> {
                when (random) {
                    0 -> "When experiencing anger, try to pause and slowly manage it. Communicate calmly and search for better solutions."
                    1 -> "Anger is a natural emotion, but it's important to manage it effectively. Find ways to express your feelings calmly and constructively."
                     else -> "error"
                }
        }

            message.contains("i was feeling sentimental") or message.contains("anggry")-> {
                when (random) {
                    0 -> "I understand that you're feeling something unique right now. It may be helpful to reflect on your emotions and seek support from loved ones."
                    1 -> "Every emotion has its own significance. Take the time to explore and understand your feelings, and remember that you're not alone in your experiences."
                    else -> "error"
                }
            }

            message.contains("What should I do?") or message.contains("hopeless") or message.contains("help me")-> {
                when (random) {
                    0 -> "you can type \"Consultation\" for further consultation"
                    1 -> "you should get help by experts, type \"consultation\" to get help"
                    else -> "error"
                }
            }

            message.contains("flip") or message.contains("coin") ->{
                val r = (0..1).random()
                val result = if (r == 0) "heads" else "tails"

                "I Flipped a coin and landed on $result"
            }

            message.contains("solve") ->{
                val equation: String = message.substringAfter("solve")

                return  try {
                    val answer= SolveMath.solveMath(equation)
                    answer.toString()
                }catch (e: java.lang.Exception){
                    "sorry, i can't solve that!"
                }
            }

            message.contains("time") or message.contains("?") ->{
                Time.timeStamp()
            }

            message.contains("open") or message.contains("google") ->{
                OPEN_GOOGLE
            }

            message.contains("search") ->{
                OPEN_SEARCH
            }
            message.contains("consultation") ->{
                CONSUL
            }

            else -> {
                when(random){
                    0 -> "i understand your problem, can you repeat more clearly ?"
                    1 -> "I see, can you repeat your question again ?"
                    2 -> "Don't worry, can you explain in more detail?"
                    else -> "error"
                }
            }
        }
    }
}