FROM java8action:latest
COPY *.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]