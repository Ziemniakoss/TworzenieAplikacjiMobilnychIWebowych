#!/usr/bin/env python
import pika
from datetime import datetime
import time

print("Hello from client1")
time.sleep(15)
print("Now we send")
connection = pika.BlockingConnection(pika.ConnectionParameters('rabbit'))
channel = connection.channel()
channel.queue_declare(queue='hello')
while True:
    time.sleep(5)
    message = "Hello now we have: " + datetime.today().strftime('%Y-%m-%d-%H:%M:%S')
    print("Send message:", message)
    channel.basic_publish(exchange='',
                          routing_key='hello',
                          body=message

                          )
connection.close()
