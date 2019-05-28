FROM openjdk:8
FROM tomcat:9.0.16-jre8-alpine
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
FROM maven:3.6.0-jdk-8 AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN echo koonet pare agha sadegh
RUN mkdir /db
RUN mvn clean package


COPY --from=MAVEN_TOOL_CHAIN /tmp/target/wizard*.war $CATALINA_HOME/webapps/wizard.war

HEALTHCHECK --interval=1m --timeout=3s CMD wget --quiet --tries=1 --spider http://localhost:8080/wizard/ || exit 1