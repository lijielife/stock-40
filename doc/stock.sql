/*
SQLyog Ultimate v11.24 (32 bit)
MySQL - 5.1.73-log : Database - stock
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`stock` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `stock`;

/*Table structure for table `t_customer` */

DROP TABLE IF EXISTS `t_customer`;

CREATE TABLE `t_customer` (
  `pk_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `type` tinyint(3) NOT NULL DEFAULT '0' COMMENT '类型，1供应商，2客户',
  `name` varchar(15) NOT NULL DEFAULT '' COMMENT '名称',
  `mobile` varchar(11) NOT NULL DEFAULT '' COMMENT '手机号',
  `company` varchar(31) NOT NULL DEFAULT '' COMMENT '公司',
  `remark` varchar(200) NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_customer` */

/*Table structure for table `t_good` */

DROP TABLE IF EXISTS `t_good`;

CREATE TABLE `t_good` (
  `pk_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品主键',
  `name` varchar(31) NOT NULL DEFAULT '' COMMENT '名称',
  `fk_user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属用户',
  `number` int(11) NOT NULL DEFAULT '0' COMMENT '数量',
  `unit_price` double(8,2) NOT NULL DEFAULT '0.00' COMMENT '单价',
  `fk_record_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '当前正在卖哪一批次的进货',
  `stock_number` int(11) NOT NULL DEFAULT '0' COMMENT '当前批次的货剩下多少数量',
  `create_time` datetime NOT NULL DEFAULT '1970-01-01 08:00:00' COMMENT '创建时间',
  PRIMARY KEY (`pk_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `t_good` */

insert  into `t_good`(`pk_id`,`name`,`fk_user_id`,`number`,`unit_price`,`fk_record_id`,`stock_number`,`create_time`) values (1,'red hot2',2,0,0.00,0,0,'2017-01-02 22:50:30'),(2,'red hot',2,0,0.00,0,0,'2017-01-02 22:53:31');

/*Table structure for table `t_record` */

DROP TABLE IF EXISTS `t_record`;

CREATE TABLE `t_record` (
  `pk_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '类型，1进货，2售出，3折损',
  `fk_customer_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '客户id',
  `fk_good_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '商品id',
  `number` int(11) NOT NULL DEFAULT '0' COMMENT '数量',
  `production_price` double(8,2) NOT NULL DEFAULT '0.00' COMMENT '产品价格',
  `other_price` double(8,2) NOT NULL DEFAULT '0.00' COMMENT '其他费用',
  `damage_rpice` double(8,2) NOT NULL DEFAULT '0.00' COMMENT '折损费',
  `sell_price` double(8,2) NOT NULL DEFAULT '0.00' COMMENT '销售价格',
  `profit_price` double(8,2) NOT NULL DEFAULT '0.00' COMMENT '利润',
  `fk_user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '归属用户',
  `create_time` datetime NOT NULL DEFAULT '1970-01-01 08:00:00' COMMENT '创建时间',
  PRIMARY KEY (`pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_record` */

/*Table structure for table `t_stat_user_day` */

DROP TABLE IF EXISTS `t_stat_user_day`;

CREATE TABLE `t_stat_user_day` (
  `pk_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '统计天主键',
  `time` datetime NOT NULL DEFAULT '1970-01-01 08:00:00' COMMENT '时间，每天凌晨执行',
  `fk_user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户',
  `production_price` double(8,2) NOT NULL DEFAULT '0.00' COMMENT '产品费用',
  `other_price` double(8,2) NOT NULL DEFAULT '0.00' COMMENT '其他费用',
  `sell_price` double(8,2) NOT NULL DEFAULT '0.00' COMMENT '售出价格',
  `damage_price` double(8,2) NOT NULL DEFAULT '0.00' COMMENT '折损价格',
  `profit_price` double(8,2) NOT NULL DEFAULT '0.00' COMMENT '利润',
  PRIMARY KEY (`pk_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `t_stat_user_day` */

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `pk_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(15) NOT NULL DEFAULT '' COMMENT '名称',
  `mobile` varchar(11) NOT NULL DEFAULT '' COMMENT '手机号',
  `password` varchar(31) NOT NULL DEFAULT '' COMMENT '密码',
  `create_time` datetime NOT NULL DEFAULT '1970-01-01 08:00:00' COMMENT '创建时间',
  `stock_price` double(8,2) NOT NULL DEFAULT '0.00' COMMENT '库存价格',
  `production_price` double(8,2) NOT NULL DEFAULT '0.00' COMMENT '产品价格',
  `other_price` double(8,2) NOT NULL DEFAULT '0.00' COMMENT '其他价格',
  `sell_price` double(8,2) NOT NULL DEFAULT '0.00' COMMENT '卖出价格',
  `damage_price` double(8,2) NOT NULL DEFAULT '0.00' COMMENT '折损价格',
  `profit_price` double(8,2) NOT NULL DEFAULT '0.00' COMMENT '利润',
  PRIMARY KEY (`pk_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `t_user` */

insert  into `t_user`(`pk_id`,`name`,`mobile`,`password`,`create_time`,`stock_price`,`production_price`,`other_price`,`sell_price`,`damage_price`,`profit_price`) values (1,'13','13011265819','123','1970-01-01 08:00:00',0.00,0.00,0.00,0.00,0.00,0.00),(2,'yaoer','13011265820','123','1970-01-01 08:00:00',0.00,0.00,0.00,0.00,0.00,0.00),(3,'yaoer','13011265821','123','2017-01-02 19:04:52',0.00,0.00,0.00,0.00,0.00,0.00);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
