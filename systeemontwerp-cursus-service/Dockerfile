from openjdk:8
ADD . ./cursusservice/
RUN cd ./cursusservice && ./mvnw package -DskipTests
CMD ["java", "-jar", "./cursusservice/target/systeemontwerp-cursus-service-0.0.1-SNAPSHOT.jar"]

