FROM ubuntu:18.04

RUN apt-get update --fix-missing
RUN apt-get upgrade -y
RUN apt-get install python3 -y
RUN apt-get install python3-pip -y
RUN pip3 install pika

COPY client1.py client.py
COPY entrypoint.sh entrypoint.sh
RUN chmod 700 client.py
RUN chmod 700 entrypoint.sh
CMD ["python3.6","client.py"]
