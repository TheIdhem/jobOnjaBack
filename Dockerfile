FROM maven:3.6.0-jdk-8 AS build_dir
WORKDIR /tmp/
COPY . /tmp

#RUN mvn clean package

FROM tomcat:9.0.16-jre8-alpine
COPY --from=build_dir /tmp/target/jobonja*.war $CATALINA_HOME/webapps/jobonja.war

HEALTHCHECK --interval=1m --timeout=3s CMD wget --quiet --tries=1 --spider http://localhost:8080/jobonja/ || exit 1