/*
 Navicat Premium Data Transfer

 Source Server         : e800
 Source Server Type    : MySQL
 Source Server Version : 50641
 Source Host           : 192.168.0.200:3306
 Source Schema         : idc

 Target Server Type    : MySQL
 Target Server Version : 50641
 File Encoding         : 65001

 Date: 30/11/2022 11:13:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for base_info
-- ----------------------------
DROP TABLE IF EXISTS `base_info`;
CREATE TABLE `base_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idc_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `idc_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `idc_owner` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `idc_contact` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `idc_owner_phone` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `idc_operator` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `idc_operator_contact` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `idc_operator_contact_phone` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `idc_area_m2` double(11, 2) NULL DEFAULT NULL,
  `idc_service_start` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `idc_storey` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `idc_total_floors` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `idc_design_cabinets_number` int(11) NULL DEFAULT NULL,
  `idc_actual_cabinets_number` int(11) NULL DEFAULT NULL,
  `idc_design_total_power_kw` double(11, 2) NULL DEFAULT NULL,
  `idc_facility_dieselgenerator` tinyint(1) NULL DEFAULT NULL,
  `idc_facility_cabinet` tinyint(1) NULL DEFAULT NULL,
  `idc_facility_office` tinyint(1) NULL DEFAULT NULL,
  `idc_facility_fossilgenerator` tinyint(1) NULL DEFAULT NULL,
  `idc_facility_renewable` tinyint(1) NULL DEFAULT NULL,
  `idc_facility_storage` tinyint(1) NULL DEFAULT NULL,
  `idc_facility_coldsite` tinyint(1) NULL DEFAULT NULL,
  `idc_submetering_supply` tinyint(1) NULL DEFAULT NULL,
  `idc_submetering_dieselgenerator` tinyint(1) NULL DEFAULT NULL,
  `idc_submetering_itec` tinyint(1) NULL DEFAULT NULL,
  `idc_submetering_cabinet` tinyint(1) NULL DEFAULT NULL,
  `idc_submetering_office` tinyint(1) NULL DEFAULT NULL,
  `idc_submetering_fossilgenerator` tinyint(1) NULL DEFAULT NULL,
  `idc_submetering_renewable` tinyint(1) NULL DEFAULT NULL,
  `idc_submetering_storage` tinyint(1) NULL DEFAULT NULL,
  `idc_submetering_coldsite` tinyint(1) NULL DEFAULT NULL,
  `idc_submetering_temperaturefield` tinyint(1) NULL DEFAULT NULL,
  `idc_submetering_airconditioning` tinyint(1) NULL DEFAULT NULL,
  `idc_submetering_lighting` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for device
-- ----------------------------
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `position` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `meter_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `meter_category` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `meter_unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `device_category` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备大类',
  `device_small_class` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备小类',
  `real_is` tinyint(1) NULL DEFAULT NULL COMMENT '真实设备',
  `need_upload` tinyint(1) NULL DEFAULT NULL COMMENT '是否上传',
  `mapping` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '对应设备',
  `formula` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计算公式',
  `coe` double(11, 6) NULL DEFAULT NULL COMMENT '倍率',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;


-- ----------------------------
-- Table structure for dic
-- ----------------------------
DROP TABLE IF EXISTS `dic`;
CREATE TABLE `dic`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NULL DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `category` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type` int(10) NULL DEFAULT NULL COMMENT '1代表设备的分类',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for dic_pue
-- ----------------------------
DROP TABLE IF EXISTS `dic_pue`;
CREATE TABLE `dic_pue`  (
  `id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计量修正表示',
  `molecular` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分子',
  `molecular_formula` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分子计算公式',
  `denominator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分母',
  `denominator_formula` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分母计算公式',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for idc_pue
-- ----------------------------
DROP TABLE IF EXISTS `idc_pue`;
CREATE TABLE `idc_pue`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `denominator_val` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `molecular_val` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `current_val` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dic_pue_id` bigint(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for idc_pue_record
-- ----------------------------
DROP TABLE IF EXISTS `idc_pue_record`;
CREATE TABLE `idc_pue_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `time` datetime(0) NOT NULL,
  `val` double(11, 2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for idc_user
-- ----------------------------
DROP TABLE IF EXISTS `idc_user`;
CREATE TABLE `idc_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for limit_ranges
-- ----------------------------
DROP TABLE IF EXISTS `limit_ranges`;
CREATE TABLE `limit_ranges`  (
  `id` bigint(11) NOT NULL,
  `lower_limit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下限',
  `upper_limit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上限',
  `val1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `val2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `val3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for low_frequency
-- ----------------------------
DROP TABLE IF EXISTS `low_frequency`;
CREATE TABLE `low_frequency`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` double(11, 2) NULL DEFAULT NULL,
  `unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `start_time` datetime(0) NULL DEFAULT NULL,
  `end_time` datetime(0) NULL DEFAULT NULL,
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type2` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for pull_time
-- ----------------------------
DROP TABLE IF EXISTS `pull_time`;
CREATE TABLE `pull_time`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `in_time` datetime(0) NULL DEFAULT NULL,
  `success_is` tinyint(1) NULL DEFAULT NULL COMMENT '是否成功',
  `type` int(2) NULL DEFAULT NULL COMMENT '1来自客户，2发送给互联网中心',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `upload_again_time` datetime(0) NULL DEFAULT NULL COMMENT '重新上传的时间',
  `pue_m` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `pue_d` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for scheduler_cron
-- ----------------------------
DROP TABLE IF EXISTS `scheduler_cron`;
CREATE TABLE `scheduler_cron`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `cron` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `state` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sys_logs
-- ----------------------------
DROP TABLE IF EXISTS `sys_logs`;
CREATE TABLE `sys_logs`  (
                             `id` int(11) NOT NULL AUTO_INCREMENT,
                             `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             `type` int(11) NULL DEFAULT NULL COMMENT '类型',
                             `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
                             `create_time` datetime(0) NULL DEFAULT NULL,
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
