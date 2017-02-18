drop database if exists `payments`;

CREATE DATABASE `payments`;

USE `payments`;

CREATE TABLE `payments`.`payments_payments_status` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `status_name` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `idpayments_peayments_status_UNIQUE` (`id` ASC)
) ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `payments`.`payments_roles` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(45) NOT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `payments_roles_id_uindex` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `payments`.`payments_users` (
    `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `login` VARCHAR(45) NOT NULL,
    `password` VARCHAR(200) NOT NULL,
    `firstname` VARCHAR(45) NOT NULL,
    `lastname` VARCHAR(45) NOT NULL,
    `blocked` TINYINT(3) UNSIGNED ZEROFILL NOT NULL,
    `activated` TINYINT(3) UNSIGNED ZEROFILL NOT NULL,
    `language` VARCHAR(45) NOT NULL DEFAULT 'ru',
    `role` INT(11) NOT NULL,
    `email` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `id_UNIQUE` (`id`),
    UNIQUE KEY `login_UNIQUE` (`login`),
    KEY `role` (`role`),
    CONSTRAINT `role` FOREIGN KEY (`role`)
        REFERENCES `payments_roles` (`role_id`)
        ON DELETE CASCADE ON UPDATE CASCADE
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `payments`.`payments_bills` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `user_id` INT UNSIGNED NOT NULL,
    `number` VARCHAR(16) NOT NULL,
    `name` VARCHAR(45) NULL,
    `balance` DECIMAL(65,2) UNSIGNED NOT NULL DEFAULT '0',
    `blocked` TINYINT(3) UNSIGNED ZEROFILL NOT NULL DEFAULT '0',
    `need_to_unblock` TINYINT(3) UNSIGNED ZEROFILL NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `idpayments_bills_UNIQUE` (`id` ASC),
    INDEX `user_id_idx` (`user_id` ASC),
    CONSTRAINT `user_id` FOREIGN KEY (`user_id`)
        REFERENCES `payments`.`payments_users` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;

CREATE TABLE `payments`.`payments_outgoing_payments` (
    `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `from_bill_id` INT(10) UNSIGNED NOT NULL,
    `to_bill` VARCHAR(16) NOT NULL,
    `comment` VARCHAR(80) DEFAULT NULL,
    `date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `sum` DECIMAL(65,2) UNSIGNED NOT NULL,
    `commission` DECIMAL(65,2) UNSIGNED NULL DEFAULT '0',
    `payment_status` INT(10) UNSIGNED NOT NULL DEFAULT '1',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idpayments_payments_UNIQUE` (`id`),
    KEY `from_bill_idx` (`from_bill_id`),
    KEY `in_status_idx` (`payment_status`),
    CONSTRAINT `from_bill` FOREIGN KEY (`from_bill_id`)
        REFERENCES `payments`.`payments_bills` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `out_status` FOREIGN KEY (`payment_status`)
        REFERENCES `payments`.`payments_payments_status` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `payments`.`payments_incoming_payments` (
    `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `from_bill` VARCHAR(16) NOT NULL,
    `to_bill_id` INT(10) UNSIGNED NOT NULL,
    `comment` VARCHAR(80) DEFAULT NULL,
    `date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `sum` DECIMAL(65,2) UNSIGNED NOT NULL,
    `commission` DECIMAL(65,2) UNSIGNED NOT NULL DEFAULT '0',
    `payment_status` INT(10) UNSIGNED NOT NULL DEFAULT '2',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idpayments_payments_UNIQUE` (`id`),
    KEY `to_bill_idx` (`to_bill_id`),
    KEY `out_status_idx` (`payment_status`),
    CONSTRAINT `in_status` FOREIGN KEY (`payment_status`)
        REFERENCES `payments`.`payments_payments_status` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `to_bill` FOREIGN KEY (`to_bill_id`)
        REFERENCES `payments`.`payments_bills` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `payments`.`payments_credit_cards` (
    `id` INT UNSIGNED NOT NULL,
    `expiration_date` TIMESTAMP NOT NULL,
    `user_id` INT UNSIGNED NOT NULL,
    `bill_id` INT UNSIGNED NOT NULL,
    `card_number` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `idpayments_credit_card_UNIQUE` (`id` ASC),
    INDEX `user_id_idx` (`user_id` ASC),
    INDEX `bill_id_idx` (`bill_id` ASC),
    CONSTRAINT `user__id` FOREIGN KEY (`user_id`)
        REFERENCES `payments`.`payments_users` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `bill_id` FOREIGN KEY (`bill_id`)
        REFERENCES `payments`.`payments_bills` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;

