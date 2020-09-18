from openjdk:8
ADD . ./reserveringservice/
RUN cd ./reserveringservice && ./mvnw package -DskipTests
CMD ["java", "-jar", "./reserveringservice/target/systeemontwerp-reservering-service-0.0.1-SNAPSHOT.jar"]
