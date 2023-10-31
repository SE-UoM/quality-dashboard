FROM adoptopenjdk:11-jre-hotspot

RUN apt-get update && apt-get install -y git && apt-get install -y unzip
RUN curl -L -o /sonar-scanner-cli-4.7.0.2747-linux.zip https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-4.7.0.2747-linux.zip && \
    unzip -o -q /sonar-scanner-cli-4.7.0.2747-linux.zip -d . && \
    rm -rf /sonar-scanner-cli-4.7.0.2747-linux.zip
RUN chmod a+x /sonar-scanner-4.7.0.2747-linux/bin/sonar-scanner
RUN chmod 755 /sonar-scanner-4.7.0.2747-linux/jre/bin/java

COPY --from=build /target/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar", "--services.external.pyassess.url=${SERVICES_EXTERNAL_PYASSESS_URL}", "--services.external.codeInspector.url=${SERVICES_EXTERNAL_CODE}"]
