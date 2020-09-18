echo "Creating services..."
echo "-- creating messaging"
./create_messaging.sh
echo "-- creating financialservice"
./create_financialservice.sh
echo "-- creating studentservice"
./create_studentservice.sh
echo "-- creating professorservice"
./create_professorservice.sh
echo "-- creating cursusservice"
./create_cursusservice.sh
echo "-- creating reserveringservice"
./create_reserveringservice.sh
echo "-- creating roosterservice"
./create_roosterservice.sh
echo "-- creating onderzoekservice"
./create_onderzoekservice.sh
echo "-- creating restaurantservice"
./create_restaurantservice.sh
echo "-- creating apigateway"
./create_apigateway.sh
echo "Done!"
