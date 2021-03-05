CREATE DATABASE  IF NOT EXISTS `demo_projecct` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `demo_projecct`;
-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: demo_projecct
-- ------------------------------------------------------
-- Server version	5.7.13-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `sys_dict`
--

DROP TABLE IF EXISTS `sys_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_dict` (
  `ID` varchar(60) CHARACTER SET utf8 NOT NULL COMMENT '主键ID',
  `SD_CODE` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '代码',
  `SD_NAME` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '名称',
  `SD_PID` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '父ID',
  `SD_LEVEL` int(11) DEFAULT NULL COMMENT '层级',
  `ORDERR` int(11) DEFAULT NULL COMMENT '排序',
  `CREATE_TIME` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '更新时间',
  `SYNC_TIME` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '时间戳',
  `PATH` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '路径',
  `PATH_NAME` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '路径名称',
  `IS_END` int(11) DEFAULT NULL COMMENT '是否末节点',
  `IS_SYS_DIC` int(11) DEFAULT NULL COMMENT '是否系统字典',
  `SD_VALUE` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '值',
  `IS_DEL` int(11) DEFAULT NULL COMMENT '是否删除',
  `TYPE` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '类型',
  `BZ` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  `JD` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '经度',
  `WD` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '纬度',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict`
--

LOCK TABLES `sys_dict` WRITE;
/*!40000 ALTER TABLE `sys_dict` DISABLE KEYS */;
INSERT INTO `sys_dict` VALUES ('1247736737871310849','1247736737871310849','人员大类','0',1,1,'2020-05-24 01:29:00','2020-05-24 01:29:00','','#1247736737871310849#','#人员大类#',0,1,'',1,'人员大类','',NULL,NULL),('1247736737871310850','1247736737871310850','危害国家安全嫌疑人','1247736737871310849',2,1,'2020-05-24 01:29:00','2020-05-24 01:29:00','','#1247736737871310849#1247736737871310850#','#人员大类#危害国家安全嫌疑人#',1,1,'',1,'人员大类','',NULL,NULL),('1247736737871310851','1247736737871310851','刑事犯罪人员','1247736737871310849',2,2,'2020-05-24 01:29:00','2020-05-24 01:29:00','','#1247736737871310849#1247736737871310851#','#人员大类#刑事犯罪人员#',1,1,'',1,'人员大类','',NULL,NULL);
/*!40000 ALTER TABLE `sys_dict` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_generator`
--

DROP TABLE IF EXISTS `sys_generator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_generator` (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'ID',
  `table_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '表名',
  `menu_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '菜单名称',
  `pid` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '上级菜单',
  `gen_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_generator`
--

LOCK TABLES `sys_generator` WRITE;
/*!40000 ALTER TABLE `sys_generator` DISABLE KEYS */;
INSERT INTO `sys_generator` VALUES ('1243351567286628354','sys_dict','字典管理','e549c4b8-72ca-4ba3-91a8-9ffa1daf77cf','2020-03-27 09:38:08'),('1247090160007966722','sys_property','系统属性','e549c4b8-72ca-4ba3-91a8-9ffa1daf77cf','2020-04-06 17:13:58'),('2','sys_generator','代码生成','0','2020-03-20 13:16:47'),('3','sys_log','日志管理','0','2020-03-20 13:16:47'),('4','sys_permission','权限管理','0','2020-03-20 13:16:47'),('5','sys_role','角色管理','0','2020-03-20 13:16:47'),('6','sys_role_permission','角色权限管理','0','2020-03-20 13:16:47'),('7','sys_user','用户管理','0','2020-03-20 13:16:47'),('8','sys_user_role','用户角色管理','0','2020-03-20 13:16:47');
/*!40000 ALTER TABLE `sys_generator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_log`
--

DROP TABLE IF EXISTS `sys_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_log` (
  `id` varchar(64) CHARACTER SET utf8 NOT NULL,
  `user_id` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户id',
  `username` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户操作',
  `time` int(11) DEFAULT NULL COMMENT '响应时间',
  `method` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) CHARACTER SET utf8 DEFAULT NULL COMMENT '请求参数',
  `ip` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT 'IP地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_log`
--

LOCK TABLES `sys_log` WRITE;
/*!40000 ALTER TABLE `sys_log` DISABLE KEYS */;
INSERT INTO `sys_log` VALUES ('170293ff-9909-41bb-9f21-b5f9186cdacc','fcf34b56-a7a2-4719-9236-867495e74c31','admin','用户管理-分页获取用户列表',6,'com.pqkj.controller.UserController.pageInfo()','[{\"pageNum\":1,\"pageSize\":10}]','0:0:0:0:0:0:0:1','2020-06-24 12:07:13'),('18a840b0-4835-4df3-a300-dfc55dd4250d','fcf34b56-a7a2-4719-9236-867495e74c31','admin','用户管理-更新用户信息',38,'com.pqkj.controller.UserController.updateUserInfo()',NULL,'0:0:0:0:0:0:0:1','2020-06-24 12:16:44'),('29afcfea-5187-4698-b207-92ca6549b97d','fcf34b56-a7a2-4719-9236-867495e74c31','admin','用户管理-分页获取用户列表',5,'com.pqkj.controller.UserController.pageInfo()','[{\"pageNum\":1,\"pageSize\":10}]','0:0:0:0:0:0:0:1','2020-06-01 15:45:33'),('386595dc-a400-4da5-b080-dd299c8dd209','fcf34b56-a7a2-4719-9236-867495e74c31','admin','用户管理-分页获取用户列表',9,'com.pqkj.controller.UserController.pageInfo()','[{\"pageNum\":1,\"pageSize\":10}]','0:0:0:0:0:0:0:1','2020-06-24 12:24:49'),('3bc6556b-6a31-4534-a1ff-a80564b010b8','fcf34b56-a7a2-4719-9236-867495e74c31','admin','用户管理-更新用户信息',28,'com.pqkj.controller.UserController.updateUserInfo()',NULL,'0:0:0:0:0:0:0:1','2020-06-24 12:25:58'),('46c0c274-f165-49e9-a6c4-2d928b777e69','fcf34b56-a7a2-4719-9236-867495e74c31','admin','用户管理-分页获取用户列表',12,'com.pqkj.controller.UserController.pageInfo()','[{\"pageNum\":1,\"pageSize\":10}]','0:0:0:0:0:0:0:1','2020-06-24 12:03:45'),('51f210e5-bb54-4dfd-b7a2-7b095f1b2fbd','fcf34b56-a7a2-4719-9236-867495e74c31','admin','菜单权限管理-获取所有菜单权限',67,'com.pqkj.controller.PermissionController.getAllMenusPermission()',NULL,'0:0:0:0:0:0:0:1','2020-06-24 12:24:51'),('6bb0fcb5-eb71-4093-98d8-24d475f1100f','fcf34b56-a7a2-4719-9236-867495e74c31','admin','用户管理-新增用户',16,'com.pqkj.controller.UserController.addUser()','[{\"password\":\"dsj\",\"phone\":\"13696382274\",\"username\":\"dsj\"}]','0:0:0:0:0:0:0:1','2020-06-24 12:15:25'),('768d3020-ca1b-48a5-98f5-b4968a262796','fcf34b56-a7a2-4719-9236-867495e74c31','admin','角色管理-分页获取角色信息',97,'com.pqkj.controller.RoleController.pageInfo()','[{\"pageNum\":1,\"pageSize\":10}]','0:0:0:0:0:0:0:1','2020-06-24 12:03:43'),('769d8652-2635-477c-88e6-b1f9cf651ee9','fcf34b56-a7a2-4719-9236-867495e74c31','admin','菜单权限管理-获取所有菜单权限',86,'com.pqkj.controller.PermissionController.getAllMenusPermission()',NULL,'0:0:0:0:0:0:0:1','2020-06-24 12:24:47'),('783f62d9-cdcd-42d4-9a97-9a845f37e1f7','fcf34b56-a7a2-4719-9236-867495e74c31','admin','系统操作日志管理-分页查询系统操作日志',11,'com.pqkj.controller.SysLogController.pageInfo()','[{\"pageNum\":1,\"pageSize\":10}]','0:0:0:0:0:0:0:1','2020-06-01 15:45:58'),('78b52f8a-9403-4aab-9195-18f2ab3da514','fcf34b56-a7a2-4719-9236-867495e74c31','admin','用户管理-分页获取用户列表',12,'com.pqkj.controller.UserController.pageInfo()','[{\"pageNum\":1,\"pageSize\":10}]','0:0:0:0:0:0:0:1','2020-06-24 12:06:51'),('81ff8f66-c1e4-4871-86a6-8cdae00c509e','fcf34b56-a7a2-4719-9236-867495e74c31','admin','角色管理-分页获取角色信息',53,'com.pqkj.controller.RoleController.pageInfo()','[{\"pageNum\":1,\"pageSize\":10}]','0:0:0:0:0:0:0:1','2020-06-24 12:24:46'),('98cc513a-3eea-4923-b1d9-8ba7865a0142','fcf34b56-a7a2-4719-9236-867495e74c31','admin','用户管理-分页获取用户列表',7,'com.pqkj.controller.UserController.pageInfo()','[{\"pageNum\":1,\"pageSize\":10}]','0:0:0:0:0:0:0:1','2020-06-24 12:04:32'),('a13c8e3e-3168-48ce-9b81-0081ec59072e','fcf34b56-a7a2-4719-9236-867495e74c31','admin','用户管理-分页获取用户列表',11,'com.pqkj.controller.UserController.pageInfo()','[{\"pageNum\":1,\"pageSize\":10}]','0:0:0:0:0:0:0:1','2020-06-24 12:15:03'),('acca36d3-123b-49f8-970c-dffded0d67d2','fcf34b56-a7a2-4719-9236-867495e74c31','admin','用户管理-分页获取用户列表',6,'com.pqkj.controller.UserController.pageInfo()','[{\"pageNum\":1,\"pageSize\":10}]','0:0:0:0:0:0:0:1','2020-06-24 12:15:25'),('c998c9d4-f149-46de-a632-29fc30663f69','fcf34b56-a7a2-4719-9236-867495e74c31','admin','菜单权限管理-获取所有菜单权限',60,'com.pqkj.controller.PermissionController.getAllMenusPermission()',NULL,'0:0:0:0:0:0:0:1','2020-06-01 15:45:35'),('c9d29517-ed8d-4ce2-81b8-09118eaa7c5c','fcf34b56-a7a2-4719-9236-867495e74c31','admin','角色管理-分页获取角色信息',4,'com.pqkj.controller.RoleController.pageInfo()','[{\"pageNum\":1,\"pageSize\":10}]','0:0:0:0:0:0:0:1','2020-06-01 15:45:34'),('cc3450f5-fd3b-468b-8c85-e4a2c45af8e1','fcf34b56-a7a2-4719-9236-867495e74c31','admin','用户管理-分页获取用户列表',5,'com.pqkj.controller.UserController.pageInfo()','[{\"pageNum\":1,\"pageSize\":10}]','0:0:0:0:0:0:0:1','2020-06-24 12:24:53'),('cc77e1b7-05e4-4cab-964b-59021634049f','fcf34b56-a7a2-4719-9236-867495e74c31','admin','用户管理-退出',8,'com.pqkj.controller.UserController.logout()',NULL,'0:0:0:0:0:0:0:1','2020-06-01 15:45:28'),('d769528c-d3dc-460b-ae3f-c83ce8eaf8f3','fcf34b56-a7a2-4719-9236-867495e74c31','admin','用户管理-分页获取用户列表',6,'com.pqkj.controller.UserController.pageInfo()','[{\"pageNum\":1,\"pageSize\":10}]','0:0:0:0:0:0:0:1','2020-06-24 12:25:09'),('d9f995fb-69cb-41dc-bab3-022018a97bb4','fcf34b56-a7a2-4719-9236-867495e74c31','admin','用户管理-分页获取用户列表',7,'com.pqkj.controller.UserController.pageInfo()','[{\"pageNum\":1,\"pageSize\":10}]','0:0:0:0:0:0:0:1','2020-06-24 12:07:17'),('dc47f643-db0e-40a3-959b-e108df337b09','fcf34b56-a7a2-4719-9236-867495e74c31','admin','用户管理-分页获取用户列表',14,'com.pqkj.controller.UserController.pageInfo()','[{\"nickName\":\"\",\"pageNum\":1,\"pageSize\":10,\"userId\":\"\",\"username\":\"\"}]','0:0:0:0:0:0:0:1','2020-06-24 12:16:34'),('e1c20771-4ed7-4d6b-888e-1dbaf29a0134','fcf34b56-a7a2-4719-9236-867495e74c31','admin','用户管理-分页获取用户列表',4,'com.pqkj.controller.UserController.pageInfo()','[{\"pageNum\":1,\"pageSize\":10}]','0:0:0:0:0:0:0:1','2020-06-24 12:25:49'),('e374397d-6d65-4d4b-aa55-ab525a3d930d','fcf34b56-a7a2-4719-9236-867495e74c31','admin','用户管理-新增用户',15,'com.pqkj.controller.UserController.addUser()','[{\"password\":\"djs\",\"phone\":\"13800000000\",\"username\":\"djs\"}]','0:0:0:0:0:0:0:1','2020-06-24 12:25:09'),('f120b452-ddfd-4094-b6c1-db78484134dd','fcf34b56-a7a2-4719-9236-867495e74c31','admin','用户管理-分页获取用户列表',5,'com.pqkj.controller.UserController.pageInfo()','[{\"pageNum\":1,\"pageSize\":10}]','0:0:0:0:0:0:0:1','2020-06-24 12:04:43'),('fa4ff218-cd9b-4ae0-9b24-858bf8729a40','fcf34b56-a7a2-4719-9236-867495e74c31','admin','用户管理-分页获取用户列表',5,'com.pqkj.controller.UserController.pageInfo()','[{\"pageNum\":1,\"pageSize\":10}]','0:0:0:0:0:0:0:1','2020-06-24 12:25:58'),('fb466c95-c3d0-471e-9f6d-9ad2307a6b14','fcf34b56-a7a2-4719-9236-867495e74c31','admin','用户管理-分页获取用户列表',7,'com.pqkj.controller.UserController.pageInfo()','[{\"nickName\":\"\",\"pageNum\":1,\"pageSize\":10,\"userId\":\"\",\"username\":\"\"}]','0:0:0:0:0:0:0:1','2020-06-24 12:16:44');
/*!40000 ALTER TABLE `sys_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_permission`
--

DROP TABLE IF EXISTS `sys_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_permission` (
  `id` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '主键',
  `code` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '菜单权限编码',
  `name` varchar(300) CHARACTER SET utf8 DEFAULT NULL COMMENT '菜单权限名称',
  `perms` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：sys:user:add,sys:user:edit)',
  `url` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '访问地址URL',
  `method` varchar(10) CHARACTER SET utf8 DEFAULT NULL COMMENT '资源请求类型',
  `pid` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '父级菜单权限名称',
  `order_num` int(11) DEFAULT '0' COMMENT '排序',
  `type` tinyint(4) DEFAULT NULL COMMENT '菜单权限类型(1:目录;2:菜单;3:按钮)',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态1:正常 0：禁用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(4) DEFAULT '1' COMMENT '是否删除(1未删除；0已删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_permission`
--

LOCK TABLES `sys_permission` WRITE;
/*!40000 ALTER TABLE `sys_permission` DISABLE KEYS */;
INSERT INTO `sys_permission` VALUES ('010bcf8b-3172-4331-9941-25788ca8cbb0',NULL,'删除','sysGenerator:delete','sysGenerator/delete','DELETE','43d1bbf7-fc4a-4a10-9ad1-205b16c7c05f',1,3,1,NULL,NULL,1),('0d99b687-3f46-4632-9d56-8dd5e476dae7','','SQL 监控','','/druid/sql.html','GET','e549c4b8-72ca-4ba3-91a8-9ffa1daf77cf',98,2,1,'2019-11-09 20:58:23','2019-11-09 20:59:57',1),('185c0229-6323-45b5-9b1d-3f2a8ffa175f',NULL,'修改','sysDictTest:update','sysDictTest/update','PUT','b56d321a-4d17-40af-be49-8da611917ee8',1,3,1,'2020-03-27 07:59:06',NULL,0),('1a2ec857-e775-4377-9fb7-e3c77738b3e5','btn-role-add','新增','sys:role:add','/sys/role','POST','e0b16b95-09de-4d60-a283-1eebd424ed47',0,3,1,'2019-09-22 16:00:59',NULL,1),('1dec779d-a9ec-448a-9389-a2b4eefce119',NULL,'添加','sysGenerator:add','sysGenerator/add','POST','43d1bbf7-fc4a-4a10-9ad1-205b16c7c05f',1,3,1,NULL,NULL,1),('26764d88-1d90-402d-b355-a75deef116f2','','接口管理','','/doc.html','GET','e549c4b8-72ca-4ba3-91a8-9ffa1daf77cf',100,2,1,'2019-11-09 20:56:37','2020-03-18 03:22:52',1),('2fd4355e-9190-465b-bfd5-dfba0c152077',NULL,'列表','sysScheduleJob:list','sysScheduleJob/listByPage','POST','b5d97766-c952-4c75-a991-85c606b79fa9',1,3,1,'2020-04-07 01:28:29',NULL,1),('355f387f-a22b-4f8c-9cd6-ae10e930cd70','btn-logs-list','列表','sys:log:list','/sys/logs','POST','37101ed5-e840-4082-ae33-682ca6e41ad8',100,3,1,'2019-11-09 21:00:49','2019-11-09 21:02:08',1),('37101ed5-e840-4082-ae33-682ca6e41ad8','','日志管理','','/index/logs','GET','e549c4b8-72ca-4ba3-91a8-9ffa1daf77cf',100,2,1,'2019-11-09 20:59:09',NULL,1),('3c390dfd-0d9a-46de-9a5b-1ed884febcb2','btn-user-role-update','赋予角色','sys:user:role:update','/sys/user/roles/*','POST','78f8e29a-cccd-49e5-ada7-5af40dd95312',100,3,1,'2019-11-09 20:39:09',NULL,1),('3dac936c-c4e1-4560-ac93-905502f61ae0',NULL,'菜单权限管理','','/index/menus','GET','d6214dcb-8b6d-494b-88fa-f519fc08ff8f',98,2,1,'2019-09-22 15:18:12','2019-11-09 20:59:33',1),('4018e179-e599-41d0-bac5-c5408e1d4bc6','btn-role-deleted','删除','sys:role:delete','/sys/role/*','DELETED','e0b16b95-09de-4d60-a283-1eebd424ed47',100,3,1,'2019-11-09 20:54:28',NULL,1),('43d1bbf7-fc4a-4a10-9ad1-205b16c7c05f',NULL,'代码生成',NULL,'/index/sysGenerator','GET','e549c4b8-72ca-4ba3-91a8-9ffa1daf77cf',1,2,1,NULL,NULL,1),('46dbb867-51d6-4523-852f-b12e75b34e3d',NULL,'列表','sysGenerator:list','sysGenerator/listByPage','POST','43d1bbf7-fc4a-4a10-9ad1-205b16c7c05f',1,3,1,NULL,NULL,1),('475b4c24-40fa-4823-863a-ba6d793b7610','btn-permission-detail','详情','sys:permission:detail','/sys/permission/*','GET','3dac936c-c4e1-4560-ac93-905502f61ae0',100,3,1,'2019-11-09 20:43:05','2020-03-18 08:53:38',1),('49e36867-8cae-4aba-b6b0-97e735857a58',NULL,'删除','sysDictTest:delete','sysDictTest/delete','DELETE','b56d321a-4d17-40af-be49-8da611917ee8',1,3,1,'2020-03-27 07:59:06',NULL,0),('4a04ba4c-b393-4385-889a-d5ab14d53e49',NULL,'添加','sysDictTest:add','sysDictTest/add','POST','b56d321a-4d17-40af-be49-8da611917ee8',1,3,1,'2020-03-27 07:59:06',NULL,0),('58612968-d93c-4c21-8fdc-a825c0ab0275','btn-role-list','列表','sys:role:list','/sys/roles','POST','e0b16b95-09de-4d60-a283-1eebd424ed47',0,3,1,'2019-09-22 16:04:33',NULL,1),('5ff828bf-300a-4488-af4d-a3b5b86415de',NULL,'系统属性',NULL,'/index/sysProperty','GET','e549c4b8-72ca-4ba3-91a8-9ffa1daf77cf',1,2,1,'2020-04-06 09:13:58',NULL,1),('60c3443a-5ec9-4ea7-9484-d2870af93059',NULL,'修改','sysGenerator:update','sysGenerator/update','PUT','43d1bbf7-fc4a-4a10-9ad1-205b16c7c05f',1,3,1,NULL,NULL,1),('783aedd8-5d93-46b6-8c6d-e4d3f0f3f466','btn-user-list','列表','sys:user:list','/sys/users','POST','78f8e29a-cccd-49e5-ada7-5af40dd95312',100,3,1,'2020-01-01 19:44:37',NULL,1),('7890b9aea44e7a1c3f1dbdea36804a3f','','数据管理','','xx.html','','0',99,1,1,'2020-03-24 13:18:36','2020-03-24 13:20:10',1),('78f8e29a-cccd-49e5-ada7-5af40dd95312','','用户管理','','/index/users','GET','d6214dcb-8b6d-494b-88fa-f519fc08ff8f',100,2,1,'2020-01-01 19:30:30','2019-11-09 20:48:29',1),('7c6e6ed9-8bd2-4152-ae25-9f0274e1b2ac',NULL,'添加','sysScheduleJob:add','sysScheduleJob/add','POST','b5d97766-c952-4c75-a991-85c606b79fa9',1,3,1,'2020-04-07 01:28:29',NULL,1),('817a58d1-ec82-4106-870a-bcc0bfaee0e7','btn-user-detail','详情','sys:user:detail','/sys/user/*','GET','78f8e29a-cccd-49e5-ada7-5af40dd95312',100,3,1,'2019-11-09 20:24:24','2019-11-09 20:48:05',1),('8623c941-5746-4667-9fb8-76f6f5059788','btn-permission-deleted','删除','sys:permission:delete','/sys/permission/*','DELETED','3dac936c-c4e1-4560-ac93-905502f61ae0',100,3,1,'2019-11-07 22:35:50','2019-11-09 20:44:44',1),('9805bec4-3970-49e8-a50b-f9f4c1e99c4a',NULL,'列表','sysProperty:list','sysProperty/listByPage','POST','5ff828bf-300a-4488-af4d-a3b5b86415de',1,3,1,'2020-04-06 09:13:58',NULL,1),('992d1a8d-b5f8-44fc-9a48-4b3e60a7b15e','btn-role-update','更新','sys:role:update','/sys/role','PUT','e0b16b95-09de-4d60-a283-1eebd424ed47',0,3,1,'2019-09-22 16:03:46',NULL,1),('9c85db9e-b32b-408d-9366-45bcdcd8c850',NULL,'列表','sysDictTest:list','sysDictTest/listByPage','POST','b56d321a-4d17-40af-be49-8da611917ee8',1,3,1,'2020-03-27 07:59:06',NULL,0),('ab527b86-f86f-45f8-bdae-bb8f27450d93',NULL,'字典管理',NULL,'/index/sysDict','GET','e549c4b8-72ca-4ba3-91a8-9ffa1daf77cf',1,2,1,'2020-03-27 01:38:09',NULL,1),('b01614ab-0538-4cca-bb61-b46f18c60aa4','btn-role-detail','详情','sys:role:detail','/sys/role/*','GET','e0b16b95-09de-4d60-a283-1eebd424ed47',100,3,1,'2019-09-22 16:06:13','2019-11-09 20:55:08',1),('b180aafa-0d1a-4898-b838-bc20cd44356d',NULL,'编辑','sys:permission:update','/sys/permission','PUT','3dac936c-c4e1-4560-ac93-905502f61ae0',100,3,1,'2019-11-07 22:27:22','2019-11-09 20:48:44',1),('c0a84726-47d8-4d7a-8d53-0736a4586647','btn-user-add','新增','sys:user:add','/sys/user','POST','78f8e29a-cccd-49e5-ada7-5af40dd95312',100,3,1,'2019-11-09 20:25:18',NULL,1),('c30389e8-eb3e-4a0d-99c4-639e1893a05f','btn-permission-list','列表','sys:permission:list','/sys/permissions','POST','3dac936c-c4e1-4560-ac93-905502f61ae0',100,3,1,'2019-09-22 15:26:45','2019-11-09 20:45:19',1),('c30389e8-eb3e-4a0d-99c4-639e1893f50a','btn-permission-list','新增','sys:permission:add','/sys/permission','POST','3dac936c-c4e1-4560-ac93-905502f61ae0',100,3,1,'2019-09-22 15:26:45','2019-11-09 20:45:25',1),('c73a7bbb-a009-4d0e-8937-46029c8cfe60',NULL,'修改','sysProperty:update','sysProperty/update','PUT','5ff828bf-300a-4488-af4d-a3b5b86415de',1,3,1,'2020-04-06 09:13:58',NULL,1),('d6214dcb-8b6d-494b-88fa-f519fc08ff8f',NULL,'组织管理','','xx.html','','0',1,1,1,'2019-09-28 15:16:14','2020-03-20 09:58:37',1),('da848675-be50-4c8c-84b6-35f9e6238b10',NULL,'修改','sysDict:update','sysDict/update','PUT','ab527b86-f86f-45f8-bdae-bb8f27450d93',1,3,1,'2020-03-27 01:38:09',NULL,1),('db2d31b7-fdcb-478e-bfde-a55eb8b0aa43','btn-user-role-detail','拥有角色','sys:user:role:detail','/sys/user/roles/*','GET','78f8e29a-cccd-49e5-ada7-5af40dd95312',100,3,1,'2019-11-09 20:29:47',NULL,1),('e0b16b95-09de-4d60-a283-1eebd424ed47','','角色管理','','/index/roles','GET','d6214dcb-8b6d-494b-88fa-f519fc08ff8f',99,2,1,'2019-09-22 15:45:45','2019-11-09 20:59:22',1),('e549c4b8-72ca-4ba3-91a8-9ffa1daf77cf','','系统管理','','xx.html','','0',98,1,1,'2019-11-09 20:56:01',NULL,1),('f21ed5e8-0756-45dc-91c5-f58a9463caaa','btn-user-update','更新','sys:user:update','/sys/user','PUT','78f8e29a-cccd-49e5-ada7-5af40dd95312',100,3,1,'2019-11-09 20:23:20',NULL,1),('f28b9215-3119-482d-bdc1-1f4c3f7c0869','btn-user-deleted','删除','sys:user:delete','/sys/user','DELETED','78f8e29a-cccd-49e5-ada7-5af40dd95312',100,3,1,'2019-11-09 20:26:45',NULL,1),('f2ff9320-c643-4c85-8b68-15f86d47b88b','btn-log-deleted','删除','sys:log:delete','/sys/logs','DELETED','37101ed5-e840-4082-ae33-682ca6e41ad8',100,3,1,'2019-11-09 21:01:49',NULL,1);
/*!40000 ALTER TABLE `sys_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_property`
--

DROP TABLE IF EXISTS `sys_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_property` (
  `id` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '主键id',
  `jtwjdz` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '静态文件地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_property`
--

LOCK TABLES `sys_property` WRITE;
/*!40000 ALTER TABLE `sys_property` DISABLE KEYS */;
INSERT INTO `sys_property` VALUES ('21333331','http://120.79.9.200:9801/static');
/*!40000 ALTER TABLE `sys_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role` (
  `id` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '角色名称',
  `description` varchar(300) CHARACTER SET utf8 DEFAULT NULL,
  `status` tinyint(4) DEFAULT '1' COMMENT '状态(1:正常0:弃用)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(4) DEFAULT '1' COMMENT '是否删除(1未删除；0已删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES ('11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','超级管理员','拥有所有权限-不能删除',1,'2019-11-01 19:26:29','2020-03-24 13:19:35',1),('2b07695a35c6d31973e3bebdb0366344','开发工程师','dev',1,'2020-03-30 07:45:29',NULL,1);
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_permission`
--

DROP TABLE IF EXISTS `sys_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role_permission` (
  `id` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '主键',
  `role_id` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '角色id',
  `permission_id` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '菜单权限id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_permission`
--

LOCK TABLES `sys_role_permission` WRITE;
/*!40000 ALTER TABLE `sys_role_permission` DISABLE KEYS */;
INSERT INTO `sys_role_permission` VALUES ('013e31086c66bf65c5d9e3257b5abd6a','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','43d1bbf7-fc4a-4a10-9ad1-205b16c7c05f','2020-06-01 15:44:45'),('0208b556ec7e5a3da2d52f091bf14154','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','010bcf8b-3172-4331-9941-25788ca8cbb0','2020-06-01 15:44:45'),('045b6febcb2b4e08ce13ec40199bfab4','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','7890b9aea44e7a1c3f1dbdea36804a3f','2020-06-01 15:44:45'),('079f2b201eb3280a8c89dc1f3765664e','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','db2d31b7-fdcb-478e-bfde-a55eb8b0aa43','2020-06-01 15:44:45'),('090173bf0c9de0062a1657a569ab9a45','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','3dac936c-c4e1-4560-ac93-905502f61ae0','2020-06-01 15:44:45'),('14e43e2fab12fa32e9778bdbc35dc246','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','37101ed5-e840-4082-ae33-682ca6e41ad8','2020-06-01 15:44:45'),('190016c02003939f79a37f4bbaa38746','2b07695a35c6d31973e3bebdb0366344','43d1bbf7-fc4a-4a10-9ad1-205b16c7c05f','2020-06-01 15:44:45'),('1b194d4223850c69a679ddf84fce0a21','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','46dbb867-51d6-4523-852f-b12e75b34e3d','2020-06-01 15:44:45'),('2017a7b1ceae33532a87fe15590fc67c','2b07695a35c6d31973e3bebdb0366344','da848675-be50-4c8c-84b6-35f9e6238b10','2020-06-01 15:44:45'),('21d7389e2c4266279440ed1c58dc0e5c','2b07695a35c6d31973e3bebdb0366344','7890b9aea44e7a1c3f1dbdea36804a3f','2020-06-01 15:44:45'),('2ced873925986c23a024f959b870afbe','2b07695a35c6d31973e3bebdb0366344','1dec779d-a9ec-448a-9389-a2b4eefce119','2020-06-01 15:44:45'),('2ec6058e86a1a383fe5a1fefe9e70552','2b07695a35c6d31973e3bebdb0366344','26764d88-1d90-402d-b355-a75deef116f2','2020-06-01 15:44:45'),('346100266ce7ab270ca9fc9870cc5de3','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','783aedd8-5d93-46b6-8c6d-e4d3f0f3f466','2020-06-01 15:44:45'),('37194668f44cee0dfc286b2b3b2ee8e1','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','1a2ec857-e775-4377-9fb7-e3c77738b3e5','2020-06-01 15:44:45'),('3e71aef80b9267c8e688f6de30e14e09','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','f21ed5e8-0756-45dc-91c5-f58a9463caaa','2020-06-01 15:44:45'),('416ac64ba1276b32988331eb0ab84ffa','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','c0a84726-47d8-4d7a-8d53-0736a4586647','2020-06-01 15:44:45'),('4535fdf83ee95f832f332183bcdf7ecb','2b07695a35c6d31973e3bebdb0366344','ab527b86-f86f-45f8-bdae-bb8f27450d93','2020-06-01 15:44:45'),('464c8c9766f5544ed8e4a6b7f68853d2','2b07695a35c6d31973e3bebdb0366344','60c3443a-5ec9-4ea7-9484-d2870af93059','2020-06-01 15:44:45'),('4c4bd7af396f1078126f07d1c619ec8c','2b07695a35c6d31973e3bebdb0366344','010bcf8b-3172-4331-9941-25788ca8cbb0','2020-06-01 15:44:45'),('58ae804174b234328b089f72f82fe4d9','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','f2ff9320-c643-4c85-8b68-15f86d47b88b','2020-06-01 15:44:45'),('65fbf40bf3f9e2e888d402bb797ee6b3','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','817a58d1-ec82-4106-870a-bcc0bfaee0e7','2020-06-01 15:44:45'),('66682851eae2bedba51dbaf4648b2660','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','1dec779d-a9ec-448a-9389-a2b4eefce119','2020-06-01 15:44:45'),('6ae9ac855c0e9cd0a487dd170392f588','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','da848675-be50-4c8c-84b6-35f9e6238b10','2020-06-01 15:44:45'),('6ea38fe57cd2e56f12d88d870d0dc5f6','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','9805bec4-3970-49e8-a50b-f9f4c1e99c4a','2020-06-01 15:44:45'),('71e8f7211589cf10a5babecd54f08d83','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','e0b16b95-09de-4d60-a283-1eebd424ed47','2020-06-01 15:44:45'),('776e935bdd60476915cbaca52b60c383','2b07695a35c6d31973e3bebdb0366344','f2ff9320-c643-4c85-8b68-15f86d47b88b','2020-06-01 15:44:45'),('78fdb61ee9dec3069e4c1c67e9a48672','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','0d99b687-3f46-4632-9d56-8dd5e476dae7','2020-06-01 15:44:45'),('7bde85680835b083cfe13286a4629b51','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','2fd4355e-9190-465b-bfd5-dfba0c152077','2020-06-01 15:44:45'),('81376e14081edfafc79e2c12e18a78bf','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','f28b9215-3119-482d-bdc1-1f4c3f7c0869','2020-06-01 15:44:45'),('821375731e5d5165c5ef50a664f05537','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','355f387f-a22b-4f8c-9cd6-ae10e930cd70','2020-06-01 15:44:45'),('8eea75b58b72ea4d841d8e1fd290c11d','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','58612968-d93c-4c21-8fdc-a825c0ab0275','2020-06-01 15:44:45'),('9ad7e3ec62da0f6f59d3d718b1b757c8','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','475b4c24-40fa-4823-863a-ba6d793b7610','2020-06-01 15:44:45'),('9c0ed5a1f8ecb9b6717abd708b81cdef','2b07695a35c6d31973e3bebdb0366344','37101ed5-e840-4082-ae33-682ca6e41ad8','2020-06-01 15:44:45'),('9c2f45435e28a01b20b62365e57bb8d8','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','c73a7bbb-a009-4d0e-8937-46029c8cfe60','2020-06-01 15:44:45'),('9ea647056be99638670eb02391eb6fdf','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','b01614ab-0538-4cca-bb61-b46f18c60aa4','2020-06-01 15:44:45'),('a410b2146e14aabea07e8f27d2b8fbd7','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','78f8e29a-cccd-49e5-ada7-5af40dd95312','2020-06-01 15:44:45'),('b95b693e21461d39cd0635731a0147ef','2b07695a35c6d31973e3bebdb0366344','355f387f-a22b-4f8c-9cd6-ae10e930cd70','2020-06-01 15:44:45'),('b996228d69b2382ef10feddc0833778b','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','26764d88-1d90-402d-b355-a75deef116f2','2020-06-01 15:44:45'),('ba962aa65af67e57ae7144c2b6669d43','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','c30389e8-eb3e-4a0d-99c4-639e1893f50a','2020-06-01 15:44:45'),('c5b60c6f26d47331dc8d5cfebe139523','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','c30389e8-eb3e-4a0d-99c4-639e1893a05f','2020-06-01 15:44:45'),('c99c37fb562bcd2ce07d121693ba7419','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','d6214dcb-8b6d-494b-88fa-f519fc08ff8f','2020-06-01 15:44:45'),('cdf330490ac2109675aab6637337272c','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','ab527b86-f86f-45f8-bdae-bb8f27450d93','2020-06-01 15:44:45'),('d427b84810b6e8ce37f6a166ef6ab03a','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','3c390dfd-0d9a-46de-9a5b-1ed884febcb2','2020-06-01 15:44:45'),('d4d2de7ce9ca7ab3dbc7324b32005851','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','8623c941-5746-4667-9fb8-76f6f5059788','2020-06-01 15:44:45'),('d5ce2632f3137542b6172aac5e283963','2b07695a35c6d31973e3bebdb0366344','e549c4b8-72ca-4ba3-91a8-9ffa1daf77cf','2020-06-01 15:44:45'),('d5df1702669d5e5a879ef44b114c398c','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','7c6e6ed9-8bd2-4152-ae25-9f0274e1b2ac','2020-06-01 15:44:45'),('db3e84046ff535a09207887644352233','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','b180aafa-0d1a-4898-b838-bc20cd44356d','2020-06-01 15:44:45'),('e43a40070bf92ab48733acca3678c5ac','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','4018e179-e599-41d0-bac5-c5408e1d4bc6','2020-06-01 15:44:45'),('e4e754612b8b3c35785e438dcacfe8c4','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','992d1a8d-b5f8-44fc-9a48-4b3e60a7b15e','2020-06-01 15:44:45'),('ea2d3ef0982801103fb2357aee005b84','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','60c3443a-5ec9-4ea7-9484-d2870af93059','2020-06-01 15:44:45'),('ebf4684c51dfddcddbbbd5a3957546e6','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','e549c4b8-72ca-4ba3-91a8-9ffa1daf77cf','2020-06-01 15:44:45'),('f9180b70741843c0d65c5e90b2983366','2b07695a35c6d31973e3bebdb0366344','0d99b687-3f46-4632-9d56-8dd5e476dae7','2020-06-01 15:44:45'),('faea0bc0a34c805b4335615216b98ff0','2b07695a35c6d31973e3bebdb0366344','46dbb867-51d6-4523-852f-b12e75b34e3d','2020-06-01 15:44:45'),('fb59f310a8ead2145d08ceb81dd62111','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','5ff828bf-300a-4488-af4d-a3b5b86415de','2020-06-01 15:44:45');
/*!40000 ALTER TABLE `sys_role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user` (
  `id` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '用户id',
  `username` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '账户名称',
  `salt` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '加密盐值',
  `password` varchar(200) CHARACTER SET utf8 NOT NULL COMMENT '用户密码密文',
  `phone` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '手机号码',
  `dept_id` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '部门id',
  `real_name` varchar(60) CHARACTER SET utf8 DEFAULT NULL COMMENT '真实名称',
  `nick_name` varchar(60) CHARACTER SET utf8 DEFAULT NULL COMMENT '昵称',
  `email` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '邮箱(唯一)',
  `status` tinyint(4) DEFAULT '1' COMMENT '账户状态(1.正常 2.锁定 )',
  `sex` tinyint(4) DEFAULT '1' COMMENT '性别(1.男 2.女)',
  `deleted` tinyint(4) DEFAULT '1' COMMENT '是否删除(1未删除；0已删除)',
  `create_id` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '创建人',
  `update_id` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '更新人',
  `create_where` tinyint(4) DEFAULT '1' COMMENT '创建来源(1.web 2.android 3.ios )',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL,
  `xq_id` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '小区id',
  `clear_pwd` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '明文密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES ('3f650f1c940ebae1c454e2c84df3f080','dev','6b6a0eb9c2a84dfaaf95','8c1d800fea388c46b15d0a42c2be5f07','13800000000',NULL,NULL,NULL,NULL,1,1,1,NULL,NULL,1,'2020-03-30 07:44:31',NULL,'1246995513644396545',NULL),('47066160c88342d8a5ec7ebf4df819bb','djs','fcd210dca68b40ed8690','bf97fed96e501fa9aa1804bb731200bd','13800000000','',NULL,NULL,NULL,1,1,1,NULL,'fcf34b56-a7a2-4719-9236-867495e74c31',1,'2020-06-24 12:25:09','2020-06-24 12:25:58',NULL,'dsj'),('56316cdc437cb78e225bfe481c4d4db7','heng','7dfee577469e4b0db874','6646068ec580cc0e96f1f6e3c4ca669f','110',NULL,NULL,NULL,NULL,1,1,1,NULL,NULL,1,'2020-04-03 08:37:46',NULL,'1246995513644396545',NULL),('5fb360d460e6966f75129db2c0e0e1f4','zhou','8584098ba84b4c67acff','504ec7e6aaf43f4a8a9c0e9c57b0a51e','',NULL,NULL,NULL,NULL,1,1,1,NULL,NULL,1,'2020-04-01 11:24:53',NULL,'1246995513644396545',NULL),('67fa3f4a4698fa2fd29569405dd88f8e','test','84b5ffd7af474c51b73e','9bcd109a2ddc803bf90d896ac56df06d','13800000000',NULL,NULL,NULL,NULL,1,1,1,NULL,NULL,1,'2020-05-06 12:36:53',NULL,NULL,NULL),('a3481f4ee9977641109fc58ee568715a','liu','5936ddb483084348890b','9eb6b091b461b5ae9b781f1eb20b2c92','',NULL,NULL,NULL,NULL,1,1,1,NULL,NULL,1,'2020-04-01 09:41:42',NULL,'1246995513644396545',NULL),('b29ea740232ca03bfa1594ffb8d4fd48','dsj','7737b6ce0f1d4a2b933f','561b0b25508b1b0085619048aec37295','13696382274','',NULL,NULL,NULL,1,1,1,NULL,'fcf34b56-a7a2-4719-9236-867495e74c31',1,'2020-06-24 12:15:25','2020-06-24 12:16:44',NULL,'dsj'),('dcf7630cce2573a5dd11f3fd3f4eef30','deng','147b6293d13a4108bf14','81aee3a1cc06282255bbf990883ecbe9','13106842116',NULL,NULL,NULL,NULL,1,1,1,NULL,NULL,1,'2020-04-01 09:41:12',NULL,'1246995513644396545',NULL),('fcf34b56-a7a2-4719-9236-867495e74c31','admin','324ce32d86224b00a02b','2102b59a75ab87616b62d0b9432569d0','110','72a4f388-50f8-4019-8c67-530cd7c74e7a','杜杰','dj','110@163.com',1,1,1,NULL,'fcf34b56-a7a2-4719-9236-867495e74c31',3,'2019-09-22 19:38:05','2020-03-18 09:15:22','1246995513644396545',NULL);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user_role` (
  `id` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '用户id',
  `user_id` varchar(64) CHARACTER SET utf8 DEFAULT NULL,
  `role_id` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '角色id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES ('0f8526cf8a8984a8fcebb9f6868b5d44','67fa3f4a4698fa2fd29569405dd88f8e','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','2020-05-06 12:37:32'),('101c7d5478afe33c6a9de563a953e934','56316cdc437cb78e225bfe481c4d4db7','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','2020-04-03 08:38:00'),('33137e13-2318-42e4-a8e7-4bb7eea0f4ef','1ff14b2c-d32b-496b-8fb7-d988b838b8e0','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','2020-03-18 08:16:12'),('50d40467f440af3a40417a1fee3526d9','7f8c0e32-058e-409d-8e7c-22a9afe6a0a0','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','2020-03-24 10:34:54'),('543b997235bc000a9ef1430a63f4ceab','5fb360d460e6966f75129db2c0e0e1f4','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','2020-04-01 11:25:06'),('6270ba485ac5a0a1528ff67f96f8b1c1','dcf7630cce2573a5dd11f3fd3f4eef30','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','2020-04-01 10:00:33'),('a9b4df61c4a1e5a9826b41a8fe9500c6','3f650f1c940ebae1c454e2c84df3f080','2b07695a35c6d31973e3bebdb0366344','2020-03-30 07:45:43'),('c3b5b4168b13244f9752d1fd1b0115f8','a3481f4ee9977641109fc58ee568715a','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','2020-04-01 10:00:40'),('da3dd29ea2f7957c144149e5040ce275','fcf34b56-a7a2-4719-9236-867495e74c31','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','2020-03-19 12:17:45'),('dfcd2f2112c8b924a9fad1fd121b01f2','fc906bda496f2080e2cede848222fc9d','2b07695a35c6d31973e3bebdb0366344','2020-04-07 04:05:57'),('f8ad372b79490bd1ad99e7ac77409647','1dfaafa7-fddf-46f2-b3d8-11bfe9ac7230','11b3b80c-4a0b-4a92-96ea-fdd4f7a4a7e9','2020-03-19 02:39:01');
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'demo_projecct'
--

--
-- Dumping routines for database 'demo_projecct'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-06-24 12:27:45
