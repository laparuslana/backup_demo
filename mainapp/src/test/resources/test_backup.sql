-- phpMyAdmin SQL Dump
-- version 5.2.1deb1
-- https://www.phpmyadmin.net/
--
-- Хост: localhost:3306
-- Час створення: Трв 11 2025 р., 11:56
-- Версія сервера: 10.11.6-MariaDB-0+deb12u1
-- Версія PHP: 8.2.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База даних: `backup_baf`
--

-- --------------------------------------------------------

--
-- Структура таблиці `backup_history`
--

CREATE TABLE `backup_history` (
  `id` bigint(20) NOT NULL,
  `database_name` varchar(255) NOT NULL,
  `backup_time` datetime NOT NULL,
  `status` varchar(255) NOT NULL,
  `storage_path` varchar(255) NOT NULL,
  `retention_period` varchar(255) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп даних таблиці `backup_history`
--

INSERT INTO `backup_history` (`id`, `database_name`, `backup_time`, `status`, `storage_path`, `retention_period`, `user_id`) VALUES
(3252, 'test2', '2025-04-11 16:43:00', 'Backup executed successfully!\n', '/home/adminbs/backup', '30', 552),
(3253, 'test2', '2025-04-11 16:43:39', 'Backup executed successfully!\n', 'backup', '30', 552),
(4452, 'test2', '2025-04-29 20:05:03', 'Backup failed with exit code: 1', 'backup', '30', 552),
(4502, 'test2', '2025-05-02 12:36:57', 'Backup executed successfully!\n', '/home/adminbs/backup', '30', 552),
(4552, 'test2', '2025-05-04 20:48:51', 'Backup executed successfully!\n', '/home/adminbs/backup', '30', 552),
(4602, 'test2', '2025-05-09 10:51:57', 'Backup executed successfully!\n', '/home/adminbs/backup', '30', 552);

-- --------------------------------------------------------

--
-- Структура таблиці `backup_history_seq`
--

CREATE TABLE `backup_history_seq` (
  `next_not_cached_value` bigint(21) NOT NULL,
  `minimum_value` bigint(21) NOT NULL,
  `maximum_value` bigint(21) NOT NULL,
  `start_value` bigint(21) NOT NULL COMMENT 'start value when sequences is created or value if RESTART is used',
  `increment` bigint(21) NOT NULL COMMENT 'increment value',
  `cache_size` bigint(21) UNSIGNED NOT NULL,
  `cycle_option` tinyint(1) UNSIGNED NOT NULL COMMENT '0 if no cycles are allowed, 1 if the sequence should begin a new cycle when maximum_value is passed',
  `cycle_count` bigint(21) NOT NULL COMMENT 'How many cycles have been done'
) ENGINE=InnoDB;

--
-- Дамп даних таблиці `backup_history_seq`
--

INSERT INTO `backup_history_seq` (`next_not_cached_value`, `minimum_value`, `maximum_value`, `start_value`, `increment`, `cache_size`, `cycle_option`, `cycle_count`) VALUES
(4701, 1, 9223372036854775806, 1, 50, 0, 0, 0);

-- --------------------------------------------------------

--
-- Структура таблиці `backup_settings`
--

CREATE TABLE `backup_settings` (
  `id` bigint(20) NOT NULL,
  `type` varchar(255) NOT NULL,
  `folder_path` varchar(255) DEFAULT NULL,
  `database_name` varchar(255) DEFAULT NULL,
  `backup_location` varchar(255) DEFAULT NULL,
  `retention_period` varchar(255) NOT NULL,
  `storage_params` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`storage_params`)),
  `storage_type` varchar(255) NOT NULL,
  `db_params` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`db_params`)),
  `schedule_params` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`schedule_params`)),
  `storage_target_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп даних таблиці `backup_settings`
--

INSERT INTO `backup_settings` (`id`, `type`, `folder_path`, `database_name`, `backup_location`, `retention_period`, `storage_params`, `storage_type`, `db_params`, `schedule_params`, `storage_target_id`) VALUES
(31, 'database', NULL, 'test2', NULL, '7', '{\"ftpPassword\":\"QQzzodB46t9XkA5xRMVFDKE+qXMLXvHFKZU7kohhBuM=\",\"ftpDirectory\":\"ouSZ1f+fISFyS8H77IlbUw==\",\"ftpServer\":\"md7Xe/vr8273i5vfhMQSB6viT6Wg+bNY3OB7lFMfeh0=\",\"ftpUser\":\"5THVyYngqAPThPJz6dd3rA==\"}', 'FTP', '{\"dbUser\":\"AcjaI8qjWr34HI5Bv3x9+g==\",\"dbServer\":\"5MW2bWi4MOPE8Pxs6Wy5xg==\",\"dbPassword\":\"AcjaI8qjWr34HI5Bv3x9+g==\"}', '{\"time\":\"07:00\",\"day\":\"\",\"frequency\":\"daily\"}', 52);

-- --------------------------------------------------------

--
-- Структура таблиці `baf_settings`
--

CREATE TABLE `baf_settings` (
  `id` bigint(20) NOT NULL,
  `baf_type` varchar(255) NOT NULL,
  `baf_path` varchar(255) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп даних таблиці `baf_settings`
--

INSERT INTO `baf_settings` (`id`, `baf_type`, `baf_path`, `user_id`) VALUES
(1, 'server', '/opt/1cv8/i386/8.3.25.1374', 552),
(3, 'file', '/opt/1cv8/i386/8.3.25.1374', 1202);

-- --------------------------------------------------------

--
-- Структура таблиці `baf_settings_seq`
--

CREATE TABLE `baf_settings_seq` (
  `next_not_cached_value` bigint(21) NOT NULL,
  `minimum_value` bigint(21) NOT NULL,
  `maximum_value` bigint(21) NOT NULL,
  `start_value` bigint(21) NOT NULL COMMENT 'start value when sequences is created or value if RESTART is used',
  `increment` bigint(21) NOT NULL COMMENT 'increment value',
  `cache_size` bigint(21) UNSIGNED NOT NULL,
  `cycle_option` tinyint(1) UNSIGNED NOT NULL COMMENT '0 if no cycles are allowed, 1 if the sequence should begin a new cycle when maximum_value is passed',
  `cycle_count` bigint(21) NOT NULL COMMENT 'How many cycles have been done'
) ENGINE=InnoDB;

--
-- Дамп даних таблиці `baf_settings_seq`
--

INSERT INTO `baf_settings_seq` (`next_not_cached_value`, `minimum_value`, `maximum_value`, `start_value`, `increment`, `cache_size`, `cycle_option`, `cycle_count`) VALUES
(101, 1, 9223372036854775806, 1, 50, 0, 0, 0);

-- --------------------------------------------------------

--
-- Структура таблиці `my_app_user`
--

CREATE TABLE `my_app_user` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп даних таблиці `my_app_user`
--

INSERT INTO `my_app_user` (`id`, `email`, `password`, `username`, `role`) VALUES
(552, 'admin@gmail.com', '$2a$10$l7PxXy0rMpDM9gRGpdTvf.vIWHqFaK1DZSAVHUa0c7aFWvcFVXKv.', 'admin', 'ADMIN'),
(602, 'test@gmail.com', '$2a$10$i5.oBWmvsiElZLOcNBJib.xNDTIzJLsUaUGZpta2.qiPuSG1dPete', 'test', 'USER'),
(1202, 'admin_file@gmail.com', '$2a$10$ZKm/oRotjE8w865m0nNCJOiRJ7ehL6yXAzkE8/S4aXtY7MPmWcOVS', 'admin_file', 'ADMIN');

-- --------------------------------------------------------

--
-- Структура таблиці `my_app_user_seq`
--

CREATE TABLE `my_app_user_seq` (
  `next_not_cached_value` bigint(21) NOT NULL,
  `minimum_value` bigint(21) NOT NULL,
  `maximum_value` bigint(21) NOT NULL,
  `start_value` bigint(21) NOT NULL COMMENT 'start value when sequences is created or value if RESTART is used',
  `increment` bigint(21) NOT NULL COMMENT 'increment value',
  `cache_size` bigint(21) UNSIGNED NOT NULL,
  `cycle_option` tinyint(1) UNSIGNED NOT NULL COMMENT '0 if no cycles are allowed, 1 if the sequence should begin a new cycle when maximum_value is passed',
  `cycle_count` bigint(21) NOT NULL COMMENT 'How many cycles have been done'
) ENGINE=InnoDB;

--
-- Дамп даних таблиці `my_app_user_seq`
--

INSERT INTO `my_app_user_seq` (`next_not_cached_value`, `minimum_value`, `maximum_value`, `start_value`, `increment`, `cache_size`, `cycle_option`, `cycle_count`) VALUES
(1301, 1, 9223372036854775806, 1, 50, 0, 0, 0);

-- --------------------------------------------------------

--
-- Структура таблиці `restore_history`
--

CREATE TABLE `restore_history` (
  `id` bigint(20) NOT NULL,
  `restore_time` datetime(6) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `backup_file` varchar(255) NOT NULL,
  `source_database` varchar(255) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп даних таблиці `restore_history`
--

INSERT INTO `restore_history` (`id`, `restore_time`, `status`, `backup_file`, `source_database`, `user_id`) VALUES
(1552, '2025-04-11 16:33:37.532456', 'Restore executed successfully!\n', 'test2_20250410.backup', 'test2', 552),
(1602, '2025-04-11 21:02:14.144591', 'Restore executed successfully!\n', 'test2_20250411_ftp.backup', 'test2', 552),
(2302, '2025-04-29 17:15:45.596460', 'Restore executed successfully!\n', 'backup_20250417.tar.gz', 'backup', 1202),
(2352, '2025-05-09 10:57:10.925430', 'Restore failed with exit code: 1', 'test2_20250509.backup', 'test2', 552),
(2353, '2025-05-09 10:58:36.912447', 'Restore executed successfully!\n', 'test2_20250507_ftp.backup', 'test2', 552),
(2354, '2025-05-09 10:59:15.293853', 'Restore executed successfully!\n', 'test2_20250509.backup', 'test2', 552);

-- --------------------------------------------------------

--
-- Структура таблиці `restore_history_seq`
--

CREATE TABLE `restore_history_seq` (
  `next_not_cached_value` bigint(21) NOT NULL,
  `minimum_value` bigint(21) NOT NULL,
  `maximum_value` bigint(21) NOT NULL,
  `start_value` bigint(21) NOT NULL COMMENT 'start value when sequences is created or value if RESTART is used',
  `increment` bigint(21) NOT NULL COMMENT 'increment value',
  `cache_size` bigint(21) UNSIGNED NOT NULL,
  `cycle_option` tinyint(1) UNSIGNED NOT NULL COMMENT '0 if no cycles are allowed, 1 if the sequence should begin a new cycle when maximum_value is passed',
  `cycle_count` bigint(21) NOT NULL COMMENT 'How many cycles have been done'
) ENGINE=InnoDB;

--
-- Дамп даних таблиці `restore_history_seq`
--

INSERT INTO `restore_history_seq` (`next_not_cached_value`, `minimum_value`, `maximum_value`, `start_value`, `increment`, `cache_size`, `cycle_option`, `cycle_count`) VALUES
(2451, 1, 9223372036854775806, 1, 50, 0, 0, 0);

-- --------------------------------------------------------

--
-- Структура таблиці `storage_settings`
--

CREATE TABLE `storage_settings` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `json_params` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`json_params`))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп даних таблиці `storage_settings`
--

INSERT INTO `storage_settings` (`id`, `name`, `type`, `json_params`) VALUES
(1, 'local1', 'LOCAL', '{\"directory\":\"VO5Hj2WFos5PPVe/n/lTYGzJ2nmqvlXKsiFDPuoeoPo=\"}'),
(52, 'ftp1', 'FTP', '{\"ftp_host\":\"md7Xe/vr8273i5vfhMQSB6viT6Wg+bNY3OB7lFMfeh0=\",\"ftp_password\":\"QQzzodB46t9XkA5xRMVFDKE+qXMLXvHFKZU7kohhBuM=\",\"ftp_directory\":\"ouSZ1f+fISFyS8H77IlbUw==\",\"ftp_user\":\"5THVyYngqAPThPJz6dd3rA==\"}');

-- --------------------------------------------------------

--
-- Структура таблиці `storage_settings_seq`
--

CREATE TABLE `storage_settings_seq` (
  `next_not_cached_value` bigint(21) NOT NULL,
  `minimum_value` bigint(21) NOT NULL,
  `maximum_value` bigint(21) NOT NULL,
  `start_value` bigint(21) NOT NULL COMMENT 'start value when sequences is created or value if RESTART is used',
  `increment` bigint(21) NOT NULL COMMENT 'increment value',
  `cache_size` bigint(21) UNSIGNED NOT NULL,
  `cycle_option` tinyint(1) UNSIGNED NOT NULL COMMENT '0 if no cycles are allowed, 1 if the sequence should begin a new cycle when maximum_value is passed',
  `cycle_count` bigint(21) NOT NULL COMMENT 'How many cycles have been done'
) ENGINE=InnoDB;

--
-- Дамп даних таблиці `storage_settings_seq`
--

INSERT INTO `storage_settings_seq` (`next_not_cached_value`, `minimum_value`, `maximum_value`, `start_value`, `increment`, `cache_size`, `cycle_option`, `cycle_count`) VALUES
(151, 1, 9223372036854775806, 1, 50, 0, 0, 0);

-- --------------------------------------------------------

--
-- Структура таблиці `users_seq`
--

CREATE TABLE `users_seq` (
  `next_not_cached_value` bigint(21) NOT NULL,
  `minimum_value` bigint(21) NOT NULL,
  `maximum_value` bigint(21) NOT NULL,
  `start_value` bigint(21) NOT NULL COMMENT 'start value when sequences is created or value if RESTART is used',
  `increment` bigint(21) NOT NULL COMMENT 'increment value',
  `cache_size` bigint(21) UNSIGNED NOT NULL,
  `cycle_option` tinyint(1) UNSIGNED NOT NULL COMMENT '0 if no cycles are allowed, 1 if the sequence should begin a new cycle when maximum_value is passed',
  `cycle_count` bigint(21) NOT NULL COMMENT 'How many cycles have been done'
) ENGINE=InnoDB;

--
-- Дамп даних таблиці `users_seq`
--

INSERT INTO `users_seq` (`next_not_cached_value`, `minimum_value`, `maximum_value`, `start_value`, `increment`, `cache_size`, `cycle_option`, `cycle_count`) VALUES
(1, 1, 9223372036854775806, 1, 50, 0, 0, 0);

--
-- Індекси збережених таблиць
--

--
-- Індекси таблиці `backup_history`
--
ALTER TABLE `backup_history`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKeifr44g7iiyeo4x40g9yqf2qe` (`user_id`);

--
-- Індекси таблиці `backup_settings`
--
ALTER TABLE `backup_settings`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKf7nyiweidtlg99gf9rmfgl1cg` (`storage_target_id`);

--
-- Індекси таблиці `baf_settings`
--
ALTER TABLE `baf_settings`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user_key` (`user_id`);

--
-- Індекси таблиці `my_app_user`
--
ALTER TABLE `my_app_user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_email` (`email`),
  ADD UNIQUE KEY `unique_username` (`username`) USING BTREE;

--
-- Індекси таблиці `restore_history`
--
ALTER TABLE `restore_history`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKg688493jiht9v2woiycaefh98` (`user_id`);

--
-- Індекси таблиці `storage_settings`
--
ALTER TABLE `storage_settings`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT для збережених таблиць
--

--
-- AUTO_INCREMENT для таблиці `backup_history`
--
ALTER TABLE `backup_history`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4603;

--
-- AUTO_INCREMENT для таблиці `backup_settings`
--
ALTER TABLE `backup_settings`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;

--
-- AUTO_INCREMENT для таблиці `baf_settings`
--
ALTER TABLE `baf_settings`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT для таблиці `my_app_user`
--
ALTER TABLE `my_app_user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1203;

--
-- AUTO_INCREMENT для таблиці `restore_history`
--
ALTER TABLE `restore_history`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2355;

--
-- AUTO_INCREMENT для таблиці `storage_settings`
--
ALTER TABLE `storage_settings`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=53;

--
-- Обмеження зовнішнього ключа збережених таблиць
--

--
-- Обмеження зовнішнього ключа таблиці `backup_history`
--
ALTER TABLE `backup_history`
  ADD CONSTRAINT `FKeifr44g7iiyeo4x40g9yqf2qe` FOREIGN KEY (`user_id`) REFERENCES `my_app_user` (`id`);

--
-- Обмеження зовнішнього ключа таблиці `backup_settings`
--
ALTER TABLE `backup_settings`
  ADD CONSTRAINT `FKf7nyiweidtlg99gf9rmfgl1cg` FOREIGN KEY (`storage_target_id`) REFERENCES `storage_settings` (`id`);

--
-- Обмеження зовнішнього ключа таблиці `baf_settings`
--
ALTER TABLE `baf_settings`
  ADD CONSTRAINT `FKn98t5k328j43iw73of7vdwicu` FOREIGN KEY (`user_id`) REFERENCES `my_app_user` (`id`);

--
-- Обмеження зовнішнього ключа таблиці `restore_history`
--
ALTER TABLE `restore_history`
  ADD CONSTRAINT `FKg688493jiht9v2woiycaefh98` FOREIGN KEY (`user_id`) REFERENCES `my_app_user` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
