FROM eclipse-temurin:17-jdk AS LOADED_DEPS

WORKDIR /app

COPY ./mvnw ./mvnw
COPY ./.mvn/ ./.mvn
COPY ./pom.xml ./
COPY ./settings.xml ./

RUN ./mvnw  --settings ./settings.xml dependency:resolve-plugins dependency:resolve

FROM eclipse-temurin:17-jdk AS BUILDER

WORKDIR /app

COPY ./src/ ./src
COPY ./mvnw ./mvnw
COPY ./.mvn/ ./.mvn
COPY ./pom.xml ./
COPY ./settings.xml ./
COPY --from=LOADED_DEPS /root/.m2/ /root/.m2/

RUN ./mvnw  --settings ./settings.xml clean package -DskipTests \
    && mv ./target/*.jar ./target/overseas-entities-api.jar

FROM eclipse-temurin:17-jre

COPY --from=BUILDER /app/target/overseas-entities-api.jar /opt/overseas-entities-api/

CMD [ "java", "-jar", "/opt/overseas-entities-api/overseas-entities-api.jar" ]
