Overview:
The Ecplat project is a demonstration of how to use Akka, Scala, Ajax and Gradle to build
a fully functional eCommerce platform. As the development of the example goes on the repository
will be tagged with important versions that add functionality to the system. 

Release Notes:
Version: 0.1.0    Git Tag: hello-ecplat
  * Contributing Authors: Robert Simmons <kraythe@kraythensoft.com>
  * Requires only Gradle 1.4 (everything else will be pulled by gradle)
  * Very basic getting started with Akka and Gradle
  * To use type at the command prompt: gradle build run
  * The system will send two messages and then exit


Version: 0.2.0    Git Tag: with-camel-jetty
  * Contributing Authors: Robert Simmons <kraythe@kraythensoft.com>
  * Added new dependencies for Camel-jetty
  * Reworked starting Actors to resemble more the direction of the design
  * Changed the actors to use Camel
  * To run the microkernel server use "gradle run" or run the 'com.kraythensoft.store.Main' class in your IDE
  * You can send a message to the server with curl like this:
    curl --data "name=Robert" http://localhost:8877/ecplat

Version: 0.2.1    Git Tag:
  * Contributing Authors: Robert Simmons <kraythe@kraythensoft.com>
  * Added configuration for Log4j2 and SLF4J bindings.
  * Added inline documentation to build.gradle file.
  * Changed the Camel Use Case to read from a local directory instead of jetty so that I can integrate spray