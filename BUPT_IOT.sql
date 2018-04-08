CREATE TABLE IF NOT EXISTS `customer` (
    `id` varchar(31) NOT NULL,
    `additional_info` varchar(255),
    `address` varchar(255),
    `email` varchar(255),
    `phone` varchar(255),
    `tenant_id` varchar(31),
    `title` varchar(255),
	PRIMARY KEY(`id`,`tenant_id`),
	KEY `tk_customer` (`tenant_id`)
);

CREATE TABLE IF NOT EXISTS `tenant` (
    `id` varchar(31) NOT NULL,
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
    `id` varchar(31) NOT NULL,
    `additional_info` varchar(255),
    `authority` varchar(255),
    `customer_id` varchar(31),
    `email` varchar(255) UNIQUE,
    `name` varchar(255),
    `tenant_id` varchar(31),
	PRIMARY KEY(`id`,`tenant_id`,`customer_id`,`authority`),
	KEY `tk_user` (`tenant_id`),
	KEY `ck_user` (`customer_id`)
);

ALTER TABLE `user`
ADD CONSTRAINT `tk_user` FOREIGN KEY (`tenant_id`) REFERENCES `tenant` (`id`);

ALTER TABLE `user`
ADD CONSTRAINT `ck_user` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`);

CREATE TABLE IF NOT EXISTS `user_credentials` (
    `id` varchar(31) NOT NULL,
    `password` varchar(255),
    `user_id` varchar(31) UNIQUE,
	PRIMARY KEY(`id`),
	KEY `uk_uc` (`user_id`)
);

ALTER TABLE `user_credentials`
ADD CONSTRAINT `uk_uc` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);