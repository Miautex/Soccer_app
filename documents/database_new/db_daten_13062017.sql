-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 13. Jun 2017 um 21:48
-- Server-Version: 5.7.17-0ubuntu0.16.04.1
-- PHP-Version: 7.0.15-0ubuntu0.16.04.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `SOCCER_APP_4BG2`
--

--
-- Daten f√ºr Tabelle `games`
--

INSERT INTO `games` (`id`, `date`, `remark`, `scoreTeamA`, `scoreTeamB`) VALUES
(39, '2017-06-11', 'hansi verletzt', 6, 3),
(40, '2017-06-12', '', 0, 0);

--
-- Daten f√ºr Tabelle `participation`
--

INSERT INTO `participation` (`id`, `idGame`, `idPlayer`, `goalsGot`, `goalsShotDefault`, `goalsShotHead`, `goalsShotHeadSnow`, `goalsShotPenalty`, `nutmeg`, `team`, `position`) VALUES
(261, 39, 1, 4, 1, 0, 0, 0, 0, 'TEAM2', 'MIDFIELD'),
(262, 39, 146, 3, 0, 0, 0, 0, 0, 'TEAM1', 'MIDFIELD'),
(263, 39, 6, 0, 0, 1, 0, 0, 0, 'TEAM1', 'MIDFIELD'),
(264, 39, 5, 0, 2, 0, 0, 0, 0, 'TEAM2', 'ATTACK'),
(265, 39, 2, 0, 1, 4, 0, 0, 0, 'TEAM1', 'MIDFIELD'),
(266, 39, 3, 2, 0, 0, 0, 0, 0, 'TEAM2', 'MIDFIELD'),
(267, 40, 147, 0, 0, 0, 0, 0, 0, 'TEAM1', 'MIDFIELD'),
(268, 40, 146, 0, 0, 0, 0, 0, 0, 'TEAM2', 'MIDFIELD'),
(269, 40, 5, 0, 0, 0, 0, 0, 0, 'TEAM2', 'ATTACK'),
(270, 40, 3, 0, 0, 0, 0, 0, 0, 'TEAM1', 'MIDFIELD'),
(271, 40, 1, 0, 0, 0, 0, 0, 0, 'TEAM2', 'MIDFIELD'),
(272, 40, 6, 0, 0, 0, 0, 0, 0, 'TEAM1', 'MIDFIELD'),
(273, 40, 2, 0, 0, 0, 0, 0, 0, 'TEAM1', 'MIDFIELD');

--
-- Daten f√ºr Tabelle `players`
--

INSERT INTO `players` (`ID`, `USERNAME`, `NAME`, `ISADMIN`, `PASSWORD`, `ISACTIVE`, `loginkey`, `location`) VALUES
(1, 'admin', 'Admin\\uD83E\\uDD18', 1, '21232f297a57a5a743894a0e4a801fc3', 1, '06f56699b37cb4faa71a6a5af6e277f7', '\0\0\0\0\0\0\0≠ÖYhÁLG@AùÚËF∞+@'),
(2, 'moe', 'Moser', 1, '23adab589ba9778898768527c930967e', 1, 'fc96f54bcbed5eaa82b0539abb9d4b7e', '\0\0\0\0\0\0\0∞…\ZıMG@»ÍVœIØ+@'),
(3, 'elias', 'Sanchez', 1, '29a2b2e1849474d94d12051309c7b4d7', 1, '6545a50b53af124d08af485a120d0a37', '\0\0\0\0\0\0\0)≥A&OG@i≤+@'),
(5, 'sunny', 'Sunny \\u261C\\u2606\\u261E', 1, '533c5ba8368075db8f6ef201546bd71a', 1, 'd2029ce9c3b8492fb53a2cd239a4d57f', NULL),
(6, 'guest', 'Tim', 0, '84e0343a0486ff05530df6c705c8bb4', 1, '686a3e458d1629663bfa3cf690a61e0e', '\0\0\0\0\0\0\0ﬂâY/∂+@í≥∞ßNG@'),
(146, 'hansi', 'hansi', 0, '4a8c21850b4ab1104b009c99ad38173d', 1, '9eaf0d12c4d87bab111c88cd13e5e60d', NULL),
(147, 'sepp', 'sepp', 0, 'f14029217ff5e7a50cdc7e70f686cf29', 1, '22a5d3deaf2ddb895a8dba00f9d7fbae', NULL);

--
-- Daten f√ºr Tabelle `playersGamePositions`
--

INSERT INTO `playersGamePositions` (`id_player`, `position`) VALUES
(1, 'ATTACK'),
(2, 'ATTACK'),
(3, 'ATTACK'),
(5, 'ATTACK'),
(6, 'ATTACK'),
(146, 'ATTACK'),
(147, 'ATTACK'),
(3, 'DEFENSE'),
(6, 'DEFENSE'),
(146, 'DEFENSE'),
(147, 'DEFENSE'),
(1, 'GOAL'),
(3, 'GOAL'),
(5, 'GOAL'),
(6, 'GOAL'),
(146, 'GOAL'),
(147, 'GOAL'),
(1, 'MIDFIELD'),
(2, 'MIDFIELD'),
(3, 'MIDFIELD'),
(6, 'MIDFIELD'),
(146, 'MIDFIELD'),
(147, 'MIDFIELD');

--
-- Daten f√ºr Tabelle `positions`
--

INSERT INTO `positions` (`NAME`) VALUES
('ATTACK'),
('DEFENSE'),
('GOAL'),
('MIDFIELD');

--
-- Daten f√ºr Tabelle `teams`
--

INSERT INTO `teams` (`NAME`) VALUES
('TEAM1'),
('TEAM2');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
