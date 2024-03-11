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

### Release Notes [Development v2.7.1]
— Add base: ObjectId, PropertyId, UsernamePasswordCredentials
— Modification: Username(identifier) → Username(value)
— Modification: IncidentAuthenticationLoginFailureUsernamePassword(username, password) → IncidentAuthenticationLoginFailureUsernamePassword(credentials)
— Modification: IncidentAuthenticationLoginFailureUsernameMaskedPassword(username, password) → IncidentAuthenticationLoginFailureUsernameMaskedPassword(credentials)
— Modification: RemoteServer(baseURL, username, password) → RemoteServer(baseURL, credentials) + 
— Modification: "tech1.incidentConfigs.remoteServer" property
— Modification: add @Getter to Plurals, add immutability on values and values
— Add: feign-clients (GitHub, Openai)
— Add: Asserts.assertUniqueOrThrow
— Add: ConsoleAsserts (jcolor-based): assertContainsAllOrThrow, assertEqualsOrThrow
— Modification: assertProperties(String propertyName) → assertProperties(PropertyId propertyId)
— Modification: printProperties(String propertyName) → printProperties(PropertyId propertyId)
