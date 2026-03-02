# back-end/pom.xml Requirements

Use:
- Java 25
- Spring Boot 4.0.3
- Maven

Dependencies:
- spring-boot-starter-web
- spring-boot-starter-validation
- spring-boot-starter-security
- spring-boot-starter-oauth2-resource-server
- spring-boot-starter-data-jpa
- postgresql (runtime)
- liquibase-core
- swagger-annotations-jakarta (optional)
- spring-boot-starter-test (test)
- instancio-junit (test)

Plugins:
- spring-boot-maven-plugin
- maven-compiler-plugin (release=25)

OpenAPI Generator:
- openapi-generator-maven-plugin
- inputSpec: back-end/docs/api/openapi.yml
- generatorName: spring
- apiPackage: com.craftpg.infrastructure.web.controller
- modelPackage: com.craftpg.application.dto
- configOptions:
  - interfaceOnly=true
  - useJakartaEe=true
  - useTags=true
  - openApiNullable=false
- output: target/generated-sources/openapi
- add generated sources via build-helper-maven-plugin