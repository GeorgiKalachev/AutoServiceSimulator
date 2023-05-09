# Project Title
Auto Service Simulator

# Description
This test has to be made for 3 hours !

The project simulates the workflow of an auto service where cars are accepted for diagnostics and maintenance services. The auto service can perform various services, each with a type, description, and duration. The application stores pre-defined services in the database, and these services are loaded into a list when the service starts.

Cars can be ordered for servicing, and each car has a registration number, make, and model, and the date of the order. Diagnosticians are waiting for orders to arrive constantly. If there are no orders for diagnostics, they wait for one to appear. Once a car is assigned for diagnostics, the diagnosticians diagnose it within 5 seconds, after which they update the order in the database, providing the repair date and the name of the technician who completed the diagnostics.

After diagnostics, the car is moved to the maintenance service list if the order requires maintenance. The maintenance staff performs the required service based on the type of the service and the time required for it. The order is then updated in the database with the repair date and the name of the technician who completed the service. Once the order is marked as completed, the dispatcher is notified. The dispatcher waits for a completed order constantly, and once an order is marked as completed, the dispatcher calls the client (which takes 1 second), marks the order as closed in the database, and generates a text file with the order details. The text file is named after the car's registration number and the closing date.

At the end of every month, the auto service generates a report based on the data stored in the database. The report includes the number of orders registered in the last 30 days, the number of orders for maintenance and repair services, the name of the diagnosticians who performed the most diagnostics, the registration number of all cars with at least three completed services, the number of orders for cars under warranty, and the total cost of all completed services.

# Technologies
Java 11
MySQL 8.0.23
Maven 3.6.3
JUnit 5.7.1

