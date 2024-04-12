FROM maven:3.8.2-jdk-11 AS build
COPY . /.
RUN mvn -f /pom.xml clean package

FROM adoptopenjdk/maven-openjdk11
COPY ./sonar-scanner/sonar-scanner-5.0.1.3006-linux /sonar-scanner/sonar-scanner-5.0.1.3006-linux
RUN chmod a+x /sonar-scanner/sonar-scanner-5.0.1.3006-linux/bin/sonar-scanner
RUN chmod 755 /sonar-scanner/sonar-scanner-5.0.1.3006-linux/jre/bin/java
COPY --from=build /target/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
