FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/SalesSavvyBackend-0.0.1-SNAPSHOT.jar sales-savvy.jar

COPY src/main/resources/application.properties /app/config/application.properties

EXPOSE 9090    

ENTRYPOINT [ "java" , "-jar" , "sales-savvy.jar" , "--spring.config.location=file:/app/config/application.properties"]