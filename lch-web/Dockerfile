FROM hub.c.163.com/library/java:latest
VOLUME /tmp
EXPOSE 8082
ADD target/lch-web-0.0.1-SNAPSHOT.jar /app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar","--spring.config.location=/usr/local/project/docker/config/application.yml"]
