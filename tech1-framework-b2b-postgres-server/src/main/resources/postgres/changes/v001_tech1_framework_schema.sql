-- =================================================================================================================
-- TECH1 FRAMEWORK
-- =================================================================================================================
CREATE TABLE "tech1_users" (
    "id" varchar(36) PRIMARY KEY,
    "username" varchar(255) NOT NULL,
    "password" varchar(255) NOT NULL,
    "zone_id" varchar(255) NOT NULL,
    "authorities" varchar(255) NOT NULL,
    "email" varchar(255),
    "name" varchar(255),
    "attributes" varchar(65535) NOT NULL
);

CREATE TABLE "tech1_invitation_codes" (
    "id" varchar(36) PRIMARY KEY,
    "owner" varchar(255) NOT NULL,
    "authorities" varchar(255) NOT NULL,
    "value" varchar(255) NOT NULL,
    "invited" varchar(255)
);

-- =================================================================================================================
-- SERVER
-- =================================================================================================================
CREATE TABLE "anything" (
    "id" varchar(36) PRIMARY KEY,
    "username" varchar(255) NOT NULL,
    "value" varchar(255) NOT NULL
);

