-- -----------------------------------------------------
-- Create Schema provide_and_order_services
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS provide_and_order_services;
CREATE SCHEMA provide_and_order_services DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE
provide_and_order_services;

-- -----------------------------------------------------
-- Create Table role
-- -----------------------------------------------------
CREATE TABLE `role`
(
    `id`   int         NOT NULL,
    `name` varchar(45) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- -----------------------------------------------------
-- Create Table user
-- -----------------------------------------------------
CREATE TABLE `user`
(
    `id`           bigint       NOT NULL AUTO_INCREMENT,
    `first_name`   varchar(16)  NOT NULL,
    `last_name`    varchar(16)  NOT NULL,
    `email`        varchar(255) NOT NULL,
    `phone_number` varchar(15)           DEFAULT NULL,
    `password`     varchar(255) NOT NULL,
    `create_time`  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `role_id`      int          NOT NULL DEFAULT '1',
    PRIMARY KEY (`id`),
    UNIQUE KEY `id_UNIQUE` (`id`),
    UNIQUE KEY `email_UNIQUE` (`email`),
    KEY            `fk_user_role_idx` (`role_id`),
    CONSTRAINT `fk_user_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb3;

-- -----------------------------------------------------
-- Create Table account_transaction
-- -----------------------------------------------------
CREATE TABLE `account_transaction`
(
    `id`          bigint    NOT NULL AUTO_INCREMENT,
    `user_id`     bigint    NOT NULL,
    `amount`      double    NOT NULL,
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `id_UNIQUE` (`id`),
    KEY           `fk_account_transaction_user_idx` (`user_id`),
    CONSTRAINT `fk_account_transaction_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8mb3;

-- -----------------------------------------------------
-- Create Table feedback
-- -----------------------------------------------------
CREATE TABLE `feedback`
(
    `id`               bigint       NOT NULL AUTO_INCREMENT,
    `text`             varchar(512) NOT NULL,
    `hidden`           tinyint(1) NOT NULL DEFAULT '0',
    `rating`           int          NOT NULL,
    `user_id`          bigint       NOT NULL,
    `service_order_id` bigint       NOT NULL,
    `create_time`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `id_UNIQUE` (`id`),
    KEY                `fk_feedback_customer_idx` (`user_id`),
    KEY                `fk_feedback_service_order_idx` (`service_order_id`),
    CONSTRAINT `fk_feedback_service_order` FOREIGN KEY (`service_order_id`) REFERENCES `service_order` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `fk_feedback_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb3;

-- -----------------------------------------------------
-- Create Table service_order_status
-- -----------------------------------------------------
CREATE TABLE `service_order_status`
(
    `id`   int         NOT NULL AUTO_INCREMENT,
    `name` varchar(45) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3;

-- -----------------------------------------------------
-- Create Table service_order
-- -----------------------------------------------------
CREATE TABLE `service_order`
(
    `id`               bigint    NOT NULL AUTO_INCREMENT,
    `title`            varchar(128)       DEFAULT NULL,
    `description`      varchar(512)       DEFAULT NULL,
    `create_time`      timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `cost`             double             DEFAULT '0',
    `customer_id`      bigint    NOT NULL,
    `specialist_id`    bigint             DEFAULT NULL,
    `work_category_id` bigint    NOT NULL,
    `status_id`        int       NOT NULL DEFAULT '1',
    PRIMARY KEY (`id`),
    UNIQUE KEY `id_UNIQUE` (`id`),
    KEY                `fk_service_order_specialist_idx` (`specialist_id`),
    KEY                `fk_service_order_status_idx` (`status_id`),
    KEY                `fk_service_order_customer_idx` (`customer_id`),
    KEY                `fk_service_order_work_category_idx` (`work_category_id`),
    CONSTRAINT `fk_service_order_customer` FOREIGN KEY (`customer_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `fk_service_order_specialist` FOREIGN KEY (`specialist_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `fk_service_order_status` FOREIGN KEY (`status_id`) REFERENCES `service_order_status` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `fk_service_order_work_category` FOREIGN KEY (`work_category_id`) REFERENCES `work_category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb3;

-- -----------------------------------------------------
-- Create Table offer_service_order
-- -----------------------------------------------------
CREATE TABLE `offer_service_order`
(
    `id`                bigint NOT NULL AUTO_INCREMENT,
    `service_order_id`  bigint NOT NULL,
    `specialist_id`     bigint NOT NULL,
    `specialist_answer` tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `id_UNIQUE` (`id`),
    KEY                 `ser_idx` (`service_order_id`),
    KEY                 `fk_offer_service_order_service_specialist_idx` (`specialist_id`),
    CONSTRAINT `fk_offer_service_order_service_order` FOREIGN KEY (`service_order_id`) REFERENCES `service_order` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `fk_offer_service_order_service_specialist` FOREIGN KEY (`specialist_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;

-- -----------------------------------------------------
-- Create Table work_category
-- -----------------------------------------------------
CREATE TABLE `work_category`
(
    `id`        bigint                              NOT NULL AUTO_INCREMENT,
    `parent_id` bigint                              DEFAULT NULL,
    `name`      varchar(64) COLLATE utf8_unicode_ci NOT NULL,
    `icon`      varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `work_category_id_uindex` (`id`),
    KEY         `fk_work_category_parent` (`parent_id`),
    CONSTRAINT `fk_work_category_parent` FOREIGN KEY (`parent_id`) REFERENCES `work_category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;

-- -----------------------------------------------------
-- Create Table specialist_work_categories
-- -----------------------------------------------------
CREATE TABLE `specialist_work_categories`
(
    `specialist_id`    bigint NOT NULL,
    `work_category_id` bigint NOT NULL,
    PRIMARY KEY (`specialist_id`, `work_category_id`),
    KEY                `fk_specialist_work_categories_work_category` (`work_category_id`),
    CONSTRAINT `fk_specialist_work_categories_specialist` FOREIGN KEY (`specialist_id`) REFERENCES `user` (`id`),
    CONSTRAINT `fk_specialist_work_categories_work_category` FOREIGN KEY (`work_category_id`) REFERENCES `work_category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;

-- -----------------------------------------------------
-- Populate Table role
-- -----------------------------------------------------
INSERT INTO role (id, name)
VALUES (0, 'ROLE_CUSTOMER'),
       (1, 'ROLE_SPECIALIST'),
       (2, 'ROLE_ADMIN');

-- -----------------------------------------------------
-- Populate Table user
-- -----------------------------------------------------
INSERT INTO user (first_name, last_name, email, password, role_id)
VALUES ('ADMIN', 'ADMIN', 'admin@mail.com', '$2a$12$ZZzIf3sow0ximoO0yRWvqey2DsVZl1CsZfjozPF0/5y3GgHA/9w2.', 2);

-- -----------------------------------------------------
-- Populate Table service_order_status
-- -----------------------------------------------------
INSERT INTO service_order_status (id, name)
VALUES (0, 'CREATED'),
       (1, 'CANCELLED'),
       (2, 'IN_WORK'),
       (3, 'WAIT_FOR_CUSTOMER_APPROVE'),
       (4, 'WAIT_FOR_PAYMENT'),
       (5, 'PAID'),
       (6, 'COMPLETED');
