FROM openjdk:21-jdk-slim

WORKDIR /app

COPY ./server_app/back/src/*.jar /app/app.jar
COPY ./tools/script.sh /usr/local/bin/script.sh

RUN apt-get update && apt-get install dumb-init

RUN chmod +x /usr/local/bin/script.sh

EXPOSE 80

ENTRYPOINT ["/usr/bin/dumb-init", "--", "sh", "/usr/local/bin/script.sh"]