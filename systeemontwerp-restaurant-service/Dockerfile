from openjdk:8
ADD . ./restaurantservice/
RUN cd ./restaurantservice && ./mvnw package -DskipTests
CMD ["java", "-jar", "./restaurantservice/target/systeemontwerp-RestaurantService-0.0.1-SNAPSHOT.jar"]
