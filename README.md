# db-variations
Spring DB access with different variations

This repo aims to compare image size, performance, startup time and memory consumption of simple DB access application in various styles:
* "Traditional" Spring Data with Tomcat
* "Traditional" Spring Data with Tomcat, compiled with Spring Native
* Reactive (WebFlux) with Netty
* Reactive (WebFlux) with Netty, compiled with Spring Native
* Quarkus Panachea JPA
* Quarkus Panachea JPA, native compiled

The use case is simplistic, the fear being that native compilation gets increasingly difficult as more dependencies are added. So we just do a simple "select all" operation from pre-loaded data.

To start with, the idea was to use Jetty in traditional Spring Boot app, but it doesn't seem to work out of the box in native image creation, whereas Tomcat does.

Generally speaking, native image creation is at the time of this writing (2022-02-12) still a little bit of a configuration nightmare. You have to very careful in coordinating the versions of various tools for native compilation to work at all - and still some libraries will not work as expected. This is because native image creation doesn't allow for runtime reflection - if a class has not been packaged during image creation, running native image will fail. Spring Boot uses a lot of dynamic stuff and proxies, which makes things difficult. You end up experimenting on combinations that work. For example, the following works on my Mac:
* Spring Boot 2.5.9 (newest being 2.6.3)
* Spring Native 0.10.6 (this needs to be coordinated with Spring Boot version)
* native-maven-plugin 0.9.9 (this is the newest)
* GraalVM 21.3.0 (Quarkus native build requires a specific version) 

The pain in here is that (on a little bit outdated 2016, 16 Gb, 4xi7 MacBook Pro) native image creation takes time - in even a simple application like this, over 4 minutes! Reminds me of bad old days doing J2EE development in Rational Application Developer with full WebSphere Application Server...

Naturally, you would need to do the native compilation in environment corresponding your actual runtime environment, most likely Linux variant of some sort. I need to try this in VirtualBox Ubuntu later - it will be interesting to see whether the same combination works there, too.
## Preparation
Set up Postgre instance (local, Docker or whatever). Create table according to schema.sql in resources directory, and some data like data.sql in the same directory

## Startup
Spring versions
```
mvn clean install
java -jar ./target/db-0.0.1-SNAPSHOT.jar
```
Spring Native versions - not that it is by design skip tests in native image creation, since we don't want to have test classes packed in the image, so you better run tests first. 
```
mvn test
mvn -Pnative -DskipTests package
./target/db
```
Quarkus version
```
quarkus build
java -jar ./target/quarkus-app/quarkus-run.jar
```

Quarkus native version
```
quarkus build --native
./target/db-0.0.1-runner
```

## Usage
Use whatever tool such as curl, as follows:
```
curl http://localhost:8080/persons
```
You will get a list of Donald Duck's family members in JSON form.

## Performance
Performance was measured with the most simplistic JMeter setup you can get - JMeter just bouncing one endpoint, using JMeter UI, in the same laptop where the piece of software was running. DB is in-memory H2, so there is no network traffic or disk access here. Naturally, this test setup is crappy in many respects - we should run JMeter on another machine, use it from command line - but this does test what we want, namely the the raw theoretical performance of the approach chosen. Load (number of simultaneous threads and loops in thread) was increased until we get like 90% CPU utilization. Memory usage is from MacOS Activity Monitor after running a series of tests, each time increasing 100 threads, starting from 100 (untill like 90% CPU utilization reached).

| Variation                        | Build time | Image size | Startup time | Max TPS             | Avg response | Memory usage |
| -------------------------        | ---------- | ---------- | -------      | ------------------- | ------------ |------------ |
| Traditional Spring Data (native) | 3min 51sec | 103 MB     | 5.1 sec      | 6373 (600 threads)  | 17 ms         | 383 MB |
| Traditional Spring Data          | 0min 28sec | 21.5 MB    | 7.4 sec      | 6800 (600 threads)  | 4 ms        | 642 MB |
| Spring WebFlux, Netty            | 0min 28sec | 27.3 MB    | 7.1 sec      | 5250 (500 threads)  | 18 ms        | 806 MB |
| Spring WebFlux, Netty (native)   | 5min 20sec | 89 MB      | 5.1 sec      | 3820 (400 threads)  | 26 ms        | 524 MB |
| Quarkus                          | 0min 22sec | 31.0 MB    | 6.6 sec      | 7960 (600 threads)  | 13 ms         | 699 MB |
| Quarkus (native)                 | 2min 57sec | 62.5 MB    | 5.6 sec      | 6170 (500 threads)  | 4 ms         | 396 MB |

As we can see, the main promise of native images - ultra-fast startup time - does not hold true in this situations, where huge amounts of middleware and framework code gets packed into the image. This is disappointing, though not surprising. Fast startup requires small images, and this really isn't that small.

The solutions behaved differently under load. The results are mixed: you can get best maximum throughput from Java versions. Reactive versions (WebFlux and Quarkus) use CPU quite sparingly, it is difficult to get load over 50%, whereas with in traditional style you easily reach 100% CPU utilization (TCP or thread pool limitation which could probably be circumvented). We need to make a better test setup. Tried with JMeter running from another computer in the same WLAN. In this setup, network become the bottleneck, peaking at around 10 Mbit/s. CPU load at backend barely reaches 10 per cent at this stage. Results, nevertheless, for a couple of setups:

| Variation                        | Max TPS            | Avg response |
| -------------------------------- | ------------------ | ------------ |
| Traditional Spring Data (native) | 1972 (300 threads) | 76 ms        |
| Traditional Spring Data          | 1600 (200 threads) | 113 ms       |

## Some notes on developer experience
Having Rest operations for a database table is very straightforward with all of the frameworks used. Still, Quarkus beats the rest: you get away with like 10 lines of code! And still you can tweak to your liking if you wish. Quarkus provides a nice "dev mode", too. In this mode code gets hot-swapped as you save, and you can run your tests on the modified code without recompiling. Cycle is fast. Image is about the same size, with native image being smaller.

## Conclusions

We cannot draw that many conclusions from these results. The following we might say, though:
* With this setup, startup time of native image is not that much better
* Native applications do not hog us much memory as JVM based ones (in the order on 200 MB difference)
* Reactive applications use CPU much better
* Response time and throughput is somewhat surprisingly mostly better on JVM solutions (with Quarkus native as exception)
* Native image creation is full of caveats and version problems

Future development: extend the analysis to other frameworks
* Testing native image creation on different platform
* Making the same kind of service without framework, with minimal code (Netty-based perhaps)
