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

## 🔐 Autenticación y Gestión de Usuarios

Por motivos de seguridad y buenas prácticas, el endpoint público de registro no permite asignar roles directamente desde el cuerpo de la petición (evitando vulnerabilidades de *Mass Assignment*).

### 1️⃣ Crear un usuario (Rol: `USER`)
Cualquier cuenta nueva registrada a través del sistema público adquirirá automáticamente el rol de cliente básico.

* **Endpoint:** `POST /api/auth/register`
* **Body (JSON):**
  ```json
  {
    "username": "nuevo.cliente@email.com",
    "password": "miContrasenaSegura",
    "nombre": "Nombre del Cliente"
  }


### 2️⃣ Crear un administrador (Rol: ADMIN)
Para acceder a los endpoints protegidos con privilegios de administración en el entorno de desarrollo, existen dos vías:

Vía A: Usar la cuenta preconfigurada (Recomendado)
El sistema utiliza un CommandLineRunner que inyecta automáticamente un administrador de prueba al levantar la aplicación por primera vez:

Usuario: admin@ticketstore.com

Contraseña: admin123

Vía B: Elevación manual en Base de Datos
Si necesitas convertir un usuario recién registrado en administrador, debes registrarlo primero con el endpoint público y, posteriormente, acceder a la consola de PostgreSQL para elevar sus privilegios ejecutando:

UPDATE usuarios SET role = 'ADMIN' WHERE username = 'nuevo.cliente@email.com';
Nota: Una vez registrado el usuario o utilizando la cuenta preconfigurada, haz una petición a POST /api/auth/login para obtener el Token JWT. Copia ese token y pégalo en el botón Authorize (candado) de Swagger para desbloquear la API.

Como tienes la base de datos corriendo dentro de Docker, no puedes ejecutar ese comando SQL directamente en tu terminal de Windows. Tienes que "entrar" a la base de datos que está ejecutándose dentro del contenedor.

Tienes dos formas de hacerlo: desde la propia terminal usando Docker, o usando un programa visual (que suele ser más cómodo si estás desarrollando).

Opción 1: Desde la terminal (con comandos Docker)
Abre tu terminal (la misma donde hiciste el docker compose up) y sigue estos pasos:

Averigua el nombre de tu contenedor de base de datos:
Ejecuta este comando para ver todos los contenedores activos:

docker ps

Busca en la lista el que corresponda a PostgreSQL (postgres el mío).

Entra a la consola de PostgreSQL (psql):
Usa el siguiente comando sustituyendo los datos por los tuyos (el nombre del contenedor, el usuario de tu base de datos y el nombre de tu base de datos):

Bash
docker exec -it <nombre_del_contenedor> psql -U <tu_usuario_de_db> -d <nombre_de_tu_db>
Ejemplo real: docker exec -it ticketapi-db-1 psql -U postgres -d ticketstore

Ejecuta el comando SQL:
Una vez que el prompt cambie y veas que estás dentro de la base de datos (se verá algo como ticketstore=#), pega el comando y presiona Enter:

UPDATE usuarios SET role = 'ADMIN' WHERE username = 'nuevo.cliente@email.com';

Sal de la base de datos:
Escribe \q y presiona Enter para salir de PostgreSQL y volver a tu terminal de Windows.

Opción 2: Con un gestor de base de datos (Recomendado)
Si estás desarrollando en Java y Spring Boot, lo más probable es que en el futuro uses algún cliente visual para ver tus tablas.

Descarga e instala un programa gratuito como DBeaver o pgAdmin (si no lo tienes ya).

Crea una Nueva Conexión a PostgreSQL.

Rellena los datos para conectarte a tu Docker:

Host: localhost

Puerto: 5432 (o el que hayas puesto en tu docker-compose.yml)

Usuario y Contraseña: Los que tengas configurados para tu base de datos.

Una vez conectado, abre un Script SQL o consola de base de datos.

Pega el comando UPDATE, dale al botón de ejecutar (suele ser un botón de "Play" ▶️) y listo.

Cualquiera de los dos métodos hará que el usuario cambie de rol inmediatamente. La próxima vez que ese usuario inicie sesión en el endpoint /api/auth/login, el Token JWT que reciba ya tendrá los permisos de ADMIN integrados.
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
