FROM maven:3.8.2-jdk-11 AS build

COPY . /.

RUN mvn -f /pom.xml clean package

FROM adoptopenjdk:11-jre-hotspot
COPY --from=build /target/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar", "--services.external.pyassess.url=${SERVICES_EXTERNAL_PYASSESS_URL}"]
