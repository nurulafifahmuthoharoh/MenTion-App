package com.example.mention

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mention.data.Message
import com.example.mention.ui.MessagingAdapter
import com.example.mention.utils.BotResponse
import com.example.mention.utils.Constant.CONSUL
import com.example.mention.utils.Constant.OPEN_GOOGLE
import com.example.mention.utils.Constant.OPEN_SEARCH
import com.example.mention.utils.Constant.RECEIVE_ID
import com.example.mention.utils.Constant.SEND_ID
import com.example.mention.utils.Time
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.coroutines.*

@OptIn(DelicateCoroutinesApi::class)
class ChatActivity : AppCompatActivity() {

    private lateinit var adapter: MessagingAdapter
    private var botList= listOf("Peter", "Francesca", "MenTion", "William")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recycleView()

        clickEvents()

        val random = (0..3).random()
        customMessage("Hello! Today you're speaking with ${botList[random]}, how may I help? ")
    }

    private fun clickEvents(){
        btn_send.setOnClickListener{
            sendMessage()
        }
        et_message.setOnClickListener {
            GlobalScope.launch {
                delay(1000)
                withContext(Dispatchers.Main){
                    rv_messages.scrollToPosition(adapter.itemCount-1)
                }
            }
        }
    }

    private fun recycleView(){
        adapter = MessagingAdapter()
        rv_messages.adapter =adapter
        rv_messages.layoutManager = LinearLayoutManager(applicationContext)
    }

    //user input
    private fun sendMessage(){
        val message = et_message.text.toString()
        val timestamp = Time.timeStamp()

        if (message.isNotEmpty()) {
            et_message.setText("")
            adapter.insertMessage(Message(message, SEND_ID, timestamp))
            rv_messages.scrollToPosition(adapter.itemCount-1)

            botResponse(message)
            }
        }

    //bot response
    private fun botResponse (message: String){
        val timeStamp = Time.timeStamp()

        GlobalScope.launch {
            delay(1000)

            withContext(Dispatchers.Main){
                val response = BotResponse.basicResponses(message)
                adapter.insertMessage(Message(response, RECEIVE_ID, timeStamp))


                rv_messages.scrollToPosition(adapter.itemCount-1)
                when(response){
                    OPEN_GOOGLE -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.google.com/")
                        startActivity(site)
                    }
                    OPEN_SEARCH ->{
                        val site = Intent(Intent.ACTION_VIEW)
                        val searchTerm : String = message.substringAfter("search")
                        site.data = Uri.parse("https://www.google.com/search?&q=$searchTerm")
                        startActivity(site)
                    }
                    CONSUL ->{
                        val site = Intent(Intent.ACTION_VIEW)
                        val searchTerm : String = message.substringAfter("consul")
                        site.data = Uri.parse("https://www.halodoc.com/kesehatan-mental")
                        startActivity(site)
                    }
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main){
                rv_messages.scrollToPosition(adapter.itemCount-1)
            }
        }

    }


    private fun customMessage(message: String){
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main){
                val timeStamp = Time.timeStamp()
                adapter.insertMessage(Message(message, RECEIVE_ID, timeStamp))
                rv_messages.scrollToPosition(adapter.itemCount-1)

            }
        }
    }
}