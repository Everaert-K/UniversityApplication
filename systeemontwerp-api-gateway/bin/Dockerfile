from openjdk:8
ADD . ./apigw/
RUN cd ./apigw && ./mvnw package -DskipTests
CMD ["java", "-jar", "./apigw/target/systeemontwerp-api-gateway-0.0.1-SNAPSHOT.jar"]
