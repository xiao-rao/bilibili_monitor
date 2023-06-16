/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : bilibili_monitor

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 16/06/2023 20:53:15
*/
CREATE
DATABASE bilibili_monitor;
use
bilibili_monitor;


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for b_account
-- ----------------------------
DROP TABLE IF EXISTS `b_account`;
CREATE TABLE `b_account` (
  `id` int NOT NULL AUTO_INCREMENT,
  `account_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '账号ID',
  `account_nickname` varchar(255) DEFAULT NULL COMMENT '账号昵称',
  `account_works_count` int DEFAULT NULL COMMENT '账号作品数',
  `is_monitored` tinyint(1) DEFAULT '0' COMMENT '是否监控 (1为是, 0为否)',
  `created_at` datetime DEFAULT NULL COMMENT '添加时间',
  `modified_at` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for b_monitor_log
-- ----------------------------
DROP TABLE IF EXISTS `b_monitor_log`;
CREATE TABLE `b_monitor_log` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `source_id` varchar(30) DEFAULT NULL COMMENT '来源id（作者id/作品id）',
  `source_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '来源名称（作者名称/作品名称）',
  `type` tinyint NOT NULL COMMENT '类型（作者；1，作品；2）',
  `status` tinyint NOT NULL COMMENT '状态 1；启动 0；禁用',
  `message` varchar(255) DEFAULT NULL COMMENT '消息',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='监控日志表';

-- ----------------------------
-- Table structure for b_send_message_log
-- ----------------------------
DROP TABLE IF EXISTS `b_send_message_log`;
CREATE TABLE `b_send_message_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `log_id` bigint DEFAULT NULL COMMENT '日志ID',
  `sender_name` varchar(255) DEFAULT NULL COMMENT '发送人名称',
  `receiver_id` varchar(255) DEFAULT NULL COMMENT '接收人ID',
  `receiver_name` varchar(255) DEFAULT NULL COMMENT '接收人名称',
  `content` text COMMENT '发送信息内容',
  `status` int DEFAULT NULL COMMENT '状态 (0: 失败, 1: 成功)',
  `error_msg` varchar(255) DEFAULT NULL COMMENT '失败原因',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间(发送消息时间)',
  `replies` text COMMENT '评论内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='发送消息日志表';

-- ----------------------------
-- Table structure for b_task_config
-- ----------------------------
DROP TABLE IF EXISTS `b_task_config`;
CREATE TABLE `b_task_config` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `interval_time` float DEFAULT NULL COMMENT '间隔时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for b_user
-- ----------------------------
DROP TABLE IF EXISTS `b_user`;
CREATE TABLE `b_user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账号ID',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账号昵称',
  `user_cookie` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账号Cookie',
  `is_valid` tinyint(1) NOT NULL COMMENT '账号是否有效：1 - 有效，0 - 无效',
  `reply_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '回复内容',
  `status` tinyint DEFAULT NULL COMMENT '状态：1 - 启用，0 - 禁用',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for b_user_info
-- ----------------------------
DROP TABLE IF EXISTS `b_user_info`;
CREATE TABLE `b_user_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tenant_id` int DEFAULT NULL COMMENT '租户id',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `dept_id` int DEFAULT NULL COMMENT '所属部门',
  `name` varchar(128) DEFAULT NULL COMMENT '姓名',
  `gender` int DEFAULT NULL COMMENT '性别 1-男 2-女',
  `photo` longtext COMMENT '头像',
  `id_card` varchar(255) DEFAULT NULL COMMENT '身份证编码',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(255) DEFAULT NULL COMMENT '电话',
  `operable` int DEFAULT NULL COMMENT '是否可操作 1-是 0-否',
  `usable` int DEFAULT NULL COMMENT '启用状态 1-启用 0-禁用',
  `state` int DEFAULT NULL COMMENT '状态 1-正常 0-删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_username` (`username`) COMMENT '用户名普通索引',
  KEY `idx_name` (`name`) COMMENT '姓名普通索引',
  KEY `idx_tenant_id` (`tenant_id`) COMMENT '租户普通索引',
  KEY `idx_dept_id` (`dept_id`) COMMENT '部门普通索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

-- ----------------------------
-- Table structure for b_works
-- ----------------------------
DROP TABLE IF EXISTS `b_works`;
CREATE TABLE `b_works` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `work_id` varchar(30) DEFAULT NULL COMMENT '作品id',
  `work_title` varchar(255) DEFAULT NULL COMMENT '作品标题',
  `work_comment_count` int DEFAULT NULL COMMENT '作品评论数',
  `is_monitored` tinyint(1) DEFAULT '0' COMMENT '是否监控 (1为是, 0为否)',
  `author_id` varchar(30) DEFAULT NULL COMMENT '作者id',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `modified_at` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;
