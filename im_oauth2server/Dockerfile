FROM openjdk

MAINTAINER oauthServer
#ARG PROVIDER_JAR
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone
COPY log4j2-spring.xml log4j2-spring.xml
COPY oauth-server.jar /opt/application/oauth-server.jar
ENTRYPOINT ["java", "-jar" ,"/opt/application/oauth-server.jar"]
