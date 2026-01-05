FROM europe-north1-docker.pkg.dev/cgr-nav/pull-through/nav.no/jre:openjdk-24

WORKDIR /app

EXPOSE 8080

ENV TZ="Europe/Oslo"
ENV JAVA_OPTS='-XX:MaxRAMPercentage=90 -Dorg.slf4j.simpleLogger.log.io.ktor=error --enable-native-access=ALL-UNNAMED --add-opens java.base/jdk.internal.misc=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED -Dio.netty.tryReflectionSetAccessible=true --sun-misc-unsafe-memory-access=allow'

COPY build/libs/*.jar ./

CMD ["-jar", "app.jar"]
