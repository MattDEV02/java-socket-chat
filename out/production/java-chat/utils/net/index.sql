-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Mar 23, 2021 alle 10:37
-- Versione del server: 10.4.17-MariaDB
-- Versione PHP: 8.0.2


SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

CREATE SCHEMA IF NOT EXISTS JavaChat;
USE JavaChat;


CREATE TABLE IF NOT EXISTS Message (
   id INT(9) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Identificatore auto-incrementante del messaggio',
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() COMMENT 'Data creazione messaggio.',
   updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP() COMMENT 'Data aggiornamento messsaggio.',
   txt VARCHAR(255) NOT NULL COMMENT 'Testo in formato stringa del messaggio',
   PRIMARY KEY (id),
   CONSTRAINT CHECK_TXT CHECK(CHAR_LENGTH(txt) > 0)
) ENGINE = InnoDB CHARSET = UTF8 COLLATE = utf8_general_ci;

OPTIMIZE TABLE Message;

DESCRIBE Message;

SHOW CREATE TABLE Message;

SELECT * FROM Message;


/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
