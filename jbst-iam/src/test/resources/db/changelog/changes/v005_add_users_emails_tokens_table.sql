CREATE TABLE IF NOT EXISTS "jbst_users_emails_tokens"
(
    "id"               varchar(36) PRIMARY KEY,
    "email"            varchar(255) NOT NULL,
    "value"            varchar(36)  NOT NULL,
    "type"             varchar(255) NOT NULL,
    "expiry_timestamp" int8         NOT NULL
);