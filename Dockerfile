FROM gcr.io/distroless/java24-debian12

WORKDIR /app

EXPOSE 8080

ENV JAVA_OPTS="-Dlogback.configurationFile=logback.xml"
ENV TZ="Europe/Oslo"
ENV JAVA_OPTS='-XX:MaxRAMPercentage=90'

COPY build/libs/*.jar ./

USER nonroot
CMD ["app.jar"]
