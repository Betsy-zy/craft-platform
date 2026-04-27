-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: craft_platform
-- ------------------------------------------------------
-- Server version	8.0.43

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
-- Table structure for table `appointment`
--

DROP TABLE IF EXISTS `appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `experience_id` bigint NOT NULL,
  `appointment_time` datetime NOT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `experience_id` (`experience_id`),
  CONSTRAINT `appointment_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `appointment_ibfk_2` FOREIGN KEY (`experience_id`) REFERENCES `experience` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment`
--

LOCK TABLES `appointment` WRITE;
/*!40000 ALTER TABLE `appointment` DISABLE KEYS */;
INSERT INTO `appointment` VALUES (1,3,10,'2026-04-16 16:00:00','accepted','2026-04-19 05:16:58'),(2,3,9,'2026-04-13 16:00:00','rejected','2026-04-19 05:39:13');
/*!40000 ALTER TABLE `appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carousel`
--

DROP TABLE IF EXISTS `carousel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carousel` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `image` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `link` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `order_num` int NOT NULL DEFAULT '0',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'active',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carousel`
--

LOCK TABLES `carousel` WRITE;
/*!40000 ALTER TABLE `carousel` DISABLE KEYS */;
INSERT INTO `carousel` VALUES (1,'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=traditional%20crafts%20banner%20chinese%20style&image_size=landscape_16_9','非遗手作文化','/',1,'active','2026-04-16 19:04:59'),(2,'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=craftsman%20working%20traditional%20craft&image_size=landscape_16_9','匠人风采','/craftsmen',2,'active','2026-04-16 19:04:59'),(3,'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=traditional%20crafts%20experience%20workshop&image_size=landscape_16_9','手作体验','/experiences',3,'active','2026-04-16 19:04:59'),(5,'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=traditional%20crafts%20information%20culture&image_size=landscape_16_9','工艺科普','/information',5,'active','2026-04-16 19:04:59'),(6,'https://img.yzcdn.cn/vant/cat.jpeg','欢迎来到手工艺品平台','/works',1,'active','2026-04-17 23:17:58'),(7,'https://img.yzcdn.cn/vant/cat.jpeg','匠人风采','/craftsmen',2,'active','2026-04-17 23:17:58'),(8,'https://img.yzcdn.cn/vant/cat.jpeg','体验活动','/experiences',3,'active','2026-04-17 23:17:58');
/*!40000 ALTER TABLE `carousel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `collection`
--

DROP TABLE IF EXISTS `collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `collection` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `target_id` bigint NOT NULL,
  `target_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `collection_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collection`
--

LOCK TABLES `collection` WRITE;
/*!40000 ALTER TABLE `collection` DISABLE KEYS */;
/*!40000 ALTER TABLE `collection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `target_id` bigint NOT NULL,
  `target_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `craftsman`
--

DROP TABLE IF EXISTS `craftsman`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `craftsman` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `contact` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending',
  `user_id` bigint DEFAULT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `craftsman_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `craftsman`
--

LOCK TABLES `craftsman` WRITE;
/*!40000 ALTER TABLE `craftsman` DISABLE KEYS */;
INSERT INTO `craftsman` VALUES (1,'张师傅','传统编织技艺非遗传承人','张师傅是一位专注传统编织技艺30余年的非遗传承人，精通草编、柳编、竹编、棕编、藤编、绳结编等多种编织技法。他的作品融合了传统工艺与现代设计，既保留了民间手工艺的质朴美感，又注入了当代生活美学元素。张师傅坚持使用天然环保材料，每一件作品都经过精心构思和手工编织，展现了深厚的传统工艺底蕴和创新精神。他的编织作品不仅实用美观，更是对传统文化的传承与弘扬，深受广大手工艺爱好者的喜爱和收藏。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/b5a77c4e-7b85-455d-95e8-2bfd1f997bac.png','1872732718','active',NULL,'2026-04-19 04:25:24'),(2,'杨师傅','传统雕刻技艺大师','杨师傅是一位深耕雕刻艺术25年的传统工艺大师，精通根雕、核雕、木雕、竹雕等多种雕刻技法，尤其擅长木簪手作、榫卯小器物和桃木手作。他的作品以精细的刀工、独特的创意和深厚的文化底蕴著称，每件作品都经过精心构思和匠心雕琢。杨师傅坚持传统工艺与现代审美相结合，在保留传统雕刻精髓的同时，融入当代设计理念，创作出兼具艺术性和实用性的雕刻作品。他的雕刻作品不仅展现了精湛的技艺，更传递了对自然材料的敬畏和对传统文化的传承，深受收藏家和艺术爱好者的青睐。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/ebddb778-1ac2-4797-87b5-7f977e7daff0.png','17771691800','active',NULL,'2026-04-19 04:26:47'),(3,'万师傅','传统纸艺与民间工艺大师','万师傅是一位专注传统纸艺与民间工艺28年的匠心艺人，精通灯笼手作、古风纸扇、剪纸、刻纸、皮影手工和油纸伞制作等多种传统技艺。他的作品以细腻的工艺、丰富的文化内涵和独特的艺术表现力著称，每一件作品都凝聚了他对传统工艺的深厚理解和创新追求。万师傅坚持使用传统材料和工艺技法，同时融入现代设计元素，使传统手工艺品在当代生活中焕发新的生命力。他的纸艺作品不仅展现了中国传统手工艺的魅力，更承载了对传统文化的传承与弘扬，深受国内外艺术爱好者的喜爱和收藏。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/0343a83d-0a7e-49dd-a56a-70268783780c.png','16789772141','active',NULL,'2026-04-19 04:27:49'),(4,'米师傅','传统造型艺术与陶艺大师','米师傅是一位专注传统造型艺术与陶艺26年的匠心艺人，精通面塑、泥塑、手工陶艺、糖塑、陶泥和紫砂手工等多种传统造型技艺。他的作品以精湛的塑形技艺、丰富的表现力和深厚的文化底蕴著称，每一件作品都凝聚了他对传统工艺的深刻理解和创新探索。米师傅坚持传统工艺技法与现代审美相结合，在保留传统造型艺术精髓的同时，融入当代设计理念，使传统手工艺品在现代生活中展现新的艺术价值。他的作品不仅展现了中国传统造型艺术的魅力，更传递了对传统文化的传承与创新精神，深受艺术爱好者和收藏家的喜爱。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/64515d1a-d521-4f85-a78d-c70d7e0aa5ee.png','18003716120','active',NULL,'2026-04-19 04:29:02');
/*!40000 ALTER TABLE `craftsman` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `experience`
--

DROP TABLE IF EXISTS `experience`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `experience` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `price` double NOT NULL,
  `duration` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `craftsman_id` bigint NOT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `craftsman_id` (`craftsman_id`),
  CONSTRAINT `experience_ibfk_1` FOREIGN KEY (`craftsman_id`) REFERENCES `craftsman` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `experience`
--

LOCK TABLES `experience` WRITE;
/*!40000 ALTER TABLE `experience` DISABLE KEYS */;
INSERT INTO `experience` VALUES (9,'木雕体验','手工木雕体验课','专业老师指导，体验传统木雕技艺',199,'2小时',28,'active','2026-04-17 23:17:58'),(10,'陶艺体验','陶艺制作体验','感受景德镇传统陶艺魅力',299,'3小时',25,'active','2026-04-17 23:17:58'),(11,'油纸伞制作','油纸伞制作体验','学习怎么制作油纸伞',168,'1.5小时',27,'active','2026-04-17 23:17:58'),(12,'剪纸体验','剪纸体验','学习如何剪纸',899,'3小时',24,'active','2026-04-18 09:02:54');
/*!40000 ALTER TABLE `experience` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `information`
--

DROP TABLE IF EXISTS `information`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `information` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `image` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'active',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `information`
--

LOCK TABLES `information` WRITE;
/*!40000 ALTER TABLE `information` DISABLE KEYS */;
INSERT INTO `information` VALUES (1,'非遗文化传承的重要性','非物质文化遗产是中华民族的瑰宝，需要我们共同保护和传承。非遗文化不仅是历史的见证，也是民族精神的体现。通过传承和弘扬非遗文化，我们可以增强文化自信，促进文化多样性的发展。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/29107161-f257-4256-a3f7-e0821a412eaf.png','active','2026-04-16 19:04:59'),(2,'传统工艺的现代价值','传统工艺在现代社会仍然具有重要的价值，不仅是文化传承，也是经济发展的重要组成部分。传统工艺的精湛技艺和独特魅力，吸引了越来越多的年轻人关注和学习，为传统工艺注入了新的活力。','https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=traditional%20crafts%20modern%20value&image_size=square','active','2026-04-16 19:04:59'),(3,'木雕工艺的历史与发展','木雕是中国传统工艺的重要组成部分，有着悠久的历史。从原始社会的简单雕刻，到明清时期的精湛工艺，木雕艺术不断发展和创新。今天，木雕工艺仍然在不断传承和发展，成为中华文化的重要符号。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/f6243efb-64db-41a2-9b16-561bfd565faa.png','active','2026-04-16 19:04:59'),(4,'陶瓷工艺的演变','陶瓷是中国古代的伟大发明之一，有着数千年的历史。从原始的陶器，到精美的瓷器，陶瓷工艺不断演变和发展。今天，陶瓷工艺仍然在不断创新，融合现代元素，展现出新时代的魅力。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/33950807-b9ef-439e-b2b6-516acb714ed8.png','active','2026-04-16 19:04:59'),(5,'刺绣艺术的魅力','刺绣是中国传统工艺的瑰宝，有着悠久的历史。不同地区的刺绣有着不同的风格和特点，如苏绣、湘绣、粤绣、蜀绣等。刺绣艺术以其精湛的技艺和独特的魅力，成为中华文化的重要组成部分。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/1a7817f3-c340-4749-9e52-80a58420b7c3.png','active','2026-04-16 19:04:59'),(6,'竹编工艺的传承与创新','竹编是中国传统工艺的重要组成部分，有着悠久的历史。竹编工艺以其独特的艺术魅力和实用价值，在现代社会仍然受到人们的喜爱。通过不断创新，竹编工艺正在焕发出新的活力。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/2bd6f138-b157-411f-9121-e6b917d4eeb1.png','active','2026-04-16 19:04:59'),(7,'剪纸艺术的文化内涵','剪纸是中国传统民间艺术的重要组成部分，有着悠久的历史。剪纸艺术以其独特的艺术形式和丰富的文化内涵，成为中华文化的重要符号。通过剪纸，我们可以了解中国传统文化的博大精深。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/41837c00-67ff-4725-87a7-5237f3690df3.png','active','2026-04-16 19:04:59'),(8,'非遗匠人的心路历程','非遗匠人是非遗文化的守护者和传承者，他们用自己的技艺和心血，守护着中华民族的文化瑰宝。通过了解非遗匠人的故事，我们可以更深刻地理解非遗文化的价值和意义。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/c397c946-5bc1-4267-92e6-d9b95ab779e5.png','active','2026-04-16 19:04:59'),(9,'传统木雕艺术的魅力','木雕是中国传统的民间工艺，历史悠久，技法多样...','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/4ebc99ff-bdd9-43ee-9539-be9c42ff9054.png','active','2026-04-17 23:17:58'),(10,'景德镇青花瓷的发展','青花瓷是中国陶瓷的代表，以其独特的艺术风格闻名于世...','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/d182cd5b-7d95-4452-bca5-957d3d77fc98.png','active','2026-04-17 23:17:58'),(11,'匠人精神：传承与创新','在快速发展的现代社会中，传统手工艺如何传承与创新...','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/404cbc07-0967-48ca-ae00-e033bdd9d475.png','active','2026-04-17 23:17:58');
/*!40000 ALTER TABLE `information` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'unread',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `message_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=209 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` VALUES (187,8,'您的作品订单已成功支付，商家正在准备发货。','order','read','2026-04-18 23:13:51'),(188,8,'您的作品订单已成功支付，商家正在准备发货。','order','read','2026-04-18 23:13:51'),(191,8,'您的预约申请已被商家接受，请注意查看详情。','appointment','read','2026-04-18 23:16:23'),(192,8,'您的作品订单已发货，请注意查收。','order','read','2026-04-18 23:16:34'),(193,8,'您的作品订单已发货，请注意查收。','order','read','2026-04-18 23:16:37'),(194,3,'您的作品订单已成功支付，商家正在准备发货。','order','read','2026-04-19 12:56:34'),(195,3,'您的作品订单已成功支付，商家正在准备发货。','order','read','2026-04-19 12:56:34'),(196,3,'您的作品订单已成功支付，商家正在准备发货。','order','read','2026-04-19 12:56:34'),(197,3,'您的作品订单已成功支付，商家正在准备发货。','order','read','2026-04-19 12:56:34'),(202,3,'您的作品订单已发货，请注意查收。','order','read','2026-04-19 12:58:03'),(203,3,'您的作品订单已发货，请注意查收。','order','read','2026-04-19 13:00:08'),(204,3,'您的作品订单已发货，请注意查收。','order','read','2026-04-19 13:00:10'),(205,3,'您的作品订单已发货，请注意查收。','order','read','2026-04-19 13:00:11'),(206,3,'您的预约申请已提交，商家将尽快处理。','appointment','unread','2026-04-19 13:16:57'),(207,1,'【商家通知】新的预约申请已提交，请及时处理。','appointment','unread','2026-04-19 13:16:57'),(208,3,'您的预约申请已被商家接受，请注意查看详情。','appointment','unread','2026-04-19 13:17:26');
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `messages` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `content` text COLLATE utf8mb4_unicode_ci,
  `type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
INSERT INTO `messages` VALUES (1,3,'您的作品订单已成功支付，商家正在准备发货。','order','read','2026-04-19 05:37:02'),(2,1,'【商家通知】新订单 15：用户 user 购买了作品 \'刻纸\'，数量 1，总价 ¥100.0，请及时处理发货。','order','unread','2026-04-19 05:37:02'),(3,3,'您的作品订单已取消。','order','read','2026-04-19 05:37:17'),(4,3,'您的作品订单已发货，请注意查收。','order','read','2026-04-19 05:37:31'),(5,3,'您的作品订单已发货，请注意查收。','order','read','2026-04-19 05:37:32'),(6,3,'您的作品订单已发货，请注意查收。','order','read','2026-04-19 05:37:34'),(7,3,'您的作品订单已发货，请注意查收。','order','read','2026-04-19 05:38:19'),(8,3,'您的预约申请已提交，商家将尽快处理。','appointment','read','2026-04-19 05:39:13'),(9,1,'【商家通知】新的预约申请已提交，请及时处理。','appointment','unread','2026-04-19 05:39:13'),(10,3,'您的预约申请已被商家拒绝，请选择其他时间或项目。','appointment','read','2026-04-19 05:39:26');
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `image` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `price` double NOT NULL,
  `stock` int NOT NULL,
  `craftsman_id` bigint NOT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `craftsman_id` (`craftsman_id`),
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`craftsman_id`) REFERENCES `craftsman` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_order`
--

DROP TABLE IF EXISTS `product_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `work_id` bigint NOT NULL,
  `quantity` int NOT NULL,
  `total_price` double NOT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending',
  `create_time` datetime NOT NULL,
  `cancel_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `product_order_ibfk_2` (`work_id`),
  CONSTRAINT `product_order_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `product_order_ibfk_2` FOREIGN KEY (`work_id`) REFERENCES `work` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_order`
--

LOCK TABLES `product_order` WRITE;
/*!40000 ALTER TABLE `product_order` DISABLE KEYS */;
INSERT INTO `product_order` VALUES (1,3,3,1,150,'shipped','2026-04-19 12:56:34',NULL),(2,3,2,1,80,'shipped','2026-04-19 12:56:34',NULL),(3,3,1,1,100,'shipped','2026-04-19 12:56:34',NULL),(4,3,4,1,200,'shipped','2026-04-19 12:56:34',NULL),(5,3,3,1,150,'shipped','2026-04-19 13:30:13','2026-04-19 05:32:01'),(6,3,1,1,100,'pending','2026-04-19 13:30:13',NULL),(7,3,2,1,80,'pending','2026-04-19 13:30:13',NULL),(8,3,2,1,80,'pending','2026-04-19 13:30:15',NULL),(9,3,1,1,100,'pending','2026-04-19 13:30:15',NULL),(10,3,3,1,150,'shipped','2026-04-19 13:30:15','2026-04-19 05:34:38'),(11,3,1,1,100,'shipped','2026-04-19 13:32:22',NULL),(15,3,1,1,100,'shipped','2026-04-19 13:37:02','2026-04-19 05:37:17');
/*!40000 ALTER TABLE `product_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'active',
  `create_time` datetime NOT NULL,
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','admin123','admin@example.com','13800138000','admin','active','2026-04-16 19:04:59','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/7d9327c3-6ea5-462a-8d1d-1dede15c708b.jpg'),(2,'Betsy','123456','2143506847@qq.com','18727321716','user','active','2026-04-17 09:07:11','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/fbf85f95-4742-42b1-9af3-052bc05cabdb.jpg'),(3,'user','user123','4412597865@jj.com','18908976675','user','active','2026-04-19 04:48:25','');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `work`
--

DROP TABLE IF EXISTS `work`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `image` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `price` double NOT NULL,
  `craftsman_id` bigint NOT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending',
  `create_time` datetime NOT NULL,
  `stock` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `craftsman_id` (`craftsman_id`),
  CONSTRAINT `work_ibfk_1` FOREIGN KEY (`craftsman_id`) REFERENCES `craftsman` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `work`
--

LOCK TABLES `work` WRITE;
/*!40000 ALTER TABLE `work` DISABLE KEYS */;
INSERT INTO `work` VALUES (1,'刻纸','刻纸','刻纸是传统工艺制作的精美作品，展现了万师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/5e8aa6e6-6f11-404b-b3cb-8994c6548085.png',100,3,'active','2026-04-18 14:48:40',4),(2,'剪纸','剪纸','剪纸是传统工艺制作的精美作品，展现了万师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/147a31db-caeb-4c5b-9c36-cb57796878ce.png',80,3,'active','2026-04-18 14:48:41',6),(3,'古风纸扇','古风纸扇','古风纸扇是传统工艺制作的精美作品，展现了万师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/ba0eb5de-f760-480c-b019-d960efc8146e.png',150,3,'active','2026-04-18 14:48:41',9),(4,'油纸伞制作','油纸伞制作','油纸伞制作是传统工艺制作的精美作品，展现了万师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/4c5127d7-f937-4d2a-9ced-1c323ba05a2f.png',200,3,'active','2026-04-18 14:48:41',9),(5,'灯笼手作','灯笼手作','灯笼手作是传统工艺制作的精美作品，展现了万师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/c78f27dd-9114-4d6b-a8e3-a9dd75601bf2.png',180,3,'active','2026-04-18 14:48:42',10),(6,'皮影手工','皮影手工','皮影手工是传统工艺制作的精美作品，展现了万师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/1bc3dc3c-c9e3-4441-b8ae-3c45b2fe2154.png',250,3,'active','2026-04-18 14:48:42',10),(7,'柳编','柳编','柳编是传统工艺制作的精美作品，展现了张师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/01daca30-80fb-4e12-8a78-953cd582bdf4.png',120,1,'active','2026-04-18 14:48:43',10),(8,'棕编','棕编','棕编是传统工艺制作的精美作品，展现了张师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/7839a7a8-b5df-49ba-93ed-3eb707c76754.png',100,1,'active','2026-04-18 14:48:44',10),(9,'畲族彩带编','畲族彩带编','畲族彩带编是传统工艺制作的精美作品，展现了张师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/c63a5697-0e1f-44fa-bce3-d73c110b3df0.png',150,1,'active','2026-04-18 14:48:46',10),(10,'竹编','竹编','竹编是传统工艺制作的精美作品，展现了张师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/c5cecb0b-6073-41a0-817c-6d6387e792fc.png',130,1,'active','2026-04-18 14:48:46',10),(11,'绳结编','绳结编','绳结编是传统工艺制作的精美作品，展现了张师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/d8c0ade0-5113-4cd6-ad6d-3919ed4d3857.png',80,1,'active','2026-04-18 14:48:47',10),(12,'草编','草编','草编是传统工艺制作的精美作品，展现了张师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/53f9725f-51aa-4e24-83f9-0eb23248d70e.png',60,1,'active','2026-04-18 14:48:47',10),(13,'藤编','藤编','藤编是传统工艺制作的精美作品，展现了张师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/571d17e7-be02-434d-ad76-45289c6f4bd3.png',180,1,'active','2026-04-18 14:48:48',10),(14,'木簪手作','木簪手作','木簪手作是传统工艺制作的精美作品，展现了杨师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/a14eef36-9b8b-422f-90cf-2eb168b82eaf.png',160,2,'active','2026-04-18 14:48:49',10),(15,'木雕','木雕','木雕是传统工艺制作的精美作品，展现了杨师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/956d885f-1695-4936-b55c-933a6ded870d.png',300,2,'active','2026-04-18 14:48:49',10),(16,'核雕','核雕','核雕是传统工艺制作的精美作品，展现了杨师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/244c7b61-3c80-4623-9346-1a611b49eb09.png',200,2,'active','2026-04-18 14:48:50',10),(17,'根雕','根雕','根雕是传统工艺制作的精美作品，展现了杨师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/b209bd2d-d9b3-4552-92a6-48713d81e167.png',400,2,'active','2026-04-18 14:48:50',10),(18,'桃木手作','桃木手作','桃木手作是传统工艺制作的精美作品，展现了杨师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/e4075945-54c2-4288-bf87-259ef6f5c599.png',180,2,'active','2026-04-18 14:48:51',10),(19,'榫卯小器物','榫卯小器物','榫卯小器物是传统工艺制作的精美作品，展现了杨师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/99e0555f-453e-4eb1-84ce-97bb53e0e978.png',220,2,'active','2026-04-18 14:48:51',10),(20,'竹雕','竹雕','竹雕是传统工艺制作的精美作品，展现了杨师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/0839adf1-a79a-48ef-9496-1fad7f78ff78.png',250,2,'active','2026-04-18 14:48:52',10),(21,'手工陶艺','手工陶艺','手工陶艺是传统工艺制作的精美作品，展现了米师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/4bb0f3e9-6c7c-41f4-9200-514d45675fc0.png',180,4,'active','2026-04-18 14:48:53',10),(22,'泥塑','泥塑','泥塑是传统工艺制作的精美作品，展现了米师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/ed0f7cf2-5a4d-425e-a0b8-56abf4c2aede.png',120,4,'active','2026-04-18 14:48:53',10),(23,'糖塑','糖塑','糖塑是传统工艺制作的精美作品，展现了米师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/53ae8525-ceea-4d79-83ca-04a451f5cf76.png',80,4,'active','2026-04-18 14:48:54',10),(24,'紫砂手工','紫砂手工','紫砂手工是传统工艺制作的精美作品，展现了米师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/d04e518c-b408-45a3-8801-86e13ee7eb3f.png',300,4,'active','2026-04-18 14:48:54',10),(25,'陶泥','陶泥','陶泥是传统工艺制作的精美作品，展现了米师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/21a7b2ff-deef-4951-9a4c-b7fa3facf7e7.png',100,4,'active','2026-04-18 14:48:55',10),(26,'面塑','面塑','面塑是传统工艺制作的精美作品，展现了米师傅的精湛技艺，具有很高的艺术价值和收藏价值。','https://craft-platform.oss-cn-beijing.aliyuncs.com/2026/04/9e49a016-c01e-4c3e-bf28-5a0dd3ccac2d.png',90,4,'active','2026-04-18 14:48:55',10);
/*!40000 ALTER TABLE `work` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `work_craftsman_backup`
--

DROP TABLE IF EXISTS `work_craftsman_backup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_craftsman_backup` (
  `old_id` bigint NOT NULL DEFAULT '0',
  `craftsman_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `work_craftsman_backup`
--

LOCK TABLES `work_craftsman_backup` WRITE;
/*!40000 ALTER TABLE `work_craftsman_backup` DISABLE KEYS */;
INSERT INTO `work_craftsman_backup` VALUES (7,24),(8,24),(9,24),(10,24),(11,24),(12,24),(13,24),(33,24),(34,24),(35,24),(36,24),(37,24),(38,24),(39,24),(21,25),(22,25),(23,25),(24,25),(25,25),(26,25),(47,25),(48,25),(49,25),(50,25),(51,25),(52,25),(1,27),(2,27),(3,27),(4,27),(5,27),(6,27),(27,27),(28,27),(29,27),(30,27),(31,27),(32,27),(14,28),(15,28),(16,28),(17,28),(18,28),(19,28),(20,28),(40,28),(41,28),(42,28),(43,28),(44,28),(45,28),(46,28);
/*!40000 ALTER TABLE `work_craftsman_backup` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-19 14:05:22
