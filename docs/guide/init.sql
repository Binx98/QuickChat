/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 101102 (10.11.2-MariaDB)
 Source Host           : localhost:3306
 Source Schema         : quick_chat

 Target Server Type    : MySQL
 Target Server Version : 101102 (10.11.2-MariaDB)
 File Encoding         : 65001

 Date: 22/05/2024 10:41:31
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for quick_chat_apply
-- ----------------------------
DROP TABLE IF EXISTS `quick_chat_apply`;
CREATE TABLE `quick_chat_apply`
(
    `id` bigint NOT NULL COMMENT '主键id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_unicode_ci COMMENT = '好友申请' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of quick_chat_apply
-- ----------------------------

-- ----------------------------
-- Table structure for quick_chat_emoji
-- ----------------------------
DROP TABLE IF EXISTS `quick_chat_emoji`;
CREATE TABLE `quick_chat_emoji`
(
    `id`          bigint NOT NULL COMMENT '主键id',
    `account_id`  varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '账户id',
    `url`         varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '图片url',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_unicode_ci COMMENT = '表情包' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of quick_chat_emoji
-- ----------------------------

-- ----------------------------
-- Table structure for quick_chat_friend_relation
-- ----------------------------
DROP TABLE IF EXISTS `quick_chat_friend_relation`;
CREATE TABLE `quick_chat_friend_relation`
(
    `id`          bigint NOT NULL COMMENT '主键id',
    `from_id`     varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '账号id（发送方）',
    `to_id`       varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '账号id（接收方）',
    `relation_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '关联id',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_unicode_ci COMMENT = '通讯录-聊天好友' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of quick_chat_friend_relation
-- ----------------------------

-- ----------------------------
-- Table structure for quick_chat_group
-- ----------------------------
DROP TABLE IF EXISTS `quick_chat_group`;
CREATE TABLE `quick_chat_group`
(
    `group_id`     bigint NOT NULL COMMENT '群组id',
    `account_id`   varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '群主id',
    `group_name`   varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '群名',
    `group_avatar` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '群头像',
    `create_time`  datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`  datetime NULL DEFAULT NULL COMMENT '修改时间',
    `deleted`      tinyint(1) NULL DEFAULT NULL COMMENT '删除标识',
    PRIMARY KEY (`group_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_unicode_ci COMMENT = '群聊信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of quick_chat_group
-- ----------------------------

-- ----------------------------
-- Table structure for quick_chat_group_member
-- ----------------------------
DROP TABLE IF EXISTS `quick_chat_group_member`;
CREATE TABLE `quick_chat_group_member`
(
    `id`          bigint NOT NULL COMMENT '主键id',
    `group_id`    bigint NULL DEFAULT NULL COMMENT '群组id',
    `account_id`  varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '账户id',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间（入群时间）',
    `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
    `deleted`     tinyint(1) NULL DEFAULT NULL COMMENT '删除标识',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_unicode_ci COMMENT = '群成员' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of quick_chat_group_member
-- ----------------------------

-- ----------------------------
-- Table structure for quick_chat_group_relation
-- ----------------------------
DROP TABLE IF EXISTS `quick_chat_group_relation`;
CREATE TABLE `quick_chat_group_relation`
(
    `id` bigint NOT NULL COMMENT '主键',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通讯录-群聊' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of quick_chat_group_relation
-- ----------------------------

-- ----------------------------
-- Table structure for quick_chat_msg
-- ----------------------------
DROP TABLE IF EXISTS `quick_chat_msg`;
CREATE TABLE `quick_chat_msg`
(
    `id`          bigint NOT NULL COMMENT '主键id',
    `from_id`     varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '账户id（发送人）',
    `to_id`       varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '账户id（接收人）',
    `relation_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '发送关联',
    `content`     varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息内容',
    `msg_type`    tinyint(1) NULL DEFAULT NULL COMMENT '消息类型（1：文字，2：语音，3：表情包，4：文件，5：语音通话，6：视频通话）',
    `extra_info`  varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '额外信息',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `deleted`     tinyint(1) NULL DEFAULT NULL COMMENT '删除标识',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聊天信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of quick_chat_msg
-- ----------------------------

-- ----------------------------
-- Table structure for quick_chat_session
-- ----------------------------
DROP TABLE IF EXISTS `quick_chat_session`;
CREATE TABLE `quick_chat_session`
(
    `id`             bigint NOT NULL COMMENT '主键id',
    `from_id`        varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '账户id（发送者）',
    `to_id`          varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '账户id（接收者）',
    `relation_id`    varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '关联id',
    `last_read_time` datetime NULL DEFAULT NULL COMMENT '最后读取时间',
    `type`           tinyint(1) NULL DEFAULT NULL COMMENT '会话类型（1：单聊，2：群聊）',
    `create_time`    datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`    datetime NULL DEFAULT NULL COMMENT '修改时间',
    `deleted`        tinyint(1) NULL DEFAULT NULL COMMENT '删除标识',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_unicode_ci COMMENT = '聊天会话（针对单聊）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of quick_chat_session
-- ----------------------------

-- ----------------------------
-- Table structure for quick_chat_user
-- ----------------------------
DROP TABLE IF EXISTS `quick_chat_user`;
CREATE TABLE `quick_chat_user`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `account_id`  varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '账号',
    `password`    varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '密码',
    `nick_name`   varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
    `avatar`      varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '头像',
    `gender`      tinyint(1) NULL DEFAULT NULL COMMENT '性别（1：男，0：女）',
    `email`       varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '邮件',
    `location`    varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '位置',
    `line_status` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NULL DEFAULT NULL COMMENT '在线状态（Y：在线，N：下线）',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
    `deleted`     tinyint(1) NULL DEFAULT NULL COMMENT '删除标识',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_unicode_ci COMMENT = '用户信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of quick_chat_user
-- ----------------------------

SET
FOREIGN_KEY_CHECKS = 1;
