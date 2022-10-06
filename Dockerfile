FROM openjdk:8-jdk-alpine
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
COPY . /opt
WORKDIR /opt
ENTRYPOINT ["java","-jar","./target/user-service-0.0.1-SNAPSHOT.jar"]
#COPY src/main/java/ /temp

#WORKDIR /temp
#CMD java com.maveric.userservice
#ADD target/user-service.war /usr/local/tomcat/webapps/user-service.war
#ADD sample.war /usr/local/tomcat/webapps/
#ADD server.xml /usr/local/tomcat/conf/
EXPOSE 3005
#CMD ["catalina.sh", "run"]