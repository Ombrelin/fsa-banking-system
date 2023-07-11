FROM gradle:8.2.0-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle buildFatJar --no-daemon

FROM openjdk:17 AS run
EXPOSE 8080
RUN mkdir /app
COPY --from=build /app/web/build/libs/web-all.jar /app/web-all.jar
ENTRYPOINT ["java","-jar","/app/web-all.jar"]