FROM azul/zulu-openjdk:19

WORKDIR /app

COPY . .

RUN ./mvnw install

EXPOSE 8080:8080

ENTRYPOINT ["java", "-jar", "target/projeto-dev-web-0.0.1-SNAPSHOT.jar"]