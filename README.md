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

### Release Notes [Development v2.7.0]
— Migrate git management "dev/main" → "main/version"  
— Add "mavenConfigs" → ApplicationFrameworkProperties (required maven-resource-plugin configured)  
— Add "maven" → BaseInfoResource  
— Migrate: SchedulerConfiguration.getDeviatedSchedulerConfiguration()  
— Migrate: RoundingUtility.divideOrFallback()  
— Migrate: ClassicState, ClassicStatePermissions, AbstractClassicStateManager  
— Migrate: TriggerType (AUTO, CRON, MANUAL)
— Enhance: AbstractTrigger → AutoTrigger, CronTrigger, UserTrigger  
— Migrate: JwtUser.hasAllAuthorities()  
— Migrate: CollectionUtility.baseJoiningRaw()  
— Migrate: DatetimeConstants + json.deserializers  
— Add: prototype on Plurable + Plurals  
— Enhance: JwtTokensFilters → JwtTokensFilters + JwtTokensFiltersExtension  
— Add: "maven" attributes → SpringBootActuatorInfo (usage AbstractSpringBootClient)  
— Add: ServerName → AbstractSpringBootClient  
— Add: ServerName → applicationFrameworkProperties.serverConfigs.name  
