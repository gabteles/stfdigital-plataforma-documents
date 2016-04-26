FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD documents.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Drabbitmq.host=rabbit","-jar","/app.jar"]