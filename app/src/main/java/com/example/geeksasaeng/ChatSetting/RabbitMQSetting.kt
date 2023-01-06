package com.example.geeksasaeng.ChatSetting

import android.util.Log
import com.example.geeksasaeng.BuildConfig
import com.rabbitmq.client.*


class RabbitMQSetting {
    private val rabbitMQUri =
        "amqp://" + BuildConfig.RABBITMQ_ID + ":" + BuildConfig.RABBITMQ_PWD + "@" + BuildConfig.RABBITMQ_ADDRESS
    private val factory = ConnectionFactory()
    lateinit var conn: Connection
    lateinit var channel: Channel

    val QUEUE_NAME = "110"

    fun main() {
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
            val message = String(delivery.body, Charsets.UTF_8)
            Log.d("RabbitMQ-Test", "")
            println(" [x] Received '$message'")
        }
        channel.basicConsume(
            QUEUE_NAME, true, deliverCallback
        ) { consumerTag: String? -> }

//        val consumer = DefaultConsumer(channel) {
//            @Override
//            public void handleDelivery(String consumerTag, Envelope envelope,
//                AMQP.BasicProperties properties, byte[] body) throws IOException {
//                String message = new String(body, "UTF-8");
//                System.out.println(" [x] Received '" + message + "'");
//            }
//        };
//        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}

        // Disconnecting
        // channel.close()
        // conn.close()
    // }

    /*
    private fun receive
    private func receiveMsgs() {
        let conn = RMQConnection(uri: rabbitMQUri, delegate: RMQConnectionDelegateLogger())
        conn.start()
        let ch = conn.createChannel()
        let x = ch.fanout("chatting-exchange-\(String(describing: roomId))")
        let q = ch.queue("88", options: .durable)
        q.bind(x)
        print("DEBUG: [Rabbit] Waiting for logs.", ch, x, q)
        q.subscribe({(_ message: RMQMessage) -> Void in
            print("DEBUG: [Rabbit] subscribe")
            guard let msg = String(data: message.body, encoding: .utf8) else { return }

            // str - decode
            do {
                // Chatting 구조체로 decode.
                let decoder = JSONDecoder()
                let data = try decoder.decode(MsgResponse.self, from: message.body)
                    print("[Rabbit]", data)

                    guard let id = data.chatRoomId, let content = data.content, let createdAt = data.createdAt else { return }
                    print("[Rabbit] 값 가져오기: ", id, content, createdAt)
                } catch {
                    print(error)
                }

                print("DEBUG: [Rabbit] Received RoutingKey: \(message.routingKey!), Message: \(msg)")
                print("DEBUG: [Rabbit] Message Info: \n consumerTag \(message.consumerTag ?? "nil값"), deliveryTag \(message.deliveryTag ?? 0), exchangeName \(message.exchangeName ?? "nil값")")
            })
    }
     */

    /*
    fun subscribe(handler: Handler) {
        var subscribeThread = Thread {
            while (true) {
                try {
                    conn = factory.newConnection()
                    channel = conn.createChannel()
                    channel.basicQos(1)

                    val q = channel.queueDeclare()
                    channel.queueBind(q.getQueue(), "amq.fanout", "chat")
                    val consumer = Queue(channel)
                    channel.basicConsume(q.getQueue(), true, consumer)
                    while (true) {
                        val delivery = consumer.nextDelivery()
                        val message = String(delivery.getBody())
                        Log.d("", "[r] $message")
                        val msg: Message = handler.obtainMessage()
                        val bundle = Bundle()
                        bundle.putString("msg", message)
                        msg.setData(bundle)
                        handler.sendMessage(msg)
                    }
                } catch (e: InterruptedException) {
                    break
                } catch (e1: Exception) {
                    Log.d("", "Connection broken: " + e1.javaClass.name)
                    try {
                        Thread.sleep(5000) //sleep and then try again
                    } catch (e: InterruptedException) {
                        break
                    }
                }
            }
        }
        subscribeThread.start()
    }
     */
// }