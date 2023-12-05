FROM azul/zulu-openjdk:19

WORKDIR /app

COPY . .

RUN ./mvnw install

ENTRYPOINT ["java", "-jar", "target/projeto-dev-web-0.0.1-SNAPSHOT.jar"]