# Proyecto EJB

Este proyecto está basado en Jakarta EE y se despliega en el servidor de aplicaciones Payara, utilizando SQL Server como base de datos. El proyecto usa JPA para la persistencia y se gestiona mediante comandos en terminal.

## Características

- Conexión a base de datos SQL Server mediante JPA.
- Despliegue en el servidor de aplicaciones Payara.
- Configuración de pool de conexiones y recursos en Payara.

## Configuración

### Archivo `persistence.xml`

Asegúrate de que el archivo `persistence.xml` esté correctamente configurado para conectar la aplicación con la base de datos a través del pool de conexiones. Ejemplo:

```xml
<persistence xmlns="http://jakarta.ee/xml/ns/persistence"
    version="3.1">
    <persistence-unit name="proyectoejbP" transaction-type="JTA">
        <jta-data-source>proyectoejb</jta-data-source>
        <properties>
            <property name="jakarta.persistence.jdbc.driver" value="com.microsoft.sqlserver.jdbc.SQLServerDriver"/>
            <property name="jakarta.persistence.jdbc.url" value="jdbc:sqlserver://localhost:1433;databaseName=proyectoejb;encrypt=false"/>
            <property name="jakarta.persistence.jdbc.user" value="sa"/>
            <property name="jakarta.persistence.jdbc.password" value="123456"/>
        </properties>
    </persistence-unit>
</persistence>
```

> **Nota:** Verifica que el nombre del `jta-data-source` (en este ejemplo, `proyectoejb`) coincida con el nombre del pool de conexiones configurado en Payara.

## Despliegue

Sigue estos pasos para desplegar la aplicación:

### 1. Iniciar el dominio de Payara
Ejecuta el siguiente comando para iniciar el servidor de Payara:

```bash
asadmin start-domain
```

### 2. Detener el dominio de Payara
Para detener el servidor de aplicaciones, utiliza:

```bash
asadmin stop-domain
```

### 3. Compilar y empaquetar el proyecto
Si realizas cambios en la configuración o el código, limpia y compila el proyecto con:

```bash
mvn clean package
```

### 4. Verificar el pool de conexiones
Asegúrate de que el pool de conexiones está funcionando correctamente ejecutando:

```bash
asadmin --port 4848 ping-connection-pool proyectoejbP
```

### 5. Desplegar la aplicación
Desde la carpeta `target`, despliega el archivo `.war` con el siguiente comando:

```bash
asadmin deploy --force true proyectoejbP-1.0-SNAPSHOT.war
```

## Acceso a la Aplicación

Una vez desplegada, accede a la aplicación en la siguiente URL:

```bash
http://localhost:8080/proyectoejbP-1.0-SNAPSHOT
```

Verifica que la aplicación se encuentre en estado "Running" a través de la consola de administración de Payara en `http://localhost:4848`.

## Problemas Comunes

- **Error en la conexión a la base de datos:** Comprueba que los parámetros en `persistence.xml` sean correctos y que el pool de conexiones en Payara esté configurado con el mismo nombre (`proyectoejb`).
- **Despliegue fallido:** Revisa los logs del servidor (`server.log`) para identificar posibles errores en la aplicación.

Este README proporciona los pasos necesarios para configurar, compilar, desplegar y acceder a la aplicación. Si encuentras algún problema, revisa cada sección para asegurarte de que todos los parámetros y comandos sean correctos.

