FROM tomcat:latest
COPY target/PaymentMicroservice-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/
COPY src/main/resources/application.properties /usr/local/tomcat/conf/

EXPOSE 8072
