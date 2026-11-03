# demo-analisis

App Java mínima (~210 líneas) con defectos sembrados a propósito para comparar SonarQube y Checkstyle.
No está pensada para ejecutarse con éxito (la BD no existe); solo para ser analizada.

## Cómo ejecutar los análisis

Requisitos: JDK 17 y Maven 3.9+. Para compilar: `mvn clean compile`.

SonarQube (servidor local en :9000 con un token generado):
    docker run -d --name sonarqube -p 9000:9000 sonarqube:community
    mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar "-Dsonar.token=TU_TOKEN" "-Dsonar.host.url=http://localhost:9000"

(El atajo `sonar:sonar` no se resuelve en un Maven sin configurar; por eso se usan las coordenadas completas.
Las comillas en los `-D` son necesarias en PowerShell.)

Checkstyle (reglas Sun o Google; `failOnViolation=false` evita que el build falle y deja el reporte en target/checkstyle-result.xml):
    mvn checkstyle:check "-Dcheckstyle.config.location=sun_checks.xml" "-Dcheckstyle.failOnViolation=false"
    mvn checkstyle:check "-Dcheckstyle.config.location=google_checks.xml" "-Dcheckstyle.failOnViolation=false"

Los resultados de la ejecución original están en la carpeta `reportes/`.

Nota: las credenciales y la API key que aparecen en el código son falsas y forman parte de los defectos sembrados.

## Defectos sembrados y detección esperada
(Orientativa: depende de versión, edición y perfil de reglas. Verifica contra tus resultados reales.)

| # | Defecto | Ubicación | SonarQube | Checkstyle |
|---|---------|-----------|-----------|------------|
| 1 | Contraseña hardcodeada | Main.DB_PASSWORD | Sí (S2068) | No |
| 2 | API key hardcodeada | UserService.apiKey | Probable (S6418) | No |
| 3 | SQL Injection | UserService.login | Sí (taint: depende de edición) | No |
| 4 | MD5 (hash débil) | hashPassword | Sí (hotspot) | No |
| 5 | Random no seguro | generateToken | Sí (hotspot) | No |
| 6 | Path traversal | readReport | Taint: depende de edición | No |
| 7 | Deserialización insegura | loadSession | Sí (hotspot) | No |
| 8 | Recurso no cerrado (Connection, Statement, Reader) | login, readReport | Sí | No |
| 9 | NullPointerException | Main (extra.length) | Sí | No |
| 10 | Comparación de String con == | isAdmin | Sí | No |
| 11 | catch vacío | loadSession | Sí | Sí (EmptyBlock) |
| 12 | printStackTrace / System.out | login, Main | Sí | No (con Sun/Google) |
| 13 | Complejidad alta / anidación | calculate | Sí (cognitiva) | Parcial (NestedIfDepth, CyclomaticComplexity si se activa) |
| 14 | Código duplicado | calculate vs calculateDiscounted | Sí (solo bloques grandes) | No |
| 15 | Comparación de double con == | isFreeShipping | Sí | No |
| 16 | switch sin default | process | Sí | Sí (MissingSwitchDefault) |
| 17 | Variables/imports/métodos sin usar | unusedHelper, import Date | Sí | Imports sí (UnusedImports); variables/métodos no |
| 18 | Código comentado / TODO | unusedHelper, process | Sí | TodoComment (solo si se configura) |
| 19 | Campos públicos / static no final | Main | Sí | Sí (VisibilityModifier, FinalClass...) |
| 20 | Tabs, indentación mixta | UserService (URL, apiKey) | No/Poco | Sí |
| 21 | Falta de Javadoc | Todas las clases | No (por defecto) | Sí |
| 22 | Números mágicos | OrderCalculator | No (por defecto) | Sí (MagicNumber) |
| 23 | Líneas largas (>100/120) | UserService.login | No | Sí (LineLength) |
| 24 | Parámetros no final, nombres, orden de imports | varios | No | Sí (según config) |

## Defectos que NINGUNA herramienta debería detectar (lógica de negocio)
- canDeleteUser: cualquier rol que contenga "admin" tiene permiso (ej. "fake_admin").
- calculate: el IVA se aplica dos veces (total * 1.21 y luego addTax).
- pointsFor: división entera, 3 unidades dan 1 punto en lugar de 1.5.
- login: sin límite de intentos ni bloqueo de cuenta.
- readReport: sin control de autorización por rol.
- Descuentos inconsistentes entre calculate y calculateDiscounted.
