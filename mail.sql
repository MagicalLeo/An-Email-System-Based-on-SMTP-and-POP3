/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3307
 Source Schema         : mail

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 20/11/2023 01:07:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for mail
-- ----------------------------
DROP TABLE IF EXISTS `mail`;
CREATE TABLE `mail`  (
  `uid` int NOT NULL AUTO_INCREMENT,
  `from_mail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `to_mail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `message` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mail
-- ----------------------------
INSERT INTO `mail` VALUES (14, 'test@mymail.com', '974726369@qq.com', 'Date: Mon, 14 Nov 2022 23:08:41 +0800 (CST)\nFrom: test@mymail.com\nTo: 974726369@qq.com\nMessage-ID: <1321212950.2.1668438521304@localhost>\nSubject: test\nMIME-Version: 1.0\nContent-Type: multipart/mixed; \n	boundary=\"----=_Part_0_384877508.1668438521274\"\n\n------=_Part_0_384877508.1668438521274\nContent-Type: multipart/related; \n	boundary=\"----=_Part_1_285041494.1668438521284\"\n\n------=_Part_1_285041494.1668438521284\nContent-Type: text/plain; charset=us-ascii\nContent-Transfer-Encoding: 7bit\n\ntest\n------=_Part_1_285041494.1668438521284--\n\n------=_Part_0_384877508.1668438521274--\n', 0);
INSERT INTO `mail` VALUES (15, 'test@mymail.com', 'halo@mymail.com', 'Date: Mon, 14 Nov 2022 23:15:35 +0800 (CST)\nFrom: test@mymail.com\nTo: halo@mymail.com\nMessage-ID: <255896907.5.1668438935049@localhost>\nSubject: test\nMIME-Version: 1.0\nContent-Type: multipart/mixed; \n	boundary=\"----=_Part_3_1586232321.1668438935049\"\n\n------=_Part_3_1586232321.1668438935049\nContent-Type: multipart/related; \n	boundary=\"----=_Part_4_1777357262.1668438935049\"\n\n------=_Part_4_1777357262.1668438935049\nContent-Type: text/plain; charset=us-ascii\nContent-Transfer-Encoding: 7bit\n\ntest\n------=_Part_4_1777357262.1668438935049--\n\n------=_Part_3_1586232321.1668438935049--\n', 0);
INSERT INTO `mail` VALUES (16, 'test@mymail.com', 'halo@mymail.com', 'Date: Mon, 14 Nov 2022 23:16:09 +0800 (CST)\nFrom: test@mymail.com\nTo: halo@mymail.com\nMessage-ID: <1308480.8.1668438969500@localhost>\nSubject: 1\nMIME-Version: 1.0\nContent-Type: multipart/mixed; \n	boundary=\"----=_Part_6_1244998257.1668438969496\"\n\n------=_Part_6_1244998257.1668438969496\nContent-Type: multipart/related; \n	boundary=\"----=_Part_7_798871971.1668438969496\"\n\n------=_Part_7_798871971.1668438969496\nContent-Type: text/plain; charset=us-ascii\nContent-Transfer-Encoding: 7bit\n\n1\n------=_Part_7_798871971.1668438969496--\n\n------=_Part_6_1244998257.1668438969496--\n', 0);
INSERT INTO `mail` VALUES (17, 'halo@mymail.com', 'halo@mymail.com', 'Date: Tue, 15 Nov 2022 18:46:35 +0800 (CST)\nFrom: halo@mymail.com\nTo: halo@mymail.com\nMessage-ID: <15065638.2.1668509195515@localhost>\nSubject: halo\nMIME-Version: 1.0\nContent-Type: multipart/mixed; \n	boundary=\"----=_Part_0_1589600578.1668509195495\"\n\n------=_Part_0_1589600578.1668509195495\nContent-Type: multipart/related; \n	boundary=\"----=_Part_1_1319800347.1668509195500\"\n\n------=_Part_1_1319800347.1668509195500\nContent-Type: text/plain; charset=us-ascii\nContent-Transfer-Encoding: 7bit\n\nhalo\n------=_Part_1_1319800347.1668509195500--\n\n------=_Part_0_1589600578.1668509195495--\n', 0);
INSERT INTO `mail` VALUES (18, 'halo@mymail.com', 'test@mymail.com', 'Date: Tue, 15 Nov 2022 19:05:42 +0800 (CST)\nFrom: halo@mymail.com\nTo: test@mymail.com\nMessage-ID: <1253969735.2.1668510342465@localhost>\nSubject: =?UTF-8?B?5L2g5aW9?=\nMIME-Version: 1.0\nContent-Type: multipart/mixed; \n	boundary=\"----=_Part_0_686287490.1668510342457\"\n\n------=_Part_0_686287490.1668510342457\nContent-Type: multipart/related; \n	boundary=\"----=_Part_1_1262188859.1668510342457\"\n\n------=_Part_1_1262188859.1668510342457\nContent-Type: text/plain; charset=UTF-8\nContent-Transfer-Encoding: base64\n\n5L2g5aW9\n------=_Part_1_1262188859.1668510342457--\n\n------=_Part_0_686287490.1668510342457--\n', 0);
INSERT INTO `mail` VALUES (19, 'test@mymail.com', 'halo@mymail.com', 'Date: Thu, 17 Nov 2022 10:36:06 +0800 (CST)\nFrom: test@mymail.com\nTo: halo@mymail.com\nMessage-ID: <1416670808.2.1668652566126@localhost>\nSubject: =?UTF-8?B?5L2g5aW9?=\nMIME-Version: 1.0\nContent-Type: multipart/mixed; \n	boundary=\"----=_Part_0_408230882.1668652566109\"\n\n------=_Part_0_408230882.1668652566109\nContent-Type: multipart/related; \n	boundary=\"----=_Part_1_2000634962.1668652566119\"\n\n------=_Part_1_2000634962.1668652566119\nContent-Type: text/plain; charset=UTF-8\nContent-Transfer-Encoding: base64\n\n5L2g5Lmf5aW9\n------=_Part_1_2000634962.1668652566119--\n\n------=_Part_0_408230882.1668652566109--\n', 0);
INSERT INTO `mail` VALUES (20, 'test@mymail.com', 'halo@mymail.com', 'Date: Thu, 17 Nov 2022 12:38:56 +0800 (CST)\nFrom: test@mymail.com\nTo: halo@mymail.com\nMessage-ID: <1816631385.2.1668659936637@localhost>\nSubject: halo\nMIME-Version: 1.0\nContent-Type: multipart/mixed; \n	boundary=\"----=_Part_0_521458107.1668659936629\"\n\n------=_Part_0_521458107.1668659936629\nContent-Type: multipart/related; \n	boundary=\"----=_Part_1_120429730.1668659936630\"\n\n------=_Part_1_120429730.1668659936630\nContent-Type: text/plain; charset=us-ascii\nContent-Transfer-Encoding: 7bit\n\nhalo\n------=_Part_1_120429730.1668659936630--\n\n------=_Part_0_521458107.1668659936629--\n', 0);
INSERT INTO `mail` VALUES (21, 'test@mymail.com', 'halo@mymail.com', 'Date: Thu, 17 Nov 2022 12:40:48 +0800 (CST)\nFrom: test@mymail.com\nTo: halo@mymail.com\nMessage-ID: <1784419739.5.1668660048289@localhost>\nSubject: test\nMIME-Version: 1.0\nContent-Type: multipart/mixed; \n	boundary=\"----=_Part_3_1399196464.1668660048284\"\n\n------=_Part_3_1399196464.1668660048284\nContent-Type: multipart/related; \n	boundary=\"----=_Part_4_1091769743.1668660048284\"\n\n------=_Part_4_1091769743.1668660048284\nContent-Type: text/plain; charset=us-ascii\nContent-Transfer-Encoding: 7bit\n\ntest\n------=_Part_4_1091769743.1668660048284--\n\n------=_Part_3_1399196464.1668660048284--\n', 0);
INSERT INTO `mail` VALUES (22, 'admin@mymail.com', 'test2@mymail.com', 'Date: Fri, 25 Nov 2022 19:07:57 +0800 (CST)\nFrom: admin@mymail.com\nTo: test2@mymail.com\nMessage-ID: <1657029086.2.1669374477322@localhost>\nSubject: test\nMIME-Version: 1.0\nContent-Type: multipart/mixed; \n	boundary=\"----=_Part_0_414468395.1669374477292\"\n\n------=_Part_0_414468395.1669374477292\nContent-Type: multipart/related; \n	boundary=\"----=_Part_1_1873598792.1669374477297\"\n\n------=_Part_1_1873598792.1669374477297\nContent-Type: text/plain; charset=us-ascii\nContent-Transfer-Encoding: 7bit\n\nbabe\n------=_Part_1_1873598792.1669374477297--\n\n------=_Part_0_414468395.1669374477292--\n', 0);

-- ----------------------------
-- Table structure for role_routes
-- ----------------------------
DROP TABLE IF EXISTS `role_routes`;
CREATE TABLE `role_routes`  (
  `role_id` int NULL DEFAULT NULL,
  `route_id` int NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_routes
-- ----------------------------
INSERT INTO `role_routes` VALUES (1, 1);
INSERT INTO `role_routes` VALUES (1, 2);
INSERT INTO `role_routes` VALUES (2, 1);
INSERT INTO `role_routes` VALUES (2, 2);
INSERT INTO `role_routes` VALUES (1, 3);
INSERT INTO `role_routes` VALUES (1, 4);
INSERT INTO `role_routes` VALUES (1, 5);
INSERT INTO `role_routes` VALUES (1, 6);
INSERT INTO `role_routes` VALUES (1, 7);
INSERT INTO `role_routes` VALUES (2, 3);
INSERT INTO `role_routes` VALUES (2, 4);
INSERT INTO `role_routes` VALUES (2, 5);
INSERT INTO `role_routes` VALUES (2, 6);
INSERT INTO `role_routes` VALUES (2, 7);

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `id` int NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `value` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES (1, '管理员', 1);
INSERT INTO `roles` VALUES (2, '普通用户', 2);

-- ----------------------------
-- Table structure for routes
-- ----------------------------
DROP TABLE IF EXISTS `routes`;
CREATE TABLE `routes`  (
  `id` int NOT NULL,
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of routes
-- ----------------------------
INSERT INTO `routes` VALUES (1, '/mail', 'POST');
INSERT INTO `routes` VALUES (2, '/mail', 'GET');
INSERT INTO `routes` VALUES (3, '/user', 'GET');
INSERT INTO `routes` VALUES (4, '/user', 'POST');
INSERT INTO `routes` VALUES (5, '/user', 'PUT');
INSERT INTO `routes` VALUES (6, '/user', 'DELETE');
INSERT INTO `routes` VALUES (7, '/user/change_password', 'PUT');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sex` int NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `disabled` int NOT NULL DEFAULT 0,
  `role_id` int NULL DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `smtp_host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'localhost',
  `smtp_port` int NULL DEFAULT 25,
  `pop3_host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'localhost',
  `pop3_port` int NULL DEFAULT 110,
  `auth_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'halo@mymail.com', NULL, 'e005126fee782699e82b5291328b5d49', NULL, NULL, 0, 2, NULL, 'localhost', 25, 'localhost', 110, 'Hjc123456');
INSERT INTO `users` VALUES (2, 'test@mymail.com', NULL, 'e005126fee782699e82b5291328b5d49', NULL, NULL, 0, 2, NULL, 'localhost', 25, 'localhost', 110, 'Hjc123456');
INSERT INTO `users` VALUES (3, 'admin@mymail.com', NULL, 'e005126fee782699e82b5291328b5d49', NULL, NULL, 0, 1, NULL, 'localhost', 25, 'localhost', 110, 'Hjc123456');
INSERT INTO `users` VALUES (4, 'test2@mymail.com', 'geo', 'e10adc3949ba59abbe56e057f20f883e', 0, '15270141092', 0, 2, NULL, 'localhost', 25, 'localhost', 110, '123456');
INSERT INTO `users` VALUES (5, 'test3@mymail.com', 'babe', 'e005126fee782699e82b5291328b5d49', NULL, NULL, 0, 2, NULL, 'localhost', 25, 'localhost', 110, 'Hjc123456');

SET FOREIGN_KEY_CHECKS = 1;
