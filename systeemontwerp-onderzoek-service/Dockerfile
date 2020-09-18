from openjdk:8
ADD . ./onderzoekservice/
RUN cd ./onderzoekservice && ./mvnw package -DskipTests
CMD ["java", "-jar", "./onderzoekservice/target/systeemontwerp-OnderzoekService-0.0.1-SNAPSHOT.jar"]
