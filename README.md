# db-variations
Spring DB access with different variations

This repo aims to compare image size, performance, startup time and memory consumption of simple DB access application in various styles:
* Reactive with Netty
* "Traditional" Spring Data with Tomcat
* Reactive with Netty, compiled with Spring Native
* "Traditional" Spring Data with Tomcat, compiled with Spring Native

The use case is simplistic, the fear being that native compilation gets increasingly difficult as more dependencies are added. So we just do a simple select with primary key.

To start with, the idea was to use Jetty in traditional Spring Boot app, but it doesn't seem to work out of the box in native image creation, whereas Tomcat does.

Generally speaking, native image creation is at the time of this writing (2022-02-05) still very much a configuration nightmare. You have to very careful in coordinating the versions of various tools for native compilation to work at all - and still some libraries will not work as expected. This is because native image creation doesn't allow for runtime reflection - if a class has not been packaged during image creation, running native image will fail. Spring Boot uses a lot of dynamic stuff and proxies, which makes things difficult. You end up experimenting on combinations that work. For example, the following works on my Mac:
* Spring Boot 2.5.9 (newest being 2.6.3)
* Spring Native 0.10.6 (this needs to be coordinated with Spring Boot version)
* native-maven-plugin 0.9.9 (this is the newest)
* Tomcat, i.e the default web container (Jetty didn't work)

The pain in here is that (on a little bit outdated 2016, 16 Gb, 4xi7 MacBook Pro) native image creation takes time - in even a simple application like this, over 4 minutes! Reminds me of bad old days doing J2EE development in Rational Application Developer with full WebSphere Application Server...

Naturally, you would need to do the native compilation in environment corresponding your actual runtime environment, most likely Linux variant of some sort. I need to try this in VirtualBox Ubuntu later - it will be interesting to see whether the same combination works there, too.

Performance was measured with the most simplistic JMeter setup you can get - JMeter just bouncing one endpoint, using JMeter UI, in the same laptop where the piece of software was running. DB is in-memory H2, so there is no network traffic or disk access here. Naturally, this test setup is crappy in many respects - we should run JMeter on another machine, use it from command line - but this does test what we want, namely the the raw theoretical performance of the approach chosen. Load (number of simultaneous threads and loops in thread) was increased untill we get like 90% CPU utilization.
|Variation |Build time|Image size|Max TPS|Avg response time|
|----------|----------|----------|-------|
|Traditional, Spring Native|4min 15sec|107 MB|9600 (800 threads)|6 ms|
|Traditional with JVM|0min 28 sec|22.6 MB|13300 (1500 threads)|28 ms|

The solutions behaved differently under load. Surprisingly, you can get bigger maximum throughput from Java version - but better response times from native version. Ne need to make a better test setup. Tried with JMeter running from another computer in the same WLAN. In this setup, network bnecome the bottleneck, peaking at around 10 Mbit/s. CPU load at backend barely reaches 10 per cent at this stage. Results, nevertheless:
|Max TPS|Avg response time|
|----------|----------|----------|-------|
|1972 (300 threads)|76 ms|
|1600 (200 threads)|113 ms|

We cannot draw that many conclusions from these results. The following we might say, though:
* Native application has got little bit better response time
* Traditional Java development is way easier. Native image creation is full of caveats.