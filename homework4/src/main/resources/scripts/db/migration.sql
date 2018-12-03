# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.5.5-10.3.11-MariaDB-1:10.3.11+maria~bionic)
# Database: currency
# Generation Time: 2018-12-03 14:07:14 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table conversions
# ------------------------------------------------------------

DROP TABLE IF EXISTS `conversions`;

CREATE TABLE `conversions` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `from_currency` varchar(3) NOT NULL DEFAULT '',
  `to_currency` varchar(3) NOT NULL DEFAULT '',
  `conversion_rate` float NOT NULL,
  `number_of_conversions` int(11) unsigned NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `from` (`from_currency`,`to_currency`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `conversions` WRITE;
/*!40000 ALTER TABLE `conversions` DISABLE KEYS */;

INSERT INTO `conversions` (`id`, `from_currency`, `to_currency`, `conversion_rate`, `number_of_conversions`)
VALUES
	(1,'USD','SEK',9.029,0),
	(2,'SEK','USD',0.1108,0),
	(3,'USD','DKK',6.568,0),
	(4,'USD','EUR',0.8799,0),
	(5,'USD','NOK',8.5262,0),
	(6,'SEK','DKK',0.728,0),
	(7,'SEK','EUR',0.0975,0),
	(8,'SEK','NOK',0.9446,0),
	(9,'DKK','USD',0.1523,0),
	(10,'DKK','SEK',1.374,0),
	(11,'DKK','EUR',0.134,0),
	(12,'DKK','NOK',1.2975,0),
	(13,'EUR','USD',1.136,0),
	(14,'EUR','SEK',10.25,0),
	(15,'EUR','DKK',7.4615,0),
	(16,'EUR','NOK',9.6832,0),
	(17,'NOK','USD',0.1173,0),
	(18,'NOK','SEK',1.0585,0),
	(19,'NOK','DKK',0.7706,0),
	(20,'NOK','EUR',0.1032,0);

/*!40000 ALTER TABLE `conversions` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
