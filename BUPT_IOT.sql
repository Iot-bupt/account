CREATE TABLE IF NOT EXISTS `customer` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `additional_info` varchar(255),
    `address` varchar(255),
    `email` varchar(255),
    `phone` varchar(255),
    `tenant_id` INT,
    `title` varchar(255),
	PRIMARY KEY(`id`),
	KEY `tk_customer` (`tenant_id`)
);

CREATE TABLE IF NOT EXISTS `tenant` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `additional_info` varchar(255),
    `address` varchar(255),
    `email` varchar(255),
    `phone` varchar(255),
    `title` varchar(255),
	PRIMARY KEY(`id`)
);

ALTER TABLE `customer`
ADD CONSTRAINT `tk_customer` FOREIGN KEY (`tenant_id`) REFERENCES `tenant` (`id`);

CREATE TABLE IF NOT EXISTS `user` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `additional_info` varchar(255),
    `authority` varchar(255),
    `customer_id` INT,
    `email` varchar(255) UNIQUE,
    `name` varchar(255),
    `tenant_id` INT,
	PRIMARY KEY(`id`),
	KEY `tk_user` (`tenant_id`),
	KEY `ck_user` (`customer_id`)
);

ALTER TABLE `user`
ADD CONSTRAINT `tk_user` FOREIGN KEY (`tenant_id`) REFERENCES `tenant` (`id`);

ALTER TABLE `user`
ADD CONSTRAINT `ck_user` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`);

CREATE TABLE IF NOT EXISTS `user_credentials` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `password` varchar(255),
    `user_id` INT,
	PRIMARY KEY(`id`),
	KEY `uk_uc` (`user_id`)
);

ALTER TABLE `user_credentials`
ADD CONSTRAINT `uk_uc` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);