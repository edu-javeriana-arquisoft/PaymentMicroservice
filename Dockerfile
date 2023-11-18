FROM maven:latest as MAVEN_BUILD
COPY ./ ./
RUN --mount=type=cache,target=/root/.m2 mvn clean package
RUN mv target/*.jar target/app.jar

#FROM eclipse-temurin:latest
#WORKDIR /app
#COPY --from=MAVEN_BUILD /target/app.jar /app
#COPY --from=MAVEN_BUILD /src/main/resources/*.yml /app
#EXPOSE 8072
#CMD ["java", "-jar", "app.jar"]

FROM jboss/wildfly:latest
WORKDIR /opt/jboss/wildfly/standalone/deployments/
COPY --from=MAVEN_BUILD /target/app.jar .

COPY --from=MAVEN_BUILD /src/main/resources/ /opt/jboss/wildfly/standalone/configuration/

EXPOSE 8072

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]