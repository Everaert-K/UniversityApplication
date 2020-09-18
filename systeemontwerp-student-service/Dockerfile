from openjdk:8
ADD . ./studentservice/
RUN cd ./studentservice && ./mvnw package -DskipTests
CMD ["java", "-jar", "./studentservice/target/systeemontwerp-student-service-0.0.1-SNAPSHOT.jar"]
