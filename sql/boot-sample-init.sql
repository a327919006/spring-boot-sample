/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.6.21 : Database - boot-sample
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`boot-sample` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `boot-sample`;

/*Table structure for table `t_client` */

DROP TABLE IF EXISTS `t_client`;

CREATE TABLE `t_client` (
  `client_id` varchar(32) NOT NULL COMMENT '商户ID',
  `plat_id` varchar(32) DEFAULT NULL COMMENT '平台ID',
  `name` varchar(32) DEFAULT NULL COMMENT '商户名称',
  `status` tinyint(2) unsigned DEFAULT '1' COMMENT '商户状态（0-无效，1-有效）',
  `threshold` int(5) unsigned DEFAULT NULL COMMENT '人脸相似度阈值',
  `repeat_second` int(11) unsigned DEFAULT NULL COMMENT '去重间隔（秒）',
  `oss_type` tinyint(2) unsigned DEFAULT '0' COMMENT '第三方存储类型 0腾讯 1瑞为 2华为',
  `oss_config_id` varchar(32) DEFAULT NULL COMMENT '第三方存储配置ID',
  `third_type` tinyint(2) unsigned DEFAULT NULL COMMENT '第三方算法类型 0腾讯 1瑞为 2华为',
  `third_app_id` varchar(32) DEFAULT NULL COMMENT '第三方应用APPID',
  `third_secret_id` varchar(40) DEFAULT NULL COMMENT '第三方应用SecretId',
  `third_secret_key` varchar(40) DEFAULT NULL COMMENT '第三方应用密钥',
  `third_user_id` varchar(32) DEFAULT NULL COMMENT '第三方应用userId',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='商户信息';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
