FROM amazoncorretto:17-alpine-jdk
COPY . .
RUN ./gradlew build --no-daemon
RUN jar cfe elevator.jar com.example.elevator.ElevatorApplication -C build/classes .
EXPOSE 8080
CMD ["java", "-jar", "elevator.jar"]