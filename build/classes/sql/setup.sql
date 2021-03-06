DROP DATABASE IF EXISTS `gb_talks`;
CREATE DATABASE `gb_talks`;
USE `gb_talks`;

CREATE TABLE `talks`(
`id` INT(3) NOT NULL,
`year` INT(2) NOT NULL,
`speaker` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
`title` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
`available` INT(1) DEFAULT 0 NOT NULL,
PRIMARY KEY (`id`, `year`))
ENGINE = InnoDB;

CREATE TABLE `orders` (
`id` INT(3) PRIMARY KEY NOT NULL, 
`fulfilled` INT(1) DEFAULT 0 NOT NULL,
`year` INT(2) NOT NULL)
ENGINE = InnoDB;

CREATE TABLE `order_items` (
 `order_id` INT( 3 ) NOT NULL ,
 `talk_id` INT( 3 ) NOT NULL ,
CONSTRAINT `pk_order_items`
PRIMARY KEY `pk_order_items` ( `order_id` , `talk_id` ) ,
CONSTRAINT `fk_order_items_talks`
FOREIGN KEY `fk_order_items_talks` ( `talk_id` )
REFERENCES `talks` ( `id` )
ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = INNODB;

CREATE TABLE `upload_queue` (
`sequence` INT(3) PRIMARY KEY NOT NULL,
`priority` INT(1) NOT NULL,
`talk_id` INT(3) UNIQUE NOT NULL,
CONSTRAINT `fk_upload_queue_talks`
        FOREIGN KEY `fk_upload_queue_talks`(`talk_id`)
        REFERENCES `talks`(`id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE `transcode_queue` (
`sequence` INT(3) PRIMARY KEY NOT NULL,
`priority` INT(1) NOT NULL,
`talk_id` INT(3) UNIQUE NOT NULL,
CONSTRAINT `fk_transcode_queue_talks`
        FOREIGN KEY `fk_transcode_queue_talks`(`talk_id`)
        REFERENCES `talks`(`id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION)
ENGINE = InnoDB;

