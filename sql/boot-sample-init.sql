/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`boot_sample` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `boot_sample`;

/*Table structure for table `client` */

CREATE TABLE `client` (
  `id` varchar(20) CHARACTER SET utf8mb4 NOT NULL COMMENT '商户ID',
  `plat_id` varchar(32) NOT NULL COMMENT '平台ID',
  `name` varchar(32) NOT NULL COMMENT '商户名称',
  `status` int(2) unsigned NOT NULL DEFAULT '1' COMMENT '商户状态（0-无效，1-有效）',
  `threshold` int(5) unsigned NOT NULL DEFAULT '80' COMMENT '人脸相似度阈值',
  `repeat_second` int(11) unsigned NOT NULL DEFAULT '3600' COMMENT '去重间隔（秒）',
  `oss_type` int(2) unsigned NOT NULL DEFAULT '2' COMMENT '第三方存储类型 0腾讯 1瑞为 2华为',
  `oss_config_id` varchar(32) NOT NULL COMMENT '第三方存储配置ID',
  `third_type` int(2) unsigned NOT NULL DEFAULT '0' COMMENT '第三方算法类型 0腾讯 1瑞为 2华为',
  `third_app_id` varchar(32) NOT NULL COMMENT '第三方应用APPID',
  `third_secret_id` varchar(40) NOT NULL COMMENT '第三方应用SecretId',
  `third_secret_key` varchar(40) NOT NULL COMMENT '第三方应用密钥',
  `third_user_id` varchar(32) NOT NULL COMMENT '第三方应用userId',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='商户信息';

/*Table structure for table `role_resource` */

CREATE TABLE `role_resource` (
  `role_resource_id` varchar(32) NOT NULL DEFAULT '' COMMENT '角色资源唯一标识',
  `role_id` varchar(32) DEFAULT NULL COMMENT '角色唯一标识',
  `resource_id` varchar(32) DEFAULT NULL COMMENT '资源唯一标识',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`role_resource_id`),
  KEY `FK_ROLE_RESOURCE` (`role_id`),
  KEY `FK_RESOURCE_ROLE` (`resource_id`),
  CONSTRAINT `role_resource_ibfk_1` FOREIGN KEY (`resource_id`) REFERENCES `sys_resource` (`resource_id`),
  CONSTRAINT `role_resource_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='角色资源关联表';

/*Table structure for table `student` */

CREATE TABLE `student` (
  `id` varchar(32) NOT NULL,
  `name` varchar(32) NOT NULL COMMENT '姓名',
  `age` int(5) NOT NULL COMMENT '年龄',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Table structure for table `sys_resource` */

CREATE TABLE `sys_resource` (
  `resource_id` varchar(32) NOT NULL DEFAULT '' COMMENT '资源唯一标识',
  `name` varchar(64) DEFAULT NULL COMMENT '资源名称',
  `url` varchar(256) DEFAULT NULL COMMENT '资源路径',
  `type` tinyint(11) DEFAULT NULL COMMENT '资源类型（0：菜单；1：按钮）',
  `icon` varchar(256) DEFAULT NULL COMMENT '资源图标',
  `priority` int(11) DEFAULT NULL COMMENT '资源显示顺序',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '资源父编号',
  `permission` varchar(128) DEFAULT NULL COMMENT '权限字符串',
  `status` tinyint(11) DEFAULT NULL COMMENT '资源状态（0：禁用；1：启用）',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '资源创建时间',
  `update_user` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '资源更新时间',
  PRIMARY KEY (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*Table structure for table `sys_role` */

CREATE TABLE `sys_role` (
  `role_id` varchar(32) NOT NULL COMMENT '角色编号',
  `role_name` varchar(50) DEFAULT NULL COMMENT '角色名称',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态(1:正常 -1:停用)',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*Table structure for table `sys_user` */

CREATE TABLE `sys_user` (
  `sys_user_id` varchar(32) NOT NULL COMMENT '系统用户编号',
  `user_name` varchar(32) DEFAULT NULL COMMENT '用户名',
  `user_status` tinyint(4) DEFAULT '1' COMMENT '用户状态(0:待审核 1:审核通过 2:审核不通过 -1:停用)',
  `user_pwd` varchar(50) DEFAULT NULL COMMENT '用户密码',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`sys_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*Table structure for table `uid_worker_node` */

CREATE TABLE `uid_worker_node` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'auto increment id',
  `host_name` varchar(64) NOT NULL COMMENT 'host name',
  `port` varchar(64) NOT NULL COMMENT 'port',
  `type` int(11) NOT NULL COMMENT 'node type: ACTUAL or CONTAINER',
  `launch_date` date NOT NULL COMMENT 'launch date',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1383 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='DB WorkerID Assigner for UID Generator';

/*Table structure for table `user` */

CREATE TABLE `user` (
  `id` varchar(20) NOT NULL COMMENT '用户ID',
  `username` varchar(16) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `status` int(2) unsigned NOT NULL DEFAULT '1' COMMENT '用户状态 1启用 0禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Table structure for table `user_role` */

CREATE TABLE `user_role` (
  `id` varchar(32) NOT NULL COMMENT '系统编号',
  `sys_user_id` varchar(32) NOT NULL COMMENT '系统用户编号',
  `role_id` varchar(32) NOT NULL COMMENT '角色编号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `FK_sys_user` (`sys_user_id`),
  KEY `FK_role_2` (`role_id`),
  CONSTRAINT `user_role_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`),
  CONSTRAINT `user_role_ibfk_2` FOREIGN KEY (`sys_user_id`) REFERENCES `sys_user` (`sys_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*Table structure for table `t_order_0` */

CREATE TABLE `t_order_0` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) unsigned DEFAULT NULL,
  `order_id` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `t_order_1` */

CREATE TABLE `t_order_1` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) unsigned DEFAULT NULL,
  `order_id` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
