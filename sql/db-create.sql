# Script da eseguire come root per la creazione del DB
#
# per esempio in linux (con password di root vuota): sudo mariadb -p < sql/db-create.sql 
#

CREATE DATABASE `spring-webapp-db` /*!40100 COLLATE 'utf8mb3_general_ci' */;

CREATE USER 'web'@'%' IDENTIFIED BY 'web';
GRANT SELECT, DELETE, INSERT, UPDATE, EXECUTE  ON *.* TO 'web'@'%';
CREATE USER 'server-admin'@'%' IDENTIFIED BY 'server-admin';
GRANT ALL PRIVILEGES  ON *.* TO 'server-admin'@'%';
FLUSH PRIVILEGES;

USE `spring-webapp-db`;

CREATE TABLE `users` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(50) NOT NULL,
	`surname` VARCHAR(50) NOT NULL,
	`email` VARCHAR(100) NOT NULL UNIQUE,
	`password` VARCHAR(60) NOT NULL,
	`birthday` DATE NOT NULL,
	PRIMARY KEY (`id`)
)
COLLATE='utf8mb3_general_ci';

CREATE TABLE `roles` (
	`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(50) NOT NULL UNIQUE,
	PRIMARY KEY (`id`)
)
COLLATE='utf8mb3_general_ci';

INSERT INTO roles(name) VALUES ('USER');
INSERT INTO roles(name) VALUES ('ADMIN');

CREATE TABLE `user_roles` (
	`user_id` INT UNSIGNED NOT NULL,
	`role_id` INT UNSIGNED NOT NULL,
	PRIMARY KEY (`user_id`, `role_id`),
	CONSTRAINT `FK_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE,
	CONSTRAINT `FK_roles` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE
)
COLLATE='utf8mb3_general_ci';
