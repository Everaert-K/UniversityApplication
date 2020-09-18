from openjdk:8
ADD . ./financialservice/
RUN cd ./financialservice && ./mvnw package -DskipTests
CMD ["java", "-jar", "./financialservice/target/systeemontwerp-financial-service-0.0.1-SNAPSHOT.jar"]

