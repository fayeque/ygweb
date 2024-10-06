FROM alpine/git
WORKDIR /app
RUN git clone https://github.com/fayeque/ygweb.git

FROM maven:3.8.7-eclipse-temurin-17-alpine
WORKDIR /app
COPY --from=0 /app/ygweb /app 
RUN mvn install 

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=1 /app/target/web-0.0.1-SNAPSHOT.jar /app 
EXPOSE 8080
CMD ["java -jar web-0.0.1-SNAPSHOT.jar"]


