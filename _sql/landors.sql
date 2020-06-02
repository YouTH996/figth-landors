/*
 Navicat Premium Data Transfer

 Source Server         : 39.105.148.132
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : 39.105.148.132:3306
 Source Schema         : landors

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 03/06/2020 03:07:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT 'id,主键自增',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE COMMENT '用户名唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '123456', '2020-04-17 18:25:10');
INSERT INTO `user` VALUES (3, 'test', '123456', '2020-05-08 12:05:13');

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT 'id，主键自增',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `sex` bit(1) NULL DEFAULT b'0' COMMENT '性别,0为男，1为女',
  `score` int(9) NULL DEFAULT 0 COMMENT '积分',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES (1, 'admin', 'jack', b'0', -1);
INSERT INTO `user_info` VALUES (3, 'test', 'test', b'0', 5);
INSERT INTO `user_info` VALUES (4, 'test1', 'test', b'1', 1);
INSERT INTO `user_info` VALUES (5, 'test2', 'test', b'0', 3);
INSERT INTO `user_info` VALUES (6, 'test3', 'test', b'0', 2);
INSERT INTO `user_info` VALUES (7, 'test4', 'test', b'0', 11);
INSERT INTO `user_info` VALUES (8, 'test5', 'test', b'0', 6);
INSERT INTO `user_info` VALUES (9, 'test6', 'test', b'0', 8);
INSERT INTO `user_info` VALUES (10, 'test7', 'test', b'0', 16);
INSERT INTO `user_info` VALUES (11, 'test8', 'test', b'0', 100);
INSERT INTO `user_info` VALUES (12, 'test9', 'test', b'0', 20);
INSERT INTO `user_info` VALUES (13, 'test10', 'test', b'0', 3);

SET FOREIGN_KEY_CHECKS = 1;
