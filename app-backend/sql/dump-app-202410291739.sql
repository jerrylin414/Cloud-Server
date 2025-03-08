-- MySQL dump 10.13  Distrib 8.0.17, for Win64 (x86_64)
--
-- Host: localhost    Database: app
-- ------------------------------------------------------
-- Server version	8.0.17

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `app_files`
--

DROP TABLE IF EXISTS `app_files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `app_files` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `folder_id` bigint(20) DEFAULT NULL COMMENT '文件夹id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `origin_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `file_name` varchar(100) DEFAULT NULL,
  `file_type` varchar(100) DEFAULT NULL,
  `file_path` varchar(100) DEFAULT NULL,
  `file_data` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '二进制大小',
  `file_identifier` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件标志md5',
  `file_size` bigint(20) DEFAULT NULL COMMENT 'B-size',
  `is_bin` varchar(1) DEFAULT NULL,
  `is_deleted` varchar(1) DEFAULT NULL,
  `is_finished` varchar(1) DEFAULT NULL COMMENT '是否上传完',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_files`
--

LOCK TABLES `app_files` WRITE;
/*!40000 ALTER TABLE `app_files` DISABLE KEYS */;
INSERT INTO `app_files` VALUES (2,1,1,'orgin2.png','test_1720599298333.png','---','http://127.0.0.1:9000/image-bucket/20240710/test_1720599298333.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-10 16:14:58'),(3,1,1,'orgin22.png','test_1721013150955.png','image/png','http://127.0.0.1:9000/image-bucket/20240715/test_1721013150955.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-15 11:12:31'),(4,1,1,'orgin22.png','test_1721031639158.png','image/png','http://127.0.0.1:9000/image-bucket/20240715/test_1721031639158.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-15 16:20:39'),(5,16,4,'orgin5.png','test_1721033470900.png','image/png','http://127.0.0.1:9000/image-bucket/20240715/test_1721033470900.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-15 16:51:11'),(6,16,4,'orgin2.png','test_1721033498631.png','image/png','http://127.0.0.1:9000/image-bucket/20240715/test_1721033498631.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-15 16:51:39'),(12,4,4,'orgin2.png','test_1721036084283.png','image/png','http://127.0.0.1:9000/image-bucket/20240715/test_1721036084283.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-15 17:34:44'),(13,17,4,'orgin2.png','test_1721096090380.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721096090380.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 10:14:50'),(14,15,4,'orgin2.png','test_1721096190351.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721096190351.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 10:16:30'),(15,18,4,'orgin2.png','test_1721096224559.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721096224559.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 10:17:05'),(16,17,4,'orgin2.png','test_1721096251119.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721096251119.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 10:17:31'),(17,17,4,'orgin2.png','test_1721096283924.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721096283924.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 10:18:04'),(18,17,4,'orgin2.png','test_1721096325219.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721096325219.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 10:18:45'),(19,17,4,'orgin2.png','test_1721096391263.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721096391263.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 10:19:51'),(20,16,4,'orgin2.png','test_1721096511313.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721096511313.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 10:21:51'),(21,17,4,'orgin2.png','test_1721096715886.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721096715886.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 10:25:16'),(22,34,4,'orgin2.png','test_1721096840335.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721096840335.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 10:27:20'),(23,17,4,'orgin2.png','test_1721096853282.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721096853282.png','707.6 KB',NULL,NULL,'N','N','Y','2024-07-16 10:27:33'),(24,17,4,'orgin2.png','test_1721096859300.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721096859300.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 10:27:39'),(25,17,4,'orgin2.png','test_1721097198693.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721097198693.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 10:33:19'),(26,17,4,'orgin2.png','test_1721097235243.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721097235243.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 10:33:55'),(27,17,4,'orgin2.png','test_1721097242271.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721097242271.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 10:34:02'),(28,17,4,'orgin2.png','test_1721097366480.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721097366480.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 10:36:07'),(29,17,4,'orgin2.png','test_1721097468194.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721097468194.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 10:37:48'),(30,14,4,'orgin2.png','test_1721097551319.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721097551319.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 10:39:14'),(31,17,4,'orgin2.png','test_1721097672663.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721097672663.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 10:41:13'),(32,14,4,'orgin2.png','test_1721097866895.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721097866895.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 10:44:27'),(33,14,4,'orgin2.png','test_1721097923851.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721097923851.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 10:45:24'),(34,16,4,'orgin2.png','test_1721097988226.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721097988226.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 10:46:28'),(35,16,4,'orgin2.png','test_1721098021532.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721098021532.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 10:47:02'),(36,16,4,'orgin2.png','test_1721098025872.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721098025872.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 10:47:06'),(37,19,4,'orgin2.png','test_1721112923092.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721112923092.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 14:55:23'),(38,43,4,'orgin2.png','test_1721114588669.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721114588669.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 15:23:09'),(39,2,4,'orgin2.png','test_1721114636113.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721114636113.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 15:23:56'),(40,36,4,'orgin2.png','test_1721114771698.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721114771698.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 15:26:12'),(41,21,4,'orgin2.png','test_1721114791204.png','image/png','http://127.0.0.1:9000/image-bucket/20240716/test_1721114791204.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-16 15:26:31'),(42,31,4,'orgin2.png','test_1721184125818.png','image/png','http://127.0.0.1:9000/image-bucket/20240717/test_1721184125818.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-17 10:42:06'),(50,32,4,'11.png','11_1721898808464.png','image/png','http://127.0.0.1:9000/image-bucket/20240725/11_1721898808464.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-25 17:13:29'),(51,0,4,'11.png','11_1721899014807.png','image/png','http://127.0.0.1:9000/image-bucket/20240725/11_1721899014807.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-25 17:16:55'),(52,33,4,'11.png','11_1721899080307.png','image/png','http://127.0.0.1:9000/image-bucket/20240725/11_1721899080307.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-25 17:18:00'),(53,37,4,'11.png','11_1721962724814.png','image/png','http://127.0.0.1:9000/image-bucket/20240726/11_1721962724814.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-26 10:58:45'),(54,36,4,'wx.png','wx_1721962734685.png','image/png','http://127.0.0.1:9000/image-bucket/20240726/wx_1721962734685.png','707.6 KB',NULL,NULL,'Y','N','Y','2024-07-26 10:58:55'),(55,36,4,'wx.png','wx_1721962765753.png','image/png','http://127.0.0.1:9000/image-bucket/20240726/wx_1721962765753.png','707.6 KB',NULL,NULL,'N','N','Y','2024-07-26 10:59:26'),(56,38,4,'11.png','11_1721965715844.png','image/png','http://127.0.0.1:9000/image-bucket/20240726/11_1721965715844.png','725.3 KB',NULL,NULL,'Y','N','Y','2024-07-26 11:48:36'),(57,39,4,'11.png','11_1721965726315.png','image/png','http://127.0.0.1:9000/image-bucket/20240726/11_1721965726315.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-26 11:48:46'),(58,38,4,'11.png','11_1721965737690.png','image/png','http://127.0.0.1:9000/image-bucket/20240726/11_1721965737690.png','725.3 KB',NULL,NULL,'N','N','Y','2024-07-26 11:48:58'),(59,0,4,'11.png','11_1723192379323.png','image/png','http://127.0.0.1:9000/image-bucket/20240809/11_1723192379323.png','725.3 KB',NULL,NULL,'Y','N','Y','2024-08-09 16:32:59'),(60,0,4,'11.png','11_1723192396522.png','image/png','http://127.0.0.1:9000/image-bucket/20240809/11_1723192396522.png','725.3 KB',NULL,NULL,'Y','N','Y','2024-08-09 16:33:17'),(61,0,4,'11.png','11_1723192425838.png','image/png','http://127.0.0.1:9000/image-bucket/20240809/11_1723192425838.png','725.3 KB',NULL,NULL,'Y','N','Y','2024-08-09 16:33:46'),(62,0,4,'11.png','11_1723192494472.png','image/png','http://127.0.0.1:9000/image-bucket/20240809/11_1723192494472.png','725.3 KB',NULL,NULL,'Y','N','Y','2024-08-09 16:34:56'),(63,0,4,'11.png','11_1723192538733.png','image/png','http://127.0.0.1:9000/image-bucket/20240809/11_1723192538733.png','725.3 KB',NULL,NULL,'Y','N','Y','2024-08-09 16:35:39'),(64,0,4,'11.png','11_1723192569868.png','image/png','http://127.0.0.1:9000/image-bucket/20240809/11_1723192569868.png','725.3 KB',NULL,NULL,'N','N','Y','2024-08-09 16:36:10'),(65,0,4,'11.png','11_1723192669404.png','image/png','http://127.0.0.1:9000/image-bucket/20240809/11_1723192669404.png','725.3 KB',NULL,NULL,'N','N','Y','2024-08-09 16:37:49'),(66,0,4,'11.png','11_1723192743045.png','image/png','http://127.0.0.1:9000/image-bucket/20240809/11_1723192743045.png','725.3 KB',NULL,NULL,'N','N','Y','2024-08-09 16:39:03'),(67,0,4,'11.png','11_1723192798992.png','image/png','http://127.0.0.1:9000/image-bucket/20240809/11_1723192798992.png','725.3 KB',NULL,NULL,'N','N','Y','2024-08-09 16:39:59'),(68,0,4,'11.png','11_1723192837686.png','image/png','http://127.0.0.1:9000/image-bucket/20240809/11_1723192837686.png','725.3 KB',NULL,NULL,'N','N','Y','2024-08-09 16:40:38'),(69,0,4,'11.png','11_1723192866244.png','image/png','http://127.0.0.1:9000/image-bucket/20240809/11_1723192866244.png','725.3 KB',NULL,NULL,'N','N','Y','2024-08-09 16:41:06'),(76,0,8,'000.mp4','000.mp4','.mp4','http://127.0.0.1:9000/share/2024-10-22/4a513287-fe86-4255-9e2d-e3a4d05f0b03.mp4','5KB','6bdb95eb2f9d834242b2cbbc7c591360',NULL,'N','N','Y','2024-10-22 17:05:07'),(77,0,8,'ggBond.jpg','ggBond.jpg','.jpg','http://127.0.0.1:9000/share/2024-10-22/1917c76a-531b-4e66-bc46-0f9822721f3a.jpg','0KB','34dc386fc0ebcdfc7bce8ee118b0e0fd',NULL,'N','N','Y','2024-10-22 17:19:30'),(78,0,8,'war-files.zip','war-files.zip','.zip','http://127.0.0.1:9000/share/2024-10-22/891c16a4-1118-4f65-b994-41b3da3a16a9.zip','177KB','b0410cfc98412232750128ae61c13771',NULL,'N','N','Y','2024-10-22 17:21:14'),(79,0,8,'jdk-17.0.12_windows-x64_bin.exe','jdk-17.0.12_windows-x64_bin.exe','.exe','http://127.0.0.1:9000/share/2024-10-25/81004f0a-60d6-40bb-b21a-e4d43f2f4a60.exe','153KB','368cf150a29576951fd9a99298791506',NULL,'Y','N',NULL,'2024-10-25 12:07:06'),(80,0,8,'jdk-23_windows-x64_bin.exe','jdk-23_windows-x64_bin.exe','.exe','http://127.0.0.1:9000/share/2024-10-25/ddd7ee68-6347-4e38-95ff-cd76688f46d7.exe','205KB','260679d3c9fc020af5f1ab1a6771c26c',NULL,'Y','N',NULL,'2024-10-25 12:07:49'),(81,0,8,'large-file.7z','large-file.7z','e.7z','http://127.0.0.1:9000/share/2024-10-25/f1556b4c-20b0-4565-ae06-ad793736c0f6.7z','3500KB','c64ba0da0f56d8e0d89f5b2bedf32d0c',NULL,'N','N','N','2024-10-25 16:37:57'),(83,55,9,'20241025.zip','20241025.zip','.zip','http://127.0.0.1:9000/share/2024-10-29/38a6343a-15ca-4b7b-8670-346118f838e3.zip','103KB','23e2738663f49c16657fbf84367ce130',108584682,'N','N','Y','2024-10-29 14:50:12'),(84,56,9,'YesMan.zip','YesMan.zip','.zip','http://127.0.0.1:9000/share/2024-10-29/1cde4541-935c-4a39-aafa-bb723676d137.zip','356KB','5f45e5a3ed27a2f0f98f3295e5c8f95b',373618657,'N','N','Y','2024-10-29 17:03:48');
/*!40000 ALTER TABLE `app_files` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_folder`
--

DROP TABLE IF EXISTS `app_folder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `app_folder` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `folder_size` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `is_bin` varchar(1) DEFAULT NULL,
  `is_deleted` varchar(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_folder`
--

LOCK TABLES `app_folder` WRITE;
/*!40000 ALTER TABLE `app_folder` DISABLE KEYS */;
INSERT INTO `app_folder` VALUES (1,'parent-folder1',1,0,NULL,'N','N','2024-07-11 00:00:00'),(2,'parent-folder2',4,0,NULL,'N','N','2024-07-11 00:00:00'),(3,'parent-folder3',1,0,NULL,'N','N','2024-07-11 00:00:00'),(4,'son-folder2-1-1',4,5,NULL,'N','N','2024-07-11 00:00:00'),(5,'son-folder2-1',4,6,NULL,'N','N','2024-07-11 00:00:00'),(6,'son-folder2',4,2,NULL,'N','N','2024-07-11 00:00:00'),(14,'6',1,0,NULL,'N','N','2024-07-11 00:00:00'),(15,'22new',4,32,NULL,'N','N','2024-07-17 10:42:38'),(16,'333',4,15,NULL,'N','N','2024-07-17 16:27:50'),(17,'文件夹1',4,18,NULL,'N','N','2024-07-15 10:49:39'),(18,'文件架21',4,0,NULL,'N','N','2024-08-09 16:14:46'),(19,'文件夹3',4,0,NULL,'N','N','2024-07-16 14:55:13'),(20,'文件夹4',4,0,NULL,'N','N','2024-07-16 15:23:37'),(28,'2221',4,15,NULL,'N','N','2024-07-17 10:38:59'),(29,'212',4,15,NULL,'N','N','2024-07-17 10:39:48'),(30,'2321',4,15,NULL,'N','N','2024-07-17 10:40:03'),(31,'442',4,20,NULL,'N','N','2024-07-17 10:42:00'),(32,'11png_folder',4,0,NULL,'N','N','2024-07-25 17:17:32'),(33,'ss',4,32,NULL,'N','N','2024-07-25 17:17:54'),(34,'test01',4,0,NULL,'N','N','2024-07-26 10:53:37'),(35,'image',4,34,NULL,'N','N','2024-07-26 10:53:48'),(36,'test22',4,0,NULL,'N','N','2024-07-26 10:58:31'),(37,'ss1',4,19,NULL,'N','N','2024-07-26 10:59:33'),(38,'aaa',4,0,NULL,'N','N','2024-07-26 11:48:29'),(39,'aa22',4,38,NULL,'N','N','2024-07-26 11:49:06'),(40,'2121',4,0,NULL,'N','N','2024-08-08 12:28:53'),(41,'3331',4,0,NULL,'N','N','2024-08-08 12:29:03'),(42,'222',4,41,NULL,'N','N','2024-08-08 12:29:30'),(43,'222144',4,0,NULL,'N','N','2024-08-08 15:22:52'),(44,'22',4,0,NULL,'Y','N','2024-08-08 15:15:37'),(45,'3',4,44,NULL,'Y','N','2024-08-08 15:18:35'),(46,'444',4,0,NULL,'N','N','2024-08-08 16:05:26'),(47,'55',4,0,NULL,'N','N','2024-08-08 16:05:34'),(48,'88',4,0,NULL,'N','N','2024-08-08 16:05:50'),(49,'33',4,0,NULL,'N','N','2024-08-08 16:06:05'),(50,'999',4,0,NULL,'N','N','2024-08-08 16:06:22'),(51,'嗷嗷',4,0,NULL,'Y','N','2024-08-08 16:06:38'),(52,'嗷嗷嗷',4,0,NULL,'Y','N','2024-08-08 16:06:57'),(53,'嗷嗷嗷',4,0,NULL,'N','N','2024-08-08 16:07:09'),(54,'123嗷嗷',4,0,NULL,'N','N','2024-08-08 16:07:15'),(55,'test1',9,0,NULL,'N','N','2024-10-29 12:42:40'),(56,'test2',9,0,NULL,'N','N','2024-10-29 17:02:57');
/*!40000 ALTER TABLE `app_folder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_share`
--

DROP TABLE IF EXISTS `app_share`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `app_share` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `file_id` bigint(20) DEFAULT NULL COMMENT '文件夹或文件的id',
  `name` varchar(100) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '验证码',
  `type` char(1) DEFAULT NULL COMMENT '1文件夹; 2文件',
  `share_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `is_show` varchar(1) DEFAULT NULL COMMENT 'Y是show,N是不',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_share`
--

LOCK TABLES `app_share` WRITE;
/*!40000 ALTER TABLE `app_share` DISABLE KEYS */;
INSERT INTO `app_share` VALUES (27,4,34,'test01',1,'cbeac0fda3384781a14d40a577b5fcbf','1','2024-07-26 11:44:33','2024-08-05 00:00:00','Y'),(28,4,39,'orgin2.png',2,'e7578804cfb04f7e8729fccc72b02073','2','2024-07-26 11:45:55','2024-08-05 00:00:00','Y'),(29,4,58,'11.png',1,'13b44f160b4044fa9ed8246b6c636bee','2','2024-07-26 11:49:41','2024-08-05 00:00:00','Y'),(35,4,51,'11_1721899014807.png',23,'d5edbc28493740b291cabff0c196ff07','2','2024-08-09 17:12:38','2024-08-10 00:00:00','Y');
/*!40000 ALTER TABLE `app_share` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_user`
--

DROP TABLE IF EXISTS `app_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `app_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `mobile` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_user`
--

LOCK TABLES `app_user` WRITE;
/*!40000 ALTER TABLE `app_user` DISABLE KEYS */;
INSERT INTO `app_user` VALUES (1,'111','698d51a19d8a121ce581499d7b701668',NULL,NULL,NULL),(2,'username','58d0e1ba7c7831aa82ed7758f9db7bd7',NULL,NULL,'2024-07-12 15:08:45'),(3,'username','4297f44b13955235245b2497399d7a93',NULL,NULL,'2024-07-12 16:16:34'),(4,'jerry','30035607ee5bb378c71ab434a6d05410',NULL,NULL,'2024-07-12 16:54:44'),(5,'jerry1','a1c5094b239f77840d06fc76a65edf82',NULL,NULL,'2024-07-12 17:05:58'),(6,'jerry1','a1c5094b239f77840d06fc76a65edf82',NULL,NULL,'2024-07-12 17:07:01'),(7,'jerry','30035607ee5bb378c71ab434a6d05410',NULL,NULL,'2024-10-22 14:20:44'),(8,'jerry2','e68747cf0f4a2d0aa8de41dbb8633ebf',NULL,NULL,'2024-10-22 14:26:22'),(9,'jerry3','35622257643186814f27ff027bdd54df',NULL,NULL,'2024-10-29 12:42:25');
/*!40000 ALTER TABLE `app_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `files`
--

DROP TABLE IF EXISTS `files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `files` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `upload_id` varchar(255) DEFAULT NULL COMMENT 'µûçõ╗Âõ©èõ╝áid',
  `md5` varchar(255) DEFAULT NULL COMMENT 'µûçõ╗ÂÞ«íþ«ùmd5',
  `url` varchar(255) DEFAULT NULL COMMENT 'µûçõ╗ÂÞ«┐Úù«Õ£░ÕØÇ',
  `bucket` varchar(64) DEFAULT NULL COMMENT 'Õ¡ÿÕé¿µíÂ',
  `object` varchar(255) DEFAULT NULL COMMENT 'minioõ©¡µûçõ╗ÂÕÉì',
  `origin_file_name` varchar(255) DEFAULT NULL COMMENT 'ÕÄƒÕºïµûçõ╗ÂÕÉì',
  `size` bigint(20) DEFAULT NULL COMMENT 'µûçõ╗ÂÕñºÕ░Å',
  `type` varchar(64) DEFAULT NULL COMMENT 'µûçõ╗Âþ▒╗Õ×ï',
  `chunk_size` mediumtext COMMENT 'ÕêåþëçÕñºÕ░Å',
  `chunk_count` int(11) DEFAULT NULL COMMENT 'Õêåþëçµò░ÚçÅ',
  `is_delete` char(1) DEFAULT '0' COMMENT 'µÿ»ÕÉªÕêáÚÖñ',
  `create_time` timestamp(6) NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'ÕêøÕ╗║µùÂÚù┤',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='µûçõ╗ÂÞí¿';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `files`
--

LOCK TABLES `files` WRITE;
/*!40000 ALTER TABLE `files` DISABLE KEYS */;
INSERT INTO `files` VALUES (5,'ZWRiMDBkZGEtZDgzNy00Y2U2LTg1ODUtZWU2MDU0NDAwOTIyLjk3OWZkMzFjLWJlZmEtNGY1OC1hN2FiLTIyNWY3NWJjYTMxMXgxNzMwMTgxNjgzMjgxNTMyMTAw','15d4bc20cb9d31773a40e3e969fca722','http://127.0.0.1:9000/share/2024/10/29/20241025_15d4bc20cb9d31773a40e3e969fca722.zip','share','2024/10/29/20241025_15d4bc20cb9d31773a40e3e969fca722.zip','20241025.zip',108584682,'zip','5242880',21,'0','2024-10-29 06:01:24.810731');
/*!40000 ALTER TABLE `files` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_upload_task`
--

DROP TABLE IF EXISTS `sys_upload_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_upload_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `upload_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `file_identifier` varchar(100) DEFAULT NULL,
  `file_name` varchar(100) DEFAULT NULL,
  `bucket_name` varchar(100) DEFAULT NULL,
  `object_key` varchar(100) DEFAULT NULL,
  `total_size` bigint(20) DEFAULT NULL,
  `chunk_size` bigint(20) DEFAULT NULL,
  `chunk_num` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1725010193608 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_upload_task`
--

LOCK TABLES `sys_upload_task` WRITE;
/*!40000 ALTER TABLE `sys_upload_task` DISABLE KEYS */;
INSERT INTO `sys_upload_task` VALUES (1725010193606,'ZWRiMDBkZGEtZDgzNy00Y2U2LTg1ODUtZWU2MDU0NDAwOTIyLjZjOGNhYTU5LWFkNDgtNDg1Zi1hODM1LThhZDhkZThjMGU5NHgxNzMwMTg0NjAzMTg2MDQyMDAw','23e2738663f49c16657fbf84367ce130','20241025.zip','share','2024-10-29/38a6343a-15ca-4b7b-8670-346118f838e3.zip',108584682,5242880,21),(1725010193607,'ZWRiMDBkZGEtZDgzNy00Y2U2LTg1ODUtZWU2MDU0NDAwOTIyLmYyNDBlNDQ0LWUzMzEtNDIxOS05NGI0LTdiMzM0MTAxYTliZXgxNzMwMTkyNjI3NDE4MDkyNzAw','5f45e5a3ed27a2f0f98f3295e5c8f95b','YesMan.zip','share','2024-10-29/1cde4541-935c-4a39-aafa-bb723676d137.zip',373618657,5242880,72);
/*!40000 ALTER TABLE `sys_upload_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'app'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-10-29 17:39:44
