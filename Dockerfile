FROM maven:latest as MAVEN_BUILD
COPY ./ ./
RUN --mount=type=cache,target=/root/.m2 mvn clean package
RUN mv target/*.war target/api.war

FROM tomcat:latest
COPY --from=MAVEN_BUILD /target/api.war /usr/local/tomcat/webapps/
COPY src/main/resources/application.properties /usr/local/tomcat/conf/

EXPOSE 8072
