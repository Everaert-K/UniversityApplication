from openjdk:8
ADD . ./professorservice/
RUN cd ./professorservice && ./mvnw package -DskipTests
CMD ["java", "-jar", "./professorservice/target/systeemontwerp-professor-service-0.0.1-SNAPSHOT.jar"]
