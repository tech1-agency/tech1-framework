# Release Notes

### v1.12
- Upgraded maven-surefire-plugin dependencies 3.0.0-M5 -> 3.0.0
- Upgraded maven-javadoc-plugin dependencies 3.3.1 -> 3.5.0
- Upgraded jacoco-maven-plugin dependencies 0.8.7 -> 0.8.9
- Upgraded nexus-staging-maven-plugin dependencies 1.6.7 -> 1.6.13
- Upgraded maven-dependency-plugin dependencies 3.2.0 -> 3.5.0
- Upgraded maven-compiler-plugin 3.9.0 -> 3.11.0
- Upgraded testcontainers dependencies 1.17.6 -> 1.18.0
- Upgraded junit dependencies 5.8.2 -> 5.9.3
- Upgraded json-path 2.6.0 -> 2.8.0
- Upgraded assertj-core 3.21.0 -> 3.24.2
- Upgraded lombok dependencies 1.18.22 -> 1.18.26
- Upgraded jasypt-spring-boot-starter 3.0.4 -> 3.0.5
- Upgraded jackson dependencies 2.13.0 -> 2.15.0
- Upgraded browscap-java 1.3.12 -> 1.4.0
- Upgraded tomcat-embed-core 9.0.55 -> 9.0.74
- Upgraded jakarta.xml.bind-api 2.3.2 -> 2.3.3
- Upgraded spring-boot-dependencies 2.6.1 -> 2.7.11
- Upgraded GeoIP2 dependency 2.8.0 -> 4.0.1
- Upgraded Feign dependencies 10.7.0 -> 12.3

### v1.11
- Add email service method: send plain text + plain attachment
- Delete lombok static constructor usage
- Add trace utility (instead of throwable utility)
- Add system reset server domain classes

### v1.10
- Add queries on users, sessions and invitation codes repositories (querying and deleting)
- Add testcontainers (mongodb integration tests)
- Add tuples: TuplePresence
- Add properties printing support: ZoneId

### v1.9
- Re-configure incidents-based thread pools on @Async and @EventListener 

### v1.8
- Change user agent utility: static -> spring component
- Re-configure thread pools on @Async and @EventListener + add @Async annotation on subscriber

### v1.7
- Migrate bigdecimal-**: constants (TWO), getNumberOfDigitsAfterTheDecimalPointOrZero() method
- Fix bug with @Mandatory + "get" in the middle of the property: target1 -> getTarget1() is expected getter, not tar1()
- Migrate collection utility methods: get mutable list, set and map
- Migrate tuples: TupleToggle + serialize/deserialize process improved
- Add state machine -> TimerTask1 (created, operative, stopped)
- Add authentication logout min incident type
- Add security jwt incident configs

### v1.6
- Add "countryCode" -> GeoLocation
- Add geo location facade utility (ipapi + mindmax as failsafe)
- Add geo countries flags utility (emoji-based)
- Migrate methods TimestampUtility (toUnixTime)
- Add properties: serverConfigs
- Add Password/Email as Java objects (not plain strings) to DbUser (db migration required)
- Add endpoint /user/update2 (authenticated()) (fields: zoneId, name)
- Add email notification -> user (email is present + checkbox config is enabled)
- Change user request metad
- ata tuple3 -> { ipAddress, countryFlag, where }
- Change incidents key/values to camel-case-based strings

### v1.5
- Migrate EnumUtility methods, TuplePercentage(long, long)
- Introduce events/incidents policies
- Add sendHTML method to EmailService (thymeleaf as template-processor)
- Migrate TimestampUtility methods
- Add enable/disable option to HardwareMonitoringConfigs

### v1.4
- Migrate domain-based properties 
- Migrate domain-based exception
- Add tech1-framework-properties maven module
- Add tech1-framework-configurations maven module
- Add tech1-framework-emails maven module
- Add tech1-framework-hardware maven module
- Add tech1-framework-incidents maven module

### v1.3
- Add geo-based and user-agent-based functionality for user sessions and user incidents
- Add hardware functionality for hardware widget (CPU, ***Memories, Heap)
- Complete utilities migration
- Add spring-based utilities: environment (activeProfile), spring-boot-feign-client, base actuator

### v1.2
- Add utilities #1: CollectionUtility, CollectorUtility, EnumUtility, DevelopmentUtility
- Add utilities #2: EncodingUtility, EncryptionUtility
- Add utilities #3: BigDecimalUtility, RoundingUtility, HttpCookieUtility
- Add classes: Partitions, UserAgentHeader + cached-based http request and input stream

### v1.1
- Add tuples: TupleExceptionDetails, TupleRange
- Add triggers: CronTrigger, UserTrigger
- Add notifications
- Add Username
- Add utilities: RandomUtility, StringUtility, MaskUtility, ExceptionsMessagesUtility
- Add constants: TestsConstants, BigDecimalConstants, BigIntegerConstants

### v1.0
- Add basic asserts (assertNonNull, assertNonBlank, assertZoneId etc.)
- Add tuples

