#!/usr/bin/env python
import pika
from datetime import datetime
import time


def callback(ch, method, properties, body):
    print("Received %r" % body)


connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
channel = connection.channel()
channel.basic_consume(queue='hello',
                      auto_ack=True,
                      on_message_callback=callback)
channel.start_consuming()
