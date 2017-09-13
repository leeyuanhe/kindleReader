/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50718
Source Host           : localhost:3306
Source Database       : mysqltest

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2017-09-13 19:10:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for remember
-- ----------------------------
DROP TABLE IF EXISTS `remember`;
CREATE TABLE `remember` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Create_time` datetime DEFAULT NULL,
  `Modify_time` datetime DEFAULT NULL,
  `invariable_series` varchar(100) DEFAULT NULL COMMENT '登陆序列',
  `token` varchar(100) DEFAULT NULL COMMENT 'token',
  `user_id` varchar(100) DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
