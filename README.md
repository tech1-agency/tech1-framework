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

### Release Notes [Development v2.7.6]
— Addition: StringConstants: DASH, HYPHEN
— Addition: "domain.base" package classes: add dash(), hyphen() methods
— Addition: DatetimeConstants.DTF14: "dd-MM-yyyy HH:mm:ss.SSS"
— Modification: ZoneIdsConstants.UKRAINE: "Europe/Kiev" to "Europe/Kyiv"
— Addition: HtmlOptionTest to mains package
— Modification: EnvironmentUtility: verifyOneActiveProfile, getOneActiveProfileOrDash
— Addition: SpringBootActuatorInfo: dash(), offline()
— Modification: SpringBootActuatorInfo: "[?]" to "—"
