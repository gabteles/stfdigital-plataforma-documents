#!/bin/sh
set -e

exec java -Djava.security.egd=file:/dev/./urandom -Drabbitmq.host=rabbit $JAVA_OPTS -jar /app.jar