package com.example.geeksasaeng.ChatSetting

import android.util.Log
import com.example.geeksasaeng.BuildConfig
import com.rabbitmq.client.*


class RabbitMQSetting {
    private val rabbitMQUri =
        "amqp://" + BuildConfig.RABBITMQ_ID + ":" + BuildConfig.RABBITMQ_PWD + "@" + BuildConfig.RABBITMQ_ADDRESS
    private val factory = ConnectionFactory()

    val QUEUE_NAME = "110"

    fun initRabbitMQSetting() {
        factory.setUri(rabbitMQUri)
        // RabbitMQ 연결
        val conn: Connection = factory.newConnection()
        // Send and Receive 가능하도록 해주는 부분!
        val channel = conn.createChannel()
        // durable = true로 설정!!
        // 참고 : https://teragoon.wordpress.com/2012/01/26/message-durability%EB%A9%94%EC%8B%9C%EC%A7%80-%EC%9E%83%EC%96%B4%EB%B2%84%EB%A6%AC%EC%A7%80-%EC%95%8A%EA%B8%B0-durabletrue-propspersistent_text_plain-2/
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        Log.d("RabbitMQ-Test", " [*] Waiting for messages. To exit press CTRL+C")

        val deliverCallback = DeliverCallback { consumerTag: String?, delivery: Delivery ->
            Log.d("RabbitMQ-Test", "in deliverCallback")
            val message = String(delivery.body, Charsets.UTF_8)
            Log.d("RabbitMQ-Test", "message = $message")
            println(" [x] Received '$message'")
        }

        Log.d("RabbitMQ-Test", "deliverCallback = $deliverCallback")

        channel.basicConsume(QUEUE_NAME, true, deliverCallback) { consumerTag: String? -> }
    }
}