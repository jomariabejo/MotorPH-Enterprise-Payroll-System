-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.38 - MySQL Community Server - GPL
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


-- Dumping database structure for payroll_system
CREATE DATABASE IF NOT EXISTS `payroll_system` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `payroll_system`;

-- Dumping structure for table payroll_system.announcement
CREATE TABLE IF NOT EXISTS `announcement` (
  `announcement_id` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each announcement.',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'The title of the announcement',
  `content` tinytext COLLATE utf8mb4_unicode_ci NOT NULL,
  `announcement_date` date NOT NULL COMMENT 'The date when announcement is ',
  `created_by_employee_id` int NOT NULL COMMENT 'The creator of the announcement',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp of the last update to the announcement record.',
  PRIMARY KEY (`announcement_id`),
  KEY `idx_created_by_employee` (`created_by_employee_id`),
  CONSTRAINT `fk_announcement_employee` FOREIGN KEY (`created_by_employee_id`) REFERENCES `employee` (`EmployeeNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Stores announcements created by employees for distribution within the organization.\r\n\r\n';

-- Dumping data for table payroll_system.announcement: ~0 rows (approximately)

-- Dumping structure for table payroll_system.bonus
CREATE TABLE IF NOT EXISTS `bonus` (
  `BonusID` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each bonus id.',
  `EmployeeNumber` int NOT NULL COMMENT 'The worker who is the bonus owner.',
  `BonusAmount` decimal(18,4) NOT NULL COMMENT 'The amount of the bonus',
  `BonusDate` date NOT NULL COMMENT 'The date of the bonus.',
  `Description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Description of the bonus.',
  `CreatedBy` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Creator of the bonus.',
  `CreatedDate` datetime NOT NULL COMMENT 'The creation date of the bonus.',
  `LastModifiedBy` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'The person who modifies the bonus data.',
  `LastModifiedDate` datetime DEFAULT NULL COMMENT 'The date of the last data modification',
  PRIMARY KEY (`BonusID`),
  KEY `idx_employee` (`EmployeeNumber`),
  CONSTRAINT `fk_bonus_employee` FOREIGN KEY (`EmployeeNumber`) REFERENCES `employee` (`EmployeeNumber`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Stores employee bonuses data.';

-- Dumping data for table payroll_system.bonus: ~0 rows (approximately)

-- Dumping structure for table payroll_system.conversation
CREATE TABLE IF NOT EXISTS `conversation` (
  `conversation_id` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each conversation',
  `participant1_employee_id` int NOT NULL COMMENT 'Primary participant',
  `participant2_employee_id` int NOT NULL COMMENT 'Secondary participant',
  `last_message_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp for last message arrived',
  `last_message_content` tinytext COLLATE utf8mb4_unicode_ci NOT NULL,
  `unread_count` int NOT NULL DEFAULT '0' COMMENT 'The count of under messages.',
  PRIMARY KEY (`conversation_id`),
  KEY `idx_participant1` (`participant1_employee_id`),
  KEY `idx_participant2` (`participant2_employee_id`),
  CONSTRAINT `fk_participant1_employee` FOREIGN KEY (`participant1_employee_id`) REFERENCES `employee` (`EmployeeNumber`),
  CONSTRAINT `fk_participant2_employee` FOREIGN KEY (`participant2_employee_id`) REFERENCES `employee` (`EmployeeNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Stores conversation of the employees.';

-- Dumping data for table payroll_system.conversation: ~0 rows (approximately)

-- Dumping structure for table payroll_system.department
CREATE TABLE IF NOT EXISTS `department` (
  `DepartmentID` int NOT NULL AUTO_INCREMENT,
  `DepartmentName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Name of the department',
  `Description` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT 'Description of department',
  PRIMARY KEY (`DepartmentID`),
  UNIQUE KEY `DepartmentName` (`DepartmentName`),
  UNIQUE KEY `UKjglx6wb2or3ojhn41ytkhdbn` (`DepartmentName`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Stores departments of the company.';

-- Dumping data for table payroll_system.department: ~12 rows (approximately)
INSERT INTO `department` (`DepartmentID`, `DepartmentName`, `Description`) VALUES
	(1, 'Executive Management', 'Responsible for strategic decision-making and overall management of the organization.'),
	(2, 'Human Resources', 'Handles recruitment, employee relations, training, and benefits administration.'),
	(3, 'Information Technology', 'Manages IT systems, infrastructure, and support services.'),
	(4, 'Marketing', 'Develops and executes marketing strategies to promote products and services.'),
	(5, 'Finance', 'Manages financial planning, reporting, and budgeting.'),
	(6, 'Operations', 'Oversees day-to-day operations and ensures efficiency and quality of services.'),
	(7, 'Accounting', 'Responsible for financial record-keeping, analysis, and reporting.'),
	(8, 'Payroll', 'Handles payroll processing and ensures accurate and timely payment to employees.'),
	(9, 'Sales & Marketing', 'Drives sales growth through marketing campaigns and customer engagement.'),
	(10, 'Supply Chain and Logistics', 'Manages the supply chain process from procurement to delivery.'),
	(11, 'Customer Service and Relations', 'Handles customer inquiries, complaints, and support services.'),
	(12, 'Department Name', NULL);

-- Dumping structure for table payroll_system.employee
CREATE TABLE IF NOT EXISTS `employee` (
  `EmployeeNumber` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each employee.',
  `LastName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Last name of the employee',
  `FirstName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'First name of the employee',
  `Birthday` date NOT NULL COMMENT 'Birth date of the employee',
  `DateHired` date NOT NULL DEFAULT '2000-01-01' COMMENT 'Date the employee was hired',
  `Address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Address of the employee',
  `PhoneNumber` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Active Phone number of the employee',
  `SSSNumber` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'SSS number of the employee',
  `PhilhealthNumber` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Philhealth number of the employee',
  `TINNumber` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Tin number of the employee',
  `PagibigNumber` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Pagibig number of the employee.',
  `Status` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PROBATIONARY',
  `PositionID` int NOT NULL COMMENT 'The position id which contains position name for the employee.',
  `BasicSalary` decimal(18,4) NOT NULL DEFAULT '0.0000' COMMENT 'The monthly basic salary of the employee.',
  `RiceSubsidy` decimal(18,4) NOT NULL DEFAULT '0.0000' COMMENT 'The rice allowance of the employee',
  `PhoneAllowance` decimal(18,4) NOT NULL DEFAULT '0.0000' COMMENT 'The phone allowance of the employee',
  `ClothingAllowance` decimal(18,4) NOT NULL DEFAULT '0.0000' COMMENT 'The clothing allowance of the employee',
  `GrossSemiMonthlyRate` decimal(18,4) NOT NULL DEFAULT '0.0000' COMMENT 'The gross semi monthly rate of the employee',
  `HourlyRate` decimal(18,4) NOT NULL DEFAULT '0.0000' COMMENT 'The hourly rate of the employee',
  PRIMARY KEY (`EmployeeNumber`),
  UNIQUE KEY `SSSNumber` (`SSSNumber`),
  UNIQUE KEY `PhilhealthNumber` (`PhilhealthNumber`),
  UNIQUE KEY `TINNumber` (`TINNumber`),
  UNIQUE KEY `PagibigNumber` (`PagibigNumber`),
  KEY `idx_position` (`PositionID`),
  CONSTRAINT `fk_employee_position` FOREIGN KEY (`PositionID`) REFERENCES `position` (`PositionID`)
) ENGINE=InnoDB AUTO_INCREMENT=986 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Stores employee data such as personal information, account numbers and salary structure.';

-- Dumping data for table payroll_system.employee: ~14 rows (approximately)
INSERT INTO `employee` (`EmployeeNumber`, `LastName`, `FirstName`, `Birthday`, `Address`, `PhoneNumber`, `SSSNumber`, `PhilhealthNumber`, `TINNumber`, `PagibigNumber`, `Status`, `PositionID`, `BasicSalary`, `RiceSubsidy`, `PhoneAllowance`, `ClothingAllowance`, `GrossSemiMonthlyRate`, `HourlyRate`) VALUES
	(3, 'Lim', 'Lima Hack', '1988-06-19', 'San Antonio De Padua 2, Block 1 Lot 8 and 2, Dasmarinas, Cavite', '171-867-411', '52-2061274-9', '331735646338', '683-102-776-000', '663904995411', 'REGULAR', 2, 60000.0000, 1500.0000, 2000.0000, 1000.0000, 30000.0000, 357.1400),
	(4, 'Aquino', 'Bianca Sofia', '1989-08-04', 'Rm. 402 4/F Jiao Building Timog Avenue Cor. Quezon Avenue 1100, Quezon City', '966-889-370', '30-8870406-2', '177451189665', '971-711-280-000', '171519773969', 'REGULAR', 3, 60000.0000, 1500.0000, 2000.0000, 1000.0000, 30000.0000, 357.1400),
	(5, 'Reyes', 'Isabella', '1994-06-16', '460 Solanda Street Intramuros 1000, Manila', '786-868-477', '40-2511815-0', '341911411254', '876-809-437-000', '416946776041', 'REGULAR', 4, 60000.0000, 1500.0000, 2000.0000, 1000.0000, 30000.0000, 357.1400),
	(6, 'Hernandez', 'Eduard', '1989-09-23', 'National Highway, Gingoog, Misamis Occidental', '088-861-012', '50-5577638-1', '957436191812', '031-702-374-000', '952347222457', 'REGULAR', 5, 52670.0000, 1500.0000, 1000.0000, 1000.0000, 26335.0000, 313.5100),
	(7, 'Villanueva', 'Andrea Mae', '1988-02-14', '17/85 Stracke Via Suite 042, Poblacion, Las Piñas 4783 Dinagat Islands', '918-621-603', '49-1632020-8', '382189453145', '317-674-022-000', '441093369646', 'REGULAR', 6, 52670.0000, 1500.0000, 1000.0000, 1000.0000, 26335.0000, 313.5100),
	(8, 'San Jose', 'Brad', '1996-03-15', '99 Strosin Hills, Poblacion, Bislig 5340 Tawi-Tawi', '797-009-261', '40-2400714-1', '239192926939', '672-474-690-000', '210850209964', 'REGULAR', 7, 42975.0000, 1500.0000, 800.0000, 800.0000, 21488.0000, 255.8000),
	(9, 'Romualdez', 'Alice', '1992-05-14', '12A/33 Upton Isle Apt. 420, Roxas City 1814 Surigao del Norte', '983-606-799', '55-4476527-2', '545652640232', '888-572-294-000', '211385556888', 'REGULAR', 8, 22500.0000, 1500.0000, 500.0000, 500.0000, 11250.0000, 133.9300),
	(10, 'Atienza', 'Rosie', '1948-09-24', '90A Dibbert Terrace Apt. 190, San Lorenzo 6056 Davao del Norte', '266-036-427', '41-0644692-3', '708988234853', '604-997-793-000', '260107732354', 'REGULAR', 9, 22500.0000, 1500.0000, 500.0000, 500.0000, 11250.0000, 133.9300),
	(11, 'Alvaro', 'Roderick', '1988-03-30', '#284 T. Morato corner, Scout Rallos Street, Quezon City', '053-381-386', '64-7605054-4', '578114853194', '525-420-419-000', '799254095212', 'REGULAR', 10, 52670.0000, 1500.0000, 1000.0000, 1000.0000, 26335.0000, 313.5100),
	(12, 'Salcedo', 'Anthony', '1993-09-14', '93/54 Shanahan Alley Apt. 183, Santo Tomas 1572 Masbate', '070-766-300', '26-9647608-3', '126445315651', '210-805-911-000', '218002473454', 'REGULAR', 11, 50825.0000, 1500.0000, 1000.0000, 1000.0000, 25413.0000, 302.5300),
	(13, 'Lopez', 'Josie', '1987-01-14', '49 Springs Apt. 266, Poblacion, Taguig 3200 Occidental Mindoro', '478-355-427', '44-8563448-3', '431709011012', '218-489-737-000', '113071293354', 'REGULAR', 12, 38475.0000, 1500.0000, 800.0000, 800.0000, 19238.0000, 229.0200),
	(14, 'Farala', 'Martha', '1942-01-11', '42/25 Sawayn Stream, Ubay 1208 Zamboanga del Norte', '329-034-366', '45-5656375-0', '233693897247', '210-835-851-000', '631130283546', 'REGULAR', 13, 24000.0000, 1500.0000, 500.0000, 500.0000, 12000.0000, 142.8600),
	(15, 'Martinez', 'Leila', '1970-07-11', '37/46 Kulas Roads, Maragondon 0962 Quirino', '877-110-749', '27-2090996-4', '515741057496', '275-792-513-000', '101205445886', 'REGULAR', 14, 24000.0000, 1500.0000, 500.0000, 500.0000, 12000.0000, 142.8600),
	(16, 'Romualdez', 'Fredrick', '1985-03-10', '22A/52 Lubowitz Meadows, Pililla 4895 Zambales', '023-079-009', '26-8768374-1', '308366860059', '598-065-761-000', '223057707853', 'REGULAR', 15, 53500.0000, 1500.0000, 1000.0000, 1000.0000, 26750.0000, 318.4500);

-- Dumping structure for table payroll_system.leave_balance
CREATE TABLE IF NOT EXISTS `leave_balance` (
  `BalanceID` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier forl leave balance.',
  `EmployeeID` int NOT NULL COMMENT 'The owner of the leave balance',
  `LeaveTypeID` int NOT NULL COMMENT 'The leave type id which contains leave type name such as ''Sick Leave", "Vacation Leave" and other types of leave type that can be included.',
  `Balance` int NOT NULL DEFAULT '0' COMMENT 'The balance leave left of the employee.',
  PRIMARY KEY (`BalanceID`),
  KEY `idx_employee_leave_balance` (`EmployeeID`) USING BTREE,
  KEY `idx_leave_type_balance` (`LeaveTypeID`) USING BTREE,
  CONSTRAINT `fk_employee_leave_balance` FOREIGN KEY (`EmployeeID`) REFERENCES `employee` (`EmployeeNumber`),
  CONSTRAINT `fk_leave_type_balance` FOREIGN KEY (`LeaveTypeID`) REFERENCES `leave_request_type` (`LeaveTypeID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Stores the leave balances of employees';

-- Dumping data for table payroll_system.leave_balance: ~3 rows (approximately)
INSERT INTO `leave_balance` (`BalanceID`, `EmployeeID`, `LeaveTypeID`, `Balance`) VALUES
	(1, 4, 3, 5),
	(2, 4, 1, 5),
	(3, 4, 2, 10);

-- Dumping structure for table payroll_system.leave_request
CREATE TABLE IF NOT EXISTS `leave_request` (
  `LeaveRequestID` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each leave request.',
  `EmployeeID` int NOT NULL COMMENT 'Owner of the leave request.',
  `LeaveTypeID` int NOT NULL COMMENT 'The leave type id which contains the leave type name.',
  `DateRequested` date NOT NULL DEFAULT (CURRENT_DATE) COMMENT 'The creation date of the leave request.',
  `StartDate` date DEFAULT NULL COMMENT 'The start date of the leave.',
  `EndDate` date DEFAULT NULL COMMENT 'The end date of the leave request.',
  `Status` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'The status of the leave request.',
  `AdminApprovalEmployeeID` int DEFAULT NULL COMMENT 'The employee that process leave request.',
  `AdminApprovalDate` datetime DEFAULT NULL COMMENT 'The date on which the approval process is completed.\n\n\n\n',
  `Description` tinytext NOT NULL,
  `isPaid` boolean NOT NULL DEFAULT '0' COMMENT 'Whether the leave is paid.',
  PRIMARY KEY (`LeaveRequestID`) USING BTREE,
  KEY `idx_employee_leave_request` (`EmployeeID`) USING BTREE,
  KEY `idx_leave_type` (`LeaveTypeID`) USING BTREE,
  KEY `fk_admin_approval_employee` (`AdminApprovalEmployeeID`),
  CONSTRAINT `fk_admin_approval_employee` FOREIGN KEY (`AdminApprovalEmployeeID`) REFERENCES `employee` (`EmployeeNumber`),
  CONSTRAINT `fk_employee_leave_request` FOREIGN KEY (`EmployeeID`) REFERENCES `employee` (`EmployeeNumber`),
  CONSTRAINT `fk_leave_type` FOREIGN KEY (`LeaveTypeID`) REFERENCES `leave_request_type` (`LeaveTypeID`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='The system stores employees'' leave request data';

-- Dumping data for table payroll_system.leave_request: ~6 rows (approximately)
INSERT INTO `leave_request` (`LeaveRequestID`, `EmployeeID`, `LeaveTypeID`, `DateRequested`, `StartDate`, `EndDate`, `Status`, `AdminApprovalEmployeeID`, `AdminApprovalDate`, `Description`) VALUES
	(25, 4, 1, '2024-08-03', '2024-08-03', '2024-08-03', 'Pending', NULL, NULL, ''),
	(26, 4, 1, '2024-08-03', '2024-08-05', '2024-08-06', 'Pending', NULL, NULL, ''),
	(27, 4, 1, '2024-08-03', '2024-08-30', '2024-08-31', 'Pending', NULL, NULL, ''),
	(28, 4, 1, '2024-08-04', '2024-08-04', '2024-08-04', 'Approved', NULL, NULL, ''),
	(29, 4, 1, '2024-08-04', '2024-08-19', '2024-08-23', 'Pending', NULL, NULL, ''),
	(30, 4, 3, '2024-08-04', '2024-09-24', '2024-09-25', 'Pending', NULL, NULL, '');

-- Dumping structure for table payroll_system.leave_request_type
CREATE TABLE IF NOT EXISTS `leave_request_type` (
  `LeaveTypeID` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for leave type.',
  `LeaveTypeName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Name of the leave type',
  `MaxCredits` int DEFAULT NULL COMMENT 'The maximum number of credits that an employee can obtain each year.\n\n\n\n',
  PRIMARY KEY (`LeaveTypeID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='The system stores various types of leave request data, each with its own maximum credit allocation.';

-- Dumping data for table payroll_system.leave_request_type: ~3 rows (approximately)
INSERT INTO `leave_request_type` (`LeaveTypeID`, `LeaveTypeName`, `MaxCredits`) VALUES
	(1, 'Sick', 5),
	(2, 'Vacation', 10),
	(3, 'Emergency', 10);

-- Dumping structure for table payroll_system.messages
CREATE TABLE IF NOT EXISTS `messages` (
  `message_id` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each message.',
  `sender_employee_id` int NOT NULL COMMENT 'Employee number of the sender.',
  `recipient_employee_id` int NOT NULL COMMENT 'Employee number of recepient',
  `message_content` tinytext COLLATE utf8mb4_unicode_ci NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'The timestamp column stores the date and time when each message is processed.\n\n',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'unread',
  `conversation_id` int NOT NULL COMMENT 'The conversation id which have two participants.',
  PRIMARY KEY (`message_id`),
  KEY `idx_sender` (`sender_employee_id`),
  KEY `idx_recipient` (`recipient_employee_id`),
  KEY `idx_conversation` (`conversation_id`),
  CONSTRAINT `fk_conversation` FOREIGN KEY (`conversation_id`) REFERENCES `conversation` (`conversation_id`),
  CONSTRAINT `fk_recipient_employee` FOREIGN KEY (`recipient_employee_id`) REFERENCES `employee` (`EmployeeNumber`),
  CONSTRAINT `fk_sender_employee` FOREIGN KEY (`sender_employee_id`) REFERENCES `employee` (`EmployeeNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Stores messages exchanged between employees, including sender and recipient details, content, status (unread/read), and associated conversation IDs.';

-- Dumping data for table payroll_system.messages: ~0 rows (approximately)

-- Dumping structure for table payroll_system.message_attachments
CREATE TABLE IF NOT EXISTS `message_attachments` (
  `attachment_id` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each attachment.',
  `message_id` int NOT NULL COMMENT 'The message id which contains message data.',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'The file name of the attachmentm',
  `file_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'The file url path',
  `upload_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'The processed_timestamp column records the date and time when an attachment is processed.\n\n',
  PRIMARY KEY (`attachment_id`),
  KEY `idx_message` (`message_id`),
  CONSTRAINT `fk_message_attachment` FOREIGN KEY (`message_id`) REFERENCES `messages` (`message_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Stores attachments associated with messages, linking each attachment to its corresponding message by message_id.';

-- Dumping data for table payroll_system.message_attachments: ~0 rows (approximately)

-- Dumping structure for table payroll_system.message_folders
CREATE TABLE IF NOT EXISTS `message_folders` (
  `folder_id` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each folder.',
  `employee_id` int NOT NULL COMMENT 'The owner of the folder.',
  `folder_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'The name of the folder',
  `message_id` int NOT NULL COMMENT 'The message id that cotains two participant.',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'The processed_timestamp column records the date and time when an message folder is processed.\n\n',
  PRIMARY KEY (`folder_id`),
  KEY `idx_employee_folder` (`employee_id`),
  KEY `idx_message_folder` (`message_id`),
  CONSTRAINT `fk_folder_employee` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`EmployeeNumber`) ON DELETE CASCADE,
  CONSTRAINT `fk_folder_message` FOREIGN KEY (`message_id`) REFERENCES `messages` (`message_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Stores folders for organizing messages per employee, linking each folder to specific messages by message_id.';

-- Dumping data for table payroll_system.message_folders: ~0 rows (approximately)

-- Dumping structure for table payroll_system.message_status
CREATE TABLE IF NOT EXISTS `message_status` (
  `status_id` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each message status',
  `message_id` int NOT NULL COMMENT 'Identifier for the message associated with this status',
  `employee_id` int NOT NULL COMMENT 'Identifier for the employee associated with this status',
  `status_type` varchar(25) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'sent',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp when the status was recorded',
  PRIMARY KEY (`status_id`),
  KEY `idx_message_status_message` (`message_id`),
  KEY `idx_message_status_employee` (`employee_id`),
  CONSTRAINT `fk_message_status_employee` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`EmployeeNumber`) ON DELETE CASCADE,
  CONSTRAINT `fk_message_status_message` FOREIGN KEY (`message_id`) REFERENCES `messages` (`message_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Tracks the delivery status of messages for each employee, including whether messages have been sent, delivered, or read.';

-- Dumping data for table payroll_system.message_status: ~0 rows (approximately)

-- Dumping structure for table payroll_system.notifications
CREATE TABLE IF NOT EXISTS `notifications` (
  `notification_id` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each notification.\n',
  `employee_id` int NOT NULL COMMENT 'Identifier for the employee receiving the notification',
  `notification_type` tinytext COLLATE utf8mb4_unicode_ci NOT NULL,
  `notification_content` tinytext COLLATE utf8mb4_unicode_ci NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp when the notification was created.\n',
  `read_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Read status of the notification (0 = unread, 1 = read).\n',
  PRIMARY KEY (`notification_id`),
  KEY `idx_employee` (`employee_id`),
  CONSTRAINT `fk_notification_employee` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`EmployeeNumber`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Stores notifications for employees, including messages and system alerts, with details such as content, timestamp, and read status.';

-- Dumping data for table payroll_system.notifications: ~0 rows (approximately)

-- Dumping structure for table payroll_system.overtime_request
CREATE TABLE IF NOT EXISTS `overtime_request` (
  `RequestID` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each overtime request.',
  `EmployeeID` int NOT NULL COMMENT 'Identifier for the employee making the overtime request.',
  `DateRequested` date NOT NULL DEFAULT (CURRENT_DATE) COMMENT 'Date when the overtime request was submitted.',
  `OvertimeDate` date NOT NULL COMMENT 'Date for which the overtime is requested.',
  `HoursRequested` decimal(8,4) NOT NULL COMMENT 'Number of hours requested for overtime.',
  `Status` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Current status of the overtime request.',
  `HRRemarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Remarks or notes from the administrator handling the request.',
  PRIMARY KEY (`RequestID`),
  KEY `idx_employee_overtime` (`EmployeeID`),
  CONSTRAINT `fk_employee_overtime` FOREIGN KEY (`EmployeeID`) REFERENCES `employee` (`EmployeeNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Stores overtime requests submitted by employees, including details such as requested date, hours, status, and administrative remarks.';

-- Dumping data for table payroll_system.overtime_request: ~0 rows (approximately)

-- Dumping structure for table payroll_system.pagibig_contribution_rates
CREATE TABLE IF NOT EXISTS `pagibig_contribution_rates` (
  `ContributionRateID` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each contribution rate.',
  `SalaryBracketFrom` decimal(18,4) NOT NULL COMMENT 'Lower limit of salary bracket for the contribution rate.',
  `SalaryBracketTo` decimal(18,4) NOT NULL COMMENT 'Upper limit of salary bracket for the contribution rate.',
  `EmployeeShare` decimal(18,4) NOT NULL COMMENT 'Employee''s share for the specified salary bracket.',
  `EmployerShare` decimal(18,4) NOT NULL COMMENT 'Employer''s share for the specified salary bracket.',
  `EffectiveDate` date NOT NULL COMMENT 'Effective date from which the contribution rate is applicable.',
  PRIMARY KEY (`ContributionRateID`),
  KEY `idx_pagibig_effective_date` (`EffectiveDate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Stores Pag-IBIG contribution rates, specifying salary brackets and corresponding employee and employer shares, effective from specific dates.';

-- Dumping data for table payroll_system.pagibig_contribution_rates: ~2 rows (approximately)
INSERT INTO `pagibig_contribution_rates` (`SalaryBracketFrom`, `SalaryBracketTo`, `EmployeeShare`, `EmployerShare`, `EffectiveDate`) VALUES
	-- Monthly Basic Salary: At least 1,000 to 1,500 - Employee's Contribution Rate: 1%, Employer's Contribution Rate: 2%, Total: 3%
	(1000.0000, 1500.0000, 1.0000, 2.0000, '2024-01-01'),
	-- Monthly Basic Salary: Over 1,500 - Employee's Contribution Rate: 2%, Employer's Contribution Rate: 2%, Total: 4%
	(1500.0100, 999999999.0000, 2.0000, 2.0000, '2024-01-01');

-- Dumping structure for table payroll_system.payroll
CREATE TABLE IF NOT EXISTS `payroll` (
  `PayrollID` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each payroll run.',
  `PayrollRunDate` date NOT NULL COMMENT 'Date when the payroll run was executed.',
  `PeriodStartDate` date NOT NULL COMMENT 'Start date of the payroll period.',
  `PeriodEndDate` date NOT NULL COMMENT 'End date of the payroll period.',
  `Status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Current status of the payroll run.',
  `CreatedBy` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'The creator of the payroll data.',
  `CreatedDate` datetime DEFAULT NULL COMMENT 'The creation date of the payroll data.',
  `LastModifiedBy` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'User who created the payroll run.',
  `LastModifiedDate` datetime DEFAULT NULL COMMENT 'Date and time of the last modification to the payroll run.',
  PRIMARY KEY (`PayrollID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Manages overarching payroll runs, including dates, statuses, and who initiated or modified them.\n\n';

-- Dumping data for table payroll_system.payroll: ~0 rows (approximately)

-- Dumping structure for table payroll_system.payroll_approval
CREATE TABLE IF NOT EXISTS `payroll_approval` (
  `ApprovalID` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each payroll approval entry.',
  `PayrollID` int NOT NULL COMMENT 'Identifier linking to the payroll run being approved.',
  `ApproverID` int NOT NULL COMMENT 'Identifier for the approver.',
  `ApprovalDate` datetime NOT NULL COMMENT 'Date and time when the approval was granted.',
  `Status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Current status of the approval process.',
  PRIMARY KEY (`ApprovalID`),
  KEY `idx_payroll_approval` (`PayrollID`),
  CONSTRAINT `fk_payroll_approval_payroll` FOREIGN KEY (`PayrollID`) REFERENCES `payroll` (`PayrollID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Tracks approval status and details for each payroll run, linking back to the payroll table.\n\n';

-- Dumping data for table payroll_system.payroll_approval: ~0 rows (approximately)

-- Dumping structure for table payroll_system.payroll_changes
CREATE TABLE IF NOT EXISTS `payroll_changes` (
  `ChangeID` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each payroll change entry.',
  `PayrollID` int NOT NULL COMMENT 'Identifier linking to the payroll affected by the change.',
  `ModifiedBy` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'User who made the modification.',
  `ModificationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp when the modification was made.',
  `FieldChanged` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Name of the field changed in the payroll.',
  `OldValue` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT 'Previous value of the changed field.',
  `NewValue` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT 'New value of the changed field.',
  PRIMARY KEY (`ChangeID`),
  KEY `idx_payroll_changes` (`PayrollID`),
  CONSTRAINT `fk_payroll_changes_payroll` FOREIGN KEY (`PayrollID`) REFERENCES `payroll` (`PayrollID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Records any modifications or adjustments made to payroll data, maintaining an audit trail of changes.\n\n';

-- Dumping data for table payroll_system.payroll_changes: ~0 rows (approximately)

-- Dumping structure for table payroll_system.payroll_transactions
CREATE TABLE IF NOT EXISTS `payroll_transactions` (
  `TransactionID` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each transaction.',
  `PayrollID` int NOT NULL COMMENT 'Identifier linking to the payroll cycle.',
  `PayslipID` int NOT NULL COMMENT 'Identifier linking to the payslip involved in the transaction.',
  `TransactionType` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Type of transaction (e.g., deductions, bonuses).',
  `TransactionAmount` decimal(18,4) NOT NULL COMMENT 'Amount of the transaction.',
  `TransactionDate` date NOT NULL COMMENT 'Date of the transaction.',
  `Description` tinytext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`TransactionID`),
  KEY `idx_payroll_transactions` (`PayrollID`,`PayslipID`),
  KEY `fk_payroll_transactions_payslip` (`PayslipID`),
  CONSTRAINT `fk_payroll_transactions_payroll` FOREIGN KEY (`PayrollID`) REFERENCES `payroll` (`PayrollID`) ON DELETE CASCADE,
  CONSTRAINT `fk_payroll_transactions_payslip` FOREIGN KEY (`PayslipID`) REFERENCES `payslip` (`PayslipID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Stores detailed transaction records related to each payroll cycle, including payslips involved, transaction types (e.g., deductions, bonuses), amounts, and dates.\n\n';

-- Dumping data for table payroll_system.payroll_transactions: ~0 rows (approximately)

-- Dumping structure for table payroll_system.payslip
CREATE TABLE IF NOT EXISTS `payslip` (
  `PayslipID` int NOT NULL AUTO_INCREMENT,
  `PayrollID` int NOT NULL,
  `PayslipNumber` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Unique identifier or number for the payslip.',
  `EmployeeID` int NOT NULL COMMENT 'The ower of the payslip',
  `PeriodStartDate` date NOT NULL COMMENT 'Start pay date of the payslip',
  `PeriodEndDate` date NOT NULL COMMENT 'End pay date of the payslip.',
  `EmployeeName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Name of the employee.',
  `EmployeePosition` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Position or job title of the employee.',
  `MonthlyRate` decimal(18,4) NOT NULL COMMENT 'Monthly rate or salary of the employee.',
  `DailyRate` decimal(18,4) NOT NULL COMMENT 'Daily rate calculated based on the monthly rate.',
  `DaysWorked` int NOT NULL COMMENT 'Number of days the employee worked during the period.',
  `OvertimeHours` decimal(8,4) NOT NULL DEFAULT '0.0000' COMMENT 'Total overtime hours worked by the employee.',
  `GrossIncome` decimal(18,4) NOT NULL COMMENT 'Total gross income earned by the employee.',
  `RiceSubsidy` decimal(18,4) NOT NULL COMMENT 'Amount of rice subsidy given to the employee.',
  `PhoneAllowance` decimal(18,4) NOT NULL COMMENT 'Amount of phone allowance given to the employee.',
  `ClothingAllowance` decimal(18,4) NOT NULL COMMENT 'Amount of clothing allowance given to the employee.',
  `TotalBenefits` decimal(18,4) NOT NULL COMMENT 'Total benefits received by the employee.',
  `SSS` decimal(18,4) NOT NULL COMMENT 'Amount deducted for Social Security System (SSS) contributions.',
  `Philhealth` decimal(18,4) NOT NULL COMMENT 'Amount deducted for PhilHealth contributions.',
  `PagIbig` decimal(18,4) NOT NULL COMMENT 'Amount deducted for Pag-IBIG contributions.',
  `WithholdingTax` decimal(18,4) NOT NULL COMMENT 'Amount deducted for withholding tax.',
  `TotalDeductions` decimal(18,4) NOT NULL COMMENT 'Total deductions from the employee''s earnings.',
  `NetPay` decimal(18,4) NOT NULL COMMENT 'Net pay received by the employee after deductions.',
  `CompanyID` int DEFAULT NULL COMMENT 'Identifier linking to the company or organization.',
  `CompanyName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Name of the company or organization.',
  `PayrollRunDate` date DEFAULT NULL COMMENT 'Date when the payroll run was executed.',
  `PaymentDate` date DEFAULT NULL COMMENT 'Date when the payment was made to the employee.',
  `PaymentMethod` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Method used for payment (e.g., bank transfer, cash).',
  `EmployeeDepartment` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Department or division where the employee belongs.',
  `EmployeeManager` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Name of the employee''s manager or supervisor.',
  `LeaveDays` int DEFAULT NULL COMMENT 'Number of leave days taken by the employee during the period.',
  `AbsenceDays` int DEFAULT NULL COMMENT 'Number of absence days (without leave) taken by the employee during the period.',
  `TaxableIncome` decimal(18,4) DEFAULT NULL COMMENT 'Taxable income amount for tax computation purposes.',
  `TaxableBenefits` decimal(18,4) DEFAULT NULL COMMENT 'Taxable benefits amount for tax computation purposes.',
  `Bonus` decimal(18,4) DEFAULT NULL COMMENT 'Bonus amount received by the employee.',
  `OtherDeductions` decimal(18,4) DEFAULT NULL COMMENT 'Other deductions not covered by standard deductions.',
  `CreatedBy` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'User who created the payslip record.',
  `CreatedDate` datetime DEFAULT NULL COMMENT 'Date and time when the payslip record was created.',
  `LastModifiedBy` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'User who last modified the payslip record.',
  `LastModifiedDate` datetime DEFAULT NULL COMMENT 'Date and time of the last modification to the payslip record.',
  PRIMARY KEY (`PayslipID`) USING BTREE,
  KEY `fk_payslip_payroll` (`PayrollID`),
  CONSTRAINT `fk_payslip_payroll` FOREIGN KEY (`PayrollID`) REFERENCES `payroll` (`PayrollID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Stores details of employee payslips, including earnings, deductions, benefits, and other payroll-related information.';

-- Dumping data for table payroll_system.payslip: ~0 rows (approximately)

-- Dumping structure for table payroll_system.payslip_history
CREATE TABLE IF NOT EXISTS `payslip_history` (
  `HistoryID` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each history record.',
  `PayslipID` int NOT NULL COMMENT 'Identifier linking to the original payslip record.',
  `ModifiedBy` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'User who made the modification.',
  `ModificationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Date and time when the modification was made.',
  `FieldChanged` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Name of the field that was changed.',
  `OldValue` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT 'Previous value of the modified field.',
  `NewValue` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT 'New value of the modified field.',
  `EmployeeDepartment` int DEFAULT NULL COMMENT 'Identifier linking to the department affected by the modification.',
  `ModifiedFieldDetails` tinytext,
  PRIMARY KEY (`HistoryID`) USING BTREE,
  KEY `idx_payslip_history` (`PayslipID`) USING BTREE,
  KEY `FK_payslip_history_department` (`EmployeeDepartment`),
  CONSTRAINT `FK_payslip_history_department` FOREIGN KEY (`EmployeeDepartment`) REFERENCES `department` (`DepartmentID`),
  CONSTRAINT `fk_payslip_history_payslip` FOREIGN KEY (`PayslipID`) REFERENCES `payslip` (`PayslipID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Tracks historical changes made to payslip records, including modifications to fields such as earnings, deductions, and employee details.';

-- Dumping data for table payroll_system.payslip_history: ~0 rows (approximately)

-- Dumping structure for table payroll_system.permission
CREATE TABLE IF NOT EXISTS `permission` (
  `PermissionID` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each permission.',
  `PermissionName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Name of the permission.',
  `Description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Description or details about the permission.',
  PRIMARY KEY (`PermissionID`) USING BTREE,
  UNIQUE KEY `idx_permission_name` (`PermissionName`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Stores different permissions available in the system.';

-- Dumping data for table payroll_system.permission: ~3 rows (approximately)
INSERT INTO `permission` (`PermissionID`, `PermissionName`, `Description`) VALUES
	(1, 'View Personal Information', 'Allows viewing personal information of employees.'),
	(2, 'View Payslip', 'Allows viewing payslip details.'),
	(3, 'View Timesheet', 'Allows viewing timesheet details.');

-- Dumping structure for table payroll_system.philhealth_contribution_rates
CREATE TABLE IF NOT EXISTS `philhealth_contribution_rates` (
  `ContributionRateID` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each contribution rate.',
  `SalaryBracketFrom` decimal(18,4) NOT NULL COMMENT 'Lower salary bracket limit for the contribution rate.',
  `SalaryBracketTo` decimal(18,4) NOT NULL COMMENT 'Upper salary bracket limit for the contribution rate.',
  `EmployeeShare` decimal(18,4) NOT NULL COMMENT 'Employee share of the contribution rate.',
  `EmployerShare` decimal(18,4) NOT NULL COMMENT 'Employer share of the contribution rate.',
  `EffectiveDate` date NOT NULL COMMENT 'Effective date of the contribution rate.',
  PRIMARY KEY (`ContributionRateID`),
  KEY `idx_philhealth_effective_date` (`EffectiveDate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Stores PhilHealth contribution rates, specifying salary brackets and corresponding employee and employer shares, effective from specific dates.';

-- Dumping data for table payroll_system.philhealth_contribution_rates: ~3 rows (approximately)
INSERT INTO `philhealth_contribution_rates` (`SalaryBracketFrom`, `SalaryBracketTo`, `EmployeeShare`, `EmployerShare`, `EffectiveDate`) VALUES
	-- Monthly Basic Salary: 10,000 - Premium Rate: 3% - Monthly Premium: 300 (Employee: 150, Employer: 150)
	(0.0000, 10000.0000, 150.0000, 150.0000, '2024-01-01'),
	-- Monthly Basic Salary: 10,000.01 to 59,999.99 - Premium Rate: 3% - Monthly Premium: 300 up to 1,800 (Employee: 1.5% = 150-900, Employer: 1.5% = 150-900)
	-- Note: EmployeeShare and EmployerShare stored as 1.5 (percentage) for calculation: salary * 1.5% with min 150, max 900
	(10000.0100, 59999.9900, 1.5000, 1.5000, '2024-01-01'),
	-- Monthly Basic Salary: 60,000+ - Premium Rate: 3% - Monthly Premium: 1,800 (Employee: 900, Employer: 900)
	(60000.0000, 999999999.0000, 900.0000, 900.0000, '2024-01-01');

-- Dumping structure for table payroll_system.position
CREATE TABLE IF NOT EXISTS `position` (
  `PositionID` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each position.',
  `PositionName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Name of the position.',
  `DepartmentID` int NOT NULL COMMENT 'Identifier linking to the department associated with the position.',
  `Description` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT 'Description or details about the position.',
  PRIMARY KEY (`PositionID`),
  KEY `idx_department` (`DepartmentID`),
  CONSTRAINT `fk_position_department` FOREIGN KEY (`DepartmentID`) REFERENCES `department` (`DepartmentID`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Stores information about positions within the organization, including department associations and position descriptions.';

-- Dumping data for table payroll_system.position: ~18 rows (approximately)
INSERT INTO `position` (`PositionID`, `PositionName`, `DepartmentID`, `Description`) VALUES
	(1, 'Chief Executive Officer', 1, 'Leads the executive team and sets the company’s overall direction and strategy.'),
	(2, 'Chief Operating Officer', 1, 'Responsible for the daily operation of the company and implementing its policies.'),
	(3, 'Chief Finance Officer', 5, 'Oversees financial planning, budgeting, and reporting for the organization.'),
	(4, 'Chief Marketing Officer', 4, 'Leads marketing strategies and activities to promote the company’s products and services.'),
	(5, 'IT Operations and Systems', 3, 'Manages IT infrastructure, operations, and support services for the organization.'),
	(6, 'HR Manager', 2, 'Oversees all aspects of human resources including recruitment, training, and employee relations.'),
	(7, 'HR Team Leader', 2, 'Leads a team of HR professionals in executing HR strategies and initiatives.'),
	(8, 'HR Rank and File', 2, 'Handles day-to-day HR tasks such as payroll, benefits administration, and employee records.'),
	(9, 'Accounting Head', 7, 'Leads the accounting department and ensures accurate financial reporting.'),
	(10, 'Payroll Manager', 8, 'Manages payroll processing and ensures compliance with labor laws and regulations.'),
	(11, 'Payroll Team Leader', 8, 'Supervises a team responsible for payroll processing and related tasks.'),
	(12, 'Payroll Rank and File', 8, 'Performs payroll processing and supports payroll-related activities.'),
	(13, 'Account Manager', 9, 'Manages client accounts and relationships, ensuring customer satisfaction and business growth.'),
	(14, 'Account Team Leader', 9, 'Leads a team of account managers in achieving sales targets and client satisfaction.'),
	(15, 'Account Rank and File', 9, 'Handles day-to-day account management tasks and supports account managers.'),
	(16, 'Sales & Marketing', 9, 'Drives sales growth through marketing campaigns and customer engagement.'),
	(17, 'Supply Chain and Logistics', 10, 'Manages the supply chain process from procurement to delivery of goods and services.'),
	(18, 'Customer Service and Relations', 11, 'Handles customer inquiries, complaints, and support services.');

-- Dumping structure for table payroll_system.reimbursement_requests
CREATE TABLE IF NOT EXISTS `reimbursement_requests` (
  `RequestID` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each reimbursement request.',
  `EmployeeNumber` int NOT NULL COMMENT 'Identifier of the employee submitting the reimbursement request.',
  `RequestDate` date NOT NULL COMMENT 'Date when the reimbursement request was submitted.',
  `Amount` decimal(18,4) NOT NULL COMMENT 'Amount requested for reimbursement.',
  `Description` tinytext COLLATE utf8mb4_unicode_ci,
  `Status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Current status of the reimbursement request.',
  `ApprovedBy` int DEFAULT NULL COMMENT 'Employee ID of the approver who approved the reimbursement request.',
  `ApprovedDate` datetime DEFAULT NULL COMMENT 'Date and time when the reimbursement request was approved.',
  `ProcessedBy` int DEFAULT NULL COMMENT 'Employee ID of the processor who processed the reimbursement request.',
  `ProcessedDate` datetime DEFAULT NULL COMMENT 'Date and time when the reimbursement request was processed.',
  PRIMARY KEY (`RequestID`),
  KEY `idx_reimbursement_requests_employee` (`EmployeeNumber`),
  KEY `fk_reimbursement_requests_approver` (`ApprovedBy`),
  KEY `fk_reimbursement_requests_processor` (`ProcessedBy`),
  CONSTRAINT `fk_reimbursement_requests_approver` FOREIGN KEY (`ApprovedBy`) REFERENCES `employee` (`EmployeeNumber`) ON DELETE SET NULL,
  CONSTRAINT `fk_reimbursement_requests_employee` FOREIGN KEY (`EmployeeNumber`) REFERENCES `employee` (`EmployeeNumber`) ON DELETE CASCADE,
  CONSTRAINT `fk_reimbursement_requests_processor` FOREIGN KEY (`ProcessedBy`) REFERENCES `employee` (`EmployeeNumber`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='This table stores information about each reimbursement request submitted by employees. \nIt tracks details such as request date, amount, description, status, approval and processing information. \nForeign keys link the request to the employee submitting it, the approver, and the processor.';

-- Dumping data for table payroll_system.reimbursement_requests: ~0 rows (approximately)

-- Dumping structure for table payroll_system.reimbursement_transactions
CREATE TABLE IF NOT EXISTS `reimbursement_transactions` (
  `TransactionID` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each reimbursement transaction.',
  `RequestID` int NOT NULL COMMENT 'Identifier linking to the reimbursement request for which this transaction is recorded.',
  `TransactionDate` date NOT NULL COMMENT 'Date when the reimbursement transaction took place.',
  `Amount` decimal(18,4) NOT NULL COMMENT 'Amount of money involved in the reimbursement transaction.',
  `PaidTo` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Recipient or entity to whom the reimbursement was paid.',
  `PaymentMethod` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Method used for the reimbursement payment.',
  `Details` tinytext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`TransactionID`),
  KEY `idx_reimbursement_transactions_request` (`RequestID`),
  CONSTRAINT `fk_reimbursement_transactions_request` FOREIGN KEY (`RequestID`) REFERENCES `reimbursement_requests` (`RequestID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='This table records details of each reimbursement transaction processed after a request has been approved.\n\n';

-- Dumping data for table payroll_system.reimbursement_transactions: ~0 rows (approximately)

-- Dumping structure for table payroll_system.role
CREATE TABLE IF NOT EXISTS `role` (
  `RoleID` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each role.',
  `RoleName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Name of the role.',
  `Description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Description or details about the role.',
  PRIMARY KEY (`RoleID`),
  UNIQUE KEY `idx_role_name` (`RoleName`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Stores different role of system.';

-- Dumping data for table payroll_system.role: ~0 rows (approximately)
INSERT INTO `role` (`RoleID`, `RoleName`, `Description`) VALUES
	(1, 'Employee', 'Standard employee role with basic permissions.'),
	(5, 'HR Administrator', 'Manage employee records and leave requests.'),
	(7, 'Payroll Administrator', 'Manage payroll processing and approvals.'),
	(9, 'System Administrator', 'Manage system configuration and user access.');

-- Dumping structure for table payroll_system.role_permission
CREATE TABLE IF NOT EXISTS `role_permission` (
  `RolePermissionID` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each role-permission mapping.',
  `RoleID` int NOT NULL COMMENT 'Identifier for the role associated with this permission mapping.',
  `PermissionID` int NOT NULL COMMENT 'Identifier for the permission associated with this role mapping.',
  PRIMARY KEY (`RolePermissionID`) USING BTREE,
  UNIQUE KEY `idx_role_permission` (`RoleID`,`PermissionID`) USING BTREE,
  KEY `fk_role_permission_permission` (`PermissionID`),
  CONSTRAINT `fk_role_permission_permission` FOREIGN KEY (`PermissionID`) REFERENCES `permission` (`PermissionID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_role_permission_role` FOREIGN KEY (`RoleID`) REFERENCES `role` (`RoleID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Maps permissions to roles in the system.';

-- Dumping data for table payroll_system.role_permission: ~3 rows (approximately)
INSERT INTO `role_permission` (`RolePermissionID`, `RoleID`, `PermissionID`) VALUES
	(7, 1, 1),
	(8, 1, 2),
	(9, 1, 3),
	(10, 5, 1),
	(11, 5, 2),
	(12, 5, 3),
	(13, 7, 1),
	(14, 7, 2),
	(15, 7, 3),
	(16, 9, 1),
	(17, 9, 2),
	(18, 9, 3);

-- Dumping structure for table payroll_system.sss_contribution_rates
CREATE TABLE IF NOT EXISTS `sss_contribution_rates` (
  `ContributionRateID` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each SSS contribution rate entry.',
  `SalaryBracketFrom` decimal(18,4) NOT NULL COMMENT 'Lower limit of the salary bracket for this contribution rate.',
  `SalaryBracketTo` decimal(18,4) NOT NULL COMMENT 'Upper limit of the salary bracket for this contribution rate.',
  `EmployeeShare` decimal(18,4) NOT NULL COMMENT 'Employee contribution share for this salary bracket.',
  `EmployerShare` decimal(18,4) NOT NULL COMMENT 'Employer contribution share for this salary bracket.',
  `EffectiveDate` date NOT NULL COMMENT 'Date when this contribution rate becomes effective.',
  PRIMARY KEY (`ContributionRateID`),
  KEY `idx_sss_effective_date` (`EffectiveDate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Stores SSS contribution rates, specifying salary brackets and corresponding employee and employer shares, effective from specific dates.';

-- Dumping data for table payroll_system.sss_contribution_rates: ~0 rows (approximately)

-- Dumping structure for table payroll_system.timesheet
CREATE TABLE IF NOT EXISTS `timesheet` (
  `TimesheetID` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each timesheet entry.',
  `EmployeeID` int NOT NULL COMMENT 'Identifier of the employee associated with this timesheet entry.',
  `Date` date NOT NULL COMMENT 'Date of the timesheet entry.',
  `TimeIn` time NOT NULL COMMENT 'Time when the employee started work.',
  `TimeOut` time DEFAULT NULL COMMENT 'Time when the employee finished work, if available.',
  `HoursWorked` decimal(8,2) GENERATED ALWAYS AS ((case when ((`TimeOut` is not null) and (`TimeIn` is not null)) then (time_to_sec(timediff(least(`TimeOut`,_utf8mb4'17:00:00'),greatest(`TimeIn`,_utf8mb4'08:00:00'))) / 3600.0) else NULL end)) STORED COMMENT 'Automatically calculated hours worked based on TimeIn and TimeOut, capped between 08:00:00 and 17:00:00.',
  `Remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Optional remarks or notes related to the timesheet entry.',
  `Status` varchar(20) NOT NULL DEFAULT 'Not Submitted',
  `Approver` int DEFAULT NULL COMMENT 'Employee ID of the person who approved this timesheet entry, if applicable.',
  PRIMARY KEY (`TimesheetID`) USING BTREE,
  KEY `fk_employee` (`EmployeeID`) USING BTREE,
  KEY `FK_timesheet_employee` (`Approver`),
  CONSTRAINT `fk_employee` FOREIGN KEY (`EmployeeID`) REFERENCES `employee` (`EmployeeNumber`),
  CONSTRAINT `FK_timesheet_employee` FOREIGN KEY (`Approver`) REFERENCES `employee` (`EmployeeNumber`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Stores employee timesheet entries including time in, time out, hours worked, and status (submitted, approved, disapproved). \nCalculates hours worked based on time in and time out, capped between 08:00:00 and 17:00:00. \nLinks each entry to the employee and optional approver.';

-- Dumping data for table payroll_system.timesheet: ~1 rows (approximately)
INSERT INTO `timesheet` (`TimesheetID`, `EmployeeID`, `Date`, `TimeIn`, `TimeOut`, `Remarks`, `Status`, `Approver`) VALUES
	(2, 1, '2024-07-22', '07:57:15', '13:57:24', NULL, 'Not Submitted', NULL);

-- Dumping structure for table payroll_system.tin_compliance
CREATE TABLE IF NOT EXISTS `tin_compliance` (
  `TINComplianceID` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each TIN compliance record.',
  `EmployeeID` int NOT NULL COMMENT 'Identifier of the employee associated with this TIN compliance record.',
  `TINNumber` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Tax Identification Number (TIN) of the employee.',
  `DateRegistered` date NOT NULL COMMENT 'Date when the TIN was registered or acquired by the employee.',
  PRIMARY KEY (`TINComplianceID`),
  KEY `idx_tin_employee_id` (`EmployeeID`),
  CONSTRAINT `fk_tin_employee` FOREIGN KEY (`EmployeeID`) REFERENCES `employee` (`EmployeeNumber`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Stores records of Tax Identification Number (TIN) compliance for employees, including their TIN number and registration date.';

-- Dumping data for table payroll_system.tin_compliance: ~0 rows (approximately)

-- Dumping structure for table payroll_system.user
CREATE TABLE IF NOT EXISTS `user` (
  `UserID` int NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each user account.',
  `Username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Username used for login authentication.',
  `Password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Password stored securely for user login.',
  `Email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Email address associated with the user account.',
  `FullName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Full name of the user.',
  `RoleID` int NOT NULL COMMENT 'Identifier of the role assigned to the user.',
  `Status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'Active' COMMENT 'Status of the user account (Active, Inactive, etc.).',
  `EmployeeId` int NOT NULL COMMENT 'Identifier of the employee associated with this user account.',
  PRIMARY KEY (`UserID`),
  UNIQUE KEY `idx_username` (`Username`),
  UNIQUE KEY `idx_email` (`Email`),
  KEY `fk_user_role` (`RoleID`),
  KEY `FK_user_employee` (`EmployeeId`),
  CONSTRAINT `FK_user_employee` FOREIGN KEY (`EmployeeId`) REFERENCES `employee` (`EmployeeNumber`),
  CONSTRAINT `fk_user_role` FOREIGN KEY (`RoleID`) REFERENCES `role` (`RoleID`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Stores user accounts with login credentials and associated employee information, linked to roles.';

-- Dumping data for table payroll_system.user: ~4 rows (approximately)
INSERT INTO `user` (`UserID`, `Username`, `Password`, `Email`, `FullName`, `RoleID`, `Status`, `EmployeeId`) VALUES
	(1, 'emp_demo', 'emp123', 'emp_demo@motorph.local', 'Employee Demo', 1, 'Active', 4),
	(2, 'hr_demo', 'hr123', 'hr_demo@motorph.local', 'HR Demo', 5, 'Active', 6),
	(3, 'payroll_demo', 'payroll123', 'payroll_demo@motorph.local', 'Payroll Demo', 7, 'Active', 3),
	(4, 'sysadmin_demo', 'sysadmin123', 'sysadmin_demo@motorph.local', 'System Admin Demo', 9, 'Active', 5),
	(5, 'sysadmin', 'admin123', 'sysadmin@motorph.local', 'System Administrator', 9, 'Active', 5),
	(6, 'hrmanager_demo', 'hrmanager123', 'hrmanager_demo@motorph.local', 'HR Manager Demo', 5, 'Active', 7);

-- Dumping structure for table payroll_system.user_log
CREATE TABLE IF NOT EXISTS `user_log` (
  `LogID` bigint NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for each log entry.',
  `UserID` int NOT NULL COMMENT 'Identifier of the user performing the action.',
  `Action` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Description of the action performed by the user.',
  `IPAddress` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'IP address from which the action was performed.',
  `LogDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp when the action was logged.',
  PRIMARY KEY (`LogID`),
  KEY `idx_user_log_user` (`UserID`),
  CONSTRAINT `fk_user_log_user` FOREIGN KEY (`UserID`) REFERENCES `user` (`UserID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=154 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Records user actions and logins, including the associated user, action description, IP address, and timestamp.';

-- Dumping data for table payroll_system.user_log: ~153 rows (approximately)
INSERT INTO `user_log` (`LogID`, `UserID`, `Action`, `IPAddress`, `LogDateTime`) VALUES
	(1, 1, 'User logged in', '172.24.16.1', '2024-07-30 23:17:37'),
	(2, 1, 'User logged in', '172.24.16.1', '2024-07-30 23:26:33'),
	(3, 1, 'User logged in', '172.24.16.1', '2024-07-30 23:28:06'),
	(4, 1, 'User logged in', '172.24.16.1', '2024-07-30 23:30:15'),
	(5, 1, 'User logged in', '172.24.16.1', '2024-07-30 23:45:25'),
	(6, 1, 'User logged in', '172.24.16.1', '2024-07-31 01:45:40'),
	(7, 1, 'User logged in', '172.24.16.1', '2024-07-31 10:07:17'),
	(8, 1, 'User logged in', '172.24.16.1', '2024-07-31 13:08:06'),
	(9, 1, 'User logged in', '172.24.16.1', '2024-07-31 13:16:27'),
	(10, 1, 'User logged in', '172.24.16.1', '2024-07-31 13:17:53'),
	(11, 1, 'User logged in', '172.24.16.1', '2024-07-31 13:19:05'),
	(12, 1, 'User logged in', '172.24.16.1', '2024-07-31 13:20:00'),
	(13, 1, 'User logged in', '172.24.16.1', '2024-07-31 13:21:28'),
	(14, 1, 'User logged in', '172.24.16.1', '2024-07-31 13:23:12'),
	(15, 1, 'User logged in', '172.24.16.1', '2024-07-31 13:23:39'),
	(16, 1, 'User logged in', '172.24.16.1', '2024-07-31 14:21:07'),
	(17, 1, 'User logged in', '172.24.16.1', '2024-07-31 14:23:25'),
	(18, 1, 'User logged in', '172.24.16.1', '2024-07-31 14:24:06'),
	(19, 1, 'User logged in', '172.24.16.1', '2024-07-31 14:31:39'),
	(20, 1, 'User logged in', '172.24.16.1', '2024-07-31 15:08:33'),
	(21, 1, 'User logged in', '172.24.16.1', '2024-07-31 15:26:46'),
	(22, 1, 'User logged in', '172.24.16.1', '2024-07-31 15:36:26'),
	(23, 1, 'User logged in', '172.24.16.1', '2024-07-31 19:55:08'),
	(24, 1, 'User logged in', '172.24.16.1', '2024-08-01 10:02:34'),
	(25, 1, 'User logged in', '172.24.16.1', '2024-08-01 10:05:48'),
	(26, 1, 'User logged in', '172.24.16.1', '2024-08-01 10:13:11'),
	(27, 1, 'User logged in', '172.24.16.1', '2024-08-01 10:18:21'),
	(28, 1, 'User logged in', '172.24.16.1', '2024-08-01 10:31:35'),
	(29, 1, 'User logged in', '172.24.16.1', '2024-08-01 10:33:01'),
	(30, 1, 'User logged in', '172.24.16.1', '2024-08-01 10:38:39'),
	(31, 1, 'User logged in', '172.24.16.1', '2024-08-01 10:40:06'),
	(32, 1, 'User logged in', '172.24.16.1', '2024-08-02 07:47:47'),
	(33, 1, 'User logged in', '172.24.16.1', '2024-08-02 07:53:24'),
	(34, 1, 'User logged in', '172.24.16.1', '2024-08-02 07:54:40'),
	(35, 1, 'User logged in', '172.24.16.1', '2024-08-02 07:55:30'),
	(36, 1, 'User logged in', '172.24.16.1', '2024-08-02 08:28:57'),
	(37, 1, 'User logged in', '172.24.16.1', '2024-08-02 08:32:12'),
	(38, 1, 'User logged in', '172.24.16.1', '2024-08-02 08:33:50'),
	(39, 1, 'User logged in', '172.24.16.1', '2024-08-02 08:47:34'),
	(40, 1, 'User logged in', '172.24.16.1', '2024-08-02 08:49:01'),
	(41, 1, 'User logged in', '172.24.16.1', '2024-08-02 08:57:47'),
	(42, 1, 'User logged in', '172.24.16.1', '2024-08-02 08:59:18'),
	(43, 1, 'User logged in', '172.24.16.1', '2024-08-02 09:04:04'),
	(44, 1, 'User logged in', '172.24.16.1', '2024-08-02 09:11:07'),
	(45, 1, 'User logged in', '172.24.16.1', '2024-08-02 09:13:25'),
	(46, 1, 'User logged in', '172.24.16.1', '2024-08-02 09:14:16'),
	(47, 1, 'User logged in', '172.24.16.1', '2024-08-02 09:16:04'),
	(48, 1, 'User logged in', '172.24.16.1', '2024-08-02 09:18:02'),
	(49, 1, 'User logged in', '172.24.16.1', '2024-08-02 09:23:08'),
	(50, 1, 'User logged in', '172.24.16.1', '2024-08-02 09:25:13'),
	(51, 1, 'User logged in', '172.24.16.1', '2024-08-02 14:45:20'),
	(52, 1, 'User logged in', '172.24.16.1', '2024-08-02 14:52:29'),
	(53, 1, 'User logged in', '172.24.16.1', '2024-08-02 14:55:02'),
	(54, 1, 'User logged in', '172.24.16.1', '2024-08-02 14:57:07'),
	(55, 1, 'User logged in', '172.24.16.1', '2024-08-02 17:22:08'),
	(56, 1, 'User logged in', '172.24.16.1', '2024-08-02 17:24:04'),
	(57, 1, 'User logged in', '172.24.16.1', '2024-08-03 06:44:47'),
	(58, 1, 'User logged in', '172.24.16.1', '2024-08-03 06:49:07'),
	(59, 1, 'User logged in', '172.24.16.1', '2024-08-03 06:52:29'),
	(60, 1, 'User logged in', '172.24.16.1', '2024-08-03 06:54:09'),
	(61, 1, 'User logged in', '172.24.16.1', '2024-08-03 06:56:02'),
	(62, 1, 'User logged in', '172.24.16.1', '2024-08-03 07:02:11'),
	(63, 1, 'User logged in', '172.24.16.1', '2024-08-03 07:03:54'),
	(64, 1, 'User logged in', '172.24.16.1', '2024-08-03 07:22:53'),
	(65, 1, 'User logged in', '172.24.16.1', '2024-08-03 07:24:00'),
	(66, 1, 'User logged in', '172.24.16.1', '2024-08-03 07:31:38'),
	(67, 1, 'User logged in', '172.24.16.1', '2024-08-03 07:37:41'),
	(68, 1, 'User logged in', '172.24.16.1', '2024-08-03 07:40:06'),
	(69, 1, 'User logged in', '172.24.16.1', '2024-08-03 07:41:41'),
	(70, 1, 'User logged in', '172.24.16.1', '2024-08-03 07:46:49'),
	(71, 1, 'User logged in', '172.24.16.1', '2024-08-03 07:53:06'),
	(72, 1, 'User logged in', '172.24.16.1', '2024-08-03 07:55:47'),
	(73, 1, 'User logged in', '172.24.16.1', '2024-08-03 07:57:46'),
	(74, 1, 'User logged in', '172.24.16.1', '2024-08-03 08:00:37'),
	(75, 1, 'User logged in', '172.24.16.1', '2024-08-03 08:06:13'),
	(76, 1, 'User logged in', '172.24.16.1', '2024-08-03 08:50:07'),
	(77, 1, 'User logged in', '172.24.16.1', '2024-08-03 08:55:01'),
	(78, 1, 'User logged in', '172.24.16.1', '2024-08-03 08:55:54'),
	(79, 1, 'User logged in', '172.24.16.1', '2024-08-03 09:24:51'),
	(80, 1, 'User logged in', '172.24.16.1', '2024-08-03 09:26:00'),
	(81, 1, 'User logged in', '172.24.16.1', '2024-08-03 09:51:03'),
	(82, 1, 'User logged in', '172.24.16.1', '2024-08-03 10:11:19'),
	(83, 1, 'User logged in', '172.24.16.1', '2024-08-03 14:52:01'),
	(84, 1, 'User logged in', '172.24.16.1', '2024-08-03 14:53:40'),
	(85, 1, 'User logged in', '172.24.16.1', '2024-08-03 15:07:49'),
	(86, 1, 'User logged in', '172.24.16.1', '2024-08-03 15:09:22'),
	(87, 1, 'User logged in', '172.24.16.1', '2024-08-03 15:11:09'),
	(88, 1, 'User logged in', '172.24.16.1', '2024-08-03 15:13:13'),
	(89, 1, 'User logged in', '172.24.16.1', '2024-08-03 15:25:40'),
	(90, 1, 'User logged in', '172.24.16.1', '2024-08-03 20:47:08'),
	(91, 1, 'User logged in', '172.24.16.1', '2024-08-04 06:21:02'),
	(92, 1, 'User logged in', '172.24.16.1', '2024-08-04 07:37:02'),
	(93, 1, 'User logged in', '172.24.16.1', '2024-08-04 08:09:15'),
	(94, 1, 'User logged in', '172.24.16.1', '2024-08-04 08:10:34'),
	(95, 1, 'User logged in', '172.24.16.1', '2024-08-04 08:16:16'),
	(96, 1, 'User logged in', '172.24.16.1', '2024-08-04 08:20:02'),
	(97, 1, 'User logged in', '172.24.16.1', '2024-08-04 08:22:42'),
	(98, 1, 'User logged in', '172.24.16.1', '2024-08-04 08:25:40'),
	(99, 1, 'User logged in', '172.24.16.1', '2024-08-04 09:04:27'),
	(100, 1, 'User logged in', '172.24.16.1', '2024-08-04 09:11:46'),
	(101, 1, 'User logged in', '172.24.16.1', '2024-08-04 09:13:31'),
	(102, 1, 'User logged in', '172.24.16.1', '2024-08-04 09:23:14'),
	(103, 1, 'User logged in', '172.24.16.1', '2024-08-04 09:39:36'),
	(104, 1, 'User logged in', '172.24.16.1', '2024-08-04 09:45:30'),
	(105, 1, 'User logged in', '172.24.16.1', '2024-08-04 09:46:50'),
	(106, 1, 'User logged in', '172.24.16.1', '2024-08-04 09:49:12'),
	(107, 1, 'User logged in', '172.24.16.1', '2024-08-04 10:03:33'),
	(108, 1, 'User logged in', '172.24.16.1', '2024-08-04 10:18:04'),
	(109, 1, 'User logged in', '172.24.16.1', '2024-08-04 10:21:20'),
	(110, 1, 'User logged in', '172.24.16.1', '2024-08-04 14:33:15'),
	(111, 1, 'User logged in', '172.24.16.1', '2024-08-04 15:02:46'),
	(112, 1, 'User logged in', '172.24.16.1', '2024-08-04 15:26:14'),
	(113, 1, 'User logged in', '172.24.16.1', '2024-08-04 15:27:46'),
	(114, 1, 'User logged in', '172.24.16.1', '2024-08-04 15:29:42'),
	(115, 1, 'User logged in', '172.24.16.1', '2024-08-04 15:30:47'),
	(116, 1, 'User logged in', '172.24.16.1', '2024-08-04 15:33:20'),
	(117, 1, 'User logged in', '172.24.16.1', '2024-08-04 15:36:53'),
	(118, 1, 'User logged in', '172.24.16.1', '2024-08-04 15:40:02'),
	(119, 1, 'User logged in', '172.24.16.1', '2024-08-04 15:41:04'),
	(120, 1, 'User logged in', '172.24.16.1', '2024-08-04 15:41:37'),
	(121, 1, 'User logged in', '172.24.16.1', '2024-08-04 15:44:36'),
	(122, 1, 'User logged in', '172.24.16.1', '2024-08-04 15:57:58'),
	(123, 1, 'User logged in', '172.24.16.1', '2024-08-04 15:59:04'),
	(124, 1, 'User logged in', '172.24.16.1', '2024-08-04 16:00:13'),
	(125, 1, 'User logged in', '172.24.16.1', '2024-08-04 16:02:09'),
	(126, 1, 'User logged in', '172.24.16.1', '2024-08-04 16:02:51'),
	(127, 1, 'User logged in', '172.24.16.1', '2024-08-04 16:04:35'),
	(128, 1, 'User logged in', '172.24.16.1', '2024-08-04 21:12:18'),
	(129, 1, 'User logged in', '172.24.16.1', '2024-08-04 21:13:08'),
	(130, 1, 'User logged in', '172.24.16.1', '2024-08-04 21:16:03'),
	(131, 1, 'User logged in', '172.24.16.1', '2024-08-06 08:07:01'),
	(132, 1, 'User logged in', '172.24.16.1', '2024-08-06 08:11:45'),
	(133, 1, 'User logged in', '172.24.16.1', '2024-08-06 08:17:17'),
	(134, 1, 'User logged in', '172.24.16.1', '2024-08-06 08:23:33'),
	(135, 1, 'User logged in', '172.24.16.1', '2024-08-06 08:34:24'),
	(136, 1, 'User logged in', '172.24.16.1', '2024-08-06 12:23:01'),
	(137, 1, 'User logged in', '172.24.16.1', '2024-08-06 12:40:03'),
	(138, 1, 'User logged in', '172.24.16.1', '2024-08-06 12:43:01'),
	(139, 1, 'User logged in', '172.24.16.1', '2024-08-06 12:46:40'),
	(140, 1, 'User logged in', '172.24.16.1', '2024-08-06 12:52:18'),
	(141, 1, 'User logged in', '172.24.16.1', '2024-08-06 12:52:55'),
	(142, 1, 'User logged in', '172.24.16.1', '2024-08-06 12:56:36'),
	(143, 1, 'User logged in', '172.24.16.1', '2024-08-06 12:59:09'),
	(144, 1, 'User logged in', '172.24.16.1', '2024-08-06 13:00:59'),
	(145, 1, 'User logged in', '172.24.16.1', '2024-08-06 13:02:48'),
	(146, 1, 'User logged in', '172.24.16.1', '2024-08-06 13:07:00'),
	(147, 1, 'User logged in', '172.24.16.1', '2024-08-06 13:07:41'),
	(148, 1, 'User logged in', '172.24.16.1', '2024-08-06 13:11:12'),
	(149, 1, 'User logged in', '172.24.16.1', '2024-08-06 13:17:54'),
	(150, 1, 'User logged in', '172.24.16.1', '2024-08-06 13:20:00'),
	(151, 1, 'User logged in', '172.24.16.1', '2024-08-06 13:22:18'),
	(152, 1, 'User logged in', '172.24.16.1', '2024-08-06 13:24:01'),
	(153, 1, 'User logged in', '172.24.16.1', '2024-08-06 13:24:43');

-- Dumping structure for view payroll_system.vw_active_employees
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `vw_active_employees` (
	`EmployeeNumber` INT(10) NOT NULL COMMENT 'Unique identifier for each employee.',
	`LastName` VARCHAR(100) NOT NULL COMMENT 'Last name of the employee' COLLATE 'utf8mb4_unicode_ci',
	`FirstName` VARCHAR(100) NOT NULL COMMENT 'First name of the employee' COLLATE 'utf8mb4_unicode_ci',
	`PositionID` INT(10) NOT NULL COMMENT 'The position id which contains position name for the employee.',
	`PositionName` VARCHAR(100) NOT NULL COMMENT 'Name of the position.' COLLATE 'utf8mb4_unicode_ci',
	`DepartmentName` VARCHAR(100) NOT NULL COMMENT 'Name of the department' COLLATE 'utf8mb4_0900_ai_ci'
) ENGINE=MyISAM;

-- Dumping structure for view payroll_system.vw_active_users
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `vw_active_users` (
  `UserID` INT(10) NOT NULL,
  `Username` VARCHAR(50) NOT NULL,
  `Email` VARCHAR(100) NOT NULL,
  `FullName` VARCHAR(100) NOT NULL,
  `RoleName` VARCHAR(50) NOT NULL,
  `Status` VARCHAR(255) NOT NULL
) ENGINE=MyISAM;

-- Dumping structure for view payroll_system.vw_department_employee_count
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `vw_department_employee_count` (
	`DepartmentID` INT(10) NOT NULL,
	`DepartmentName` VARCHAR(100) NOT NULL COMMENT 'Name of the department' COLLATE 'utf8mb4_0900_ai_ci',
	`EmployeeCount` BIGINT(19) NOT NULL
) ENGINE=MyISAM;

-- Dumping structure for view payroll_system.vw_employee_details
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `vw_employee_details` (
	`EmployeeNumber` INT(10) NOT NULL COMMENT 'Unique identifier for each employee.',
	`LastName` VARCHAR(100) NOT NULL COMMENT 'Last name of the employee' COLLATE 'utf8mb4_unicode_ci',
	`FirstName` VARCHAR(100) NOT NULL COMMENT 'First name of the employee' COLLATE 'utf8mb4_unicode_ci',
	`Birthday` DATE NOT NULL COMMENT 'Birth date of the employee',
	`Address` VARCHAR(255) NOT NULL COMMENT 'Address of the employee' COLLATE 'utf8mb4_unicode_ci',
	`PhoneNumber` VARCHAR(20) NOT NULL COMMENT 'Active Phone number of the employee' COLLATE 'utf8mb4_unicode_ci',
	`Status` VARCHAR(255) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`PositionName` VARCHAR(100) NOT NULL COMMENT 'Name of the position.' COLLATE 'utf8mb4_unicode_ci',
	`DepartmentName` VARCHAR(100) NOT NULL COMMENT 'Name of the department' COLLATE 'utf8mb4_0900_ai_ci'
) ENGINE=MyISAM;

-- Dumping structure for view payroll_system.vw_employee_leave_balance
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `vw_employee_leave_balance` (
	`EmployeeID` INT(10) NOT NULL COMMENT 'The owner of the leave balance',
	`FirstName` VARCHAR(100) NOT NULL COMMENT 'First name of the employee' COLLATE 'utf8mb4_unicode_ci',
	`LastName` VARCHAR(100) NOT NULL COMMENT 'Last name of the employee' COLLATE 'utf8mb4_unicode_ci',
	`LeaveTypeName` VARCHAR(50) NULL COMMENT 'Name of the leave type' COLLATE 'utf8mb4_0900_ai_ci',
	`Balance` INT(10) NOT NULL COMMENT 'The balance leave left of the employee.'
) ENGINE=MyISAM;

-- Dumping structure for view payroll_system.vw_employee_payslip_details_view
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `vw_employee_payslip_details_view` (
	`PayslipID` INT(10) NOT NULL,
	`EmployeeID` INT(10) NOT NULL COMMENT 'The ower of the payslip',
	`CONCAT(e.FirstName, ' ',e.LastName)` VARCHAR(201) NULL COLLATE 'utf8mb4_unicode_ci',
	`PeriodStartDate` DATE NOT NULL COMMENT 'Start pay date of the payslip',
	`PeriodEndDate` DATE NOT NULL COMMENT 'End pay date of the payslip.',
	`GrossIncome` DECIMAL(18,4) NOT NULL COMMENT 'Total gross income earned by the employee.',
	`TotalBenefits` DECIMAL(18,4) NOT NULL COMMENT 'Total benefits received by the employee.',
	`TotalDeductions` DECIMAL(18,4) NOT NULL COMMENT 'Total deductions from the employee\'s earnings.',
	`NetPay` DECIMAL(18,4) NOT NULL COMMENT 'Net pay received by the employee after deductions.',
	`PayrollRunDate` DATE NULL COMMENT 'Date when the payroll run was executed.',
	`TransactionType` VARCHAR(50) NULL COMMENT 'Type of transaction (e.g., deductions, bonuses).' COLLATE 'utf8mb4_0900_ai_ci',
	`TransactionAmount` DECIMAL(18,4) NULL COMMENT 'Amount of the transaction.',
	`TransactionDate` DATE NULL COMMENT 'Date of the transaction.'
) ENGINE=MyISAM;

-- Dumping structure for view payroll_system.vw_employee_timesheet
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `vw_employee_timesheet` (
	`TimesheetID` INT(10) NOT NULL COMMENT 'Unique identifier for each timesheet entry.',
	`EmployeeID` INT(10) NOT NULL COMMENT 'Identifier of the employee associated with this timesheet entry.',
	`FirstName` VARCHAR(100) NOT NULL COMMENT 'First name of the employee' COLLATE 'utf8mb4_unicode_ci',
	`LastName` VARCHAR(100) NOT NULL COMMENT 'Last name of the employee' COLLATE 'utf8mb4_unicode_ci',
	`Date` DATE NOT NULL COMMENT 'Date of the timesheet entry.',
	`TimeIn` TIME NOT NULL COMMENT 'Time when the employee started work.',
	`TimeOut` TIME NULL COMMENT 'Time when the employee finished work, if available.',
	`HoursWorked` DECIMAL(8,2) NULL COMMENT 'Automatically calculated hours worked based on TimeIn and TimeOut, capped between 08:00:00 and 17:00:00.',
	`Remarks` VARCHAR(255) NULL COMMENT 'Optional remarks or notes related to the timesheet entry.' COLLATE 'utf8mb4_0900_ai_ci'
) ENGINE=MyISAM;

-- Dumping structure for view payroll_system.vw_leave_requests
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `vw_leave_requests` (
	`LeaveRequestID` INT(10) NOT NULL COMMENT 'Unique identifier for each leave request.',
	`EmployeeID` INT(10) NOT NULL COMMENT 'Owner of the leave request.',
	`FirstName` VARCHAR(100) NOT NULL COMMENT 'First name of the employee' COLLATE 'utf8mb4_unicode_ci',
	`LastName` VARCHAR(100) NOT NULL COMMENT 'Last name of the employee' COLLATE 'utf8mb4_unicode_ci',
	`DateRequested` DATETIME NOT NULL COMMENT 'The creation date of the leave request.',
	`StartDate` DATE NULL COMMENT 'The start date of the leave.',
	`EndDate` DATE NULL COMMENT 'The end date of the leave request.',
	`Status` TINYTEXT NOT NULL COMMENT 'The status of the leave request.' COLLATE 'utf8mb4_0900_ai_ci'
) ENGINE=MyISAM;

-- Dumping structure for view payroll_system.vw_monthly_salary_costs
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `vw_monthly_salary_costs` (
	`PeriodStartDate` DATE NOT NULL COMMENT 'Start pay date of the payslip',
	`PeriodEndDate` DATE NOT NULL COMMENT 'End pay date of the payslip.',
	`TotalSalaryCost` DECIMAL(40,4) NULL
) ENGINE=MyISAM;

-- Dumping structure for view payroll_system.vw_overtime_requests
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `vw_overtime_requests` (
	`RequestID` INT(10) NOT NULL COMMENT 'Unique identifier for each overtime request.',
	`EmployeeID` INT(10) NOT NULL COMMENT 'Identifier for the employee making the overtime request.',
	`FirstName` VARCHAR(100) NOT NULL COMMENT 'First name of the employee' COLLATE 'utf8mb4_unicode_ci',
	`LastName` VARCHAR(100) NOT NULL COMMENT 'Last name of the employee' COLLATE 'utf8mb4_unicode_ci',
	`DateRequested` TIMESTAMP NOT NULL COMMENT 'Timestamp when the overtime request was submitted.',
	`Date` DATE NOT NULL COMMENT 'Date for which the overtime is requested.',
	`HoursRequested` DECIMAL(8,4) NOT NULL COMMENT 'Number of hours requested for overtime.',
	`Status` TINYTEXT NOT NULL COMMENT 'Current status of the overtime request.' COLLATE 'utf8mb4_0900_ai_ci',
	`HR Remarks` VARCHAR(255) NULL COMMENT 'Remarks or notes from the administrator handling the request.' COLLATE 'utf8mb4_0900_ai_ci'
) ENGINE=MyISAM;

-- Dumping structure for view payroll_system.vw_payroll_changes_history_view
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `vw_payroll_changes_history_view` (
	`ChangeID` INT(10) NOT NULL COMMENT 'Unique identifier for each payroll change entry.',
	`PayrollID` INT(10) NOT NULL COMMENT 'Identifier linking to the payroll affected by the change.',
	`ModifiedBy` VARCHAR(100) NOT NULL COMMENT 'User who made the modification.' COLLATE 'utf8mb4_0900_ai_ci',
	`ModificationDate` TIMESTAMP NOT NULL COMMENT 'Timestamp when the modification was made.',
	`FieldChanged` VARCHAR(100) NOT NULL COMMENT 'Name of the field changed in the payroll.' COLLATE 'utf8mb4_0900_ai_ci',
	`OldValue` TINYTEXT NULL COMMENT 'Previous value of the changed field.' COLLATE 'utf8mb4_0900_ai_ci',
	`NewValue` TINYTEXT NULL COMMENT 'New value of the changed field.' COLLATE 'utf8mb4_0900_ai_ci',
	`PayrollRunDate` DATE NULL COMMENT 'Date when the payroll run was executed.'
) ENGINE=MyISAM;

-- Dumping structure for view payroll_system.vw_payroll_summary_view
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `vw_payroll_summary_view` (
	`PayrollID` INT(10) NOT NULL COMMENT 'Unique identifier for each payroll run.',
	`PayrollRunDate` DATE NOT NULL COMMENT 'Date when the payroll run was executed.',
	`NumberOfEmployees` BIGINT(19) NOT NULL,
	`TotalGrossIncome` DECIMAL(40,4) NULL,
	`TotalBenefits` DECIMAL(40,4) NULL,
	`TotalDeductions` DECIMAL(40,4) NULL,
	`TotalNetPay` DECIMAL(40,4) NULL,
	`NumberOfPayslips` BIGINT(19) NOT NULL,
	`NumberOfChanges` BIGINT(19) NOT NULL
) ENGINE=MyISAM;

-- Dumping structure for view payroll_system.vw_payslip_summary
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `vw_payslip_summary` (
	`PayslipID` INT(10) NOT NULL,
	`PayslipNumber` VARCHAR(20) NOT NULL COMMENT 'Unique identifier or number for the payslip.' COLLATE 'utf8mb4_0900_ai_ci',
	`EmployeeID` INT(10) NOT NULL COMMENT 'The ower of the payslip',
	`PeriodStartDate` DATE NOT NULL COMMENT 'Start pay date of the payslip',
	`PeriodEndDate` DATE NOT NULL COMMENT 'End pay date of the payslip.',
	`EmployeeName` VARCHAR(100) NOT NULL COMMENT 'Name of the employee.' COLLATE 'utf8mb4_0900_ai_ci',
	`EmployeePosition` VARCHAR(100) NOT NULL COMMENT 'Position or job title of the employee.' COLLATE 'utf8mb4_0900_ai_ci',
	`GrossIncome` DECIMAL(18,4) NOT NULL COMMENT 'Total gross income earned by the employee.',
	`TotalBenefits` DECIMAL(18,4) NOT NULL COMMENT 'Total benefits received by the employee.',
	`TotalDeductions` DECIMAL(18,4) NOT NULL COMMENT 'Total deductions from the employee\'s earnings.',
	`NetPay` DECIMAL(18,4) NOT NULL COMMENT 'Net pay received by the employee after deductions.'
) ENGINE=MyISAM;

-- Dumping structure for view payroll_system.vw_reimbursement_summary_view
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `vw_reimbursement_summary_view` (
	`RequestID` INT(10) NOT NULL COMMENT 'Unique identifier for each reimbursement request.',
	`EmployeeNumber` INT(10) NOT NULL COMMENT 'Identifier of the employee submitting the reimbursement request.',
	`EmployeeName` VARCHAR(201) NULL COLLATE 'utf8mb4_unicode_ci',
	`RequestDate` DATE NOT NULL COMMENT 'Date when the reimbursement request was submitted.',
	`Amount` DECIMAL(18,4) NOT NULL COMMENT 'Amount requested for reimbursement.',
	`Status` VARCHAR(50) NOT NULL COMMENT 'Current status of the reimbursement request.' COLLATE 'utf8mb4_0900_ai_ci',
	`ApprovedBy` INT(10) NULL COMMENT 'Employee ID of the approver who approved the reimbursement request.',
	`ApprovedDate` DATETIME NULL COMMENT 'Date and time when the reimbursement request was approved.',
	`ProcessedBy` INT(10) NULL COMMENT 'Employee ID of the processor who processed the reimbursement request.',
	`ProcessedDate` DATETIME NULL COMMENT 'Date and time when the reimbursement request was processed.',
	`TransactionID` INT(10) NULL COMMENT 'Unique identifier for each reimbursement transaction.',
	`TransactionDate` DATE NULL COMMENT 'Date when the reimbursement transaction took place.',
	`TransactionAmount` DECIMAL(18,4) NULL COMMENT 'Amount of money involved in the reimbursement transaction.',
	`PaidTo` VARCHAR(100) NULL COMMENT 'Recipient or entity to whom the reimbursement was paid.' COLLATE 'utf8mb4_0900_ai_ci',
	`PaymentMethod` VARCHAR(50) NULL COMMENT 'Method used for the reimbursement payment.' COLLATE 'utf8mb4_0900_ai_ci',
	`Details` TINYTEXT NULL COLLATE 'utf8mb4_unicode_ci'
) ENGINE=MyISAM;

-- Dumping structure for view payroll_system.vw_timesheet_summary_view
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `vw_timesheet_summary_view` (
	`TimesheetID` INT(10) NOT NULL COMMENT 'Unique identifier for each timesheet entry.',
	`EmployeeID` INT(10) NOT NULL COMMENT 'Identifier of the employee associated with this timesheet entry.',
	`EmployeeName` VARCHAR(201) NULL COLLATE 'utf8mb4_unicode_ci',
	`Date` DATE NOT NULL COMMENT 'Date of the timesheet entry.',
	`HoursWorked` DECIMAL(8,2) NULL COMMENT 'Automatically calculated hours worked based on TimeIn and TimeOut, capped between 08:00:00 and 17:00:00.',
	`OvertimeHours` DECIMAL(30,4) NOT NULL,
	`Status` VARCHAR(20) NOT NULL COLLATE 'utf8mb4_0900_ai_ci'
) ENGINE=MyISAM;

-- Dumping structure for view payroll_system.vw_yearly_payroll_summary_view
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `vw_yearly_payroll_summary_view` (
	`Year` INT(10) NULL,
	`NumberOfPayrolls` BIGINT(19) NOT NULL,
	`TotalEmployees` DECIMAL(42,0) NULL,
	`TotalGrossIncome` DECIMAL(40,4) NULL,
	`TotalBenefits` DECIMAL(40,4) NULL,
	`TotalDeductions` DECIMAL(40,4) NULL,
	`TotalNetPay` DECIMAL(40,4) NULL,
	`TotalPayslips` BIGINT(19) NOT NULL,
	`TotalChanges` BIGINT(19) NOT NULL
) ENGINE=MyISAM;

-- Dumping structure for view payroll_system.vw_active_employees
-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `vw_active_employees`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `vw_active_employees` AS select `e`.`EmployeeNumber` AS `EmployeeNumber`,`e`.`LastName` AS `LastName`,`e`.`FirstName` AS `FirstName`,`e`.`PositionID` AS `PositionID`,`p`.`PositionName` AS `PositionName`,`d`.`DepartmentName` AS `DepartmentName` from ((`employee` `e` join `position` `p` on((`e`.`PositionID` = `p`.`PositionID`))) join `department` `d` on((`p`.`DepartmentID` = `d`.`DepartmentID`))) where (`e`.`Status` in ('Regular','Contractual','Probationary'));

-- Dumping structure for view payroll_system.vw_active_users
-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `vw_active_users`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `vw_active_users` AS
select
  `u`.`UserID` AS `UserID`,
  `u`.`Username` AS `Username`,
  `u`.`Email` AS `Email`,
  `u`.`FullName` AS `FullName`,
  `r`.`RoleName` AS `RoleName`,
  `u`.`Status` AS `Status`
from (`user` `u` join `role` `r` on((`u`.`RoleID` = `r`.`RoleID`)))
where (`u`.`Status` = 'Active');

-- Dumping structure for view payroll_system.vw_department_employee_count
-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `vw_department_employee_count`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `vw_department_employee_count` AS select `d`.`DepartmentID` AS `DepartmentID`,`d`.`DepartmentName` AS `DepartmentName`,count(`e`.`EmployeeNumber`) AS `EmployeeCount` from ((`department` `d` left join `position` `p` on((`d`.`DepartmentID` = `p`.`DepartmentID`))) left join `employee` `e` on((`p`.`PositionID` = `e`.`PositionID`))) group by `d`.`DepartmentID`,`d`.`DepartmentName`;

-- Dumping structure for view payroll_system.vw_employee_details
-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `vw_employee_details`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `vw_employee_details` AS select `e`.`EmployeeNumber` AS `EmployeeNumber`,`e`.`LastName` AS `LastName`,`e`.`FirstName` AS `FirstName`,`e`.`Birthday` AS `Birthday`,`e`.`Address` AS `Address`,`e`.`PhoneNumber` AS `PhoneNumber`,`e`.`Status` AS `Status`,`p`.`PositionName` AS `PositionName`,`d`.`DepartmentName` AS `DepartmentName` from ((`employee` `e` join `position` `p` on((`e`.`PositionID` = `p`.`PositionID`))) join `department` `d` on((`p`.`DepartmentID` = `d`.`DepartmentID`)));

-- Dumping structure for view payroll_system.vw_employee_leave_balance
-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `vw_employee_leave_balance`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `vw_employee_leave_balance` AS select `lb`.`EmployeeID` AS `EmployeeID`,`e`.`FirstName` AS `FirstName`,`e`.`LastName` AS `LastName`,`lt`.`LeaveTypeName` AS `LeaveTypeName`,`lb`.`Balance` AS `Balance` from ((`leave_balance` `lb` join `employee` `e` on((`lb`.`EmployeeID` = `e`.`EmployeeNumber`))) join `leave_request_type` `lt` on((`lb`.`LeaveTypeID` = `lt`.`LeaveTypeID`)));

-- Dumping structure for view payroll_system.vw_employee_payslip_details_view
-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `vw_employee_payslip_details_view`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `vw_employee_payslip_details_view` AS select `ps`.`PayslipID` AS `PayslipID`,`ps`.`EmployeeID` AS `EmployeeID`,concat(`e`.`FirstName`,' ',`e`.`LastName`) AS `CONCAT(e.FirstName, ' ',e.LastName)`,`ps`.`PeriodStartDate` AS `PeriodStartDate`,`ps`.`PeriodEndDate` AS `PeriodEndDate`,`ps`.`GrossIncome` AS `GrossIncome`,`ps`.`TotalBenefits` AS `TotalBenefits`,`ps`.`TotalDeductions` AS `TotalDeductions`,`ps`.`NetPay` AS `NetPay`,`ps`.`PayrollRunDate` AS `PayrollRunDate`,`pt`.`TransactionType` AS `TransactionType`,`pt`.`TransactionAmount` AS `TransactionAmount`,`pt`.`TransactionDate` AS `TransactionDate` from ((`payslip` `ps` left join `payroll_transactions` `pt` on(((`ps`.`PayrollID` = `pt`.`PayrollID`) and (`ps`.`PayslipID` = `pt`.`PayslipID`)))) left join `employee` `e` on((`ps`.`EmployeeID` = `e`.`EmployeeNumber`)));

-- Dumping structure for view payroll_system.vw_employee_timesheet
-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `vw_employee_timesheet`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `vw_employee_timesheet` AS select `t`.`TimesheetID` AS `TimesheetID`,`t`.`EmployeeID` AS `EmployeeID`,`e`.`FirstName` AS `FirstName`,`e`.`LastName` AS `LastName`,`t`.`Date` AS `Date`,`t`.`TimeIn` AS `TimeIn`,`t`.`TimeOut` AS `TimeOut`,`t`.`HoursWorked` AS `HoursWorked`,`t`.`Remarks` AS `Remarks` from (`timesheet` `t` join `employee` `e` on((`t`.`EmployeeID` = `e`.`EmployeeNumber`)));

-- Dumping structure for view payroll_system.vw_leave_requests
-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `vw_leave_requests`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `vw_leave_requests` AS select `lr`.`LeaveRequestID` AS `LeaveRequestID`,`lr`.`EmployeeID` AS `EmployeeID`,`e`.`FirstName` AS `FirstName`,`e`.`LastName` AS `LastName`,`lr`.`DateRequested` AS `DateRequested`,`lr`.`StartDate` AS `StartDate`,`lr`.`EndDate` AS `EndDate`,`lr`.`Status` AS `Status` from (`leave_request` `lr` join `employee` `e` on((`lr`.`EmployeeID` = `e`.`EmployeeNumber`)));

-- Dumping structure for view payroll_system.vw_monthly_salary_costs
-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `vw_monthly_salary_costs`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `vw_monthly_salary_costs` AS select `p`.`PeriodStartDate` AS `PeriodStartDate`,`p`.`PeriodEndDate` AS `PeriodEndDate`,sum(`p`.`NetPay`) AS `TotalSalaryCost` from `payslip` `p` group by `p`.`PeriodStartDate`,`p`.`PeriodEndDate`;

-- Dumping structure for view payroll_system.vw_overtime_requests
-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `vw_overtime_requests`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `vw_overtime_requests` AS select `o`.`RequestID` AS `RequestID`,`o`.`EmployeeID` AS `EmployeeID`,`e`.`FirstName` AS `FirstName`,`e`.`LastName` AS `LastName`,`o`.`DateRequested` AS `DateRequested`,`o`.`OvertimeDate` AS `Date`,`o`.`HoursRequested` AS `HoursRequested`,`o`.`Status` AS `Status`,`o`.`HRRemarks` AS `HR Remarks` from (`overtime_request` `o` join `employee` `e` on((`o`.`EmployeeID` = `e`.`EmployeeNumber`)));

-- Dumping structure for view payroll_system.vw_payroll_changes_history_view
-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `vw_payroll_changes_history_view`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `vw_payroll_changes_history_view` AS select `pc`.`ChangeID` AS `ChangeID`,`pc`.`PayrollID` AS `PayrollID`,`pc`.`ModifiedBy` AS `ModifiedBy`,`pc`.`ModificationDate` AS `ModificationDate`,`pc`.`FieldChanged` AS `FieldChanged`,`pc`.`OldValue` AS `OldValue`,`pc`.`NewValue` AS `NewValue`,`p`.`PayrollRunDate` AS `PayrollRunDate` from (`payroll_changes` `pc` left join `payroll` `p` on((`pc`.`PayrollID` = `p`.`PayrollID`)));

-- Dumping structure for view payroll_system.vw_payroll_summary_view
-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `vw_payroll_summary_view`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `vw_payroll_summary_view` AS select `p`.`PayrollID` AS `PayrollID`,`p`.`PayrollRunDate` AS `PayrollRunDate`,count(distinct `ps`.`EmployeeID`) AS `NumberOfEmployees`,sum(`ps`.`GrossIncome`) AS `TotalGrossIncome`,sum(`ps`.`TotalBenefits`) AS `TotalBenefits`,sum(`ps`.`TotalDeductions`) AS `TotalDeductions`,sum(`ps`.`NetPay`) AS `TotalNetPay`,count(distinct `pt`.`PayslipID`) AS `NumberOfPayslips`,count(distinct `pc`.`ChangeID`) AS `NumberOfChanges` from (((`payroll` `p` left join `payslip` `ps` on((`p`.`PayrollID` = `ps`.`PayrollID`))) left join `payroll_transactions` `pt` on((`p`.`PayrollID` = `pt`.`PayrollID`))) left join `payroll_changes` `pc` on((`p`.`PayrollID` = `pc`.`PayrollID`))) group by `p`.`PayrollID`,`p`.`PayrollRunDate`;

-- Dumping structure for view payroll_system.vw_payslip_summary
-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `vw_payslip_summary`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `vw_payslip_summary` AS select `p`.`PayslipID` AS `PayslipID`,`p`.`PayslipNumber` AS `PayslipNumber`,`p`.`EmployeeID` AS `EmployeeID`,`p`.`PeriodStartDate` AS `PeriodStartDate`,`p`.`PeriodEndDate` AS `PeriodEndDate`,`p`.`EmployeeName` AS `EmployeeName`,`p`.`EmployeePosition` AS `EmployeePosition`,`p`.`GrossIncome` AS `GrossIncome`,`p`.`TotalBenefits` AS `TotalBenefits`,`p`.`TotalDeductions` AS `TotalDeductions`,`p`.`NetPay` AS `NetPay` from `payslip` `p`;

-- Dumping structure for view payroll_system.vw_reimbursement_summary_view
-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `vw_reimbursement_summary_view`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `vw_reimbursement_summary_view` AS select `rr`.`RequestID` AS `RequestID`,`rr`.`EmployeeNumber` AS `EmployeeNumber`,concat(`e`.`FirstName`,' ',`e`.`LastName`) AS `EmployeeName`,`rr`.`RequestDate` AS `RequestDate`,`rr`.`Amount` AS `Amount`,`rr`.`Status` AS `Status`,`rr`.`ApprovedBy` AS `ApprovedBy`,`rr`.`ApprovedDate` AS `ApprovedDate`,`rr`.`ProcessedBy` AS `ProcessedBy`,`rr`.`ProcessedDate` AS `ProcessedDate`,`rt`.`TransactionID` AS `TransactionID`,`rt`.`TransactionDate` AS `TransactionDate`,`rt`.`Amount` AS `TransactionAmount`,`rt`.`PaidTo` AS `PaidTo`,`rt`.`PaymentMethod` AS `PaymentMethod`,`rt`.`Details` AS `Details` from ((`reimbursement_requests` `rr` left join `reimbursement_transactions` `rt` on((`rr`.`RequestID` = `rt`.`RequestID`))) left join `employee` `e` on((`rr`.`EmployeeNumber` = `e`.`EmployeeNumber`)));

-- Dumping structure for view payroll_system.vw_timesheet_summary_view
-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `vw_timesheet_summary_view`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `vw_timesheet_summary_view` AS select `t`.`TimesheetID` AS `TimesheetID`,`t`.`EmployeeID` AS `EmployeeID`,concat(`e`.`FirstName`,' ',`e`.`LastName`) AS `EmployeeName`,`t`.`Date` AS `Date`,`t`.`HoursWorked` AS `HoursWorked`,ifnull(`o`.`TotalOvertimeHours`,0) AS `OvertimeHours`,`t`.`Status` AS `Status` from ((`timesheet` `t` left join `employee` `e` on((`t`.`EmployeeID` = `e`.`EmployeeNumber`))) left join (select `overtime_request`.`EmployeeID` AS `EmployeeID`,sum(`overtime_request`.`HoursRequested`) AS `TotalOvertimeHours` from `overtime_request` where (`overtime_request`.`Status` = 'Approved') group by `overtime_request`.`EmployeeID`) `o` on((`t`.`EmployeeID` = `o`.`EmployeeID`)));

-- Dumping structure for view payroll_system.vw_yearly_payroll_summary_view
-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `vw_yearly_payroll_summary_view`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `vw_yearly_payroll_summary_view` AS select year(`p`.`PayrollRunDate`) AS `Year`,count(distinct `p`.`PayrollID`) AS `NumberOfPayrolls`,sum(`p2`.`NumberOfEmployees`) AS `TotalEmployees`,sum(`ps`.`GrossIncome`) AS `TotalGrossIncome`,sum(`ps`.`TotalBenefits`) AS `TotalBenefits`,sum(`ps`.`TotalDeductions`) AS `TotalDeductions`,sum(`ps`.`NetPay`) AS `TotalNetPay`,count(`ps`.`PayslipID`) AS `TotalPayslips`,count((case when (`ps`.`LastModifiedBy` is not null) then 1 end)) AS `TotalChanges` from ((`payroll` `p` left join (select `payslip`.`PayrollID` AS `PayrollID`,count(distinct `payslip`.`EmployeeID`) AS `NumberOfEmployees` from `payslip` group by `payslip`.`PayrollID`) `p2` on((`p`.`PayrollID` = `p2`.`PayrollID`))) left join `payslip` `ps` on((`p`.`PayrollID` = `ps`.`PayrollID`))) group by year(`p`.`PayrollRunDate`) order by `Year` desc;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;


-- ---------------------------------------------
-- Multi-role seed (optional)
-- NOTE: Requires user_role table (apply docs/sql/schema-patch-payroll_system.sql)
-- ---------------------------------------------
-- Example: give sysadmin (UserID=5 in data-old.sql) the Employee role too (RoleID=6 in data-old.sql)
-- INSERT IGNORE INTO role_permission (RoleID, PermissionID) values (...); -- permissions are managed via Sync in-app
INSERT IGNORE INTO user_role (UserID, RoleID) VALUES
  (5, 9),  -- System Administrator
  (5, 6);  -- Employee
