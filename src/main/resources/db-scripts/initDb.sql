select 'initDb script started executing' AS '';

-- --------------------------------------
--        CREATE A DB IF NONE
-- --------------------------------------
CREATE DATABASE IF NOT EXISTS `laundry_system_db`;
USE `laundry_system_db`;

-- --------------------------------------
--  	CREATING TABLES & RELATIONSHIPS
-- --------------------------------------
CREATE TABLE IF NOT EXISTS `users` (
	`id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
	`created_date` datetime NOT NULL,
	`username` varchar(255) NOT NULL,
	`password` varchar(255) NOT NULL,
	`role` tinyint NOT NULL,
	`jwt` varchar(255) DEFAULT NULL,
	`name` varchar(255) NOT NULL,
	`surname` varchar(255) NOT NULL,
	`email` varchar(255) NOT NULL,
	`mobile_number` varchar(255) NOT NULL,
--	TODO: forgotPassword functionality related columns
	UNIQUE (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `addresses` (
	`id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
	`created_date` datetime NOT NULL,
	`street_name` varchar(255) NOT NULL,
	`street_number` varchar(255) NOT NULL,
	`postal_code` varchar(255) NOT NULL,
	`city` varchar(255) NOT NULL,
	`country` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `residences` (
	`id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
	`created_date` datetime NOT NULL,
	`name` varchar(255) NOT NULL,
    `address_id` int NOT NULL,
    FOREIGN KEY (`address_id`) REFERENCES `addresses` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `user_residence` (
	`id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
	`created_date` datetime NOT NULL,
	`tenancy_start` datetime DEFAULT NULL, -- set to null since employees are connected to the residencies but the concept of 'tenancy' does not apply to them
	`tenancy_end` datetime DEFAULT NULL,
	`user_id` int NOT NULL,
    `residence_id` int NOT NULL,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    FOREIGN KEY (`residence_id`) REFERENCES `residences` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `laundry_assets` (
	`id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
	`created_date` datetime NOT NULL,
	`name` varchar(255) NOT NULL,
	`asset_type` tinyint NOT NULL,
	`running_time` smallint NOT NULL,
	`service_price` decimal NOT NULL,
	`currency` varchar(5) NOT NULL,
	`is_operational` tinyint(1) NOT NULL,
    `residence_id` int NOT NULL,
    FOREIGN KEY (`residence_id`) REFERENCES `residences` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `payment_cards` (
	`id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
	`created_date` datetime NOT NULL,
	`is_being_used` tinyint(1) NOT NULL,
	`token` varchar(255) NOT NULL,
	`card_holder_name` varchar(255) NOT NULL,
	`expiry_date` varchar(5) NOT NULL,
	`last_four_digits` varchar(4) NOT NULL,
    `user_id` int NOT NULL,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `bookings` (
	`id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
	`created_date` datetime NOT NULL,
	`timeslot` datetime NOT NULL,
--	TODO: with the booking cancellation functionality, add is_cancelled column, cancellation_date
	`user_id` int NOT NULL,
    `laundry_asset_id` int NOT NULL,
	FOREIGN KEY (`user_id`) REFERENCES `users`(`id`),
	FOREIGN KEY (`laundry_asset_id`) REFERENCES `laundry_assets` (`id`),
	CONSTRAINT uq_timeslot UNIQUE(`timeslot`, `laundry_asset_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `purchases` (
	`id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
	`created_date` datetime NOT NULL,
    `user_id` int NOT NULL,
    `payment_card_id` int NOT NULL,
    `laundry_asset_id` int NOT NULL,
    `booking_id` int NOT NULL,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`),
    FOREIGN KEY (`payment_card_id`) REFERENCES `payment_cards`(`id`),
    FOREIGN KEY (`laundry_asset_id`) REFERENCES `laundry_assets`(`id`),
    FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `laundry_asset_uses` (
	`id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
	`created_date` datetime NOT NULL,
	`start_time` datetime NOT NULL,
	`end_time` datetime NOT NULL,
	`booking_id` int NOT NULL,
    `laundry_asset_id` int NOT NULL,
    FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`),
    FOREIGN KEY (`laundry_asset_id`) REFERENCES `laundry_assets` (`id`)
--	there can be no 2 ovelapping uses tied to the same asset in this table
-- some kind of constraint may be added for this
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

select 'initDb script finished executing' AS '';
