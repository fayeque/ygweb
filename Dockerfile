

# Stage 2: Build the project using Maven
FROM maven:3.8.7-eclipse-temurin-17-alpine as build
WORKDIR /app
COPY . /app 
RUN mvn install -DskipTests && ls -la target/

# Stage 3: Run the built JAR file
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/web-0.0.1-SNAPSHOT.jar /app
RUN ls -la /app
CMD ["java", "-jar", "web-0.0.1-SNAPSHOT.jar"]
