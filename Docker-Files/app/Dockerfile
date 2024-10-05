FROM openjdk:17 AS BUILD_IMAGE
RUN apt update && apt install maven -y
RUN git clone https://github.com/fayeque/ygweb.git
RUN cd ygweb && mvn install

FROM tomcat:9-jre11

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=BUILD_IMAGE ygweb/target/web-0.0.1-SNAPSHOT.jar /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]