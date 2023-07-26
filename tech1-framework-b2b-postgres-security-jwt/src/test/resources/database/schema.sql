CREATE SEQUENCE hibernate_sequence START 1;

-- =================================================================================================================
-- TECH1 FRAMEWORK
-- =================================================================================================================
CREATE TABLE "tech1_users" (
    "id" INT PRIMARY KEY,
    "username" varchar(255) NOT NULL,
    "password" varchar(255) NOT NULL,
    "zone_id" varchar(255) NOT NULL,
    "authorities" varchar(255) NOT NULL,
    "email" varchar(255),
    "name" varchar(255),
    "attributes" varchar(65535) NOT NULL
);


