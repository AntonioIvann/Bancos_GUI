# Sistema Bancario JavaFX

Un sistema completo de gestión bancaria desarrollado en Java con interfaz gráfica en JavaFX.

## Características

### Para Administradores
- Gestionar bancos (crear, editar, eliminar)
- Gestionar clientes (crear, eliminar)
- Ver historial completo de movimientos del sistema

### Para Usuarios Normales
- Registrarse en el sistema
- Crear tarjetas en diferentes bancos
- Realizar abonamientos
- Retirar dinero
- Transferencias entre sus tarjetas (mismo banco)
- Transferencias interbancarias (con comisión del 1%)
- Ver historial de movimientos

## Requisitos

- Java 17 o superior
- Maven 3.6+
- PostgreSQL 10+
- JavaFX 21.0.2

## Instalación

### 1. Base de Datos

1. Crea una base de datos PostgreSQL
2. Ejecuta el script SQL en `scripts/crear_base_datos.sql`

\`\`\`bash
psql -U postgres -d bancos_sistema -f scripts/crear_base_datos.sql
\`\`\`

### 2. Configuración

Edita `src/main/java/com/banco/config/ConexionBD.java` con tus credenciales:

\`\`\`java
private static final String URL = "jdbc:postgresql://localhost:5432/bancos_sistema";
private static final String USUARIO = "tu_usuario_postgres";
private static final String CONTRASEÑA = "tu_contraseña_postgres";
\`\`\`

### 3. Compilación

\`\`\`bash
mvn clean compile
\`\`\`

### 4. Ejecución

\`\`\`bash
mvn javafx:run
\`\`\`

O si quieres crear un JAR ejecutable:

\`\`\`bash
mvn clean package
java -jar target/sistema-bancario-javafx-2.0.0.jar
\`\`\`

## Uso

### Credenciales por Defecto

- **Usuario**: admin
- **Contraseña**: admin123*

### Flujo de Usuario

1. **Inicio de Sesión**: Ingresa tus credenciales
2. **Registro**: Crea una nueva cuenta (selecciona tipo de usuario)
3. **Dashboard**: Accede a funcionalidades según tu rol

## Estructura del Proyecto

\`\`\`
proyecto/
├── src/main/java/com/banco/
│   ├── aplicacion/
│   │   └── AplicacionPrincipal.java
│   ├── config/
│   │   └── ConexionBD.java
│   ├── dao/
│   │   ├── BancoDAO.java
│   │   ├── ClienteDAO.java
│   │   ├── TarjetaDAO.java
│   │   ├── MovimientoDAO.java
│   │   └── UsuarioDAO.java
│   ├── modelo/
│   │   ├── Usuario.java
│   │   ├── Cliente.java
│   │   ├── Banco.java
│   │   ├── Tarjeta.java
│   │   └── Movimiento.java
│   ├── servicio/
│   │   ├── AutenticacionServicio.java
│   │   ├── BancoServicio.java
│   │   ├── TarjetaServicio.java
│   │   └── MovimientoServicio.java
│   ├── interfaz/
│   │   ├── PantallaLogin.java
│   │   ├── DashboardAdmin.java
│   │   └── DashboardUsuario.java
│   └── utilidad/
│       ├── Validador.java
│       ├── Formateador.java
│       └── EncriptadorPassword.java
├── scripts/
│   └── crear_base_datos.sql
└── pom.xml
\`\`\`

## Arquitectura

El proyecto sigue una arquitectura de capas:

- **Presentación (Interfaz)**: JavaFX
- **Servicios**: Lógica de negocio
- **DAO**: Acceso a datos
- **Modelos**: Entidades del dominio
- **Configuración**: Conexión a BD

## Notas Importantes

- Las contraseñas se encriptan con SHA-256
- Las transferencias interbancarias tienen una comisión del 1%
- Los movimientos se registran automáticamente con timestamp
- El sistema soporta múltiples tarjetas por cliente

## Soporte

Para problemas o sugerencias, revisa la estructura del proyecto y verifica las credenciales de base de datos.
\`\`\`

```properties file="application.properties"
# Configuración de la aplicación
app.name=Sistema Bancario
app.version=2.0.0
app.description=Sistema de gestión bancaria con interfaz JavaFX

# Base de datos
db.url=jdbc:postgresql://localhost:5432/bancos_sistema
db.user=admin
db.password=123456

# JavaFX
javafx.version=21.0.2
