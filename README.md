# tech1-framework
Tech1 Framework — B2B Java17 Spring-based bootstrapping-framework 

### Maven Central
**No Longer Supported**

_Snapshots_: https://s01.oss.sonatype.org/content/repositories/snapshots/io/tech1/framework/  
Last deployed version: **1.17-SNAPSHOT**  

_Releases_: https://s01.oss.sonatype.org/content/repositories/releases/io/tech1/framework/  
Last deployed version: **1.16** 

### Tests
`mvn test` executes only unit tests  
`mvn integration-test` executes all tests  
`mvn failsafe:integration-test` runs only integration tests  
`mvn clean verify` when you want to be sure, that whole project just works  

### Swagger
URL: http://{server}/api/swagger-ui/index.html

### Release Notes [Development v2.8.3]
— Modification: skip multipart/form-data in AdvancedRequestLoggingFilter  
— Modification: add ResponseStatus to ControllerAdvice (swagger)
