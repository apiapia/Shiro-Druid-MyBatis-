/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : localhost:3306
 Source Schema         : Druid

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 09/07/2019 14:06:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '员工ID',
  `lastName` varchar(50) DEFAULT NULL COMMENT '姓名',
  `password` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL COMMENT 'EMAIL',
  `departmentId` int(10) DEFAULT NULL COMMENT '所属部门',
  `perms` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of employee
-- ----------------------------
BEGIN;
INSERT INTO `employee` VALUES (1, 'jack', 'jack', 'jack@gmail.com', 1, 'admin:add');
INSERT INTO `employee` VALUES (2, 'jean', 'jean', 'jean@gmail.com', 1, 'admin:add,');
INSERT INTO `employee` VALUES (3, 'apple', 'apple', 'apple@gmail.com', 2, 'admin:upd,admin:add,');
INSERT INTO `employee` VALUES (56, 'andrew', 'andrew', 'andrew@gmail.com', 2, 'admin:add,admin:upd,user:add,user:del,');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
