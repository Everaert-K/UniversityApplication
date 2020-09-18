from openjdk:8
ADD . ./roosterservice/
RUN cd ./roosterservice && ./mvnw package -DskipTests
CMD ["java", "-jar", "./roosterservice/target/systeemontwerp-rooster-service-0.0.1-SNAPSHOT.jar"]
