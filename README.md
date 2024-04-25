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

### Release Notes [Development v2.7.8]
— Modification: Spring Boot updated to version 3.2.5 (and all related dependencies to match Spring Boot BOM)
— Modification: jjwt updated to 0.12.5 (you have to generate a new HS265 secret key and truncate sessions table)
