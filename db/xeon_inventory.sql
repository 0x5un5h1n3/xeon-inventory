-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.7.29-log - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL Version:             11.1.0.6116
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping data for table xeon_inventory.account: ~2 rows (approximately)
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` (`id`, `username`, `type`, `nic`, `phone_no`, `password`, `account_status`) VALUES
	(0, 'xeondev', 'developer', 'unknown', 'unknown', 'dev@xeon', 'active'),
	(1, 'admin', 'admin', '389274980237', '2329872498', '1234', 'active'),
	(2, 'super', 'super', '324832974972', '4439473984', '1234', 'active');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;

-- Dumping data for table xeon_inventory.employee: ~6 rows (approximately)
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` (`id`, `username`, `position`, `nic`, `phone_no`, `salary`, `password`, `account_status`) VALUES
	(1, 'LEBOuNDi', 'Cashier', '127983268930', '0372983698', 120000, '1234', 'Active'),
	(2, 'OtENtoNO', 'Stock Manager', '728937984329', '0439846398', 112000, '1234', 'Active'),
	(3, 'aMeSPriE', 'Cashier', '323798273923', '0219827987', 144000, '1234', 'Active'),
	(4, 'anTESHIB', 'Cashier', '211321321321', '1280198209', 145000, '1234', 'Active'),
	(5, 'dawavERE', 'Clerk', '809668757647', '0729369823', 198000, '1234', 'Active'),
	(6, 'terthEaR', 'Stock Keeper', '128969869832', '0479847398', 187700, '1234', 'Active'),
	(7, 'emp001', 'Cashier', '403470374037', '8320837473', 120000, '1234', 'Dective');
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;

-- Dumping data for table xeon_inventory.employee_attendance: ~14 rows (approximately)
/*!40000 ALTER TABLE `employee_attendance` DISABLE KEYS */;
INSERT INTO `employee_attendance` (`id`, `username`, `month`, `year`, `date`, `time`, `date/time`) VALUES
	(1, 'LEBOuNDi', 'August', '2020', '2020-08-10', '17:22:38', '2020-08-10 17:22:38'),
	(2, 'OtENtoNO', 'August', '2020', '2020-08-10', '20:23:55', '2020-08-10 20:23:55'),
	(3, 'aMeSPriE', 'August', '2020', '2020-08-10', '20:24:05', '2020-08-10 20:24:05'),
	(4, 'anTESHIB', 'August', '2020', '2020-08-10', '20:24:16', '2020-08-10 20:24:16'),
	(5, 'dawavERE', 'August', '2020', '2020-08-10', '20:24:33', '2020-08-10 20:24:33'),
	(6, 'terthEaR', 'August', '2020', '2020-08-10', '20:24:46', '2020-08-10 20:24:46'),
	(7, 'emp001', 'August', '2020', '2020-08-10', '20:36:04', '2020-08-10 20:36:04'),
	(7, 'emp001', 'August', '2020', '2020-08-11', '15:15:12', '2020-08-11 15:15:12'),
	(1, 'LEBOuNDi', 'September', '2020', '2020-09-09', '08:55:51', '2020-09-09 08:55:51'),
	(1, 'LEBOuNDi', 'September', '2020', '2020-09-11', '12:22:08', '2020-09-11 12:22:08'),
	(1, 'LEBOuNDi', 'September', '2020', '2020-09-24', '09:10:35', '2020-09-24 09:10:35'),
	(1, 'LEBOuNDi', 'September', '2020', '2020-09-29', '19:46:56', '2020-09-29 19:46:56'),
	(1, 'LEBOuNDi', 'September', '2020', '2020-09-30', '09:05:06', '2020-09-30 09:05:06'),
	(1, 'LEBOuNDi', 'October', '2020', '2020-10-01', '12:17:21', '2020-10-01 12:17:21');
/*!40000 ALTER TABLE `employee_attendance` ENABLE KEYS */;

-- Dumping data for table xeon_inventory.gin: ~8 rows (approximately)
/*!40000 ALTER TABLE `gin` DISABLE KEYS */;
INSERT INTO `gin` (`id`, `invoice_id`, `item_id`, `item_code`, `item_name`, `purchase_price`, `selling_price`, `discount`, `new_item_price`, `amount`, `total`, `new_total`, `savings`, `employee`, `profit`, `date`, `month`, `year`, `date/time`) VALUES
	(1, 1, 1, 'imac', 'AppleiMac', 2000, 2200, 5, 2090, 2, 4400, 4180, 220, 'LEBOuNDi', 180, '2020-08-10', 'August', '2020', '2020-08-10 17:27:00'),
	(2, 2, 1, 'imac', 'AppleiMac', 2000, 2200, 0, 2200, 900, 1980000, 1980000, 0, 'emp001', 180000, '2020-08-10', 'August', '2020', '2020-08-10 20:49:30'),
	(3, 3, 3, 'pixel4a', 'GooglePixel4a', 400, 500, 0, 500, 2, 1000, 1000, 0, 'emp001', 200, '2020-08-11', 'August', '2020', '2020-08-11 15:15:24'),
	(4, 4, 3, 'pixel4a', 'GooglePixel4a', 400, 500, 5, 475, 5, 2500, 2375, 125, 'emp001', 375, '2020-08-11', 'August', '2020', '2020-08-11 15:33:24'),
	(5, 5, 2, 'xps13', 'DellXPS13', 800, 1000, 0, 1000, 2, 2000, 2000, 0, 'LEBOuNDi', 400, '2020-09-11', 'September', '2020', '2020-09-11 12:22:57'),
	(6, 6, 2, 'xps13', 'DellXPS13', 800, 1000, 5, 950, 8, 8000, 7600, 400, 'LEBOuNDi', 1200, '2020-09-24', 'September', '2020', '2020-09-24 09:11:56'),
	(7, 7, 5, 'nokia3310', 'Nokia3310', 100, 150, 10, 135, 10, 1500, 1350, 150, 'LEBOuNDi', 350, '2020-09-30', 'September', '2020', '2020-09-30 11:23:19'),
	(8, 8, 2, 'xps13', 'DellXPS13', 800, 1000, 5, 950, 10, 10000, 9500, 500, 'LEBOuNDi', 1500, '2020-10-01', 'October', '2020', '2020-10-01 12:18:52');
/*!40000 ALTER TABLE `gin` ENABLE KEYS */;

-- Dumping data for table xeon_inventory.grn: ~8 rows (approximately)
/*!40000 ALTER TABLE `grn` DISABLE KEYS */;
INSERT INTO `grn` (`id`, `grn_id`, `item_name`, `item_code`, `quantity`, `item_price`, `supplier`, `total`, `lead_time`, `grn_status`, `date/time`) VALUES
	(1, 1, 'AppleiMac', 'imac', 1000, 2000, 'AppleSupplier', 2000000, '2020-08-17', 'Checked', '2020-08-10 17:24:32'),
	(2, 2, 'DellXPS13', 'xps13', 1200, 800, 'DellSupplier', 960000, '2020-08-17', 'Checked', '2020-08-10 20:41:25'),
	(3, 2, 'GooglePixel4a', 'pixel4a', 1500, 400, 'GoogleSupplier', 600000, '2020-08-17', 'Checked', '2020-08-10 20:43:46'),
	(4, 3, 'MicrosoftSurfacePro4', 'surfpro4', 400, 600, 'MicrosoftSupplier', 240000, '2020-08-17', 'Checked', '2020-08-10 20:47:51'),
	(5, 4, 'Nokia3310', 'nokia3310', 2000, 100, 'NokiaSupplier', 200000, '2020-10-07', 'Checked', '2020-09-30 11:18:45'),
	(6, 5, 'Alexa', 'alx', 2000, 100, 'AmazonSupplier', 200000, '2020-10-15', 'Bad Order', '2020-10-01 12:11:55'),
	(7, 6, 'AppleiMacPro', 'imacpro', 1000, 100, 'AppleSupplier', 100000, '2020-10-08', 'Checked', '2020-10-01 12:13:28'),
	(8, 7, 'AppleiMacPro', 'imacpro', 200, 100, 'AppleSupplier', 20000, '2020-10-08', 'Checked', '2020-10-01 12:14:57'),
	(9, 8, 'AppleiMac', 'imac', 1000, 2000, 'AppleSupplier', 2000000, '2020-10-08', 'Unchecked', '2020-10-01 12:15:43');
/*!40000 ALTER TABLE `grn` ENABLE KEYS */;

-- Dumping data for table xeon_inventory.item: ~7 rows (approximately)
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` (`id`, `item_name`, `item_code`, `item_category`, `supplier`, `supplier_id`, `item_status`) VALUES
	(1, 'AppleiMac', 'imac', 'Computers', 'AppleSupplier', 1, 'Available'),
	(2, 'AppleiMacPro', 'imacpro', 'Computers', 'AppleSupplier', 1, 'Available'),
	(3, 'DellXPS13', 'xps13', 'Computers', 'DellSupplier', 2, 'Available'),
	(4, 'GooglePixel4a', 'pixel4a', 'Phones', 'GoogleSupplier', 3, 'Available'),
	(5, 'MicrosoftSurfacePro4', 'surfpro4', 'Computers', 'MicrosoftSupplier', 4, 'Available'),
	(6, 'Nokia3310', 'nokia3310', 'Phones', 'NokiaSupplier', 7, 'Available'),
	(7, 'Alexa', 'alx', 'Home', 'AmazonSupplier', 8, 'Unavailable');
/*!40000 ALTER TABLE `item` ENABLE KEYS */;

-- Dumping data for table xeon_inventory.log: ~67 rows (approximately)
/*!40000 ALTER TABLE `log` DISABLE KEYS */;
INSERT INTO `log` (`id`, `user`, `username`, `activity`, `description`, `state`, `date/time`) VALUES
	(1, 'Admin', 'Admin', 'Admin Login', 'Admin logged in', 'SUCCESS', '2020-09-25 17:32:18'),
	(1, 'Admin', 'Admin', 'Admin Logout', 'Admin logged out', 'SUCCESS', '2020-09-25 17:32:54'),
	(1, 'Admin', 'admin', 'Admin Login', 'admin logged in', 'SUCCESS', '2020-09-29 08:42:17'),
	(1, 'Admin', 'admin', 'Admin System Exit', 'admin system exited', 'SUCCESS', '2020-09-29 08:43:21'),
	(1, 'Admin', 'admin', 'Admin Login', 'admin logged in', 'SUCCESS', '2020-09-29 11:53:55'),
	(1, 'Admin', 'admin', 'Admin System Exit', 'admin system exited', 'SUCCESS', '2020-09-29 11:55:41'),
	(1, 'Admin', 'admin', 'Admin Login', 'admin logged in', 'SUCCESS', '2020-09-29 19:44:45'),
	(1, 'Admin', 'admin', 'Admin Logout', 'admin logged out', 'SUCCESS', '2020-09-29 19:46:28'),
	(1, 'Employee', 'LEBOuNDi', 'Employee Login', 'LEBOuNDi logged in', 'SUCCESS', '2020-09-29 19:46:56'),
	(1, 'Employee', 'LEBOuNDi', 'Employee System Exit', 'LEBOuNDi system exited', 'SUCCESS', '2020-09-29 21:18:22'),
	(0, 'Super User', 'xeondev', 'Super User Login', 'xeondev logged in', 'SUCCESS', '2020-09-30 09:01:28'),
	(0, 'Super User', 'xeondev', 'Super User Logout', 'xeondev logged out', 'SUCCESS', '2020-09-30 09:04:41'),
	(1, 'Employee', 'LEBOuNDi', 'Employee Login', 'LEBOuNDi logged in', 'SUCCESS', '2020-09-30 09:05:06'),
	(1, 'Employee', 'LEBOuNDi', 'Employee System Exit', 'LEBOuNDi system exited', 'SUCCESS', '2020-09-30 09:13:49'),
	(1, 'Admin', 'admin', 'Admin Login', 'admin logged in', 'SUCCESS', '2020-09-30 11:06:18'),
	(1, 'Admin', 'admin', 'Add New Supplier', 'Admin added new supplier NokiaSupplier', 'SUCCESS', '2020-09-30 11:10:11'),
	(1, 'Admin', 'admin', 'Update Supplier', 'Admin updated supplier NokiaSupplier', 'SUCCESS', '2020-09-30 11:10:48'),
	(1, 'Admin', 'admin', 'Update Supplier', 'Admin updated supplier NokiaSupplier', 'SUCCESS', '2020-09-30 11:11:06'),
	(1, 'Admin', 'admin', 'Add Item to Supplier Item Collection', 'Admin added new Item Nokia3310', 'SUCCESS', '2020-09-30 11:13:05'),
	(1, 'Admin', 'admin', 'Purchase Order', 'Admin Ordered PO Bill No. 4', 'SUCCESS', '2020-09-30 11:17:41'),
	(1, 'Admin', 'admin', 'Add New Stock Item', 'Admin added new Stock Item Nokia3310', 'SUCCESS', '2020-09-30 11:18:45'),
	(1, 'Admin', 'admin', 'GRN Checked', 'Admin checked GRN item Nokia3310', 'SUCCESS', '2020-09-30 11:18:45'),
	(1, 'Admin', 'admin', 'Admin Logout', 'admin logged out', 'SUCCESS', '2020-09-30 11:21:17'),
	(1, 'Admin', 'admin', 'Admin Login', 'admin logged in', 'SUCCESS', '2020-09-30 11:21:41'),
	(1, 'Admin', 'admin', 'Admin Logout', 'admin logged out', 'SUCCESS', '2020-09-30 11:21:49'),
	(1, 'Employee', 'LEBOuNDi', 'Employee Login', 'LEBOuNDi logged in', 'SUCCESS', '2020-09-30 11:22:10'),
	(1, 'Employee', 'LEBOuNDi', 'Add New Sale Item', 'Employee added new sale item Nokia3310', 'SUCCESS', '2020-09-30 11:23:19'),
	(1, 'Employee', 'LEBOuNDi', 'Issue Invoice/Items', 'Employee Issued Invoice No. 7', 'SUCCESS', '2020-09-30 11:23:53'),
	(1, 'Employee', 'LEBOuNDi', 'Employee Logout', 'LEBOuNDi logged out', 'SUCCESS', '2020-09-30 11:24:59'),
	(1, 'Admin', 'admin', 'Admin Login', 'admin logged in', 'SUCCESS', '2020-09-30 11:25:06'),
	(1, 'Admin', 'admin', 'Admin System Exit', 'admin system exited', 'SUCCESS', '2020-09-30 11:25:18'),
	(1, 'Admin', 'admin', 'Admin Login', 'admin logged in', 'SUCCESS', '2020-10-01 09:25:25'),
	(1, 'Admin', 'admin', 'Admin System Exit', 'admin system exited', 'SUCCESS', '2020-10-01 09:25:36'),
	(1, 'Admin', 'admin', 'Admin Login', 'admin logged in', 'SUCCESS', '2020-10-01 09:29:21'),
	(1, 'Admin', 'admin', 'Admin System Exit', 'admin system exited', 'SUCCESS', '2020-10-01 09:30:09'),
	(1, 'Admin', 'admin', 'Admin Login', 'admin logged in', 'SUCCESS', '2020-10-01 12:02:41'),
	(1, 'Admin', 'admin', 'Add New Supplier', 'Admin added new supplier AmazonSupplier', 'SUCCESS', '2020-10-01 12:05:58'),
	(1, 'Admin', 'admin', 'Update Supplier', 'Admin updated supplier SonySupplier', 'SUCCESS', '2020-10-01 12:07:10'),
	(1, 'Admin', 'admin', 'Delete Supplier', 'Admin deleted supplier SonySupplier', 'SUCCESS', '2020-10-01 12:07:33'),
	(1, 'Admin', 'admin', 'Add Item to Supplier Item Collection', 'Admin added new Item Alexa', 'SUCCESS', '2020-10-01 12:08:16'),
	(1, 'Admin', 'admin', 'Purchase Order', 'Admin Ordered PO Bill No. 5', 'SUCCESS', '2020-10-01 12:10:53'),
	(1, 'Admin', 'admin', 'Bad Order', 'Admin marked bad order item on GRN Bill No. 5', 'SUCCESS', '2020-10-01 12:11:55'),
	(1, 'Admin', 'admin', 'Purchase Order', 'Admin Ordered PO Bill No. 6', 'SUCCESS', '2020-10-01 12:12:55'),
	(1, 'Admin', 'admin', 'Add New Stock Item', 'Admin added new Stock Item AppleiMacPro', 'SUCCESS', '2020-10-01 12:13:28'),
	(1, 'Admin', 'admin', 'GRN Checked', 'Admin checked GRN item AppleiMacPro', 'SUCCESS', '2020-10-01 12:13:28'),
	(1, 'Admin', 'admin', 'Purchase Order', 'Admin Ordered PO Bill No. 7', 'SUCCESS', '2020-10-01 12:14:42'),
	(1, 'Admin', 'admin', 'Re-Order Stock Item', 'Admin Re-Ordered Item AppleiMacPro', 'SUCCESS', '2020-10-01 12:14:56'),
	(1, 'Admin', 'admin', 'GRN Checked', 'Admin checked GRN item AppleiMacPro', 'SUCCESS', '2020-10-01 12:14:56'),
	(1, 'Admin', 'admin', 'Purchase Order', 'Admin Ordered PO Bill No. 8', 'SUCCESS', '2020-10-01 12:15:55'),
	(1, 'Admin', 'admin', 'Admin Logout', 'admin logged out', 'SUCCESS', '2020-10-01 12:16:13'),
	(1, 'Admin', 'admin', 'Admin Login', 'admin logged in', 'SUCCESS', '2020-10-01 12:16:24'),
	(1, 'Admin', 'admin', 'Admin System Exit', 'admin system exited', 'SUCCESS', '2020-10-01 12:17:07'),
	(1, 'Employee', 'LEBOuNDi', 'Employee Login', 'LEBOuNDi logged in', 'SUCCESS', '2020-10-01 12:17:21'),
	(1, 'Employee', 'LEBOuNDi', 'Add New Sale Item', 'Employee added new sale item DellXPS13', 'SUCCESS', '2020-10-01 12:18:52'),
	(1, 'Employee', 'LEBOuNDi', 'Employee Logout', 'LEBOuNDi logged out', 'SUCCESS', '2020-10-01 12:23:47'),
	(1, 'Admin', 'admin', 'Admin Login', 'admin logged in', 'SUCCESS', '2020-10-01 12:23:55'),
	(1, 'Admin', 'admin', 'Employee Payroll', 'Admin paid employee LEBOuNDi', 'SUCCESS', '2020-10-01 12:25:57'),
	(1, 'Admin', 'admin', 'Admin Logout', 'admin logged out', 'SUCCESS', '2020-10-01 12:29:42'),
	(2, 'Super User', 'super', 'Super User Login', 'super logged in', 'SUCCESS', '2020-10-01 12:30:16'),
	(2, 'Super User', 'super', 'Super User Logout', 'super logged out', 'SUCCESS', '2020-10-01 12:30:39'),
	(1, 'Employee', 'LEBOuNDi', 'Employee Login', 'LEBOuNDi logged in', 'SUCCESS', '2020-10-01 12:30:44'),
	(1, 'Employee', 'LEBOuNDi', 'Employee System Exit', 'LEBOuNDi system exited', 'SUCCESS', '2020-10-01 12:31:02'),
	(2, 'Super User', 'super', 'Super User Login', 'super logged in', 'SUCCESS', '2020-10-01 12:31:16'),
	(2, 'Super User', 'super', 'Return Stock Item', 'Super user returned stock item DellXPS13', 'SUCCESS', '2020-10-01 12:32:47'),
	(2, 'Super User', 'super', 'Force Access Employee Login', 'super force accessed Employee Login', 'SUCCESS', '2020-10-01 12:34:02'),
	(NULL, 'Employee', 'super', 'Employee Logout', 'super logged out', 'SUCCESS', '2020-10-01 12:34:20'),
	(1, 'Admin', 'admin', 'Admin Login', 'admin logged in', 'SUCCESS', '2020-10-01 12:34:31'),
	(1, 'Admin', 'admin', 'Create Backup', 'Admin created new backup on file location : ', 'SUCCESS', '2020-10-01 12:35:53'),
	(1, 'Admin', 'admin', 'Delete Employee', 'Admin deleted employee emp001', 'SUCCESS', '2020-10-01 12:36:53'),
	(1, 'Admin', 'admin', 'Admin System Exit', 'admin system exited', 'SUCCESS', '2020-10-01 12:37:54'),
	(1, 'Admin', 'admin', 'Admin Login', 'admin logged in', 'SUCCESS', '2020-10-01 12:39:16'),
	(1, 'Admin', 'admin', 'Admin System Exit', 'admin system exited', 'SUCCESS', '2020-10-01 12:39:41'),
	(1, 'Admin', 'admin', 'Admin Login', 'admin logged in', 'SUCCESS', '2020-10-01 12:42:28'),
	(1, 'Admin', 'admin', 'Admin Logout', 'admin logged out', 'SUCCESS', '2020-10-01 12:44:16'),
	(1, 'Employee', 'LEBOuNDi', 'Employee Login', 'LEBOuNDi logged in', 'SUCCESS', '2020-10-01 12:44:47'),
	(1, 'Employee', 'LEBOuNDi', 'Employee Logout', 'LEBOuNDi logged out', 'SUCCESS', '2020-10-01 12:46:20'),
	(1, 'Admin', 'admin', 'Admin Login', 'admin logged in', 'SUCCESS', '2020-10-01 12:46:28'),
	(1, 'Admin', 'admin', 'Admin System Exit', 'admin system exited', 'SUCCESS', '2020-10-01 13:03:31'),
	(1, 'Admin', 'admin', 'Admin Login', 'admin logged in', 'SUCCESS', '2020-10-01 13:30:19'),
	(1, 'Admin', 'admin', 'Admin System Exit', 'admin system exited', 'SUCCESS', '2020-10-01 13:30:45'),
	(1, 'Admin', 'Admin', 'Admin System Exit', 'Admin system exited', 'SUCCESS', '2020-10-03 19:47:53'),
	(1, 'Admin', 'Admin', 'Admin System Exit', 'Admin system exited', 'SUCCESS', '2020-10-03 19:48:40'),
	(1, 'Admin', 'Admin', 'Admin System Exit', 'Admin system exited', 'SUCCESS', '2020-10-19 15:55:24'),
	(1, 'Admin', 'admin', 'Admin Login', 'admin logged in', 'SUCCESS', '2020-11-19 16:32:59'),
	(1, 'Admin', 'admin', 'Update Supplier Item', 'Admin updated supplier item Alexa', 'SUCCESS', '2020-11-19 16:33:34'),
	(1, 'Admin', 'admin', 'Update Supplier Item', 'Admin updated supplier item Alexa', 'SUCCESS', '2020-11-19 16:33:46'),
	(1, 'Admin', 'admin', 'Admin System Exit', 'admin system exited', 'SUCCESS', '2020-11-19 16:33:50'),
	(NULL, 'Employee', 'admin', 'Employee Login', 'admin login failed', 'FAILED', '2020-12-09 14:46:20'),
	(NULL, 'Employee', 'admin', 'Employee Login', 'admin login failed', 'FAILED', '2020-12-09 14:46:28'),
	(1, 'Admin', 'admin', 'Admin Login', 'admin logged in', 'SUCCESS', '2020-12-09 14:47:11'),
	(1, 'Admin', 'admin', 'Admin Logout', 'admin logged out', 'SUCCESS', '2020-12-09 14:50:29'),
	(NULL, 'Super User', 'Super User', 'Super User System Exit', 'Super User system exited', 'SUCCESS', '2020-12-26 11:32:47');
/*!40000 ALTER TABLE `log` ENABLE KEYS */;

-- Dumping data for table xeon_inventory.payroll: ~6 rows (approximately)
/*!40000 ALTER TABLE `payroll` DISABLE KEYS */;
INSERT INTO `payroll` (`id`, `username`, `month`, `year`, `date`, `attendance`, `payment`, `status`, `date/time`) VALUES
	(1, 'LEBOuNDi', 'August', '2020', '2020-10-01', 1, 120000, 'PAID', '2020-10-01 12:25:57'),
	(2, 'OtENtoNO', 'August', '2020', '2020-08-10', 1, 112000, 'PAID', '2020-08-10 20:23:55'),
	(3, 'aMeSPriE', 'August', '2020', '2020-08-10', 1, 144000, 'PAID', '2020-08-10 20:24:05'),
	(4, 'anTESHIB', 'August', '2020', '2020-08-10', 1, 145000, 'PAID', '2020-08-10 20:24:16'),
	(5, 'dawavERE', 'August', '2020', '2020-08-10', 1, 198000, 'PAID', '2020-08-10 20:24:33'),
	(6, 'terthEaR', 'August', '2020', '2020-08-10', 1, 187700, 'PAID', '2020-08-10 20:24:46'),
	(7, 'emp001', 'August', '2020', '2020-08-10', 2, 120000, 'PAID', '2020-08-11 15:15:12'),
	(1, 'LEBOuNDi', 'September', '2020', '2020-10-01', 5, 120000, 'PAID', '2020-10-01 12:25:57'),
	(1, 'LEBOuNDi', 'October', '2020', '2020-10-01', 1, 120000, 'PAID', '2020-10-01 12:25:57');
/*!40000 ALTER TABLE `payroll` ENABLE KEYS */;

-- Dumping data for table xeon_inventory.purchase_order: ~8 rows (approximately)
/*!40000 ALTER TABLE `purchase_order` DISABLE KEYS */;
INSERT INTO `purchase_order` (`id`, `po_id`, `item_name`, `item_code`, `quantity`, `item_price`, `supplier`, `total`, `lead_time`, `date/time`) VALUES
	(1, 1, 'AppleiMac', 'imac', 1000, 2000, 'AppleSupplier', 2000000, '2020-08-17', '2020-08-10 17:20:59'),
	(2, 2, 'DellXPS13', 'xps13', 1200, 800, 'DellSupplier', 960000, '2020-08-17', '2020-08-10 20:41:01'),
	(3, 2, 'GooglePixel4a', 'pixel4a', 1500, 400, 'GoogleSupplier', 600000, '2020-08-17', '2020-08-10 20:43:27'),
	(4, 3, 'MicrosoftSurfacePro4', 'surfpro4', 400, 600, 'MicrosoftSupplier', 240000, '2020-08-17', '2020-08-10 20:47:38'),
	(5, 4, 'Nokia3310', 'nokia3310', 2000, 100, 'NokiaSupplier', 200000, '2020-10-07', '2020-09-30 11:17:06'),
	(6, 5, 'Alexa', 'alx', 2000, 100, 'AmazonSupplier', 200000, '2020-10-15', '2020-10-01 12:10:03'),
	(7, 6, 'AppleiMacPro', 'imacpro', 1000, 100, 'AppleSupplier', 100000, '2020-10-08', '2020-10-01 12:12:44'),
	(8, 7, 'AppleiMacPro', 'imacpro', 200, 100, 'AppleSupplier', 20000, '2020-10-08', '2020-10-01 12:14:36'),
	(9, 8, 'AppleiMac', 'imac', 1000, 2000, 'AppleSupplier', 2000000, '2020-10-08', '2020-10-01 12:15:43');
/*!40000 ALTER TABLE `purchase_order` ENABLE KEYS */;

-- Dumping data for table xeon_inventory.sales: ~8 rows (approximately)
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
INSERT INTO `sales` (`id`, `invoice_id`, `item_id`, `item_code`, `item_name`, `purchase_price`, `selling_price`, `discount`, `new_item_price`, `amount`, `total`, `new_total`, `savings`, `employee`, `profit`, `date`, `month`, `year`, `date/time`) VALUES
	(1, 1, 1, 'imac', 'AppleiMac', 2000, 2200, 5, 2090, 2, 4400, 4180, 220, 'LEBOuNDi', 180, '2020-08-10', 'August', '2020', '2020-08-10 17:27:00'),
	(2, 2, 1, 'imac', 'AppleiMac', 2000, 2200, 0, 2200, 900, 1980000, 1980000, 0, 'emp001', 180000, '2020-08-10', 'August', '2020', '2020-08-10 20:49:30'),
	(3, 3, 3, 'pixel4a', 'GooglePixel4a', 400, 500, 0, 500, 2, 1000, 1000, 0, 'emp001', 200, '2020-08-11', 'August', '2020', '2020-08-11 15:15:24'),
	(4, 4, 3, 'pixel4a', 'GooglePixel4a', 400, 500, 5, 475, 5, 2500, 2375, 125, 'emp001', 375, '2020-08-11', 'August', '2020', '2020-08-11 15:33:24'),
	(5, 5, 2, 'xps13', 'DellXPS13', 800, 1000, 0, 1000, 2, 2000, 2000, 0, 'LEBOuNDi', 400, '2020-09-11', 'September', '2020', '2020-09-11 12:22:57'),
	(6, 6, 2, 'xps13', 'DellXPS13', 800, 1000, 5, 950, 8, 8000, 7600, 400, 'LEBOuNDi', 1200, '2020-09-24', 'September', '2020', '2020-09-24 09:11:56'),
	(7, 7, 5, 'nokia3310', 'Nokia3310', 100, 150, 10, 135, 10, 1500, 1350, 150, 'LEBOuNDi', 350, '2020-09-30', 'September', '2020', '2020-09-30 11:23:19'),
	(8, 8, 2, 'xps13', 'DellXPS13', 800, 1000, 5, 950, 10, 10000, 9500, 500, 'LEBOuNDi', 1500, '2020-10-01', 'October', '2020', '2020-10-01 12:18:52');
/*!40000 ALTER TABLE `sales` ENABLE KEYS */;

-- Dumping data for table xeon_inventory.stock: ~6 rows (approximately)
/*!40000 ALTER TABLE `stock` DISABLE KEYS */;
INSERT INTO `stock` (`id`, `item_code`, `item_name`, `item_price`, `selling_price`, `profit`, `supplier`, `stock_count`, `low_stock`, `stock_status`, `date/time`) VALUES
	(1, 'imac', 'AppleiMac', 2000, 2200, 200, 'AppleSupplier', 98, 100, 'Available', '2020-08-10 20:49:31'),
	(2, 'xps13', 'DellXPS13', 800, 1000, 200, 'DellSupplier', 1100, 50, 'Available', '2020-10-01 12:32:47'),
	(3, 'pixel4a', 'GooglePixel4a', 400, 500, 100, 'GoogleSupplier', 1481, 10, 'Available', '2020-08-11 15:33:25'),
	(4, 'surfpro4', 'MicrosoftSurfacePro4', 600, 800, 200, 'MicrosoftSupplier', 400, 20, 'Available', '2020-08-10 20:47:51'),
	(5, 'nokia3310', 'Nokia3310', 100, 150, 50, 'NokiaSupplier', 1990, 10, 'Available', '2020-09-30 11:23:21'),
	(6, 'imacpro', 'AppleiMacPro', 100, 150, 50, 'AppleSupplier', 1200, 10, 'Available', '2020-10-01 12:14:56');
/*!40000 ALTER TABLE `stock` ENABLE KEYS */;

-- Dumping data for table xeon_inventory.stock_return: ~0 rows (approximately)
/*!40000 ALTER TABLE `stock_return` DISABLE KEYS */;
INSERT INTO `stock_return` (`id`, `sku_id`, `item_code`, `item_name`, `supplier`, `return_count`, `date / time`) VALUES
	(1, 3, 'pixel4a', 'GooglePixel4a', 'GoogleSupplier', 12, '2020-08-11 14:59:02'),
	(2, 2, 'xps13', 'DellXPS13', 'DellSupplier', 80, '2020-10-01 12:32:47');
/*!40000 ALTER TABLE `stock_return` ENABLE KEYS */;

-- Dumping data for table xeon_inventory.supplier: ~8 rows (approximately)
/*!40000 ALTER TABLE `supplier` DISABLE KEYS */;
INSERT INTO `supplier` (`id`, `username`, `address`, `email`, `phone_no`, `company`, `account_status`) VALUES
	(1, 'AppleSupplier', 'apple address', 'contact@apple.com', '0938209380', 'Apple', 'Active'),
	(2, 'DellSupplier', 'dell address', 'contact@dell.com', '3802830928', 'Dell', 'Active'),
	(3, 'GoogleSupplier', 'google address', 'contact@google.com', '1230982109', 'Google', 'Active'),
	(4, 'MicrosoftSupplier', 'microsoft address', 'contact@microsoft.com', '7398279382', 'Microsoft', 'Active'),
	(5, 'SamsungSupplier', 'samsung address', 'contact@samsung.com', '3209483209', 'Samsung', 'Active'),
	(6, 'SonySupplier', 'bla blah', 'contact@sony.com', '2109839042', 'Sony', 'Dective'),
	(7, 'NokiaSupplier', 'nokia address', 'contact@nokia.com', '3242342378', 'Nokia', 'Active'),
	(8, 'AmazonSupplier', 'AmazonAddress', 'amazon@jeiow.com', '3782947983', 'Amazon', 'Active');
/*!40000 ALTER TABLE `supplier` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
