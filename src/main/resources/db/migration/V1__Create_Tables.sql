CREATE TABLE roles
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

INSERT IGNORE INTO roles (id, name) VALUES (1, 'admin');
INSERT IGNORE INTO roles (id, name) VALUES (2, 'basic');

CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    username   VARCHAR(255) NULL,
    password   VARCHAR(255) NULL,
    created_at datetime NULL,
    updated_at datetime NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE users_roles
(
    role_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_users_roles PRIMARY KEY (role_id, user_id)
);

CREATE TABLE todo
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    title         VARCHAR(255) NULL,
    `description` VARCHAR(255) NULL,
    status        VARCHAR(255) NULL,
    created_at    datetime NULL,
    updated_at    datetime NULL,
    user_id       BIGINT NULL,
    CONSTRAINT pk_todo PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

ALTER TABLE todo
    ADD CONSTRAINT FK_TODO_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES users (id);