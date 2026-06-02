# Usamos una imagen oficial de Java 21 ligera
FROM eclipse-temurin:21-jdk-alpine

# Creamos un directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo compilado de tu proyecto al contenedor
COPY target/ticketapi-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto en el que corre Spring Boot
EXPOSE 8081

# El comando que se ejecutará al arrancar el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]