-- V1__init.sql

CREATE TABLE auth_users
(
    auth_user_id BIGINT PRIMARY KEY,
    user_id      BIGINT DEFAULT NULL,
    social_type  VARCHAR(255) NOT NULL,
    social_id    VARCHAR(255) NOT NULL
);

CREATE TABLE users
(
    user_id      BIGINT PRIMARY KEY,
    auth_user_id BIGINT       NOT NULL UNIQUE,
    name         VARCHAR(255) NOT NULL,
    created_at   DATETIME(6)  NOT NULL,
    updated_at   DATETIME(6)  NOT NULL
);
