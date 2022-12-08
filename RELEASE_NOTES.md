# Release Notes

### v1.6
- Add "countryCode" -> GeoLocation

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

