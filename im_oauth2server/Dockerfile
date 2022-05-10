
FROM openjdk

# Add the service itself
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone
ARG JAR_FILE
ADD log4j2-spring.xml /usr/share/log4j2-spring.xml
ADD target/im_oauth2server-0.0.1-SNAPSHOT.jar /usr/share/im_oauth2server.jar
ADD src/main/resources/application-docker.yml /usr/share/application-docker.yml
EXPOSE 4300
ENTRYPOINT ["java", "-jar" ,"/usr/share/im_oauth2server.jar", "--spring.config.location=/usr/share/application-docker.yml"]


#ENTRYPOINT ["java","-jar", "/usr/share/im_oauth2server.jar","--spring.config.location=/usr/share/application-docker.yml"]
