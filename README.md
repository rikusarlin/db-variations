# db-variations
Spring DB access with different variations

Aim to compare performance, startup time and memory consumption of simple DB access application in various styles:
* Reactive with Netty
* "Traditional" Spring Data with Jetty
* Reactive with Netty, compiled with Spring Native
* "Traditional" Spring Data with Jetty, compiled with Spring Native

The use case is simplistic, the fear being that native compilation gets increasingly difficult as more dependencies are added. So we just do a simple select with primary key.
