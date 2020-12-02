/*
Navicat MySQL Data Transfer

Source Server         : 开发
Source Server Version : 50616
Source Host           : rm-2ze0a77phjgk85878mo.mysql.rds.aliyuncs.com:3306
Source Database       : webadmin

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2020-04-30 17:28:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for equipment
-- ----------------------------
DROP TABLE IF EXISTS `equipment`;
CREATE TABLE `equipment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `equipment_id` varchar(128) DEFAULT NULL,
  `elevator` varchar(128) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `group` int(11) DEFAULT NULL,
  `update_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for material
-- ----------------------------
DROP TABLE IF EXISTS `material`;
CREATE TABLE `material` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `url` varchar(128) DEFAULT NULL,
  `material_id` varchar(128) DEFAULT NULL,
  `is_used` int(11) DEFAULT NULL,
  `run_num` int(11) DEFAULT NULL,
  `update_user` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `poster` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for push_task
-- ----------------------------
DROP TABLE IF EXISTS `push_task`;
CREATE TABLE `push_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `bgtime` timestamp NULL DEFAULT NULL,
  `edtime` timestamp NULL DEFAULT NULL,
  `material` varchar(128) DEFAULT '',
  `device_id` varchar(128) DEFAULT NULL,
  `task_id` varchar(128) DEFAULT NULL,
  `update_user` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for push_task_default
-- ----------------------------
DROP TABLE IF EXISTS `push_task_default`;
CREATE TABLE `push_task_default` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `location` varchar(128) DEFAULT NULL,
  `elevator` varchar(128) DEFAULT '',
  `device_id` varchar(128) DEFAULT NULL,
  `material` varchar(128) DEFAULT '',
  `update_user` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `account_id` varchar(128) DEFAULT NULL,
  `number` varchar(128) DEFAULT NULL,
  `company_name` varchar(128) DEFAULT NULL,
  `del` int(11) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `userId` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
