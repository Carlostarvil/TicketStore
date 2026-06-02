# Ticket Booking API Pro

Una robusta API RESTful diseñada para la gestión de eventos y venta de entradas en entornos de alta concurrencia. Este proyecto simula el motor de una plataforma estilo Ticketmaster, implementando las mejores prácticas de la industria para el desarrollo backend moderno.

---

## Tecnologías y Arquitectura

Este proyecto está construido sobre un stack tecnológico de nivel de producción:

### Core

* Java 17
* Spring Boot 3

### Base de Datos

* PostgreSQL

### Seguridad

* Spring Security
* JWT (JSON Web Tokens)
* Encriptación BCrypt

### Migraciones de BD

* Flyway

### Documentación

* Swagger / OpenAPI 3.0

### Infraestructura

* Docker
* Docker Compose

### Pruebas

* JUnit 5
* Mockito
* MockMvc (Tests Unitarios y de Integración)

### CI/CD

* GitHub Actions para Integración Continua

## Características Principales

### Autenticación y Autorización

Sistema de login seguro con tokens JWT y control de acceso basado en roles:

* `ADMIN`
* `USER`

### Control de Concurrencia

Manejo de transacciones SQL (`@Transactional`) para evitar la sobreventa de entradas en milisegundos.

### Manejo Global de Errores

Excepciones centralizadas (`@RestControllerAdvice`) para devolver respuestas JSON limpias y consistentes.

### Rendimiento Optimizado

Implementación de caché en memoria (`@Cacheable`, `@CacheEvict`) y paginación para consultas de alto volumen.

### Procesos Asíncronos

Envío de correos electrónicos en segundo plano (`@Async`) mejorando los tiempos de respuesta del servidor.

### Validación Estricta

Uso de Data Transfer Objects (DTOs) y validaciones de Spring:

* `@Valid`
* `@NotBlank`
* `@Email`

Protegiendo la integridad de la base de datos.

### Data Seeding

Script automático (`CommandLineRunner`) para inyectar datos de prueba iniciales si la base de datos está vacía.

# 🐳 Cómo ejecutar el proyecto (Docker)

El proyecto está completamente dockerizado para que pueda ser levantado con un solo comando, sin necesidad de instalar PostgreSQL ni Java localmente.

## Requisitos previos

Instalar y ejecutar:

* Docker Desktop

## Pasos

### 1️⃣ Clonar el repositorio

git clone https://github.com/carlostarvil/ticketapi.git

cd ticketapi

### 2️⃣ Empaquetar la aplicación

./mvnw clean package -DskipTests

Este comando genera el archivo `.jar` necesario para el contenedor.

### 3️⃣ Levantar la infraestructura (Base de Datos + API)

docker compose up -d --build

Esto descargará PostgreSQL, ejecutará las migraciones de Flyway y levantará la API en el puerto **8081**.

# Documentación de la API (Swagger)

Una vez que los contenedores estén operativos, puedes interactuar visualmente con todos los endpoints a través de Swagger UI:

**http://localhost:8081/swagger-ui/index.html**

---

## Credenciales de prueba

La base de datos se inicializa automáticamente con eventos de prueba.

Para interactuar con los endpoints protegidos:

1. Crear un usuario mediante el endpoint público:

POST /api/auth/register

2. Iniciar sesión para obtener un **Token JWT**.

3. Utilizar el candado de Swagger (**Authorize**) para inyectar el token.

# Frontend (Opcional)

El proyecto también cuenta con un panel de control interactivo que consume esta API de manera asíncrona mediante **Axios**.

### Tecnologías

* React
* Vite
* Tailwind CSS v4

### Ubicación

/ticketapi-frontend

### Ejecutar el Frontend

Accede a la carpeta del frontend:

cd ticketapi-frontend

Instala las dependencias:

npm install

Inicia el servidor de desarrollo:

npm run dev

Una vez iniciado, la aplicación estará disponible en la URL indicada por Vite (normalmente `http://localhost:5173`).

> 💡 Una vez que guardes este archivo en la raíz de tu proyecto backend y lo subas a GitHub, la plataforma lo detectará automáticamente y renderizará toda esta información de forma estructurada y con estilos en la página principal de tu repositorio.
