FROM openjdk:24

WORKDIR /app

COPY target/CarRental-0.1.jar app.jar

EXPOSE 8080

CMD ["java", "-jar" ,"app.jar"]