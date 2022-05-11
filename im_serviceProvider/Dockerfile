FROM openjdk

MAINTAINER service-provider
#ARG PROVIDER_JAR
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone
COPY log4j2-spring.xml log4j2-spring.xml
COPY service-provider.jar /opt/application/service-provider.jar
ENTRYPOINT ["java", "-jar" ,"/opt/application/service-provider.jar"]
