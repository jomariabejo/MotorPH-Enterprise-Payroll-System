-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.30 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for payroll_sys
CREATE DATABASE IF NOT EXISTS `payroll_sys` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `payroll_sys`;

-- Dumping structure for table payroll_sys.allowance
CREATE TABLE IF NOT EXISTS `allowance` (
  `alw_id` int NOT NULL AUTO_INCREMENT,
  `clothing` int NOT NULL,
  `rice` int NOT NULL,
  `phone` int NOT NULL,
  `total_amount` int NOT NULL,
  `dateCreated` date NOT NULL,
  `dateModified` datetime NOT NULL,
  `employee_id` int DEFAULT NULL,
  PRIMARY KEY (`alw_id`),
  KEY `FK_allowance_employee` (`employee_id`),
  CONSTRAINT `FK_allowance_employee` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table payroll_sys.deduction
CREATE TABLE IF NOT EXISTS `deduction` (
  `deduction_id` int NOT NULL AUTO_INCREMENT,
  `employee_id` int NOT NULL,
  `sss` decimal(20,2) NOT NULL,
  `philhealth` decimal(20,2) NOT NULL,
  `pagibig` decimal(20,2) DEFAULT NULL,
  `total_contribution` decimal(20,2) NOT NULL,
  `date_created` date NOT NULL,
  PRIMARY KEY (`deduction_id`),
  KEY `FK__employee` (`employee_id`),
  CONSTRAINT `FK__employee` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table payroll_sys.department
CREATE TABLE IF NOT EXISTS `department` (
  `dept_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  `description` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `date_created` date NOT NULL,
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table payroll_sys.employee
CREATE TABLE IF NOT EXISTS `employee` (
  `employee_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `last_name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `birthday` date NOT NULL,
  `address` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `contact_number` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `status` enum('Regular','Probationary') COLLATE utf8mb4_general_ci NOT NULL,
  `date_hired` date DEFAULT NULL,
  `position_id` int NOT NULL,
  `supervisor` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `dept_id` int NOT NULL,
  `sss` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `philhealth` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `pagibig` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `tin` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `basic_salary` decimal(10,2) NOT NULL,
  `gross_semi_monthly_rate` decimal(10,2) NOT NULL,
  `hourly_rate` decimal(10,2) NOT NULL,
  `isActive` bit(1) DEFAULT b'1',
  `profile_img` blob,
  PRIMARY KEY (`employee_id`),
  KEY `FK_employee_department` (`dept_id`),
  KEY `FK_employee_position` (`position_id`),
  CONSTRAINT `FK_employee_department` FOREIGN KEY (`dept_id`) REFERENCES `department` (`dept_id`),
  CONSTRAINT `FK_employee_position` FOREIGN KEY (`position_id`) REFERENCES `position` (`position_id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table payroll_sys.leave_request
CREATE TABLE IF NOT EXISTS `leave_request` (
  `leave_request_id` int NOT NULL AUTO_INCREMENT,
  `employee_id` int DEFAULT NULL,
  `leave_request_category_id` int NOT NULL DEFAULT '0',
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `date_created` timestamp NOT NULL,
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  `status` enum('Approved','Disapproved','Pending') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'Pending',
  PRIMARY KEY (`leave_request_id`),
  KEY `FK_leave_request_leave_request_category` (`leave_request_category_id`),
  KEY `FK_leave_request_employee` (`employee_id`),
  CONSTRAINT `FK_leave_request_employee` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`),
  CONSTRAINT `FK_leave_request_leave_request_category` FOREIGN KEY (`leave_request_category_id`) REFERENCES `leave_request_category` (`leave_req_cat_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table payroll_sys.leave_request_category
CREATE TABLE IF NOT EXISTS `leave_request_category` (
  `leave_req_cat_id` int NOT NULL AUTO_INCREMENT,
  `categoryName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `maxCredits` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`leave_req_cat_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table payroll_sys.payslip
CREATE TABLE IF NOT EXISTS `payslip` (
  `payslip_id` int NOT NULL AUTO_INCREMENT,
  `employee_id` int DEFAULT NULL,
  `alw_id` int NOT NULL,
  `deduction_id` int NOT NULL,
  `tax_id` int NOT NULL,
  `pay_period_start` date NOT NULL,
  `pay_period_end` date NOT NULL,
  `total_hours_worked` int NOT NULL,
  `grossIncome` decimal(20,6) NOT NULL,
  `netIncome` decimal(20,6) NOT NULL,
  `date_created` date NOT NULL,
  PRIMARY KEY (`payslip_id`),
  KEY `FK_payslip_employee` (`employee_id`),
  KEY `FK_payslip_allowance` (`alw_id`),
  KEY `FK_payslip_tax` (`tax_id`),
  KEY `FK_payslip_deduction` (`deduction_id`),
  CONSTRAINT `FK_payslip_allowance` FOREIGN KEY (`alw_id`) REFERENCES `allowance` (`alw_id`),
  CONSTRAINT `FK_payslip_deduction` FOREIGN KEY (`deduction_id`) REFERENCES `deduction` (`deduction_id`),
  CONSTRAINT `FK_payslip_employee` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`),
  CONSTRAINT `FK_payslip_tax` FOREIGN KEY (`tax_id`) REFERENCES `tax` (`tax_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table payroll_sys.permission
CREATE TABLE IF NOT EXISTS `permission` (
  `permission_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  PRIMARY KEY (`permission_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table payroll_sys.position
CREATE TABLE IF NOT EXISTS `position` (
  `position_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`position_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table payroll_sys.role
CREATE TABLE IF NOT EXISTS `role` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  `description` varchar(255) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  `permission_id` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`role_id`),
  KEY `FK_role_permission` (`permission_id`),
  CONSTRAINT `FK_role_permission` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`permission_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table payroll_sys.tax
CREATE TABLE IF NOT EXISTS `tax` (
  `tax_id` int NOT NULL AUTO_INCREMENT,
  `taxable_income` decimal(20,6) NOT NULL,
  `tax_cat_id` int NOT NULL DEFAULT '0',
  `withheld_tax` int NOT NULL,
  PRIMARY KEY (`tax_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table payroll_sys.tax_category
CREATE TABLE IF NOT EXISTS `tax_category` (
  `tax_cat_id` int NOT NULL AUTO_INCREMENT,
  `minimum_monthly_rate` int DEFAULT NULL,
  `maximum_monthly_rate` int DEFAULT NULL,
  `tax_rate` int DEFAULT NULL,
  `additional_tax_rate` int DEFAULT NULL,
  `name` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`tax_cat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table payroll_sys.timesheet
CREATE TABLE IF NOT EXISTS `timesheet` (
  `timesheet_id` int NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `time_in` datetime NOT NULL,
  `time_out` datetime DEFAULT NULL,
  `employee_id` int DEFAULT NULL,
  PRIMARY KEY (`timesheet_id`),
  KEY `FK_timesheet_employee` (`employee_id`),
  CONSTRAINT `FK_timesheet_employee` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8465 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table payroll_sys.user
CREATE TABLE IF NOT EXISTS `user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `password` varchar(255) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `employee_id` int NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`user_id`),
  KEY `FK_user_role` (`role_id`),
  KEY `FK_user_employee` (`employee_id`),
  CONSTRAINT `FK_user_employee` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`),
  CONSTRAINT `FK_user_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
