-- =================================================================================================================
-- TECH1 FRAMEWORK
-- =================================================================================================================
CREATE TABLE "tech1_users" (
    "id" varchar(36) PRIMARY KEY,
    "username" varchar(256) NOT NULL,
    "password" varchar(256) NOT NULL,
    "zone_id" varchar(256) NOT NULL,
    "authorities" varchar(1024) NOT NULL,
    "email" varchar(256),
    "name" varchar(256),
    "attributes" varchar(65535)
);

CREATE TABLE "tech1_users_sessions" (
    "id" varchar PRIMARY KEY,
    "username" varchar(256) NOT NULL,
    "metadata" varchar(65535) NOT NULL
);

CREATE TABLE "tech1_invitation_codes" (
    "id" varchar(36),
    "owner" varchar(256) NOT NULL,
    "authorities" varchar(1024) NOT NULL,
    "value" varchar(40) NOT NULL,
    "invited" varchar
);

-- =================================================================================================================
-- SERVER
-- =================================================================================================================
CREATE TABLE "anything" (
    "id" varchar(36) PRIMARY KEY,
    "username" varchar(256) NOT NULL,
    "value" varchar
);

