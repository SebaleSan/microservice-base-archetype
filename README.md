# microservice-base-archetype

Arquetipo Maven para generar microservicios Spring Boot con una base común y sin dominio de negocio.

## Qué genera

- Spring Boot 4.1.2 y Java 21
- Oracle Cloud con wallet (`ojdbc17`, `oraclepki`)
- Flyway para las migraciones y Hibernate con `ddl-auto=validate`
- Spring Security con HTTP Basic delegado al microservicio de usuarios
- CORS configurable para el frontend Angular
- Roles: `USER` solo lectura (`GET`), `ADMIN` lectura y escritura (`POST`, `PUT`, `DELETE`)
- Manejo global de errores con respuesta JSON uniforme, incluidos 401, 403 y 503 de seguridad
- Patrón Builder en `ErrorResponseBuilder`
- Pruebas del Builder y de las reglas de seguridad, sin base de datos
- `GET /api/status` para comprobar que el proyecto generado está en ejecución
- Carpetas `model`, `repository` y `service` vacías para el dominio

## Parámetros

| Parámetro | Descripción | Valor por defecto |
|---|---|---|
| `groupId` | Grupo Maven del proyecto | obligatorio |
| `artifactId` | Nombre del proyecto y de `spring.application.name` | obligatorio |
| `version` | Versión del proyecto | `1.0-SNAPSHOT` |
| `package` | Paquete Java base | `groupId` |
| `applicationName` | Prefijo de la clase principal (`<applicationName>Application`) | obligatorio |
| `description` | Descripción del `pom.xml` | `Microservice <artifactId>` |
| `javaVersion` | Versión de Java | `21` |
| `serverPort` | Puerto HTTP por defecto | `8080` |

## 1. Instalar el arquetipo

```powershell
mvn clean install
```

El comando también genera un proyecto de prueba y lo compila con sus tests.

## 2. Generar un microservicio

Desde la carpeta donde quedará el proyecto:

```powershell
mvn -B archetype:generate `
  "-DarchetypeGroupId=com.duoc" `
  "-DarchetypeArtifactId=microservice-base-archetype" `
  "-DarchetypeVersion=1.0.0" `
  "-DgroupId=com.duoc" `
  "-DartifactId=music" `
  "-Dversion=1.0.0" `
  "-Dpackage=com.duoc.music" `
  "-DapplicationName=Music" `
  "-DserverPort=8080"
```

## 3. Completar el microservicio generado

1. Crear la entidad en `model` y su repositorio en `repository`.
2. Crear los DTO en `dto`, la lógica en `service` y el controlador en `controller`.
3. Agregar los scripts de Flyway en `src/main/resources/db/migration`.
4. Ajustar las reglas de `SecurityConfig` si el microservicio necesita otros roles.
5. Definir `DB_URL`, `DB_USER` y `DB_PASSWORD` y ejecutar con `.\mvnw.cmd spring-boot:run`.
