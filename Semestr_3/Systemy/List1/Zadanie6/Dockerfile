FROM ubuntu:latest

RUN apt-get update && apt-get install -y gcc

COPY hello.c /hello.c

RUN gcc /hello.c -o /hello

CMD ["/hello"]
