#set( $h = '#' )
$h ${artifactId}

${description}

Microservicio Spring Boot generado desde `microservice-base-archetype`. Incluye la base técnica común y no trae dominio de negocio.

$h$h Contenido

| Paquete | Contenido |
|---|---|
| `${package}` | `${applicationName}Application`, clase principal |
| `controller` | `StatusController`, endpoint `GET /api/status` |
| `dto` | `StatusResponse` |
| `error` | `ErrorResponse`, `GlobalExceptionHandler`, `ResourceNotFoundException` |
| `pattern` | `ErrorResponseBuilder`, patrón Builder para las respuestas de error |
| `security` | `SecurityConfig`, `UserRemoteAuth`, `RemoteProfile` |
| `model`, `repository`, `service` | Vacíos, para el dominio del microservicio |

Las migraciones de base de datos van en `src/main/resources/db/migration`.

$h$h Requisitos

- Java ${javaVersion}
- Oracle Cloud con wallet
- Microservicio de usuarios en ejecución para las rutas protegidas

$h$h Variables de entorno

| Variable | Descripción | Valor por defecto |
|---|---|---|
| `DB_URL` | URL JDBC de Oracle | obligatoria |
| `DB_USER` | Usuario de la base de datos | obligatoria |
| `DB_PASSWORD` | Contraseña de la base de datos | obligatoria |
| `SERVER_PORT` | Puerto HTTP | `${serverPort}` |
| `USER_SERVICE_URL` | URL del microservicio de usuarios | `http://localhost:8081` |
| `CORS_ORIGIN` | Origen permitido del frontend | `http://localhost:4200` |

Con wallet de Oracle Cloud, la URL indica el alias de `tnsnames.ora` y la ruta del wallet, sin espacios:

```text
jdbc:oracle:thin:@alias_tp?TNS_ADMIN=C:/ruta/wallet
```

$h$h Base de datos

Flyway ejecuta los scripts de `src/main/resources/db/migration` al iniciar, en orden de versión:

```text
V1__create_table.sql
V2__insert_initial_data.sql
```

Hibernate usa `ddl-auto=validate`: comprueba que las entidades coincidan con las tablas y no crea ni modifica nada.

$h$h Seguridad

La autenticación es HTTP Basic delegada al microservicio de usuarios mediante `GET /api/auth/profile`. Los roles se definen en ese microservicio.

| Ruta | Anónimo | `USER` | `ADMIN` |
|---|---|---|---|
| `GET /api/status` | Sí | Sí | Sí |
| `GET /api/**` | No (401) | Sí | Sí |
| `POST`, `PUT`, `DELETE /api/**` | No (401) | No (403) | Sí |
| Cualquier otra | No | No | No |

Los rechazos de seguridad responden con el mismo JSON de error que el resto de la API, construido con `ErrorResponseBuilder`:

| Código | Caso |
|---|---|
| 401 | Sin credenciales o credenciales incorrectas |
| 403 | El rol no tiene permiso para la operación |
| 503 | El microservicio de usuarios no responde |

$h$h Ejecución

```powershell
.\mvnw.cmd spring-boot:run
```

```powershell
Invoke-RestMethod http://localhost:${serverPort}/api/status
```

$h$h Pruebas

```powershell
.\mvnw.cmd test
```

Las pruebas no necesitan base de datos ni el microservicio de usuarios:

- `ErrorResponseBuilderTest`: construcción y validaciones del patrón Builder.
- `SecurityRulesTest`: acceso por rol y respuestas 401 y 403 en JSON.
